package com.example.unicarapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupViewModel extends ViewModel {

    private FormState step1FormState = new FormState();

    public FormState getStep1FormState() {
        return step1FormState;
    }
}
