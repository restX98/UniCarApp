package com.example.unicarapp.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unicarapp.R;
import com.example.unicarapp.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng cittadella = new LatLng(37.526119, 15.074313);
        MarkerOptions options = new MarkerOptions();
        options.position(cittadella);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cittadella, 15f));

        enableMyLocation();
        moveCameraToMyLocation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        moveCameraToMyLocation();
        return true;
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (PermissionUtils.isPermissionGranted(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                || PermissionUtils.isPermissionGranted(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            map.setMyLocationEnabled(true);
        } else {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
        }

        if(!PermissionUtils.isLocationEnabled(getContext())) {
            PermissionUtils.requestLocationSettings(getContext());
        }
    }

    @SuppressLint("MissingPermission")
    private void moveCameraToMyLocation() {
        if (!PermissionUtils.isPermissionGranted(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                || !PermissionUtils.isPermissionGranted(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionUtils.requestLocationPermissions((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
        } else if (!PermissionUtils.isLocationEnabled(getContext())) {
            PermissionUtils.requestLocationSettings(getContext());
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location
                    if (location != null) {
                        // Move the camera to the user's location
                        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                    }
                }
            });
        }
    }
}