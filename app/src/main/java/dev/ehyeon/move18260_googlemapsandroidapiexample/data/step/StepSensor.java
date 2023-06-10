package dev.ehyeon.move18260_googlemapsandroidapiexample.data.step;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;

public class StepSensor {

    private static StepSensor stepSensor = null;

    private final SensorManager sensorManager;
    private final StepSensorEventListenerImpl sensorEventListener;

    private StepSensor(SharedPreferences stepSharedPreferences, SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        sensorEventListener = new StepSensorEventListenerImpl(stepSharedPreferences, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
    }

    public static StepSensor getStepSensor() {
        return stepSensor;
    }

    public static StepSensor setStepSensor(SharedPreferences stepSharedPreferences, SensorManager sensorManager) {
        if (stepSensor == null) {
            stepSensor = new StepSensor(stepSharedPreferences, sensorManager);
        }

        return stepSensor;
    }

    public void startSensor() {
        sensorManager.registerListener(sensorEventListener, sensorEventListener.getSensor(), SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    public LiveData<Integer> getStep() {
        return sensorEventListener.getStep();
    }
}
