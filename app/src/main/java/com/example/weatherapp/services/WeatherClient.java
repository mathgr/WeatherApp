package com.example.weatherapp.services;

import android.util.Log;

import com.example.weatherapp.services.weather.CurrentWeatherData;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherClient {
    private CurrentWeatherData currentWeatherData;

    public WeatherClient(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            this.currentWeatherData = new CurrentWeatherData(jsonResponse);
        } catch (JSONException e) {
            Log.e("WeatherClient", e.getMessage());
        }
    }

    public CurrentWeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }
}
