package com.self.distance;

/**
 * Created by nisha on 11-05-2017.
 */

public class Latlng {
    double Lat, Lng;

    public Latlng(double lat, double lng) {
        Lat = lat;
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public double getLng() {
        return Lng;
    }
}
