package com.example.unicarapp.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.databinding.FragmentSignupSummaryBinding;
import com.example.unicarapp.ui.map.MapActivity;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;
import com.example.unicarapp.utils.formvalidation.FormTextWatcher;

public class SignupSummaryFragment extends Fragment {

    private SignupViewModel signupViewModel;
    private FragmentSignupSummaryBinding binding;

    FormState formState;
    private EditText passwordEt;
    private Button signupBtn;

    public SignupSummaryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        binding = FragmentSignupSummaryBinding.inflate(inflater, container, false);
        binding.setUser(signupViewModel.getUser());

        passwordEt = binding.etPassword;
        signupBtn = binding.btnSignup;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFormState();

        signupViewModel.getAuthenticationStatus()
                .observe(getViewLifecycleOwner(), new AuthenticationObserver());

        signupBtn.setOnClickListener(v -> {
            signupViewModel.signUp(passwordEt.getText().toString());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {
        formState = signupViewModel.getSummaryFormState();

        FormFieldState passwordField = formState.addField(passwordEt.getId(),
                value -> value.length() > 5
                        ? FormFieldState.Status.VALID : FormFieldState.Status.INVALID_CUSTOM
        );
        passwordField.setErrorMessage(FormFieldState.Status.INVALID_CUSTOM, "Min length is 6.");

        formState.getFormStateLiveData()
                .observe(getViewLifecycleOwner(), new SummaryValidationObserver());

        passwordEt.addTextChangedListener(new FormTextWatcher(formState, passwordEt));
    }

    private class AuthenticationObserver implements Observer<AuthRepository.AuthStatus> {
        @Override
        public void onChanged(AuthRepository.AuthStatus authenticationResult) {
            if(authenticationResult.isSuccess()) {
                startActivity(new Intent(requireContext(), MapActivity.class));
            } else {
                Toast.makeText(requireContext(),
                        authenticationResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SummaryValidationObserver implements Observer<FormState> {
        @Override
        public void onChanged(FormState formState) {
            if (formState == null) {
                return;
            }

            signupBtn.setEnabled(formState.isFormValid());
            FormFieldState passwordState = formState.getFieldState(passwordEt.getId());
            if (!passwordState.isValid() && passwordState.getError() != null) {
                passwordEt.setError(passwordState.getError());
            }
        }
    }
}