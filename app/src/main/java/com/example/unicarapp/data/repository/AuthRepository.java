package com.example.unicarapp.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void authenticateUser(String email, String password, AuthCallback authCallback) {
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

    public interface AuthCallback {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailure(String errorMessage);
    }
}
