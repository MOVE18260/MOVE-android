package dev.ehyeon.move18260_googlemapsandroidapiexample.data.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import androidx.lifecycle.LiveData;

public class LocationSensor {

    private static final long MIN_TIME_INTERVAL = 1000;
    private static final float MIN_DISTANCE_CHANGE = 0;

    private static LocationSensor locationSensor = null;

    private final LocationManager locationManager;
    private final LocationListenerImpl locationListener = new LocationListenerImpl();

    private LocationSensor(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public static LocationSensor getLocationSensor() {
        return locationSensor;
    }

    public static LocationSensor setLocationSensor(LocationManager locationManager) {
        if (locationSensor == null) {
            locationSensor = new LocationSensor(locationManager);
        }

        return locationSensor;
    }

    @SuppressLint("MissingPermission")
    public void startListening() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE_CHANGE, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE_CHANGE, locationListener);
    }

    public void stopListening() {
        locationManager.removeUpdates(locationListener);
    }

    @SuppressLint("MissingPermission")
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

    public LiveData<Double> getLatitude() {
        return locationListener.getLatitude();
    }

    public LiveData<Double> getLongitude() {
        return locationListener.getLongitude();
    }
}
