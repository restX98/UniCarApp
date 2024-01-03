package com.example.unicarapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.FragmentSignupStep1Binding;

public class SignupStep1Fragment extends Fragment {

    private FragmentSignupStep1Binding binding;

    public SignupStep1Fragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> Navigation
                .findNavController(v).navigate(R.id.action_loginFragment_to_welcome_fragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignupStep1Binding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}