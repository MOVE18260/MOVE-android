package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import dev.ehyeon.move18260_googlemapsandroidapiexample.R;
import dev.ehyeon.move18260_googlemapsandroidapiexample.data.location.LocationSensor;
import dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel.LocationViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private static final String TAG = "MapFragment";

    private final LocationSensor locationSensor = LocationSensor.getLocationSensor();

    private long startTime;
    private long previousTime;
    private float totalDistance;
    private float averageSpeed;
    private float maxSpeed;

    private View view;
    private boolean tracking;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Polyline polyline;
    private List<LatLng> points;

    private TextView tvTotalDistance;
    private TextView tvAverageSpeed;
    private TextView tvMaxSpeed;
    private Button btnTracking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        // DEBUG 용도
        TextView tvLatitude = view.findViewById(R.id.latitude);
        TextView tvLongitude = view.findViewById(R.id.longitude);
        tvTotalDistance = view.findViewById(R.id.totalDistance);
        tvAverageSpeed = view.findViewById(R.id.averageSpeed);
        tvMaxSpeed = view.findViewById(R.id.maxSpeed);
        btnTracking = view.findViewById(R.id.trackingButton);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        btnTracking.setOnClickListener(v -> {
            if (tracking) {
                Log.d(TAG, "press STOP button");

                tracking = false;
                btnTracking.setText("START");

                stopLocationUpdates();

                tvTotalDistance.setText("이동 거리 = ");
                tvAverageSpeed.setText("평균 속력 = ");
            } else {
                Log.d(TAG, "press START button");

                tracking = true;
                btnTracking.setText("STOP");

                startLocationUpdates();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        LocationViewModel locationSensor = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new LocationViewModel(LocationSensor.getLocationSensor());
            }
        }).get(LocationViewModel.class);

        locationSensor.getLatitude().observe(getViewLifecycleOwner(),
                latitude -> tvLatitude.setText("위도 = " + latitude));
        locationSensor.getLongitude().observe(getViewLifecycleOwner(),
                longitude -> tvLongitude.setText("경도 = " + longitude));

        mapFragment.getMapAsync(this);

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        Location lastKnownLocation = locationSensor.getLastKnownLocation();

        // zoom = 15 == 반경 1.5km
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
    }

    private void startLocationUpdates() {
        startTime = previousTime = System.currentTimeMillis();
        totalDistance = averageSpeed = maxSpeed = 0;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000); // 1초
        locationRequest.setFastestInterval(5 * 100); // 0.5초
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();

                if (location != null) {
                    updateMap(location);

                    calculate();

                    if (totalDistance > 1000) {
                        tvTotalDistance.setText("이동 거리 = " + roundNumberToI(totalDistance / 1000, 1) + " km");
                    } else {
                        tvTotalDistance.setText("이동 거리 = " + roundNumberToI(totalDistance, 1) + " m");
                    }

                    tvAverageSpeed.setText("평균 속력 = " + roundNumberToI(averageSpeed, 1) + " km/h");
                    tvMaxSpeed.setText("최고 속력 = " + roundNumberToI(maxSpeed, 1) + "km/h");
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(view.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateMap(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        addPolyline(latLng);

        Log.d(TAG, "latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void addPolyline(LatLng latLng) {
        if (polyline == null) {
            polyline = googleMap.addPolyline(new PolylineOptions());
        }

        points = polyline.getPoints();
        points.add(latLng);
        polyline.setPoints(points);
    }

    // 이동 거리(meter)
    private void calculate() {
        // 이동 거리 계산
        float distance = getDistanceThroughPoints(points);

        totalDistance += distance;

        // km/h
        long currentTime = System.currentTimeMillis();

        averageSpeed = metersToKiloMeters(totalDistance) / millisecondsToHours(currentTime - startTime);

        // 최고 속력 보정 적용
        float currentMaxSpeed = metersToKiloMeters(distance) / millisecondsToHours(currentTime - previousTime);

        if (currentMaxSpeed < 36 && maxSpeed < currentMaxSpeed) {
            float correctionValue;

            if (maxSpeed < 3) {
                correctionValue = 1.1f;
            } else if (maxSpeed < 6) {
                correctionValue = 0.09f;
            } else {
                correctionValue = 0.07f;
            }

            maxSpeed += Math.min(correctionValue, currentMaxSpeed);
        }

        previousTime = currentTime;

        Log.d(TAG, "distance = " + distance);
        Log.d(TAG, "totalDistance = " + totalDistance);
        Log.d(TAG, "averageSpeed = " + averageSpeed);
        Log.d(TAG, "maxSpeed = " + maxSpeed);
    }

    private float getDistanceThroughPoints(List<LatLng> points) {
        return points.size() < 2 ? 0 : getDistanceBetweenTwoLatLng(points.get(points.size() - 2), points.get(points.size() - 1));
    }

    private float getDistanceBetweenTwoLatLng(LatLng latLng1, LatLng latLng2) {
        float[] distance = new float[1];

        Location.distanceBetween(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude, distance);

        return Math.abs(distance[0]);
    }

    private float roundNumberToI(float number, int i) {
        float pow = (float) Math.pow(10, i);

        return Math.round(number * pow) / pow;
    }

    private float metersToKiloMeters(float i) {
        return i / 1000;
    }

    private float millisecondsToHours(float i) {
        return i / 3600000; // 60 * 60 * 1000
    }

    @Override
    public void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        polyline.remove();
        polyline = null;

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}
