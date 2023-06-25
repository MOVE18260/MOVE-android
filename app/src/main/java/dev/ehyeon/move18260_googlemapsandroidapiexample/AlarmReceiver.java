package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("step", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("step", 0);
        editor.apply();

        Log.d("AlarmReceiver", "updated step = " + sharedPreferences.getInt("step", 0));
    }
}
