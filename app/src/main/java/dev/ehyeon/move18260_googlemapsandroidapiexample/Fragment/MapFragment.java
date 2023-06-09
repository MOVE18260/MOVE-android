package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.List;

import dev.ehyeon.move18260_googlemapsandroidapiexample.R;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private static final String TAG = "MapFragment";

    private boolean tracking;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Polyline polyline;
    private Marker startMarker;
    private Marker finishMarker;

    private TextView tvTotalDistance;
    private TextView tvAverageSpeed;
    private TextView tvMaxSpeed;
    private Button btnTracking;

    private long startTime;
    private long previousTime;
    private float totalDistance;
    private float averageSpeed;
    private float maxSpeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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

                List<LatLng> points = polyline.getPoints();

                startMarker = googleMap.addMarker(new MarkerOptions()
                        .position(points.get(0))
                        .title("START")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));

                finishMarker = googleMap.addMarker(new MarkerOptions()
                        .position(points.get(points.size() - 1))
                        .title("FINISH")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish)));

                stopLocationUpdates();
            } else {
                Log.d(TAG, "press START button");

                tracking = true;
                btnTracking.setText("STOP");

                if (startMarker != null) {
                    startMarker.remove();
                }

                if (finishMarker != null) {
                    finishMarker.remove();
                }

                startLocationUpdates();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

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

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            LatLng latLng = location != null ?
                    new LatLng(location.getLatitude(), location.getLongitude()) :
                    new LatLng(35.9078, 127.7669);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        startTime = previousTime = System.currentTimeMillis();
        totalDistance = averageSpeed = maxSpeed = 0;

        if (polyline != null) {
            polyline.remove();
        }

        polyline = googleMap.addPolyline(new PolylineOptions());

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000); // 1초
        locationRequest.setFastestInterval(5 * 100); // 0.5초
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();

                Log.d(TAG, "latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());

                List<LatLng> points = polyline.getPoints();

                points.add(new LatLng(location.getLatitude(), location.getLongitude()));

                polyline.setPoints(points);

                // TODO 프래그먼트, 계산 분리
                calculate(points);

                if (totalDistance > 1000) {
                    tvTotalDistance.setText("이동 거리 = " + roundNumberToI(totalDistance / 1000, 1) + " km");
                } else {
                    tvTotalDistance.setText("이동 거리 = " + roundNumberToI(totalDistance, 1) + " m");
                }

                tvAverageSpeed.setText("평균 속력 = " + roundNumberToI(averageSpeed, 1) + " km/h");
                tvMaxSpeed.setText("최고 속력 = " + roundNumberToI(maxSpeed, 1) + " km/h");
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // 이동 거리(meter)
    private void calculate(List<LatLng> points) {
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

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();
    }
}
