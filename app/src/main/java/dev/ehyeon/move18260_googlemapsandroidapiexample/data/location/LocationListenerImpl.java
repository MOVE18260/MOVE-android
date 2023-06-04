package dev.ehyeon.move18260_googlemapsandroidapiexample.data.location;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class LocationListenerImpl implements LocationListener {

    private static final String TAG = "LocationListenerImpl";

    private final MutableLiveData<Double> latitude = new MutableLiveData<>(35.9078);
    private final MutableLiveData<Double> longitude = new MutableLiveData<>(127.7669);

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
        latitude.setValue(location.getLatitude());
        longitude.setValue(location.getLongitude());
        Log.d(TAG, "latitude = " + latitude.getValue() + " longitude = " + longitude.getValue());
    }

    public MutableLiveData<Double> getLatitude() {
        return latitude;
    }

    public MutableLiveData<Double> getLongitude() {
        return longitude;
    }
}
