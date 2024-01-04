package com.example.unicarapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.databinding.FragmentLoginBinding;
import com.example.unicarapp.ui.MapActivity;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    FormState formState;
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

        initFormState();

        // loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new ValidationObserver());

        loginViewModel.getAuthenticationStatus().observe(getViewLifecycleOwner(), new AuthenticationObserver());

        loginBtn.setOnClickListener(v -> loginViewModel.signIn(emailEt.getText().toString(), passwordEt.getText().toString()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {
        emailEt.addTextChangedListener(new EmailTextListeners());
        passwordEt.addTextChangedListener(new PasswordTextListeners());

        formState = loginViewModel.getLoginFormState();

        formState.addField(emailEt.getId(), Patterns.EMAIL_ADDRESS);
        formState.addField(passwordEt.getId());

        formState.getFormStateLiveData().observe(getViewLifecycleOwner(), new LoginValidationObserver());
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

    private class LoginValidationObserver implements Observer<FormState> {

        @Override
        public void onChanged(FormState formState) {
            if (formState == null) {
                return;
            }

            loginBtn.setEnabled(formState.isFormValid());

            FormFieldState emailState = formState.getFieldState(emailEt.getId());
            if (!emailState.isValid() && emailState.getError() != null) {
                emailEt.setError(emailState.getError());
            }

            FormFieldState passwordState = formState.getFieldState(passwordEt.getId());
            if(!passwordState.isValid() && passwordState.getError() != null) {
                passwordEt.setError(passwordState.getError());
            }
        }
    }

    private class EmailTextListeners implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            formState.getFieldState(emailEt.getId()).validate(emailEt.getText().toString());
        }
    }

    private class PasswordTextListeners implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            formState.getFieldState(passwordEt.getId()).validate(passwordEt.getText().toString());
        }
    }
}