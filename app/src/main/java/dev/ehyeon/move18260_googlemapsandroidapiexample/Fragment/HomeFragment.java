package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ehyeon.move18260_googlemapsandroidapiexample.R;
import dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository.StepRepository;
import dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel.StepViewModel;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvStep = view.findViewById(R.id.step);
        ImageView ivDinosaur = view.findViewById(R.id.dinosaur);

        StepViewModel stepViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new StepViewModel(StepRepository.getStepRepository());
            }
        }).get(StepViewModel.class);

        stepViewModel.getStep().observe(getViewLifecycleOwner(), step -> {
            tvStep.setText("걸음 수 = " + step);
            ivDinosaur.setImageResource((step & 1) == 0 ? R.drawable.dinosaur_1 : R.drawable.dinosaur_2);
        });

        return view;
    }
}
