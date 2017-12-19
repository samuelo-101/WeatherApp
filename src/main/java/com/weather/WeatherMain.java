package com.weather;

import com.weather.adapter.TableAdapter;
import com.weather.domain.LocationWeather;
import com.weather.renderer.TableRenderer;
import com.weather.util.GeographyUtil;
import com.weather.util.ConditionUtil;
import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main class: Will accept argument of how many records
 * to generate or fall back to a default of 20
 */
public class WeatherMain {

    public static final String GEO_DATA_FILE_NAME = "/simplemaps-worldcities-basic.csv";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_CITY_ASCII = "city_ascii";
    public static final String COLUMN_LATITUDE = "lat";
    public static final String COLUMN_LONGITUDE = "lng";
    public static final String COLUMN_POPULATION = "pop";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_ISO2 = "iso2";
    public static final String COLUMN_ISO3 = "iso3";
    public static final String COLUMN_PROVINCE = "province";

    public static final int COLUMN_CITY_INDEX = 0;
    public static final int COLUMN_CITY_ASCII_INDEX = 1;
    public static final int COLUMN_LATITUDE_INDEX = 2;
    public static final int COLUMN_LONGITUDE_INDEX = 3;
    public static final int COLUMN_POPULATION_INDEX = 4;
    public static final int COLUMN_COUNTRY_INDEX = 5;
    public static final int COLUMN_ISO2_INDEX = 6;
    public static final int COLUMN_ISO3_INDEX = 7;
    public static final int COLUMN_PROVINCE_INDEX = 8;


    public static final int TOTAL_NUMBER_OF_LOCATIONS_IN_CSV_DATA = 7322;
    public static final int DEFAULT_NUMBER_OF_CITIES_TO_PROCESS = 20;


    public static void main(String[] args) {

        int numberOfCities = DEFAULT_NUMBER_OF_CITIES_TO_PROCESS;

        if (args != null && args.length > 0) {
            numberOfCities = Integer.valueOf(args[0]);
            if(numberOfCities > TOTAL_NUMBER_OF_LOCATIONS_IN_CSV_DATA) {
                System.out.println("The number of desired locations cannot exceed " + TOTAL_NUMBER_OF_LOCATIONS_IN_CSV_DATA + ". PLease try again with a smaller number of desired results.");
                System.exit(1);
            }
        }

        try {
            buildAndRenderLocationWeather(numberOfCities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buildAndRenderLocationWeather(int numberOfCities) throws IOException {
        GeographyUtil geographyUtil = new GeographyUtil();
        ConditionUtil conditionUtil = new ConditionUtil();

        InputStream fileInputStream = WeatherMain.class.getResourceAsStream(GEO_DATA_FILE_NAME);
        //URL cityDataFileURL = WeatherMain.class.getClassLoader().getResource(GEO_DATA_FILE_NAME);
        if (fileInputStream != null) {

            List<LocationWeather> locationWeatherList = new ArrayList<>();

            List<String> geoData = IOUtils.readLines(fileInputStream);

            for (int interationCount = 1; interationCount < numberOfCities; interationCount++) {

                int randomCSVDataIndex = ThreadLocalRandom.current().nextInt(1, geoData.size());

                String cityInformation = geoData.get(randomCSVDataIndex);

                String[] cityGeoData = cityInformation.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String cityName = cityGeoData[COLUMN_CITY_INDEX];
                String latitude = cityGeoData[COLUMN_LATITUDE_INDEX];
                String longitude = cityGeoData[COLUMN_LONGITUDE_INDEX];

                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.now();

                Double[][] pixelFromLatLng = geographyUtil.getPixelFromLatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                String elevation = String.valueOf(geographyUtil.getElevationAtPixel(pixelFromLatLng[0][0].intValue(), pixelFromLatLng[0][1].intValue()));

                double locationTemperature = conditionUtil.generateTemperatureRangesForRedChannel(Double.valueOf(elevation));
                String temperature = String.format("%.1f", locationTemperature);
                String condition = conditionUtil.getConditoinFromTemperature(locationTemperature);
                int locationPressure = conditionUtil.generatePressureForRedChannel(Double.valueOf(elevation));
                String pressure = String.valueOf(locationPressure);
                String humidity = conditionUtil.getHumidityForTemperature(locationTemperature);

                LocationWeather locationWeather = new LocationWeather(cityName, latitude, longitude, elevation, dateTimeFormatter.print(localDateTime), condition, (locationTemperature >= 0 ? "+" + temperature : temperature), pressure, humidity);
                locationWeatherList.add(locationWeather);
            }

            TableRenderer tableRenderer = new TableRenderer();
            TableAdapter tableAdapter = new TableAdapter(locationWeatherList);
            tableRenderer.setAdapter(tableAdapter);
            tableRenderer.render();
        }
    }
}
