package com.tanishqaggarwal.handshake;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class AuthenticateActivity extends AppCompatActivity implements SensorEventListener, Response.Listener<JSONObject> {

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
        setContentView(R.layout.activity_authenticate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(AuthenticateActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        sensorManager.registerListener(AuthenticateActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        sensorManager.registerListener(AuthenticateActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        readings      = new LinkedList<>();

        username = getIntent().getStringExtra("username");
        signText = (TextView) findViewById(R.id.authenticateText);

        signText.setText(startCapture);

    }

    public void toggleCapture(View v) {
        if (capturing) {
            capturing = false;
            signText.setText(shakeSaved + startCapture);

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            Gson gsonObj = new Gson();
            try {
                JSONObject jsonData = new JSONObject(gsonObj.toJson(new APIObject(username, "authentication", readings)));
                JsonObjectRequest jsonObj = new JsonObjectRequest("http://" + R.string.URL + R.string.PORT + "/authenticate", jsonData, this, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AuthenticateActivity.this, MainActivity.class));
                        finish();
                    }
                });
                requestQueue.add(jsonObj);
            }
            catch (JSONException e) {
            }
        }
        else {
            capturing = true;
            signText.setText(stopCapture);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Intent i;
        try {
            if (response.getBoolean("authenticated")) {
                Toast.makeText(getApplicationContext(), "Authentication successful!", Toast.LENGTH_LONG).show();
            } else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_LONG).show();
        }
        i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
        //Read response
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
