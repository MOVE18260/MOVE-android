package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import dev.ehyeon.move18260_googlemapsandroidapiexample.data.location.LocationSensor;

public class GoogleMapImpl implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private final LocationSensor locationSensor = LocationSensor.getLocationSensor();

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        Location lastKnownLocation = locationSensor.getLastKnownLocation();

        // zoom = 15 == 반경 1.5km
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
    }

    // 내 위치 버튼 클릭
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    // 파란색 점 클릭
    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}
