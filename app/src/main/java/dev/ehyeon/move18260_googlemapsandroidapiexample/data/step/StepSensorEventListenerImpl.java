package dev.ehyeon.move18260_googlemapsandroidapiexample.data.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StepSensorEventListenerImpl implements SensorEventListener {

    private static final String TAG = "SensorEventListenerImpl";

    private final Sensor sensor;
    private final MutableLiveData<Integer> step = new MutableLiveData<>(0);

    public StepSensorEventListenerImpl(Sensor sensor) {
        if (sensor == null || sensor.getType() != Sensor.TYPE_STEP_DETECTOR) {
            throw new UnsupportedOperationException();
        }

        this.sensor = sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        step.setValue(step.getValue() + 1);
        Log.d(TAG, "step = " + step.getValue());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void initStep() {
        step.setValue(0);
    }

    public LiveData<Integer> getStep() {
        return step;
    }
}
