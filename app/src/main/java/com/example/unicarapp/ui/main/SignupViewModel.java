package com.example.unicarapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.User;
import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupViewModel extends ViewModel {

    private FormState step1FormState = new FormState();
    private FormState step2FormState = new FormState();

    private User newUser = new User();

    public SignupViewModel() { }

    public FormState getStep1FormState() {
        return step1FormState;
    }

    public FormState getStep2FormState() {
        return step2FormState;
    }

    public void submitEmailForm(String email) {
        newUser.setEmail(email);
    }

    public void submitInfoForm(String firstname, String lastname, String department) {
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setDepartment(department);
    }
}
