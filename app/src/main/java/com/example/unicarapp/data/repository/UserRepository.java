package com.example.unicarapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.data.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class UserRepository {

    private static volatile  UserRepository instance;
    private final AuthRepository authRepository;
    private final FirestoreRepository<User> userFirestoreRepository;
    private final MediatorLiveData<User> currentUserLiveData = new MediatorLiveData<>();

    private UserRepository() {
        this.authRepository = new AuthRepository();
        this.userFirestoreRepository = new FirestoreRepository<>("userProfiles");

        currentUserLiveData.addSource(authRepository.getFirebaseUser(), firebaseUser -> {
            if (firebaseUser != null) {
                String uid = firebaseUser.getUid();
                LiveData<User> userLiveData = userFirestoreRepository.getDocumentData(uid, User.class);

                currentUserLiveData.addSource(userLiveData, user -> {
                    currentUserLiveData.setValue(user);

                    currentUserLiveData.removeSource(userLiveData);
                });
            } else {
                currentUserLiveData.setValue(null);
            }
        });
    }

    public static UserRepository getInstance() {
        if(instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return authRepository.isAuthenticated();
    }

    public MutableLiveData<User> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public User getCurrentUser() {
        return currentUserLiveData.getValue();
    }

    public void signUp(User newUser, String password, AuthRepository.SignupCallback signupCallback) {
        authRepository.createUserWithEmailAndPassword(newUser.getEmail(), password, new AuthRepository.AuthCallback() {
            @Override
            public void onAuthSuccess(FirebaseUser user) {
                newUser.setUID(user.getUid());
                addUserInfo(newUser, signupCallback);
            }

            @Override
            public void onAuthFailure(String errorMessage) {
                signupCallback.onSignupFailure(errorMessage);
            }
        });
    }

    private void addUserInfo(User user, AuthRepository.SignupCallback signupCallback) {
        userFirestoreRepository.addDocument(user, new FirestoreRepository.FirestoreCallback() {
            @Override
            public void onLoadSuccess() {
                currentUserLiveData.setValue(user);
                signupCallback.onSignupSuccess();
            }

            @Override
            public void onLoadFailure(String errorMessage) {
                //TODO: Cancel created user
                signupCallback.onSignupFailure(errorMessage);
            }
        });
    }
    public void signIn(String email, String password, AuthRepository.AuthCallback authCallback) {
        authRepository.signInWithEmailAndPassword(email, password, authCallback);
    }

    public void signOut() {
        authRepository.signOut();
    }
}
