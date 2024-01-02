package com.example.unicarapp.data.model;

public class Ride {
    private double lat;
    private double lng;
    private String latLng;
    private String userId;

    public Ride() {
    }

    public Ride(double lat, double lng, String userId) {
        this.lat = lat;
        this.lng = lng;
        this.latLng = Ride.mergeCoordinates(this.lat, this.lng);
        this.userId = userId;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;

        double[] splitCoordinates = Ride.splitCoordinates(latLng);
        setLat(splitCoordinates[0]);
        setLng(splitCoordinates[1]);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }


    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static String mergeCoordinates(double lat, double lng) {
        double absLat = lat + 90;
        double absLng = lng + 180;

        int intLat = (int) absLat;
        double decLat = absLat - intLat;
        String formattedLat = String.format("%03d", intLat) + "." + String.format("%07d", Math.round(decLat * 1e7));

        int intLng = (int) absLng;
        double decLng = absLng - intLng;
        String formattedLng = String.format("%03d", intLng) + "." + String.format("%07d", Math.round(decLng * 1e7));

        return new String(formattedLat + "-" + formattedLng);
    }

    public static double[] splitCoordinates(String mergedValue) {
        String[] coords = mergedValue.split("-");

        double latitude = Double.valueOf(coords[0]) - 90;
        double longitude = Double.valueOf(coords[1]) - 180;

        return new double[]{latitude, longitude};
    }
}
