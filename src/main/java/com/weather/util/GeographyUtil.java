package com.weather.util;

import com.weather.WeatherMain;
import com.weather.domain.LocationWeather;
import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates geographic data, including conversion of lat;lng to pixels using Mercator Projection
 */
public class GeographyUtil {

    private final String GEO_DATA_FILE_NAME = "/simplemaps-worldcities-basic.csv";
    private final String ELEVATION_FILE_NAME = "/elevation.bmp";

    private final int COLUMN_CITY_INDEX = 0;
    private final int COLUMN_LATITUDE_INDEX = 2;
    private final int COLUMN_LONGITUDE_INDEX = 3;

    private int mapWidth;
    private int mapHeight;
    private int[][] elevationImagePixels;

    private BufferedImage bufferedImage;
    private ConditionUtil conditionUtil;

    public GeographyUtil() {
        try {
            bufferedImage =  ImageIO.read(GeographyUtil.class.getResourceAsStream(ELEVATION_FILE_NAME));
            mapWidth = bufferedImage.getWidth();
            mapHeight = bufferedImage.getHeight();
            conditionUtil = new ConditionUtil();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocationWeather> getLocationData(int numberOfCities) {
        try {
            List<LocationWeather> locationWeatherList = new ArrayList<>();
            InputStream fileInputStream = WeatherMain.class.getResourceAsStream(GEO_DATA_FILE_NAME);
            if (fileInputStream != null) {
                List<String> geoData = IOUtils.readLines(fileInputStream);

                for (int interationCount = 1; interationCount < numberOfCities + 1; interationCount++) {

                    int randomCSVDataIndex = ThreadLocalRandom.current().nextInt(1, geoData.size());

                    String cityInformation = geoData.get(randomCSVDataIndex);

                    String[] cityGeoData = cityInformation.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    String cityName = cityGeoData[COLUMN_CITY_INDEX];
                    String latitude = cityGeoData[COLUMN_LATITUDE_INDEX];
                    String longitude = cityGeoData[COLUMN_LONGITUDE_INDEX];

                    Calendar startDate = Calendar.getInstance();
                    startDate.set(2015, Calendar.JANUARY, 1);
                    Calendar endDate = Calendar.getInstance();
                    long dateForLocation = ThreadLocalRandom.current().nextLong(startDate.getTimeInMillis(), endDate.getTimeInMillis());
                    LocalDateTime localDateTime = new LocalDateTime(dateForLocation);

                    Double[][] pixelFromLatLng = getPixelFromLatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    String elevation = String.valueOf(getElevationAtPixel(pixelFromLatLng[0][0].intValue(), pixelFromLatLng[0][1].intValue()));

                    double locationTemperature = conditionUtil.generateTemperatureRangesForRedChannel(Double.valueOf(elevation));
                    String temperature = String.format("%.1f", locationTemperature);
                    String condition = conditionUtil.getConditionFromTemperature(locationTemperature);
                    int locationPressure = conditionUtil.generatePressureForRedChannel(Double.valueOf(elevation));
                    String pressure = String.valueOf(locationPressure);
                    String humidity = conditionUtil.getHumidityForTemperature(locationTemperature);

                    LocationWeather locationWeather = new LocationWeather(cityName, latitude, longitude, elevation, localDateTime.toString(), condition, (locationTemperature >= 0 ? "+" + temperature : temperature), pressure, humidity);
                    locationWeatherList.add(locationWeather);
                }
                fileInputStream.close();
            }
            return locationWeatherList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int[][] loadAllPixelInformation() {
        elevationImagePixels = new int[mapWidth][mapHeight];
        for(int horizontalIndex = 0; horizontalIndex < mapWidth; horizontalIndex++) {
            for(int verticalIndex = 0; verticalIndex < mapHeight; verticalIndex++) {
                elevationImagePixels[horizontalIndex][verticalIndex] = bufferedImage.getRGB(horizontalIndex, verticalIndex);
            }
        }
        return elevationImagePixels;
    }

    private Double[][] getPixelFromLatLng(double lat, double lng) {
        Double[][] pixelLatLng = new Double[1][2];
        double x = (lng + 180) * (mapWidth / 360);
        double latitudeRadians = lat * Math.PI / 180;
        double mercator = Math.log(Math.tan((Math.PI / 4) + (latitudeRadians / 2)));
        double y = (mapHeight / 2) - (mapHeight * mercator / (2 * Math.PI));

        pixelLatLng[0][0] = x;
        pixelLatLng[0][1] = y;
        return pixelLatLng;
    }

    private double getElevationAtPixel(int x, int y) {
        return getPixelRedChannel(bufferedImage.getRGB(x, y));
    }

    private int getPixelRedChannel(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    private int getPixelGreenChannel(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    private int getPixelBlueChannel(int pixel) {
        return pixel & 0xFF;
    }
}
