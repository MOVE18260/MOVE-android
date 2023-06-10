package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ehyeon.move18260_googlemapsandroidapiexample.R;
import dev.ehyeon.move18260_googlemapsandroidapiexample.domain.repository.StepRepository;
import dev.ehyeon.move18260_googlemapsandroidapiexample.presentation.viewmodel.StepViewModel;

public class HomeFragment extends Fragment {

    private SharedPreferences stepSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepSharedPreferences = this.getActivity().
                getSharedPreferences("step", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvStep = view.findViewById(R.id.step);

        StepViewModel stepViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new StepViewModel(StepRepository.getStepRepository());
            }
        }).get(StepViewModel.class);

        stepViewModel.getStep().observe(getViewLifecycleOwner(), step -> {
            saveStep(step);
            tvStep.setText("걸음 수 = " + step);
        });

        return view;
    }

    private void saveStep(int step) {
        SharedPreferences.Editor editor = stepSharedPreferences.edit();
        editor.putInt("step", step);
        editor.apply();
    }
}
