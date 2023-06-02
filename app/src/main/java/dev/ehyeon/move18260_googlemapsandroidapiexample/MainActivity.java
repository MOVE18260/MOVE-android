package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private static final String TAG = "MainActivity";

    private LocationManager locationManager;
    private LocationListenerImpl locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        PermissionUtil permissionUtil = new PermissionUtil(this);

        // 권한 거부 처리 생략
        if (!permissionUtil.hasPermissions()) {
            permissionUtil.getPermissions();
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListenerImpl();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        // 0, 0 방지
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        double latitude;
        double longitude;
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        } else {
            // 대한민국
            latitude = 35.9078;
            longitude = 127.7669;
        }

        Log.d(TAG, "latitude = " + latitude + " longitude = " + longitude);

        // zoom = 15 == 반경 1.5km
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    // 내 위치 버튼 클릭
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    // 파란색 점 클릭
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "latitude = " + location.getLatitude() + " longitude = " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}
