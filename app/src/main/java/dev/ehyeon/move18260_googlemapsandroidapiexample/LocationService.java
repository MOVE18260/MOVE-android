package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService {

    private static final String TAG = "LocationService";
    private static final long MIN_TIME_INTERVAL = 1000;
    private static final float MIN_DISTANCE_CHANGE = 0;

    private static LocationService locationService = null;

    private LocationManager locationManager;
    private final LocationListenerImpl locationListener = new LocationListenerImpl();
    private Geocoder geocoder;

    private LocationService() {
    }

    private LocationService(LocationManager locationManager, Context context) {
        this.locationManager = locationManager;
        this.geocoder = new Geocoder(context, Locale.KOREA);
    }

    public static LocationService getLocationService() {
        return locationService;
    }

    public static LocationService setLocationService(LocationManager locationManager, Context context) {
        if (locationService == null) {
            locationService = new LocationService(locationManager, context);
        }
        return locationService;
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

    public String getAddress() {
        try {
            List<Address> address = geocoder.getFromLocation(locationListener.getLatitude(), locationListener.getLongitude(), 1);

            return address != null && address.size() > 0 ? address.get(0).getAddressLine(0) : "";
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }
}
