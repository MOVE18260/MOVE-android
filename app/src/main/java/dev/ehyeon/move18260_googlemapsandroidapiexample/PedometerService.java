package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

// TODO SensorEventListener 와 서비스 분리하면 좋을꺼 같은데
public class PedometerService implements SensorEventListener {

    private static final String TAG = "pedometerService";

    private static PedometerService pedometerService = null;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private int step;

    private PedometerService() {
    }

    private PedometerService(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public static PedometerService getPedometerService() {
        return pedometerService;
    }

    public static PedometerService setPedometerService(SensorManager sensorManager) {
        if (pedometerService == null) {
            pedometerService = new PedometerService(sensorManager);
        }
        return pedometerService;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "step = " + (int) event.values[0]);
        this.step = (int) event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "accuracy = " + accuracy);
    }

    public int getStep() {
        return step;
    }
}
