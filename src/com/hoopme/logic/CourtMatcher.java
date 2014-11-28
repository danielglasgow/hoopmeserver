package com.hoopme.logic;

import com.hoopme.objects.Court;

public class CourtMatcher {

    public static int COURT_RADIUS_METERS = 5;

    public static boolean isMatch(Court court, LatLng latlng) {
        System.out.println("lat: " + latlng.lat + " long" + latlng.lng);
        double distance = VincentyDistanceCalculator.getDistanceMeters(court.latlng.lat,
                court.latlng.lng, latlng.lat, latlng.lng);
        System.out.println("distance" + distance);
        return distance < COURT_RADIUS_METERS;
    }
}
