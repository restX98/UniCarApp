package com.example.unicarapp.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unicarapp.R;

public class WelcomeFragment extends Fragment {

    public WelcomeFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Button) view.findViewById(R.id.btn_goto_login)).setOnClickListener(v -> Navigation
                .findNavController(v).navigate(R.id.action_welcome_fragment_to_loginFragment));
        ((Button) view.findViewById(R.id.btn_goto_signup)).setOnClickListener(v -> Navigation
                .findNavController(v).navigate(R.id.action_welcome_fragment_to_signupStep1Fragment));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }
}