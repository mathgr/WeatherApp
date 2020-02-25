package com.example.weatherapp.services.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DayWeatherData {
    private String dayShort;
    private String condition;
    private String iconCondition;
    private String maxTemperature;
    private String minTemperature;

    public DayWeatherData(JSONObject dayData) {
        try {
            this.dayShort = dayData.getString("day_short");
            this.condition = dayData.getString("condition");
            this.iconCondition = dayData.getString("icon");
            this.maxTemperature = dayData.getString("tmax");
            this.minTemperature = dayData.getString("tmin");
        } catch (JSONException e) {
            Log.e("DayWeatherData", e.getMessage());
        }
    }

    public String getDayShort() {
        return dayShort;
    }

    public String getCondition() {
        return condition;
    }

    public String getIconCondition() {
        return iconCondition;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }
}
