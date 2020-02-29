package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        ListView listView = findViewById(R.id.listeViewViles);
        try {
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

        ImageButton returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // TextView output = findViewById(R.id.output);
        //Intent intent = getIntent();
        //String text = intent.getExtras().getString(EXTRA_MESSAGE);
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
                bufferedWriter.write("{ \"villes\" : [] }");
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
