package com.example.unicarapp.ui.welcome;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.data.repository.UserRepository;
import com.example.unicarapp.utils.formvalidation.FormState;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {

    private final FormState loginFormState = new FormState();

    private final MutableLiveData<AuthRepository.AuthStatus> authenticationStatus = new MutableLiveData<>();
    private final UserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public FormState getLoginFormState() {
        return loginFormState;
    }

    public LiveData<AuthRepository.AuthStatus> getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void signIn(String email, String password) {
        userRepository.signIn(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onAuthSuccess(FirebaseUser user) {
                authenticationStatus.setValue(new AuthRepository.AuthStatus(true, null));
            }

            @Override
            public void onAuthFailure(String errorMessage) {
                authenticationStatus.setValue(new AuthRepository.AuthStatus(false, errorMessage));
            }
        });
    }
}
