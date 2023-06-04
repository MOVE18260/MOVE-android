package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;

import dev.ehyeon.move18260_googlemapsandroidapiexample.LocationService;
import dev.ehyeon.move18260_googlemapsandroidapiexample.R;

public class MapFragment extends Fragment {

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

        mapFragment.getMapAsync(new GoogleMapImpl());

        return view;
    }
}
