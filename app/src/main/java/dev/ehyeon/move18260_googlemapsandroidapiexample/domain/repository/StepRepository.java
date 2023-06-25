package dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository;

import androidx.lifecycle.LiveData;

import dev.ehyeon.move18260_googlemapsandroidapiexample.data.step.StepSensor;

public class StepRepository {

    private static StepRepository stepRepository = null;

    private final StepSensor stepSensor;

    private StepRepository(StepSensor stepSensor) {
        this.stepSensor = stepSensor;
    }

    public static StepRepository getStepRepository() {
        return stepRepository;
    }

    public static StepRepository setStepRepository(StepSensor stepSensor) {
        if (stepRepository == null) {
            stepRepository = new StepRepository(stepSensor);
        }

        return stepRepository;
    }

    public LiveData<Integer> getStep() {
        return stepSensor.getStep();
    }
}
