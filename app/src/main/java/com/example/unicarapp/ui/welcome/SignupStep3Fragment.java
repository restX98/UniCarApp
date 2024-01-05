package com.example.unicarapp.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.FragmentSignupStep3Binding;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;
import com.example.unicarapp.utils.formvalidation.FormTextWatcher;

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
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        binding = FragmentSignupStep3Binding.inflate(inflater, container, false);
        binding.setUser(signupViewModel.getUser());

        carPlateEt = binding.etCarPlate;
        carColorEt = binding.etCarColor;
        carModelEt = binding.etCarModel;
        gotoSummaryBtn = binding.btnGotoSummary;

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
        formState = signupViewModel.getStep3FormState();

        formState.addField(carPlateEt.getId());
        formState.addField(carColorEt.getId());
        formState.addField(carModelEt.getId());

        formState.getFormStateLiveData()
                .observe(getViewLifecycleOwner(), new Step3ValidationObserver());

        carPlateEt.addTextChangedListener(new FormTextWatcher(formState, carPlateEt));
        carColorEt.addTextChangedListener(new FormTextWatcher(formState, carColorEt));
        carModelEt.addTextChangedListener(new FormTextWatcher(formState, carModelEt));
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
}