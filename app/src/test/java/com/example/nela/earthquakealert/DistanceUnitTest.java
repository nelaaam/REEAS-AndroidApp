package com.example.nela.earthquakealert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceUnitTest {
    @Test
    public void distanceCalculation_isCorrect() {
        final double expected = 40.962e3; // in meters
        double elat = 14.5995, elong = 120.9842, clat = 14.2456, clong = 120.8786; // manila to cavite in degrees
        Alert alert = new Alert();
        double actual = alert.calculateDistance(elat, elong, clat, clong);

        assertEquals(expected, actual, 0.5);
    }
}
