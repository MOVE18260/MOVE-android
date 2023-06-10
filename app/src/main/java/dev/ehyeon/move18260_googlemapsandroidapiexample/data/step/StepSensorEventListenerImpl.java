package dev.ehyeon.move18260_googlemapsandroidapiexample.data.step;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StepSensorEventListenerImpl implements SensorEventListener {

    private final SharedPreferences stepSharedPreferences;
    private final Sensor sensor;
    private final MutableLiveData<Integer> stepMutableLiveData;

    public StepSensorEventListenerImpl(SharedPreferences stepSharedPreferences, Sensor sensor) {
        this.stepSharedPreferences = stepSharedPreferences;

        if (sensor == null || sensor.getType() != Sensor.TYPE_STEP_DETECTOR) {
            throw new UnsupportedOperationException();
        }
        this.sensor = sensor;

        this.stepMutableLiveData = new MutableLiveData<>(
                stepSharedPreferences.getInt("step", 0));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int step = stepSharedPreferences.getInt("step", 0) + 1;

        SharedPreferences.Editor editor = stepSharedPreferences.edit();
        editor.putInt("step", step);
        editor.apply();

        stepMutableLiveData.setValue(step);

        Log.d("StepSensorEventListenerImpl", "updated step = " + step);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public Sensor getSensor() {
        return sensor;
    }

    public LiveData<Integer> getStep() {
        return stepMutableLiveData;
    }
}
