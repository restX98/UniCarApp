package com.example.unicarapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.FragmentSignupStep1Binding;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupStep1Fragment extends Fragment {

    private SignupViewModel signupViewModel;
    private FragmentSignupStep1Binding binding;

    private FormState formState;
    private EditText emailEt;
    private Button gotoStep2Btn;

    public SignupStep1Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignupStep1Binding.inflate(inflater, container, false);

        emailEt = binding.etEmail;
        gotoStep2Btn = binding.btnGotoStep2;

        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFormState();

        gotoStep2Btn.setOnClickListener(v -> {
            signupViewModel.submitEmailForm(emailEt.getText().toString());
            Navigation.findNavController(v)
                    .navigate(R.id.action_signupStep1Fragment_to_signupStep2Fragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {
        emailEt.setOnFocusChangeListener(new EmailTextListeners());
        emailEt.addTextChangedListener(new EmailTextListeners());

        formState = signupViewModel.getStep1FormState();

        FormFieldState emailField = formState.addField(emailEt.getId(), Patterns.EMAIL_ADDRESS,
                value -> value.endsWith("@mydomain.rest")
                        ? FormFieldState.Status.VALID : FormFieldState.Status.INVALID_CUSTOM
        );
        emailField.setErrorMessage(FormFieldState.Status.INVALID_CUSTOM, "Domain not allowed.");

        formState.getFormStateLiveData().observe(getViewLifecycleOwner(), new Step1ValidationObserver());
    }

    private class Step1ValidationObserver implements Observer<FormState> {
        @Override
        public void onChanged(FormState formState) {
            if (formState == null) {
                return;
            }

            gotoStep2Btn.setEnabled(formState.isFormValid());
            FormFieldState emailState = formState.getFieldState(emailEt.getId());
            if (!emailState.isValid() && emailState.getError() != null) {
                emailEt.setError(emailState.getError());
            }
        }
    }

    private class EmailTextListeners implements TextWatcher, View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                formState.getFieldState(emailEt.getId()).validate(emailEt.getText().toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            formState.getFieldState(emailEt.getId()).validate(emailEt.getText().toString());
        }
    }
}