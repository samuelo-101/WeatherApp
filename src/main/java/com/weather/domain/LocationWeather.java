package com.weather.domain;

/**
 * Created by samuelojo on 2017/12/17.
 */
public class LocationWeather {

    private String location;
    private String latitude;
    private String longitude;
    private String elevation;
    private String localTime;
    private String condition;
    private String temperature;
    private String pressure;
    private String humidity;

    public LocationWeather(String location, String latitude, String longitude, String elevation,
                           String localTime, String condition, String temperature, String pressure, String humidity) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.localTime = localTime;
        this.condition = condition;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosition() {
        return latitude + "," + longitude + "," + elevation;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
