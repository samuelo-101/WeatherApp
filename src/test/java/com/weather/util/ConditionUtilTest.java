package com.weather.util;

import com.weather.ConditionEnum;
import org.junit.Test;

import static org.junit.Assert.*;


public class ConditionUtilTest {

    @Test
    public void getHumidityForTemperature() throws Exception {
        ConditionUtil conditionUtil = new ConditionUtil();
        String sunnyConditionHumidity = conditionUtil.getHumidityForTemperature(44.0);
        assertNotNull(sunnyConditionHumidity);
        assertTrue(Double.valueOf(sunnyConditionHumidity) >= 0.0 && Double.valueOf(sunnyConditionHumidity) <= 33.33);

        String snowConditionHumidity = conditionUtil.getHumidityForTemperature(-20.0);
        assertNotNull(snowConditionHumidity);
        assertTrue(Double.valueOf(snowConditionHumidity) >= 33.33 && Double.valueOf(snowConditionHumidity) <= 66.66);

        String rainConditionHumidity = conditionUtil.getHumidityForTemperature(12.0);
        assertNotNull(rainConditionHumidity);
        assertTrue(Double.valueOf(rainConditionHumidity) >= 66.66 && Double.valueOf(rainConditionHumidity) <= 100.0);
    }

    @Test
    public void getConditionFromTemperature() throws Exception {
        ConditionUtil conditionUtil = new ConditionUtil();
        String snowCondition = conditionUtil.getConditionFromTemperature(-20.0);
        assertNotNull(snowCondition);
        assertEquals(ConditionEnum.Snow.name(), snowCondition);

        String rainCondition = conditionUtil.getConditionFromTemperature(13.0);
        assertNotNull(rainCondition);
        assertEquals(ConditionEnum.Rain.name(), rainCondition);

        String sunnyCondition = conditionUtil.getConditionFromTemperature(29.0);
        assertNotNull(sunnyCondition);
        assertEquals(ConditionEnum.Sunny.name(), sunnyCondition);
    }

    @Test
    public void generateTemperatureRangesForRedChannel() throws Exception {
        ConditionUtil conditionUtil = new ConditionUtil();
        double temperature = conditionUtil.generateTemperatureRangesForRedChannel(255.0);
        assertNotNull(temperature);

        double invalidRedChannelTemperature = conditionUtil.generateTemperatureRangesForRedChannel(999.0);
        assertEquals(0, invalidRedChannelTemperature, 0);
    }

    @Test
    public void generatePressureForRedChannel() throws Exception {
        ConditionUtil conditionUtil = new ConditionUtil();
        int pressure = conditionUtil.generatePressureForRedChannel(180.0);
        assertNotNull(pressure);

        int invalidRedChannelPressure = conditionUtil.generatePressureForRedChannel(-2.2);
        assertEquals(0, invalidRedChannelPressure, 0);
    }

}