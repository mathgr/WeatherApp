package com.example.weatherapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.services.SuffixAdder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AjoutVille extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "message.extra";
    public final static String TAG = "RechercheVille";

    TextView villeRechercher;

    private String currentCity;

    final String BASE_URL = "https://www.prevision-meteo.ch/services/json/";
    protected RequestQueue queue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_ville_display);

        final AutoCompleteTextView textRecherche = findViewById(R.id.searchVilleText);

        villeRechercher = findViewById(R.id.villeRechercher);
        villeRechercher.setClickable(false);

        ImageView returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button rechercher = findViewById(R.id.searchVille);
        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                villeRechercher.setText(R.string.loading);
                villeRechercher.setClickable(false);
                rechercher(textRecherche.getText().toString());

            }
        });

        villeRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (villeRechercher.isClickable()) {
                    if (villeRechercher.getText() != null) {
                        String nomVille = villeRechercher.getText().toString();
                        Log.i("enregistrement", "ville : " + villeRechercher.getText());
                        addVilleJson(currentCity);
                        finish();
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        villeRechercher.setClickable(false);
    }

    public void addVilleJson(String name){
        try {
            JSONArray jsonArray = new JSONArray(loadJSON());

            JSONObject jsonObject = new JSONObject(); //new Json Object

            //Add data
            jsonObject.put( "name",name);
            //Append
            jsonArray.put(jsonObject);
            File file = new File(AjoutVille.this.getFilesDir(), "villesfav.json");
            writeJsonFile(file,jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String loadJSON() {
        String json = null;
        FileWriter fileWriter = null ;
        BufferedWriter bufferedWriter = null ;
        try {

            StringBuffer output = new StringBuffer();
            File file = new File(AjoutVille.this.getFilesDir(), "villesfav.json");

            if(!file.exists()){
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("[{\"name\" : \"Montpellier\"}]");
                bufferedWriter.close();
            }

            FileReader fileReader = new FileReader(file);
            String line = "" ;
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine())!= null ) {
                output.append(line + "\n");
            }
            json= output.toString();
            bufferedReader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }

    public static void writeJsonFile(File file, String json) {
        BufferedWriter bufferedWriter = null;
        try {

            if (!file.exists()) {
                Log.e("App","file not exist");
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void rechercher(String nomVille) {
        this.queue= Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL+nomVille,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"Response" + response);

                        try{
                            JSONObject json = new JSONObject(response);
                            JSONObject ville = json.getJSONObject("city_info");
                            String name = ville.getString("name");
                            String country = ville.getString("country");
                            JSONObject current = json.getJSONObject("current_condition");

                            String temperatureCourante = current.getString("tmp");

                            Log.i(TAG , "Ville : "+name +"  "+ temperatureCourante + "°C");

                            currentCity = name;
                            String cityText = name + ", " + country +  " (" + SuffixAdder.addDegreeSymbol(temperatureCourante) + ")";
                            villeRechercher.setText(cityText);
                            villeRechercher.setClickable(true);
                        } catch (JSONException e) {
                            villeRechercher.setText(R.string.no_city_found);
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Response" + error);
            }
        });
        this.queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
