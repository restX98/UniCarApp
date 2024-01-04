package com.example.unicarapp.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.User;
import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.data.repository.UserRepository;
import com.example.unicarapp.utils.formvalidation.FormState;

public class SignupViewModel extends ViewModel {

    private final UserRepository userRepository;
    private MutableLiveData<AuthRepository.AuthStatus> authenticationStatus = new MutableLiveData<>();

    private FormState step1FormState = new FormState();
    private FormState step2FormState = new FormState();
    private FormState step3FormState = new FormState();
    private FormState summaryFormState = new FormState();

    private User newUser = new User();

    public SignupViewModel() {
        userRepository = UserRepository.getInstance();
    }

    public FormState getStep1FormState() {
        return step1FormState;
    }

    public FormState getStep2FormState() {
        return step2FormState;
    }

    public FormState getStep3FormState() {
        return step3FormState;
    }

    public FormState getSummaryFormState() {
        return summaryFormState;
    }

    public LiveData<AuthRepository.AuthStatus> getAuthenticationStatus() {
        return authenticationStatus;
    }

    public User getUser() {
        return newUser;
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

    public void signUp(String password) {
        userRepository.signUp(newUser, password, new AuthRepository.SignupCallback() {
            @Override
            public void onSignupSuccess() {
                authenticationStatus.setValue(new AuthRepository.AuthStatus(true, null));
            }

            @Override
            public void onSignupFailure(String errorMessage) {
                authenticationStatus.setValue(new AuthRepository.AuthStatus(false, errorMessage));
            }
        });
    }
}
