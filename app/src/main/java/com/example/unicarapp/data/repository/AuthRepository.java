package com.example.unicarapp.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();

    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser.setValue(firebaseAuth.getCurrentUser());
        initAuthStateListener();
    }

    public boolean isAuthenticated() {
        return firebaseUser.getValue() != null;
    }

    private void initAuthStateListener() {
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                firebaseUser.setValue(user);
            }
        });
    }

    public void signInWithEmailAndPassword(String email, String password, AuthCallback authCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            authCallback.onAuthSuccess(user);
                        } else {
                            authCallback.onAuthFailure("Authentication failed!");
                        }
                    }
                });
    }

    public void signOut() {
        if (isAuthenticated()) {
            firebaseAuth.signOut();
        }
    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    public interface AuthCallback {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailure(String errorMessage);
    }

    public static class AuthStatus {
        private boolean success;
        private String errorMessage;

        public AuthStatus(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
