package com.example.drugfoodapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.drugfoodapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String ARG_USER_EMAIL = "user_email";
    private String userEmail;

    public static HomeFragment newInstance(String userEmail) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView welcomeText = view.findViewById(R.id.welcomeUser);
        TextView dateText = view.findViewById(R.id.textDate);

        // Burada Örnek olarak email. İsim de gösterilebilir.
        if (userEmail != null) {
            welcomeText.setText("Welcome, " + userEmail + "!");
        } else {
            welcomeText.setText("Welcome!");
        }

        String currentDateTime = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(new Date());
        dateText.setText(currentDateTime);

        return view;
    }
}
