package com.weather.util;

import com.weather.domain.LocationWeather;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class GeographyUtilTest {

    @Test
    public void getLocationData() throws Exception {
        GeographyUtil geographyUtil = new GeographyUtil();
        List<LocationWeather> locationData = geographyUtil.getLocationData(30);
        assertNotNull(locationData);
        assertEquals(30, locationData.size());
        for (LocationWeather data : locationData) {
            assertNotNull(data.getLocation());
            assertNotNull(data.getPosition());
            assertNotNull(data.getLocalTime());
            assertNotNull(data.getCondition());
            assertNotNull(data.getHumidity());
            assertNotNull(data.getPressure());
            assertNotNull(data.getTemperature());
        }
    }

}