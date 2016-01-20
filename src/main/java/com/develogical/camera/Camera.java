package com.develogical.camera;

public class Camera implements WriteListener{

    private boolean isOn;
    private Sensor sensor;
    private MemoryCard memoryCard;
    private boolean writeComplete;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if(isSwitchedOn()){
            byte[] sensorData = sensor.readData();
            memoryCard.write(sensorData);
        }
    }

    public void powerOn() {
        isOn = true;
        sensor.powerUp();
    }

    public void powerOff() {
        isOn = false;
        if(writeComplete)
        sensor.powerDown();
    }
    
    private boolean isSwitchedOn(){
        return this.isOn;
    }

    @Override
    public void writeComplete() {
        writeComplete = true;
        if(!isOn){
            sensor.powerDown();
        }
    }

}

