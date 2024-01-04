package com.example.unicarapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.unicarapp.databinding.FragmentSignupSummaryBinding;

public class SignupSummaryFragment extends Fragment {

    private SignupViewModel signupViewModel;
    private FragmentSignupSummaryBinding binding;

    private Button loginBtn;

    public SignupSummaryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        binding = FragmentSignupSummaryBinding.inflate(inflater, container, false);
        binding.setUser(signupViewModel.getUser());

        loginBtn = binding.btnLogin;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initFormState();

        loginBtn.setOnClickListener(v -> {
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}