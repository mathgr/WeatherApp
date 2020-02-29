package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReglageVille extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "message.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ville_display);
        ImageButton addVille = findViewById(R.id.addVille);
        addVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ReglageVille.this,AjoutVille.class);
                ReglageVille.this.startActivity(intent);
            }
        });

        final ListView listView = findViewById(R.id.listeViewViles);
        loadData();
        ImageButton returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String res = "Ville supprimée ! ";
                Toast toast = Toast.makeText(ReglageVille.this, res, Toast.LENGTH_SHORT);
                toast.show();
                deleteVille((int)id);
                loadData();
                return true;

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name ="";
                JSONObject villeName = null;
                try {
                    villeName = new JSONObject(parent.getItemAtPosition(position).toString());
                    name = villeName.getString("villeName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Intent intent = new Intent(ReglageVille.this,MainActivity.class);
                intent.putExtra("ville",name);
                Log.i("ville selectionné",name);
                ReglageVille.this.startActivity(intent);
            }
        });


    }

    public void loadData(){
        try {
            final ListView listView = findViewById(R.id.listeViewViles);
            String jsonVille = loadJSON() ;
            //JSONObject json = new JSONObject(jsonVille);
            JSONArray array = new JSONArray(jsonVille);

            List<Map<String, String>> dataList = new ArrayList<>(0);
            Map<String, String> dataItem;

            for (int i = 0; i < array.length(); i++) {

                JSONObject item = array.getJSONObject(i);
                String name = item.getString("name");

                dataItem = new HashMap<>(0);
                dataItem.put("villeName", name);
                dataList.add(dataItem);

            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(ReglageVille.this, dataList,
                    R.layout.listeview_item,
                    new String[]{"villeName"},
                    new int[]{R.id.villeName});
            listView.setAdapter(simpleAdapter);


            //finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    public void deleteVille(int idx){

        try {
            JSONArray jsonArray = new JSONArray(loadJSON());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                jsonArray.remove(idx);
            }
            File file = new File(ReglageVille.this.getFilesDir(), "villesfav.json");
            writeJsonFile(file,jsonArray.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


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



    public String loadJSON() {
        String json = null;
        FileWriter fileWriter = null ;
        BufferedWriter bufferedWriter = null ;
        File file = new File(ReglageVille.this.getFilesDir(), "villesfav.json");
        String line = "" ;
        try {
            StringBuffer output = new StringBuffer();
            if(!file.exists()){
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("[{\"name\" : \"Montepllier\"}]");
                bufferedWriter.close();
                Log.i("creation file", file.getPath());
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine())!= null ) {
                output.append(line + "\n");
            }

            json = output.toString();
            Log.i("find file", json);
            bufferedReader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
