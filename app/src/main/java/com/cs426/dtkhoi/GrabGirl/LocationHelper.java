package com.cs426.dtkhoi.GrabGirl;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class LocationHelper {
    public static boolean bRealData = true;
    public static Context context;
    private static LatLng simulatedLocation;
    private static int PERMISSION_REQUEST = 1;
    private static LatLng realLocation;


    public static LatLng getCurrentLocation() {
        if (bRealData)
            return realLocation;
        else
            return simulatedLocation;
    }

    public static void setSimulatedLocation(LatLng newLatLng){
        LocationHelper.simulatedLocation = newLatLng;
    }

    public static void setRealLocation(LatLng realLocation) {
        LocationHelper.realLocation = realLocation;
    }
}