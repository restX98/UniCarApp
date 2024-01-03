package com.example.unicarapp.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.data.model.AuthenticationResult;
import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<AuthenticationResult> authenticationResult = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public boolean isAuthenticated() {
        return userRepository.isAuthenticated();
    }

    public void onLoginButtonClicked(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            userRepository.signIn(email, password, new AuthRepository.AuthCallback() {
                @Override
                public void onAuthSuccess(FirebaseUser user) {
                    authenticationResult.setValue(new AuthenticationResult(true, null));
                }

                @Override
                public void onAuthFailure(String errorMessage) {
                    authenticationResult.setValue(new AuthenticationResult(false, errorMessage));
                }
            });
        } else {
            Toast.makeText(getApplication(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public LiveData<AuthenticationResult> getAuthenticationResult() {
        return authenticationResult;
    }
}
