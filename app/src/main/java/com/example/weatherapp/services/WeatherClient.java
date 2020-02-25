package com.example.weatherapp.services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeatherClient {
    private String city;
    private String temperature;
    private String maxTemperature;
    private String minTemperature;
    private String humidity;
    private String pressure;
    private String windDirection;
    private String windSpeed;
    private String condition;
    private String iconCondition;
    private String dayShort;

    public WeatherClient(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            JSONObject city = jsonResponse.getJSONObject("city_info");
            this.city = city.getString("name");

            JSONObject currentDay = jsonResponse.getJSONObject("fcst_day_0");
            this.dayShort = currentDay.getString("day_short");
            this.maxTemperature = currentDay.getString("tmax");
            this.minTemperature = currentDay.getString("tmin");

            JSONObject hourlyDataCurrentDay = currentDay.getJSONObject("hourly_data");

            Date currentDate = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(currentDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            JSONObject currentHourData = hourlyDataCurrentDay.getJSONObject(hour + "H00");
            this.temperature = currentHourData.getString("TMP2m");
            this.humidity = currentHourData.getString("RH2m");
            this.pressure = currentHourData.getString("PRMSL");
            this.windDirection = currentHourData.getString("WNDDIRCARD10");
            this.windSpeed = currentHourData.getString("WNDSPD10m");
            this.condition = currentHourData.getString("CONDITION");
            this.iconCondition = currentHourData.getString("ICON");
        } catch (JSONException e) {
            Log.e("WeatherClient", e.getMessage());
        }
    }

    public String getCity() {
        return this.city;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public String getMaxTemperature() {
        return this.maxTemperature;
    }

    public String getMinTemperature() {
        return this.minTemperature;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public String getPressure() {
        return this.pressure;
    }

    public String getWindDirection() {
        return this.windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCondition() {
        return this.condition;
    }

    public String getIconCondition() {
        return this.iconCondition;
    }

    public String getDayShort() {
        return this.dayShort;
    }
}
