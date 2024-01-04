package com.example.unicarapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.User;
import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupViewModel extends ViewModel {

    private FormState step1FormState = new FormState();
    private FormState step2FormState = new FormState();
    private FormState step3FormState = new FormState();

    private User newUser = new User();

    public SignupViewModel() { }

    public FormState getStep1FormState() {
        return step1FormState;
    }

    public FormState getStep2FormState() {
        return step2FormState;
    }

    public FormState getStep3FormState() {
        return step3FormState;
    }

    public void submitEmailForm(String email) {
        newUser.setEmail(email);
    }

    public void submitUserInfoForm(String firstname, String lastname, String department, boolean hasCar) {
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setDepartment(department);
        newUser.setHasCar(hasCar);
    }

    public void submitCarInfoForm(String plate, String color, String model) {
        newUser.setCarPlate(plate);
        newUser.setCarColor(color);
        newUser.setCarModel(model);
    }
}
