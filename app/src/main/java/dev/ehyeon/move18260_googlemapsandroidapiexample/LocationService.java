package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

public class LocationService {

    private static final long MIN_TIME_INTERVAL = 1000;
    private static final float MIN_DISTANCE_CHANGE = 0;

    private final LocationManager locationManager;
    private final LocationListenerImpl locationListener = new LocationListenerImpl();

    public LocationService(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @SuppressLint("MissingPermission") // permission 필요
    public void startListening() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE_CHANGE, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE_CHANGE, locationListener);
    }

    public void stopListening() {
        locationManager.removeUpdates(locationListener);
    }

    @SuppressLint("MissingPermission")  // permission 필요
    public Location getLastKnownLocation() {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation != null) {
            return lastKnownLocation;
        } else {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastKnownLocation != null) {
                return lastKnownLocation;
            } else {
                Location tempLocation = new Location(LocationManager.GPS_PROVIDER);
                tempLocation.setLatitude(35.9078);
                tempLocation.setLongitude(127.7669);

                return tempLocation;
            }
        }
    }

    public double getLatitude() {
        return locationListener.getLatitude();
    }

    public double getLongitude() {
        return locationListener.getLongitude();
    }
}
