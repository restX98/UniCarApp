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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.FragmentSignupStep3Binding;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupStep3Fragment extends Fragment {

    private SignupViewModel signupViewModel;
    private FragmentSignupStep3Binding binding;

    private FormState formState;
    private EditText carPlateEt;
    private EditText carColorEt;
    private EditText carModelEt;
    private Button gotoSummaryBtn;

    public SignupStep3Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupStep3Binding.inflate(inflater, container, false);

        carPlateEt = binding.etCarPlate;
        carColorEt = binding.etCarColor;
        carModelEt = binding.etCarModel;
        gotoSummaryBtn = binding.btnGotoSummary;

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFormState();

        gotoSummaryBtn.setOnClickListener(v -> {
            signupViewModel.submitCarInfoForm(
                    carPlateEt.getText().toString(),
                    carColorEt.getText().toString(),
                    carModelEt.getText().toString());
            Navigation.findNavController(v)
                    .navigate(R.id.action_signupStep3Fragment_to_signupSummaryFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {
        carPlateEt.addTextChangedListener(new TextListeners(carPlateEt));
        carColorEt.addTextChangedListener(new TextListeners(carColorEt));
        carModelEt.addTextChangedListener(new TextListeners(carModelEt));

        formState = signupViewModel.getStep3FormState();

        formState.addField(carPlateEt.getId());
        formState.addField(carColorEt.getId());
        formState.addField(carModelEt.getId());

        formState.getFormStateLiveData().observe(getViewLifecycleOwner(), new Step3ValidationObserver());
    }

    private class Step3ValidationObserver implements Observer<FormState> {
        @Override
        public void onChanged(FormState formState) {
            if (formState == null) {
                return;
            }

            gotoSummaryBtn.setEnabled(formState.isFormValid());

            FormFieldState carPlateState = formState.getFieldState(carPlateEt.getId());
            if (!carPlateState.isValid() && carPlateState.getError() != null) {
                carPlateEt.setError(carPlateState.getError());
            }

            FormFieldState carColorState = formState.getFieldState(carColorEt.getId());
            if (!carColorState.isValid() && carColorState.getError() != null) {
                carColorEt.setError(carColorState.getError());
            }

            FormFieldState carModelState = formState.getFieldState(carModelEt.getId());
            if (!carModelState.isValid() && carModelState.getError() != null) {
                carModelEt.setError(carModelState.getError());
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