package com.example.myapplication.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.EventBus;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.TextEvent;


public class SettingsFragment extends Fragment {

    private Spinner spinner;
    private CheckBox checkBoxWind;
    private CheckBox checkBoxHumidity;
    private CheckBox darkThemeCB;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(root);
        checkBoxHumidity.setChecked(MainActivity.humVisCB);
        checkBoxWind.setChecked(MainActivity.windVisCB);
        darkThemeCB.setChecked(MainActivity.darkThemeCB);
        spinner.setSelection(MainActivity.cityPos);
        setOnClickBehavior();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner.setSelection(MainActivity.cityPos);
    }

    private void setOnClickBehavior() {
        checkBoxWind.setOnClickListener(view -> MainActivity.windVisCB = checkBoxWind.isChecked());
        checkBoxHumidity.setOnClickListener(view -> MainActivity.humVisCB = checkBoxHumidity.isChecked());
        darkThemeCB.setOnClickListener(view -> {
            MainActivity.darkThemeCB = darkThemeCB.isChecked();
            requireActivity().recreate();
        });
    }

    @Override
    public void onDestroy() {
        String text = spinner.getSelectedItem().toString();
        EventBus.getBus().post(new TextEvent(text));
        super.onDestroy();
    }

    private void initViews(View view) {
        spinner = view.findViewById(R.id.spinner);
        checkBoxWind = view.findViewById(R.id.checkBox_wind);
        checkBoxHumidity = view.findViewById(R.id.checkBox_humidity);
        darkThemeCB = view.findViewById(R.id.dark_theme);
    }
}