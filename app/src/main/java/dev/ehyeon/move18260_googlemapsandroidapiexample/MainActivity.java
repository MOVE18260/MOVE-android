package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private static final String TAG = "MainActivity";
    private static final long MIN_TIME_INTERVAL = 1000; // 1초마다 TextView 업데이트

    private PermissionUtil permissionUtil;

    private LocationService locationService;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        TextView tvLatitude = findViewById(R.id.latitude);
        TextView tvLongitude = findViewById(R.id.longitude);

        permissionUtil = new PermissionUtil(this);

        // 권한 거부 처리 생략
        if (!permissionUtil.hasPermissions()) {
            permissionUtil.requestPermissions();
        }

        locationService = new LocationService((LocationManager) getSystemService(LOCATION_SERVICE));

        locationService.startListening();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                tvLatitude.setText("위도 = " + locationService.getLatitude());
                tvLongitude.setText("경도 = " + locationService.getLongitude());
                handler.postDelayed(this, MIN_TIME_INTERVAL);
            }
        };

        handler.postDelayed(runnable, MIN_TIME_INTERVAL);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationService.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionUtil.hasPermissions()) {
            locationService.startListening();
        } else {
            permissionUtil.requestPermissions();
            // TODO permission 핸들링 필요
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        Location lastKnownLocation = locationService.getLastKnownLocation();

        Log.d(TAG, "latitude = " + lastKnownLocation.getLatitude() + " longitude = " + lastKnownLocation.getLongitude());

        // zoom = 15 == 반경 1.5km
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15));
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
