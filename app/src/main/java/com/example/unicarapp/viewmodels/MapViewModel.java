package com.example.unicarapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.User;
import com.example.unicarapp.data.repository.UserRepository;

public class MapViewModel extends ViewModel {

    private UserRepository userRepository;

    public MapViewModel() {
        this.userRepository = new UserRepository();
    }

    public boolean isAuthenticated() {
        return userRepository.isAuthenticated();
    }

    public void signOut() {
        userRepository.signOut();
    }

    public MutableLiveData<User> getCurrentUserLiveData() {
        return userRepository.getCurrentUserLiveData();
    }

}
