package dev.ehyeon.move18260_googlemapsandroidapiexample;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class PedometerService {

    private static PedometerService pedometerService = null;

    private SensorEventListenerImpl sensorEventListener;

    private PedometerService() {
    }

    private PedometerService(SensorManager sensorManager) {
        sensorEventListener = new SensorEventListenerImpl(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));

        sensorManager.registerListener(sensorEventListener, sensorEventListener.getSensor(), SensorManager.SENSOR_DELAY_FASTEST);
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

    public int getStep() {
        return sensorEventListener.getStep();
    }
}
