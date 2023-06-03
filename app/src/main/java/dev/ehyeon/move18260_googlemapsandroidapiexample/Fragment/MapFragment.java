package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import dev.ehyeon.move18260_googlemapsandroidapiexample.LocationService;
import dev.ehyeon.move18260_googlemapsandroidapiexample.R;

// TODO 나중에 fragment 와 map implements 분리
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private static final String TAG = "MapFragment";
    private static final long MIN_TIME_INTERVAL = 1000; // 1초마다 TextView 업데이트

    private LocationService locationService;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationService = LocationService.getLocationService();

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // DEBUG 용도
        TextView tvLatitude = view.findViewById(R.id.latitude);
        TextView tvLongitude = view.findViewById(R.id.longitude);
        TextView tvAddress = view.findViewById(R.id.address);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvLatitude.setText("위도 = " + locationService.getLatitude());
                tvLongitude.setText("경도 = " + locationService.getLongitude());
                tvAddress.setText("주소 = " + locationService.getAddress());
                handler.postDelayed(this, MIN_TIME_INTERVAL);
            }
        };

        handler.postDelayed(runnable, MIN_TIME_INTERVAL);

        mapFragment.getMapAsync(this);

        return view;
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
