package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        CheckBox cb1 = view.findViewById(R.id.checkBox_1);
        CheckBox cb2 = view.findViewById(R.id.checkBox_2);
        CheckBox cb3 = view.findViewById(R.id.checkBox_3);
        CheckBox cb4 = view.findViewById(R.id.checkBox_4);
        CheckBox cb5 = view.findViewById(R.id.checkBox_5);

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

            cb1.setChecked(step >= 20);
            cb2.setChecked(step >= 40);
            cb3.setChecked(step >= 60);
            cb4.setChecked(step >= 80);
            cb5.setChecked(step >= 100);
        });

        return view;
    }
}
