package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.services.DownloadImageTask;
import com.example.weatherapp.services.WeatherClient;
import com.example.weatherapp.services.weather.CurrentWeatherData;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue;

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

        this.queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.prevision-meteo.ch/services/json/montpellier", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                WeatherClient weatherClient= new WeatherClient(response);
                CurrentWeatherData currentWeatherData = weatherClient.getCurrentWeatherData();

                //TODO extract this initialization into a function

                TextView currentTemperature = findViewById(R.id.current_temperature);
                ImageView currentConditionIcon = findViewById(R.id.current_condition_icon);
                TextView currentCondition = findViewById(R.id.current_condition);

                currentTemperature.setText(currentWeatherData.getTemperature());
                new DownloadImageTask(currentConditionIcon).execute(currentWeatherData.getIconCondition());
                currentCondition.setText(currentWeatherData.getCondition());

                View detailsCurrentWeatherGrid = findViewById(R.id.details_current_weather_grid);

                View humidityView = detailsCurrentWeatherGrid.findViewById(R.id.humidity);
                ImageView humidityIcon = humidityView.findViewById(R.id.detail_icon);
                TextView humidityPercentage = humidityView.findViewById(R.id.detail_value);
                TextView humidityLabel = humidityView.findViewById(R.id.detail_label);

                humidityIcon.setImageResource(R.drawable.water);
                humidityPercentage.setText(currentWeatherData.getHumidity());
                humidityLabel.setText(R.string.humidity_label);

                View pressureView = detailsCurrentWeatherGrid.findViewById(R.id.pressure);
                ImageView pressureIcon = pressureView.findViewById(R.id.detail_icon);
                TextView pressureValue = pressureView.findViewById(R.id.detail_value);
                TextView pressureLabel = pressureView.findViewById(R.id.detail_label);

                pressureIcon.setImageResource(R.drawable.meter);
                pressureValue.setText(currentWeatherData.getPressure());
                pressureLabel.setText(R.string.pressure_label);

                View windDirectionView = detailsCurrentWeatherGrid.findViewById(R.id.wind_direction);
                ImageView windDirectionIcon = windDirectionView.findViewById(R.id.detail_icon);
                TextView windDirectionValue = windDirectionView.findViewById(R.id.detail_value);
                TextView windDirectionLabel = windDirectionView.findViewById(R.id.detail_label);

                windDirectionIcon.setImageResource(R.drawable.compass);
                windDirectionValue.setText(currentWeatherData.getWindDirection());
                windDirectionLabel.setText(R.string.wind_direction_label);

                View windSpeedView = detailsCurrentWeatherGrid.findViewById(R.id.wind_speed);
                ImageView windSpeedIcon = windSpeedView.findViewById(R.id.detail_icon);
                TextView windSpeedValue = windSpeedView.findViewById(R.id.detail_value);
                TextView windSpeedLabel = windSpeedView.findViewById(R.id.detail_label);

                windSpeedIcon.setImageResource(R.drawable.wind);
                windSpeedValue.setText(currentWeatherData.getWindSpeed());
                windSpeedLabel.setText(R.string.wind_speed_label);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RequestError", error.getMessage());
            }
        });
        this.queue.add(stringRequest);
    }
}
