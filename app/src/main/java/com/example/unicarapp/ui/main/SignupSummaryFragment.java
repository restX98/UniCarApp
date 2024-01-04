package com.example.unicarapp.ui.main;

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

import com.example.unicarapp.databinding.FragmentSignupSummaryBinding;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;

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

        signupBtn.setOnClickListener(v -> {
            
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {
        passwordEt.addTextChangedListener(new TextListeners(passwordEt));

        formState = signupViewModel.getSummaryFormState();

        FormFieldState passwordField = formState.addField(passwordEt.getId(),
                value -> value.length() > 5
                        ? FormFieldState.Status.VALID : FormFieldState.Status.INVALID_CUSTOM
        );
        passwordField.setErrorMessage(FormFieldState.Status.INVALID_CUSTOM, "Min length is 6.");

        formState.getFormStateLiveData().observe(getViewLifecycleOwner(), new SummaryValidationObserver());
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

    private class TextListeners implements TextWatcher {
        private EditText editText;
        public TextListeners(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            formState.getFieldState(editText.getId()).validate(editText.getText().toString());
        }
    }
}