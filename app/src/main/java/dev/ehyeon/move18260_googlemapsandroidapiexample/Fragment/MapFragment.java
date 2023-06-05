package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import dev.ehyeon.move18260_googlemapsandroidapiexample.R;
import dev.ehyeon.move18260_googlemapsandroidapiexample.data.location.LocationSensor;
import dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel.LocationViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private final LocationSensor locationSensor = LocationSensor.getLocationSensor();

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // DEBUG 용도
        TextView tvLatitude = view.findViewById(R.id.latitude);
        TextView tvLongitude = view.findViewById(R.id.longitude);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}
