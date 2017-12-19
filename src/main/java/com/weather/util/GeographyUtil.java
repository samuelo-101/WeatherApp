package com.weather.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Generates geographic data, including conversion of lat;lng to pixels using Mercator Projection
 */
public class GeographyUtil {

    int mapWidth;
    int mapHeight;
    int[][] elevationImagePixels;

    private BufferedImage bufferedImage;

    private final String ELEVATION_FILE_NAME = "elevation.bmp";

    public GeographyUtil() {
        try {
            URL elevationFileURL = GeographyUtil.class.getClassLoader().getResource(ELEVATION_FILE_NAME);
            bufferedImage =  ImageIO.read(elevationFileURL);
            mapWidth = bufferedImage.getWidth();
            mapHeight = bufferedImage.getHeight();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int[][] loadAllPixelInformation() {
        elevationImagePixels = new int[mapWidth][mapHeight];
        for(int horizontalIndex = 0; horizontalIndex < mapWidth; horizontalIndex++) {
            for(int verticalIndex = 0; verticalIndex < mapHeight; verticalIndex++) {
                elevationImagePixels[horizontalIndex][verticalIndex] = bufferedImage.getRGB(horizontalIndex, verticalIndex);
            }
        }
        return elevationImagePixels;
    }

    public Double[][] getPixelFromLatLng(double lat, double lng) {
        Double[][] pixelLatLng = new Double[1][2];
        double x = (lng + 180) * (mapWidth / 360);
        double latitudeRadians = lat * Math.PI / 180;
        double mercator = Math.log(Math.tan((Math.PI / 4) + (latitudeRadians / 2)));
        double y = (mapHeight / 2) - (mapHeight * mercator / (2 * Math.PI));

        pixelLatLng[0][0] = x;
        pixelLatLng[0][1] = y;
        return pixelLatLng;
    }

    public double getElevationAtPixel(int x, int y) {
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
