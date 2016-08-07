package com.tanishqaggarwal.handshake;

/**
 * Created by Tanishq on 8/6/2016.
 */
public class SensorReading {
    private long time;
    private float[] data;

    public SensorReading() {
        data = new float[9];
    }

    public void setTime(long t) {
        time = t;
    }

    public void setAccelData(float[] accelData) {
        data[0] = accelData[0];
        data[1] = accelData[1];
        data[2] = accelData[2];
    }

    public void setGyroData(float[] gyroData) {
        data[3] = gyroData[0];
        data[4] = gyroData[1];
        data[5] = gyroData[2];
    }

    public void setMagneticData(float[] magneticData) {
        data[6] = magneticData[0];
        data[7] = magneticData[1];
        data[8] = magneticData[2];
    }

    public long getTime() {
        return time;
    }

    public float[] getAccelData() {
        return new float[] {data[0], data[1], data[2]};
    }

    public float[] getGyroData() {
        return new float[] {data[3], data[4], data[5]};
    }

    public float[] getMagneticData() {
        return new float[] {data[6], data[7], data[8]};
    }
}
