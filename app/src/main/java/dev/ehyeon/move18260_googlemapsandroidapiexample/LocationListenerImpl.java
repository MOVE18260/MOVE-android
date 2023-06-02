package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class LocationListenerImpl implements LocationListener {

    private static final String TAG = "GPS";

    private double latitude;
    private double longitude;

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d(TAG, "latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
