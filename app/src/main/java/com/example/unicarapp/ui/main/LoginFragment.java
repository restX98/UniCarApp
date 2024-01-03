package com.example.unicarapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.databinding.FragmentLoginBinding;
import com.example.unicarapp.ui.MapActivity;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    private EditText emailEt;
    private EditText passwordEt;
    private Button loginBtn;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        emailEt = binding.etEmail;
        passwordEt = binding.etPassword;
        loginBtn = binding.btnLogin;

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new ValidationObserver());

        loginViewModel.getAuthenticationStatus().observe(getViewLifecycleOwner(), new AuthenticationObserver());

        emailEt.setOnFocusChangeListener(new EmailTextListeners());

        loginBtn.setOnClickListener(v -> loginViewModel.signIn(emailEt.getText().toString(), passwordEt.getText().toString()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class AuthenticationObserver implements Observer<AuthRepository.AuthStatus> {
        @Override
        public void onChanged(AuthRepository.AuthStatus authenticationResult) {
            if(authenticationResult.isSuccess()) {
                startActivity(new Intent(requireContext(), MapActivity.class));
            } else {
                Toast.makeText(requireContext(), authenticationResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ValidationObserver implements Observer<LoginViewModel.LoginFormState> {
        @Override
        public void onChanged(LoginViewModel.LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }

            loginBtn.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                emailEt.setError(getString(loginFormState.getUsernameError()));
            }
        }
    }

    private class EmailTextListeners implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                loginViewModel.loginDataChanged(emailEt.getText().toString());
            }
        }
    }
}