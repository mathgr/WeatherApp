package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


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

    @Override
    public void onBackPressed() {
        finish();
    }
}
