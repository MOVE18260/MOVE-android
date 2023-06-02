package dev.ehyeon.move18260_googlemapsandroidapiexample;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    private static final String[] PERMISSIONS = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    private static final int REQUEST_CODE = 1;

    private final Activity activity;

    public PermissionUtil(Activity activity) {
        this.activity = activity;
    }

    public boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(activity.getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
    }
}
