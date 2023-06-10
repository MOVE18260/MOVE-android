package dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository;

import androidx.lifecycle.LiveData;

import dev.ehyeon.move18260_googlemapsandroidapiexample.data.step.StepSensor;

public class StepRepository {

//    private static final String TAG = "StepRepository";

    private static StepRepository stepRepository = null;

    private final StepSensor stepSensor;

    private StepRepository(StepSensor stepSensor, int initStep) {
        this.stepSensor = stepSensor;
        stepSensor.initStep(initStep);
//        startInitStepTimer();
    }

    public static StepRepository getStepRepository() {
        return stepRepository;
    }

    public static StepRepository setStepRepository(StepSensor stepSensor, int initStep) {
        if (stepRepository == null) {
            stepRepository = new StepRepository(stepSensor, initStep);
        }

        return stepRepository;
    }

    public LiveData<Integer> getStep() {
        return stepSensor.getStep();
    }

    // TODO refactor
//    private void startInitStepTimer() {
//        // 총 10분, 5 초씩 onTick
//        new CountDownTimer(10 * 60 * 1000, 5 * 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                stepSensor.resetStep();
//                Log.d(TAG, "init step");
//            }
//
//            @Override
//            public void onFinish() {
//                stepSensor.resetStep();
//                start();
//            }
//        }.start();
//    }
}
