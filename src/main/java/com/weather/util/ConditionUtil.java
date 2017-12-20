package com.weather.util;

import com.weather.ConditionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates condition data: Temperature, Pressure, and Condition Type
 */
public class ConditionUtil {

    private final int MAX_PRESSURE = 1079;
    private final int MIN_PRESSURE = 337;
    private final float DIFFERENCE_OF_EARTH_PRESSURE = MAX_PRESSURE - MIN_PRESSURE;

    /*private final double MAX_EARTH_TEMPERATURE = 58f;
    private final double MIN_EARTH_TEMPERATURE = -88f;*/
    private final double MAX_EARTH_TEMPERATURE = 40f;
    private final double MIN_EARTH_TEMPERATURE = -30f;
    private final double TOTAL_OF_EARTH_MAX_MIN_TEMPERATURES = MAX_EARTH_TEMPERATURE - MIN_EARTH_TEMPERATURE;

    private final int RED_CHANNEL_MAX = 256;

    private Map<Map<Integer, Integer>, Map<Double, Double>> temperatureRanges;
    private Map<Map<Integer, Integer>, Map<Double, Double>> pressureRanges;

    public ConditionUtil() {
        init();
    }

    private void init() {
        generateTemperatureData();
        generatePressureData();
    }

    private void generatePressureData() {
        pressureRanges = new HashMap<>();

        double redChannelMxToPressureRatio = RED_CHANNEL_MAX / DIFFERENCE_OF_EARTH_PRESSURE;

        double nextMin = MAX_PRESSURE - redChannelMxToPressureRatio;
        double nextMax = MAX_PRESSURE;

        int redChannelRangMin = 0;
        int redChannelRangMax = redChannelRangMin + (int) Math.ceil(redChannelMxToPressureRatio);

        while (redChannelRangMax <= 256) {
            Map<Integer, Integer> channelRange = new HashMap<>();
            Map<Double, Double> pressureRange = new HashMap<>();
            channelRange.put(redChannelRangMin, redChannelRangMax);
            pressureRange.put(nextMin, nextMax);
            pressureRanges.put(channelRange, pressureRange);
            nextMin = nextMin - redChannelMxToPressureRatio;
            nextMax = nextMax - redChannelMxToPressureRatio;

            redChannelRangMin += Math.ceil(redChannelMxToPressureRatio);
            redChannelRangMax += Math.ceil(redChannelMxToPressureRatio);
        }


    }

    /*
     * Generate max and min temperature values based on ratio of
     * red channel max value (255) : total of earth's max+ min temperatures
     */
    private void generateTemperatureData() {
        temperatureRanges = new HashMap<>();
        double RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO = RED_CHANNEL_MAX / TOTAL_OF_EARTH_MAX_MIN_TEMPERATURES;

        double nextMin = MAX_EARTH_TEMPERATURE - RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO;
        double nextMax = MAX_EARTH_TEMPERATURE;

        int redChannelRangMin = 0;
        int redChannelRangMax = redChannelRangMin + (int) Math.ceil(RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO);

        while (redChannelRangMax <= 256) {
            Map<Integer, Integer> channelRange = new HashMap<>();
            Map<Double, Double> temperatureRange = new HashMap<>();
            channelRange.put(redChannelRangMin, redChannelRangMax);
            temperatureRange.put(nextMin, nextMax);
            temperatureRanges.put(channelRange, temperatureRange);
            nextMin = nextMin - RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO;
            nextMax = nextMax - RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO;

            redChannelRangMin += Math.ceil(RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO);
            redChannelRangMax += Math.ceil(RED_CHANNEL_MAX_TO_TEMPERATURE_RATIO);
        }
    }

    public String getHumidityForTemperature(Double temperature) {
        ConditionEnum condition = ConditionEnum.valueOf(getConditionFromTemperature(temperature));
        switch (condition) {
            case Sunny:
                return String.valueOf(getRandomNumber(0.0, 33.33));
            case Snow:
                return String.valueOf(getRandomNumber(33.33, 66.66));
            case Rain:
                return String.valueOf(getRandomNumber(66.66, 100.0));
            default:
                return "0";
        }
    }

    public String getConditionFromTemperature(Double temperature) {
        if(temperature < 0)
            return ConditionEnum.Snow.name();
        else if(temperature >= 0 && temperature < 20)
            return ConditionEnum.Rain.name();
        else
            return ConditionEnum.Sunny.name();
    }

    public double generateTemperatureRangesForRedChannel(Double redChannelValue) {

        for (Map<Integer, Integer> channelRangeMap : temperatureRanges.keySet()) {
            int channelMin = channelRangeMap.keySet().iterator().next();
            int channelMax = channelRangeMap.get(channelMin);

            Map<Double, Double> temperatureMap = temperatureRanges.get(channelRangeMap);
            Double temperatureMin = temperatureMap.keySet().iterator().next();
            Double temperatureMax = temperatureMap.get(temperatureMin);

            if(redChannelValue >= channelMin && redChannelValue < channelMax) {
                return getRandomDouble(temperatureMin, temperatureMax);
            }
        }
        return 0;
    }

    public int generatePressureForRedChannel(Double redChannelValue) {
        for (Map<Integer, Integer> channelRangeMap : pressureRanges.keySet()) {
            int channelMin = channelRangeMap.keySet().iterator().next();
            int channelMax = channelRangeMap.get(channelMin);

            Map<Double, Double> pressureMap = pressureRanges.get(channelRangeMap);
            Double pressureMin = pressureMap.keySet().iterator().next();
            Double pressureMax = pressureMap.get(pressureMin);

            if(redChannelValue >= channelMin && redChannelValue < channelMax) {
                return getRandomDouble(pressureMin, pressureMax).intValue();
            }
        }
        return 0;
    }

    private Double getRandomDouble(Double min, Double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private int getRandomNumber(Double min, Double max) {
        return ThreadLocalRandom.current().nextInt(min.intValue(), max.intValue());
    }
}