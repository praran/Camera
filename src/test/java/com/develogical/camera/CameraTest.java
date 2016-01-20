package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Sensor sensor = context.mock(Sensor.class);
    private MemoryCard memoryCard = context.mock(MemoryCard.class);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerDown();
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOff();
    }


    @Test
    public void pressingShutterWhenPowerOffDoesNothing() {
        context.checking(new Expectations(){{
            never(sensor);
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.pressShutter();
    }


    @Test
    public void pressingShutterWhenPowerOnDoesCopiesDataFromSensorToMemoryCard() {
        final byte[] sensorData = "Test data".getBytes();

        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();will(returnValue(sensorData));
            exactly(1).of(memoryCard).write(sensorData);
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void pressingPowerDownWhenDataIsBeingWrittenSensorDoesNotPowerOff() {
        final byte[] sensorData = "Test data".getBytes();

        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(sensorData));
            exactly(1).of(memoryCard).write(sensorData);
            never(sensor).powerDown();
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
    }

    @Test
    public void onceWritingTheDataIsCompleteCameraPowerDownTheSensor() {
        final byte[] sensorData = "Test data".getBytes();

        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(sensorData));
            exactly(1).of(memoryCard).write(sensorData);
            exactly(1).of(sensor).powerDown();
        }});
        // SUT
        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
        camera.writeComplete();
    }
}
