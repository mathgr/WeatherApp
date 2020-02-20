package com.example.weatherapp;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

public class AjoutVille extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "message.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_ville_display);

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
