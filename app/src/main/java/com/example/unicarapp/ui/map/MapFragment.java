package com.example.unicarapp.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unicarapp.R;
import com.example.unicarapp.data.model.District;
import com.example.unicarapp.data.model.User;
import com.example.unicarapp.data.repository.FirestoreRepository;
import com.example.unicarapp.utils.PermissionUtils;
import com.example.unicarapp.viewmodels.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final FirestoreRepository<District> districtsFirestoreRepository = new FirestoreRepository<>("districts");
    MapViewModel mapViewModel;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // LatLng cittadella = new LatLng(37.526119, 15.074313);
        // MarkerOptions options = new MarkerOptions();
        // options.position(cittadella);
        // map.addMarker(options);

        createDistrictBound(map);
        enableMyLocation();
        moveCameraToMyLocation();
    }

    private void createDistrictBound(GoogleMap map) {
        mapViewModel.getDistrictListLiveData().observe((LifecycleOwner) getContext(), new Observer<List<District>>() {
            @Override
            public void onChanged(List<District> districtList) {
                PolygonOptions polygonOptions = new PolygonOptions();
                String[] ctBounds = getResources().getStringArray(R.array.ct_bounds);
                for(String ctBound: ctBounds) {
                    String[] latLng = ctBound.split(",");
                    polygonOptions.add(new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1])));
                }
                if (ctBounds.length > 0) {
                    String[] firstLatLng = ctBounds[0].split(",");
                    polygonOptions.add(new LatLng(Double.parseDouble(firstLatLng[0]), Double.parseDouble(firstLatLng[1])));
                }

                ArrayList<LatLng> districtHole = new ArrayList<>();
                for (District district: districtList) {
                    List<GeoPoint> bounds = district.getBounds();
                    for (GeoPoint vertex: bounds) {
                        districtHole.add(new LatLng(vertex.getLatitude(), vertex.getLongitude()));
                    }
                    GeoPoint vertex = bounds.get(0);
                    districtHole.add(new LatLng(vertex.getLatitude(), vertex.getLongitude()));

                    polygonOptions.addHole(districtHole);
                }
                polygonOptions.fillColor(R.color.red_opacity);
                map.addPolygon(polygonOptions);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

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