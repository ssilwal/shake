package com.tanishqaggarwal.handshake;

import com.google.gson.Gson;

import android.app.DownloadManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class SignActivity extends AppCompatActivity implements SensorEventListener {

    private TextView signText;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;
    private List<SensorReading> readings;
    private boolean capturing;

    private String shakeSaved = "Shake saved. ";
    private String startCapture = "Tap this text to start capturing your shake.";
    private String stopCapture = "Tap to stop capturing shake";

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        readings      = new LinkedList<>();

        username = getIntent().getStringExtra("username");
        signText = (TextView) findViewById(R.id.signText);

        signText.setText(startCapture);
    }

    public void toggleCapture(View v) {
        if (capturing) {
            capturing = false;
            signText.setText(shakeSaved + startCapture);

            Gson gsonObj = new Gson();
            String jsonData = gsonObj.toJson(new APIObject(username, "signature", readings));


        }
        else {
            capturing = true;
            signText.setText(stopCapture);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (capturing) {
            SensorReading reading = new SensorReading();
            SensorReading previousReading;
            if (readings.size() > 0) {
                previousReading = readings.get(readings.size() - 1);
            }
            else {
                previousReading = new SensorReading();
                previousReading.setTime(0);
                previousReading.setAccelData(new float[] {0f,0f,0f});
                previousReading.setGyroData(new float[] {0f,0f,0f});
                previousReading.setMagneticData(new float[] {0f,0f,0f});
            }

            reading.setTime(System.currentTimeMillis() - previousReading.getTime());
            reading.setAccelData(previousReading.getAccelData());
            reading.setGyroData(previousReading.getGyroData());
            reading.setMagneticData(previousReading.getMagneticData());

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                reading.setAccelData(new float[] {event.values[0], event.values[1], event.values[2]});
            }
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                reading.setAccelData(new float[] {event.values[0], event.values[1], event.values[2]});
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                reading.setAccelData(new float[] {event.values[0], event.values[1], event.values[2]});
            }

            readings.add(reading);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
