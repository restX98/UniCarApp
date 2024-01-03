package com.example.unicarapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unicarapp.data.model.District;
import com.example.unicarapp.data.model.RealtimeDatabaseFilter;
import com.example.unicarapp.data.model.Ride;
import com.example.unicarapp.data.model.User;
import com.example.unicarapp.data.repository.FirestoreRepository;
import com.example.unicarapp.data.repository.RealtimeDatabaseRepository;
import com.example.unicarapp.data.repository.UserRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

public class MapViewModel extends ViewModel {

    private UserRepository userRepository;
    private final FirestoreRepository<District> districtsFirestoreRepository = new FirestoreRepository<>("districts");
    private final RealtimeDatabaseRepository<Ride> ridesRealtimeDatabaseRepository = new RealtimeDatabaseRepository<>("rides");

    public MapViewModel() {
        this.userRepository = UserRepository.getInstance();
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

    public LiveData<List<Ride>> getRidesListLiveData(GoogleMap googleMap) {
        LatLngBounds visibleBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

        RealtimeDatabaseFilter filter = new RealtimeDatabaseFilter("latLng");
        filter.startAt(Ride.mergeCoordinates(visibleBounds.southwest.latitude, visibleBounds.southwest.longitude));
        filter.endAt(Ride.mergeCoordinates(visibleBounds.northeast.latitude, visibleBounds.northeast.longitude));

        return ridesRealtimeDatabaseRepository.getAllData(Ride.class, filter);
    }
}