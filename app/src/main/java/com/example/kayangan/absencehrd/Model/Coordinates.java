package com.example.kayangan.absencehrd.Model;

import com.google.android.gms.maps.model.LatLng;

public class Coordinates {
    private String key, name, zone;
    private LatLng latLng;

    public Coordinates() {
    }

    public Coordinates(String key, String name, String zone, LatLng latLng) {
        this.key = key;
        this.name = name;
        this.zone = zone;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
