package com.example.unicarapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.FragmentSignupStep2Binding;
import com.example.unicarapp.utils.formvalidation.FormFieldState;
import com.example.unicarapp.utils.formvalidation.FormState;
import com.example.unicarapp.utils.formvalidation.FormTextWatcher;

public class SignupStep2Fragment extends Fragment {
    private SignupViewModel signupViewModel;
    private FragmentSignupStep2Binding binding;

    private FormState formState;
    private EditText firstnameEt;
    private EditText lastnameEt;
    private EditText departmentEt;
    private Button gotoSummaryBtn;
    private Button gotoStep3Btn;

    public SignupStep2Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        binding = FragmentSignupStep2Binding.inflate(inflater, container, false);
        binding.setUser(signupViewModel.getUser());

        firstnameEt = binding.etFirstname;
        lastnameEt = binding.etLastname;
        departmentEt = binding.etDepartment;
        gotoSummaryBtn = binding.btnGotoSummary;
        gotoStep3Btn = binding.btnGotoStep3;


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFormState();

        gotoSummaryBtn.setOnClickListener(v -> {
            signupViewModel.submitUserInfoForm(
                    firstnameEt.getText().toString(),
                    lastnameEt.getText().toString(),
                    departmentEt.getText().toString(),
                    false);
            Navigation.findNavController(v)
                    .navigate(R.id.action_signupStep2Fragment_to_signupSummaryFragment);
        });

        gotoStep3Btn.setOnClickListener(v -> {
            signupViewModel.submitUserInfoForm(
                    firstnameEt.getText().toString(),
                    lastnameEt.getText().toString(),
                    departmentEt.getText().toString(),
                    true);
            Navigation.findNavController(v)
                    .navigate(R.id.action_signupStep2Fragment_to_signupStep3Fragment);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFormState() {

        firstnameEt.addTextChangedListener(new FormTextWatcher(formState, firstnameEt));
        lastnameEt.addTextChangedListener(new FormTextWatcher(formState, lastnameEt));
        departmentEt.addTextChangedListener(new FormTextWatcher(formState, departmentEt));

        formState = signupViewModel.getStep2FormState();

        formState.addField(firstnameEt.getId());
        formState.addField(lastnameEt.getId());
        formState.addField(departmentEt.getId());

        formState.getFormStateLiveData()
                .observe(getViewLifecycleOwner(), new Step2ValidationObserver());
    }

    private class Step2ValidationObserver implements Observer<FormState> {
        @Override
        public void onChanged(FormState formState) {
            if (formState == null) {
                return;
            }

            gotoStep3Btn.setEnabled(formState.isFormValid());
            gotoSummaryBtn.setEnabled(formState.isFormValid());

            FormFieldState firstnameState = formState.getFieldState(firstnameEt.getId());
            if (!firstnameState.isValid() && firstnameState.getError() != null) {
                firstnameEt.setError(firstnameState.getError());
            }

            FormFieldState lastnameState = formState.getFieldState(lastnameEt.getId());
            if (!lastnameState.isValid() && lastnameState.getError() != null) {
                lastnameEt.setError(lastnameState.getError());
            }

            FormFieldState departmentState = formState.getFieldState(departmentEt.getId());
            if (!departmentState.isValid() && departmentState.getError() != null) {
                departmentEt.setError(departmentState.getError());
            }
        }
    }
}