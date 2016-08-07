package com.tanishqaggarwal.handshake;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Tanishq on 8/6/2016.
 */
public class APIObject {
    private String username;
    private String type;
    private List<SensorReading> data;

    public APIObject(String username, String type, List<SensorReading> data) {
        this.username = username;
        this.type     = type;
        this.data     = data;
    }
}
