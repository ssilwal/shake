package com.tanishqaggarwal.handshake;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SignActivity extends AppCompatActivity implements SensorEventListener {

    private TextView signText;
    private SensorManager sensorManager;
    private List<SensorReading> readings;
    private boolean capturing;

    private String shakeSaved = "Shake saved. ";
    private String startCapture = "Tap this text to start capturing your shake.";
    private String stopCapture = "Tap to stop capturing shake";
    private long timezero;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(SignActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(SignActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(SignActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);

        readings = new LinkedList<>();

        username = getIntent().getStringExtra("username");
        signText = (TextView) findViewById(R.id.signText);

        signText.setText(startCapture);
        timezero = System.currentTimeMillis();
    }

    public void toggleCapture(View v) {
        if (capturing) {
            capturing = false;
            signText.setText(shakeSaved + startCapture);

            new SigningTask().execute(new Gson().toJson(new APIObject(username, "signature", readings)));
            readings = new LinkedList<>();
        } else {
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
            } else {
                previousReading = new SensorReading();
                timezero = System.currentTimeMillis();
                previousReading.setTime(timezero);
                previousReading.setAccelData(new float[]{0f, 0f, 0f});
                previousReading.setGyroData(new float[]{0f, 0f, 0f});
                previousReading.setMagneticData(new float[]{0f, 0f, 0f});
            }

            reading.setTime(System.currentTimeMillis() - timezero);
            reading.setAccelData(previousReading.getAccelData());
            reading.setGyroData(previousReading.getGyroData());
            reading.setMagneticData(previousReading.getMagneticData());

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                reading.setAccelData(new float[]{event.values[0], event.values[1], event.values[2]});
            }
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                reading.setGyroData(new float[]{event.values[0], event.values[1], event.values[2]});
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                reading.setMagneticData(new float[]{event.values[0], event.values[1], event.values[2]});
            }

            readings.add(reading);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private class SigningTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... data) {
            try {
                JSONObject jsonData = new JSONObject(data[0]);
                String response = HTTPClient.postData("http://"  + getString(R.string.URL) + ":" + getString(R.string.PORT) + "/sign", jsonData);
                JSONObject jsonResponse = new JSONObject(response);
                return jsonResponse.getBoolean("signed");
            } catch (Exception e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), "Signature sent successfully!", Toast.LENGTH_LONG);
                startActivity(new Intent(SignActivity.this, MainActivity.class));
            } else {
                Log.e("HandShake", "Signature not sent");
                Toast.makeText(getApplicationContext(), "Signature not sent successfully.", Toast.LENGTH_LONG);
                startActivity(new Intent(SignActivity.this, MainActivity.class));
            }
        }

    }
}

