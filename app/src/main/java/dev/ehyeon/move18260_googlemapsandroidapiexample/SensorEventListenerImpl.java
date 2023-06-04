package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class SensorEventListenerImpl implements SensorEventListener {

    private static final String TAG = "SensorEventListenerImpl";

    private final Sensor sensor;
    private int step;

    public SensorEventListenerImpl(Sensor sensor) {
        if (sensor == null || sensor.getType() != Sensor.TYPE_STEP_COUNTER) {
            throw new UnsupportedOperationException();
        }

        this.sensor = sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        step = (int) event.values[0];
        Log.d(TAG, "step = " + step);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public Sensor getSensor() {
        return sensor;
    }

    public int getStep() {
        return step;
    }
}
