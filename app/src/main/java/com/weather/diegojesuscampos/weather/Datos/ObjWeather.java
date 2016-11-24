package com.weather.diegojesuscampos.weather.Datos;

/**
 * Created by Diego Jesus Campos on 22/11/2016.
 */

public class ObjWeather {
    private String lng;
    private String lat;
    private String temperature;
    private String humidity;
    private String stationName;
    private String weatherCondition;



    public ObjWeather(String lng, String lat, String temperature, String humidity, String stationName, String weatherCondition) {
        this.lng = lng;
        this.lat = lat;
        this.temperature = temperature;
        this.humidity = humidity;
        this.stationName = stationName;
        this.weatherCondition = weatherCondition;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
