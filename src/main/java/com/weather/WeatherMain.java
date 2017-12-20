package com.weather;

import com.weather.adapter.TableAdapter;
import com.weather.domain.LocationWeather;
import com.weather.renderer.TableRenderer;
import com.weather.util.GeographyUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Main class: Will accept argument of how many records
 * to generate or fall back to a default of 20
 */
public class WeatherMain {

    public static void main(String[] args) {
        int totalRecordsOfCSVData = 7321;
        int numberOfCities = 20;

        if (args != null && args.length > 0) {
            if(!Pattern.matches("[0-9]+", args[0])) {
                System.out.println("Only integer values are allowed when specifying the number of records to generate. Please try again.");
                System.exit(1);
            }
            numberOfCities = Integer.valueOf(args[0]);
            if(numberOfCities > totalRecordsOfCSVData) {
                System.out.println("The number of desired locations cannot exceed " + totalRecordsOfCSVData + ". PLease try again with a smaller number of desired results.");
                System.exit(1);
            }
        }

        buildAndRenderLocationWeather(numberOfCities);
    }

    private static void buildAndRenderLocationWeather(int numberOfCities) {
        GeographyUtil geographyUtil = new GeographyUtil();
        List<LocationWeather> locationData = geographyUtil.getLocationData(numberOfCities);
        TableRenderer tableRenderer = new TableRenderer();
        TableAdapter tableAdapter = new TableAdapter(locationData);
        tableRenderer.setAdapter(tableAdapter);
        tableRenderer.render();
    }
}
