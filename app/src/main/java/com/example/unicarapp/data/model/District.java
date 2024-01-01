package com.example.unicarapp.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class District {

    public static List<GeoPoint> cityBound = new ArrayList<>();
    private String name;
    private ArrayList<GeoPoint> bounds;

    public District() {}

    public District(String name, ArrayList<GeoPoint> bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GeoPoint> getBounds() {
        return bounds;
    }

    public void setBounds(ArrayList<GeoPoint> bounds) {
        this.bounds = bounds;
    }
}
