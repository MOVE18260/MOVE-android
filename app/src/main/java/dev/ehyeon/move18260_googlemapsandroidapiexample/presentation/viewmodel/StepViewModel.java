package dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository.StepRepository;

public class StepViewModel extends ViewModel {

    private final StepRepository stepRepository;

    public StepViewModel(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    public void initStep() {
        stepRepository.initStep();
    }

    public LiveData<Integer> getStep() {
        return stepRepository.getStep();
    }
}
