package com.example.unicarapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.AuthenticationResult;
import com.example.unicarapp.data.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<AuthenticationResult> authenticationResult  = new MutableLiveData<>();

    public LoginViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<AuthenticationResult> isUserAuthenticated() {
        return authenticationResult;
    }

    public void onLoginButtonClicked(String email, String password) {
        authRepository.authenticateUser(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onAuthSuccess(FirebaseUser user) {
                authenticationResult.setValue(new AuthenticationResult(true, null));
            }

            @Override
            public void onAuthFailure(String errorMessage) {
                authenticationResult.setValue(new AuthenticationResult(false, errorMessage));
            }
        });
    }
}
