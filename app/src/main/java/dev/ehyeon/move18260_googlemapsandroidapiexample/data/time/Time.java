package dev.ehyeon.move18260_googlemapsandroidapiexample.data.time;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Time {

    private static final String TAG = "Time";

    private static Time time = null;

    private final MutableLiveData<Long> currentTime;

    private Time() {
        this.currentTime = new MutableLiveData<>(System.currentTimeMillis());
        start();
    }

    public static Time getTime() {
        if (time == null) {
            time = new Time();
        }

        return time;
    }

    private void start() {
        // TODO 메모리 누수 -> 주석 처리
//        Handler handler = new Handler(Looper.getMainLooper());
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                long currentTimeMillis = System.currentTimeMillis();
//
//                currentTime.setValue(currentTimeMillis);
//
//                Log.d(TAG, "time = " + currentTimeMillis);
//
//                handler.postDelayed(this, 1000 - currentTimeMillis % 1000);
//            }
//        };
//
//        handler.post(runnable);
    }

    public LiveData<Long> getCurrentTime() {
        return currentTime;
    }
}
