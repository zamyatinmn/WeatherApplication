package com.example.myapplication.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.EventBus;
import com.example.myapplication.FailConnectionDialog;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.WeatherRequest;
import com.example.myapplication.R;
import com.example.myapplication.TextEvent;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    private SwitchMaterial wtgSwitch;
    private TextView windValueView;
    private TextView city;
    private TextView humidityValue;
    private TextView tempValue;
    private TextView humidity;
    private TextView humidityUnits;
    private TextView windUnits;
    private TextView windWord;
    private final double COEFFICIENT = 1.5;
    String[] cities;
    private int windValue;
    private FailConnectionDialog dialog;
    private static final String TAG = "WEATHER";
    private static final String API_KEY = "d1fd6772013e96880c7c68af2f56a08d";


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void setParameters() {
        for (int i = 0; i < cities.length; i++) {
            if (city.getText().equals(cities[i])) {
                MainActivity.cityPos = i;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnSwitchBehavior();
        connect(city.getText().toString());
        checkDataVisibility();
        MainActivity.history.add(city.getText().toString() + "        " + tempValue.getText().toString()+ "°C");
    }

    @Override
    public void onStart() {
        EventBus.getBus().register(this);
        setParameters();
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSomeEvent(TextEvent event) {

        city.setText(event.text);
        connect(city.getText().toString());
    }

    private void initViews(View view) {
        Resources resources = getResources();
        windValue = resources.getInteger(R.integer.windValue);
        cities = resources.getStringArray(R.array.Cities);
        wtgSwitch = view.findViewById(R.id.wtgSwitch);
        windValueView = view.findViewById(R.id.windValue);
        city = view.findViewById(R.id.city);
        humidityValue = view.findViewById(R.id.humidityValue);
        tempValue = view.findViewById(R.id.temperatureValue);
        humidity = view.findViewById(R.id.humidity);
        humidityUnits = view.findViewById(R.id.humidityUnits);
        windUnits = view.findViewById(R.id.windUnits);
        windWord = view.findViewById(R.id.windWord);
    }

    private void connect(String city) {
        try {
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=";
            final URL uri = new URL(weatherUrl + API_KEY);
            final Handler handler = new Handler();
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = getLines(in);
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                    handler.post(() -> displayWeather(weatherRequest));
                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();
                    dialog = new FailConnectionDialog();
                    dialog.show(getParentFragmentManager(), "custom");
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        String temperatureValue = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp());
        tempValue.setText(temperatureValue);

        String humidityStr = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity());
        humidityValue.setText(humidityStr);

        String windSpeedStr = String.format(Locale.getDefault(), "%.0f", weatherRequest.getWind().getSpeed());
        windValueView.setText(windSpeedStr);
        windValue = (int) weatherRequest.getWind().getSpeed();
    }

    private void setOnSwitchBehavior() {
        wtgSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                windValueView.setText(String.valueOf((int) ((double) windValue * COEFFICIENT)));
            } else {
                windValueView.setText(String.valueOf(windValue));
            }
        });
    }

    private void checkDataVisibility() {
        if (MainActivity.humVisCB){
            setHumidityDataVisibility(View.VISIBLE);
        } else {
            setHumidityDataVisibility(View.INVISIBLE);
        }
        if (MainActivity.windVisCB){
            setWindDataVisibility(View.VISIBLE);
        } else {
            setWindDataVisibility(View.INVISIBLE);
        }
    }

    private void setHumidityDataVisibility(int visibility) {
        humidity.setVisibility(visibility);
        humidityValue.setVisibility(visibility);
        humidityUnits.setVisibility(visibility);
    }

    private void setWindDataVisibility(int visibility) {
        windValueView.setVisibility(visibility);
        wtgSwitch.setVisibility(visibility);
        windUnits.setVisibility(visibility);
        windWord.setVisibility(visibility);
    }
}