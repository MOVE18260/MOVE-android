package dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dev.ehyeon.move18260_googlemapsandroidapiexample.data.step.StepSensor;

public class StepViewModel extends ViewModel {

    private final StepSensor stepSensor;

    public StepViewModel(StepSensor stepSensor) {
        this.stepSensor = stepSensor;
    }

    public LiveData<Integer> getStep() {
        return stepSensor.getStep();
    }
}
