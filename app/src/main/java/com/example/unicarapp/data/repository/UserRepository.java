package com.example.unicarapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.data.model.User;

public class UserRepository {
    private final AuthRepository authRepository;
    private final FirestoreRepository<User> userFirestoreRepository;
    private final MediatorLiveData<User> currentUserLiveData = new MediatorLiveData<>();

    public UserRepository() {
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

    public boolean isAuthenticated() {
        return authRepository.isAuthenticated();
    }

    public MutableLiveData<User> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public User getCurrentUser() {
        return currentUserLiveData.getValue();
    }

    public void signIn(String email, String password, AuthRepository.AuthCallback authCallback) {
        authRepository.signInWithEmailAndPassword(email, password, authCallback);
    }

    public void signOut() {
        authRepository.signOut();
    }
}
