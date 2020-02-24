package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.services.DownloadImageTask;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton ville = findViewById(R.id.ville);

        ville.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this,ReglageVille.class);
                MainActivity.this.startActivity(intent);
            }
        });

        //TODO extract this initialization into a function

        TextView currentTemperature = findViewById(R.id.current_temperature);
        ImageView currentConditionIcon = findViewById(R.id.current_condition_icon);
        TextView currentCondition = findViewById(R.id.current_condition);

        currentTemperature.setText("32°");
        new DownloadImageTask(currentConditionIcon).execute("https://www.prevision-meteo.ch/style/images/icon/ensoleille-big.png");
        currentCondition.setText("Ensoleillé");

        View detailsCurrentWeatherGrid = findViewById(R.id.details_current_weather_grid);

        View humidityView = detailsCurrentWeatherGrid.findViewById(R.id.humidity);
        ImageView humidityIcon = humidityView.findViewById(R.id.detail_icon);
        TextView humidityPercentage = humidityView.findViewById(R.id.detail_value);
        TextView humidityLabel = humidityView.findViewById(R.id.detail_label);

        humidityIcon.setImageResource(R.drawable.water);
        humidityPercentage.setText("74 %");
        humidityLabel.setText("Humidité");


        View pressureView = detailsCurrentWeatherGrid.findViewById(R.id.pressure);
        ImageView pressureIcon = pressureView.findViewById(R.id.detail_icon);
        TextView pressureValue = pressureView.findViewById(R.id.detail_value);
        TextView pressureLabel = pressureView.findViewById(R.id.detail_label);

        pressureIcon.setImageResource(R.drawable.meter);
        pressureValue.setText("1018.6");
        pressureLabel.setText("Pression atmosphérique");


        View windDirectionView = detailsCurrentWeatherGrid.findViewById(R.id.wind_direction);
        ImageView windDirectionIcon = windDirectionView.findViewById(R.id.detail_icon);
        TextView windDirectionValue = windDirectionView.findViewById(R.id.detail_value);
        TextView windDirectionLabel = windDirectionView.findViewById(R.id.detail_label);

        windDirectionIcon.setImageResource(R.drawable.compass);
        windDirectionValue.setText("E");
        windDirectionLabel.setText("Direction du vent");

        View uvIndexView = detailsCurrentWeatherGrid.findViewById(R.id.uv_index);
        ImageView uvIndexIcon = uvIndexView.findViewById(R.id.detail_icon);
        TextView uvIndexValue = uvIndexView.findViewById(R.id.detail_value);
        TextView uvIndexLabel = uvIndexView.findViewById(R.id.detail_label);

        uvIndexIcon.setImageResource(R.drawable.uv);
        uvIndexValue.setText("Très faible");
        uvIndexLabel.setText("Indice UV");

    }
}
