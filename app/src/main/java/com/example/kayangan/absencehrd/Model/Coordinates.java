package com.example.kayangan.absencehrd.Model;

import com.google.android.gms.maps.model.LatLng;

public class Coordinates {
    private String key, name, zone;
    private LatLng latLng;
    private int id;

    public Coordinates() {
    }

    public Coordinates(String name, String zone, LatLng latLng, int id) {
        this.name = name;
        this.zone = zone;
        this.latLng = latLng;
        this.id = id;
    }

    public Coordinates(String key, String name, String zone, LatLng latLng) {
        this.key = key;
        this.name = name;
        this.zone = zone;
        this.latLng = latLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
