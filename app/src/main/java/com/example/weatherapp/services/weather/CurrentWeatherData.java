package com.example.weatherapp.services.weather;

import android.util.Log;

import com.example.weatherapp.services.SuffixAdder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CurrentWeatherData {
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

    private Date currentDate = null;

    private ArrayList<HourWeatherData> nextHours;
    private ArrayList<DayWeatherData> nextDays;

    public CurrentWeatherData(JSONObject jsonResponse) {
        try {
            JSONObject city = jsonResponse.getJSONObject("city_info");
            this.city = city.getString("name");

            JSONObject currentCondition = jsonResponse.getJSONObject("current_condition");
            this.iconCondition = currentCondition.getString("icon_big");

            JSONObject currentDay = jsonResponse.getJSONObject("fcst_day_0");
            this.dayShort = currentDay.getString("day_short");
            this.maxTemperature = currentDay.getString("tmax");
            this.minTemperature = currentDay.getString("tmin");

            JSONObject hourlyDataCurrentDay = currentDay.getJSONObject("hourly_data");

            int currentHour = getCurrentHour();

            JSONObject currentHourData = hourlyDataCurrentDay.getJSONObject(currentHour + "H00");
            this.temperature = currentHourData.getString("TMP2m");
            this.humidity = currentHourData.getString("RH2m");
            this.pressure = currentHourData.getString("PRMSL");
            this.windDirection = currentHourData.getString("WNDDIRCARD10");
            this.windSpeed = currentHourData.getString("WNDSPD10m");
            this.condition = currentHourData.getString("CONDITION");

            nextHours = new ArrayList<>();
            nextDays = new ArrayList<>();

            int nextHour;
            int day;
            for (int i = 1; i <= 4; i++) {
                nextHour = getNextHourInteger(i);
                day = getDayPositionInFunctionOfNumberOfHourToAddToTheCurrentDate(i);
                nextHours.add(new HourWeatherData(Integer.toString(nextHour), jsonResponse.getJSONObject("fcst_day_" + day).getJSONObject("hourly_data").getJSONObject(nextHour + "H00")));

                nextDays.add(new DayWeatherData(jsonResponse.getJSONObject("fcst_day_" + i)));
            }
        } catch (JSONException e) {
            Log.e("CurrentWeatherData", e.getMessage());
        }
    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return SuffixAdder.addDegreeSymbol(temperature);
    }

    public String getMaxTemperature() {
        return SuffixAdder.addDegreeSymbol(maxTemperature);
    }

    public String getMinTemperature() {
        return SuffixAdder.addDegreeSymbol(minTemperature);
    }

    public String getHumidity() {
        return SuffixAdder.addPercentageSymbol(humidity);
    }

    public String getPressure() {
        return SuffixAdder.addAtmosphericPressureUnit(pressure);
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getWindSpeed() {
        return SuffixAdder.addSpeedUnit(windSpeed);
    }

    public String getCondition() {
        return condition;
    }

    public String getIconCondition() {
        return iconCondition;
    }

    public String getDayShort() {
        return dayShort;
    }

    public ArrayList<HourWeatherData> getNextHours() {
        return nextHours;
    }

    public ArrayList<DayWeatherData> getNextDays() {
        return nextDays;
    }

    private Date getCurrentDate() {
        if (currentDate != null) {
            return currentDate;
        }

        Date currentDate = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentDate);

        return new Date();
    }

    private int getCurrentHour() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(getCurrentDate());

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getNextHourInteger(int hourToAdd) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.HOUR_OF_DAY, hourToAdd);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getDayPositionInFunctionOfNumberOfHourToAddToTheCurrentDate(int hourToAdd) {
        int dayToAdd = hourToAdd / 24;

        if (getCurrentHour() > getNextHourInteger(hourToAdd)) {
            dayToAdd++;
        }

        return dayToAdd;
    }
}
