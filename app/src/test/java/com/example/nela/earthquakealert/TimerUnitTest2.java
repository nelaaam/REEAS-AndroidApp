package com.example.nela.earthquakealert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimerUnitTest2 {
    @Test
    public void pActualTimerCalculation_isCorrect() {
        final double expected = 5.24; //in seconds
        double distance = 40962; //in meters
        int eStart = 5000; //in millis
        int sSpeed = 4; //m/ms

        double actual = new Alert().calculateSArrival(distance,sSpeed, eStart);

        assertEquals(expected, actual, 0.01);

    }
}
