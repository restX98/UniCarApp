package com.example.unicarapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.repository.UserRepository;

public class MainViewModel extends ViewModel {

    private final UserRepository userRepository;

    public MainViewModel() {
        userRepository = UserRepository.getInstance();
    }

    public boolean isAuthenticated() {
        return userRepository.isAuthenticated();
    }
}
