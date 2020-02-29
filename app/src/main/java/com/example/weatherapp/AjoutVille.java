package com.example.weatherapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjoutVille extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "message.extra";
    public final static String TAG = "RechercheVille";

    final String BASE_URL = "https://www.prevision-meteo.ch/services/json/";
    protected RequestQueue queue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_ville_display);
        final AutoCompleteTextView textRecherche = findViewById(R.id.searchVilleText);

        final Button rechercher = findViewById(R.id.searchVille);
        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rechercher(textRecherche.getText().toString());

            }
        });

        final TextView villeAfficher = findViewById(R.id.villeRechercher);
        villeAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(villeAfficher.getText()!=null) {
                   Log.i("enregistrement", "ville : " + villeAfficher.getText());

               }}
        });


    }



    private void rechercher(String nomVille) {
        this.queue= Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL+nomVille,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"Response" + response);

                        try{
                            final TextView villeRechercher = findViewById(R.id.villeRechercher);
                            JSONObject json = new JSONObject(response);
                            JSONObject ville = json.getJSONObject("city_info");
                            String name = ville.getString("name");
                            JSONObject current = json.getJSONObject("current_condition");

                            String temperatureCourante = current.getString("tmp");

                            Log.i(TAG , "Ville : "+name +"  "+ temperatureCourante + "°C");
                            String infoVille = name +"  "+ temperatureCourante + "°C" ;

                            villeRechercher.setText(infoVille);
                        } catch (JSONException e) {
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
