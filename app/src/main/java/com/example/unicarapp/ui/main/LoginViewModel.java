package com.example.unicarapp.ui.main;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.R;
import com.example.unicarapp.data.repository.AuthRepository;
import com.example.unicarapp.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AuthRepository.AuthStatus> authenticationStatus = new MutableLiveData<>();
    private final UserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<AuthRepository.AuthStatus> getAuthenticationStatus() {
        return authenticationStatus;
    }

    public boolean isAuthenticated() {
        return userRepository.isAuthenticated();
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

    public void loginDataChanged(@Nullable String email) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.error_email));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    public class LoginFormState {
        @Nullable
        private Integer usernameError;

        private boolean isDataValid;

        LoginFormState(@Nullable Integer usernameError) {
            this.usernameError = usernameError;
            this.isDataValid = false;
        }

        LoginFormState(boolean isDataValid) {
            this.usernameError = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        Integer getUsernameError() {
            return usernameError;
        }

        boolean isDataValid() {
            return isDataValid;
        }
    }
}
