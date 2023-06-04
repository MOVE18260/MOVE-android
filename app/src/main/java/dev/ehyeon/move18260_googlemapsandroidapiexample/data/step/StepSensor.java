package dev.ehyeon.move18260_googlemapsandroidapiexample.data.step;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;

public class StepSensor {

    private static StepSensor stepSensor = null;

    private final StepSensorEventListenerImpl sensorEventListener;

    private StepSensor(SensorManager sensorManager) {
        sensorEventListener = new StepSensorEventListenerImpl(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));

        sensorManager.registerListener(sensorEventListener, sensorEventListener.getSensor(), SensorManager.SENSOR_DELAY_FASTEST);
    }

    public static StepSensor getStepSensor() {
        return stepSensor;
    }

    public static StepSensor setStepSensor(SensorManager sensorManager) {
        if (stepSensor == null) {
            stepSensor = new StepSensor(sensorManager);
        }

        return stepSensor;
    }

    public void initStep() {
        sensorEventListener.initStep();
    }

    public LiveData<Integer> getStep() {
        return sensorEventListener.getStep();
    }
}
