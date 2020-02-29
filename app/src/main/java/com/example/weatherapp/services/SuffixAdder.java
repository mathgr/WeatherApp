package com.example.weatherapp.services;

public class SuffixAdder {

    public static String addDegreeSymbol(String tmp) {
        return tmp + "°";
    }

    public static String addHourSuffix(String hour) {
        return hour + "h";
    }

    public static String addPercentageSymbol(String text) {
        return text + "%";
    }

    public static String addSpeedUnit(String speed) {
        return speed + " km/h";
    }

    public static String addAtmosphericPressureUnit(String atmosphericPressure) {
        return atmosphericPressure + " hPa";
    }
}
