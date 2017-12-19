package com.weather.adapter;

import com.weather.domain.LocationWeather;

import java.util.List;

/**
 * Adapter to render single table row
 */
public class TableAdapter {

    private List<LocationWeather> locationWeatherList;

    public TableAdapter(List<LocationWeather> locationWeatherList) {
        this.locationWeatherList = locationWeatherList;
    }

    public String renderItem(int position) {
        LocationWeather locationWeather = getItem(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(locationWeather.getLocation())
                .append("|")
                .append(locationWeather.getPosition())
                .append("|")
                .append(locationWeather.getLocalTime())
                .append("|")
                .append(locationWeather.getCondition())
                .append("|")
                .append(locationWeather.getTemperature())
                .append("|")
                .append(locationWeather.getPressure())
                .append("|")
                .append(locationWeather.getHumidity());
        return stringBuilder.toString();
    }

    private LocationWeather getItem(int position) {
        return this.locationWeatherList.get(position);
    }

    public int getSize() {
        return this.locationWeatherList.size();
    }
}
