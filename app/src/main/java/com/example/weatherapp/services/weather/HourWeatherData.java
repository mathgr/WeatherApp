package com.example.weatherapp.services.weather;

import android.util.Log;

import com.example.weatherapp.services.SuffixAdder;

import org.json.JSONException;
import org.json.JSONObject;

public class HourWeatherData {
    private String hour;
    private String condition;
    private String iconCondition;
    private String temperature;

    public HourWeatherData(String hour, JSONObject hourData) {
        try {
            this.hour = hour;
            this.condition = hourData.getString("CONDITION");
            this.iconCondition = hourData.getString("ICON");
            this.temperature = hourData.getString("TMP2m")            ;
        } catch (JSONException e) {
            Log.e("HourWeatherData", e.getMessage());
        }
    }

    public String getHour() {
        return SuffixAdder.addHourSuffix(hour);
    }

    public String getCondition() {
        return condition;
    }

    public String getIconCondition() {
        return iconCondition;
    }

    public String getTemperature() {
        return SuffixAdder.addDegreeSymbol(temperature);
    }
}
