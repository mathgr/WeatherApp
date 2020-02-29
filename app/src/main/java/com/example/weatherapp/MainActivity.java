package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.weatherapp.services.weather.DayWeatherData;
import com.example.weatherapp.services.weather.HourWeatherData;

import java.util.ArrayList;
import java.util.List;

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.prevision-meteo.ch/services/json/"+"Montpellier", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                WeatherClient weatherClient= new WeatherClient(response);
                CurrentWeatherData currentWeatherData = weatherClient.getCurrentWeatherData();
                ArrayList<HourWeatherData> nextHours = currentWeatherData.getNextHours();

                TextView currentTemperature = findViewById(R.id.current_temperature);
                ImageView currentConditionIcon = findViewById(R.id.current_condition_icon);
                TextView currentCondition = findViewById(R.id.current_condition);

                currentTemperature.setText(currentWeatherData.getTemperature());
                new DownloadImageTask(currentConditionIcon).execute(currentWeatherData.getIconCondition());
                currentCondition.setText(currentWeatherData.getCondition());

                setUpDetail(R.id.humidity, R.drawable.water, currentWeatherData.getHumidity(), R.string.humidity_label);
                setUpDetail(R.id.pressure, R.drawable.meter, currentWeatherData.getPressure(), R.string.pressure_label);
                setUpDetail(R.id.wind_direction, R.drawable.compass, currentWeatherData.getWindDirection(), R.string.wind_direction_label);
                setUpDetail(R.id.wind_speed, R.drawable.wind, currentWeatherData.getWindSpeed(), R.string.wind_speed_label);

                setUpNextHour(R.id.next_hour_1, nextHours.get(0));
                setUpNextHour(R.id.next_hour_2, nextHours.get(1));
                setUpNextHour(R.id.next_hour_3, nextHours.get(2));
                setUpNextHour(R.id.next_hour_4, nextHours.get(3));

                setUpNextDay(R.id.next_day_1, currentWeatherData.getNextDays().get(0));
                setUpNextDay(R.id.next_day_2, currentWeatherData.getNextDays().get(1));
                setUpNextDay(R.id.next_day_3, currentWeatherData.getNextDays().get(2));
                setUpNextDay(R.id.next_day_4, currentWeatherData.getNextDays().get(3));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RequestError", error.getMessage());
            }
        });
        this.queue.add(stringRequest);
    }

    private void setUpDetail(int idRes, int idResDrawable, String dataToSetUp, int idResLabel) {
        View detailsCurrentWeatherGrid = findViewById(R.id.details_current_weather_grid);

        View viewDetail = detailsCurrentWeatherGrid.findViewById(idRes);
        ImageView icon = viewDetail.findViewById(R.id.detail_icon);
        TextView data = viewDetail.findViewById(R.id.detail_value);
        TextView label = viewDetail.findViewById(R.id.detail_label);

        icon.setImageResource(idResDrawable);
        data.setText(dataToSetUp);
        label.setText(idResLabel);
    }



    private void setUpNextHour(int idRes, HourWeatherData nextHourData) {
        View nextHourWeather = findViewById(idRes);
        TextView hour = nextHourWeather.findViewById(R.id.hour);
        ImageView iconCondition = nextHourWeather.findViewById(R.id.condition_icon);
        TextView condition = nextHourWeather.findViewById(R.id.condition);
        TextView temperature = nextHourWeather.findViewById(R.id.temperature);

        hour.setText(nextHourData.getHour());
        new DownloadImageTask(iconCondition).execute(nextHourData.getIconCondition());
        condition.setText(nextHourData.getCondition());
        temperature.setText(nextHourData.getTemperature());
    }

    private void setUpNextDay(int idRes, DayWeatherData nextDayData) {
        View nextDayWeather = findViewById(idRes);
        TextView dayShort = nextDayWeather.findViewById(R.id.day_short);
        ImageView iconCondition = nextDayWeather.findViewById(R.id.condition_icon);
        TextView condition = nextDayWeather.findViewById(R.id.condition);
        TextView minMaxTemperature = nextDayWeather.findViewById(R.id.min_max_temperature);

        dayShort.setText(nextDayData.getDayShort());
        new DownloadImageTask(iconCondition).execute(nextDayData.getIconCondition());
        condition.setText(nextDayData.getCondition());
        String minMaxTmpString = nextDayData.getMinTemperature() + "/" + nextDayData.getMaxTemperature();
        minMaxTemperature.setText(minMaxTmpString);
    }
}
