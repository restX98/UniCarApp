package com.example.unicarapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.District;
import com.example.unicarapp.data.model.User;
import com.example.unicarapp.data.repository.FirestoreRepository;
import com.example.unicarapp.data.repository.UserRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private UserRepository userRepository;
    private final FirestoreRepository<District> districtsFirestoreRepository = new FirestoreRepository<>("districts");
    private MutableLiveData<List<District>> districtListLiveData = new MutableLiveData<>();

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

    public LiveData<List<District>> getDistrictListLiveData() {
        return districtsFirestoreRepository.getAllDocumentData(District.class);
    }
}