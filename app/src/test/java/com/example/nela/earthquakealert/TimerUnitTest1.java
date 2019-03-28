package com.example.nela.earthquakealert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimerUnitTest1 {
    @Test
    public void pActualTimerCalculation_isCorrect() {
        final double expected = 1.82; // in seconds
        double distance = 40962; // in meters
        int eStart = 5000; // in millis
        int pSpeed = 6; // m/ms
        double actual = new Alert().calculatePArrival(distance, pSpeed, eStart);

        assertEquals(expected, actual, 0.1);


    }
}
