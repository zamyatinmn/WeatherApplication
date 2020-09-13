package com.example.myapplication.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class HistoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StringBuilder sb = new StringBuilder();
        for (String s : MainActivity.history) {
            sb.append(s).append("\n");
        }

        TextView textView = requireView().findViewById(R.id.history);
        textView.setText(sb.toString());
        super.onViewCreated(view, savedInstanceState);
    }
}