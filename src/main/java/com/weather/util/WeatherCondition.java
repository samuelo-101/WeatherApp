package com.weather.util;

import com.weather.ConditionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samuelojo on 2017/12/17.
 */
public class WeatherCondition {

    private Map<TemperatureRange, ConditionEnum> temperatureRangeConditionMap;

    public WeatherCondition() {
        temperatureRangeConditionMap = new HashMap<TemperatureRange, ConditionEnum>();

        TemperatureRange rainTemperatureRange = new TemperatureRange(2.0f, 12.0f);
        TemperatureRange snowTemperatureRange = new TemperatureRange(-99.0f, 1.99f);
        TemperatureRange sunnyTemperatureRange = new TemperatureRange(12.1f, 60.0f);

        temperatureRangeConditionMap.put(rainTemperatureRange, ConditionEnum.Rain);
        temperatureRangeConditionMap.put(snowTemperatureRange, ConditionEnum.Snow);
        temperatureRangeConditionMap.put(sunnyTemperatureRange, ConditionEnum.Sunny);
    }

    public Map<TemperatureRange, ConditionEnum> getTemperatureRangeConditionMap() {
        return temperatureRangeConditionMap;
    }

    public void setTemperatureRangeConditionMap(Map<TemperatureRange, ConditionEnum> temperatureRangeConditionMap) {
        this.temperatureRangeConditionMap = temperatureRangeConditionMap;
    }
}
