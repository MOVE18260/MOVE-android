package dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dev.ehyeon.move18260_googlemapsandroidapiexample.data.location.LocationSensor;

public class LocationViewModel extends ViewModel {

    private final LocationSensor locationSensor;

    public LocationViewModel(LocationSensor locationSensor) {
        this.locationSensor = locationSensor;
    }

    public LiveData<Double> getLatitude() {
        return locationSensor.getLatitude();
    }

    public LiveData<Double> getLongitude() {
        return locationSensor.getLongitude();
    }
}
