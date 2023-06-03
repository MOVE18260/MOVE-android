package dev.ehyeon.move18260_googlemapsandroidapiexample.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dev.ehyeon.move18260_googlemapsandroidapiexample.PedometerService;
import dev.ehyeon.move18260_googlemapsandroidapiexample.R;

public class HomeFragment extends Fragment {

    private static final long MIN_TIME_INTERVAL = 1000; // 1초마다 TextView 업데이트

    // TODO 최악의 방법, 리팩토링 필요
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PedometerService pedometerService = PedometerService.getPedometerService();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvStep = view.findViewById(R.id.step);

        handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvStep.setText("걸음 수 = " + pedometerService.getStep());
                handler.postDelayed(this, MIN_TIME_INTERVAL);
            }
        };

        handler.postDelayed(runnable, MIN_TIME_INTERVAL);

        return view;
    }
}
