package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

import dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment.HomeFragment;
import dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment.MapFragment;
import dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment.ProfileFragment;
import dev.ehyeon.move18260_googlemapsandroidapiexample.data.location.LocationSensor;
import dev.ehyeon.move18260_googlemapsandroidapiexample.data.step.StepSensor;
import dev.ehyeon.move18260_googlemapsandroidapiexample.data.time.Time;
import dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository.StepRepository;

public class MainActivity extends AppCompatActivity {

    private PermissionUtil permissionUtil;

    private LocationSensor locationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionUtil = new PermissionUtil(this);

        // 권한 거부 처리 생략
        if (!permissionUtil.hasPermissions()) {
            permissionUtil.requestPermissions();
        }

        init();

        HomeFragment homeFragment = new HomeFragment();
        MapFragment mapFragment = new MapFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.main_bottomNavigationView);

        navigationBarView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                if (homeFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.containers, homeFragment).commit();
                }

                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(profileFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.menu_map) {
                if (mapFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.containers, mapFragment).commit();
                }

                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(profileFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.menu_profile) {
                if (profileFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(profileFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.containers, profileFragment).commit();
                }

                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationSensor != null) {
            locationSensor.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionUtil.hasPermissions()) {
            locationSensor.startListening();
        } else {
            permissionUtil.requestPermissions();
        }
    }

    // TODO 추후 권한 핸들링으로 추가
    private void showPermissionRequestDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("권한 요청")
                .setMessage("앱을 사용하기 위해 권한이 필요합니다. 권한을 허용해주세요.")
                .setPositiveButton("권한 설정", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("종료", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    // TODO INFO dependency injection 라이브러리 필요성 느낌
    private void init() {
        initTime();
        initStep();
        initLocation();
    }

    private void initTime() {
        Time.getTime();
    }

    private void initStep() {
        StepSensor stepSensor = StepSensor.setStepSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));

        // TODO 기능 필요
        StepRepository.setStepRepository(stepSensor, 0);
    }

    private void initLocation() {
        locationSensor = LocationSensor.setLocationSensor((LocationManager) getSystemService(LOCATION_SERVICE));
        locationSensor.startListening();
    }
}
