package com.example.unicarapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.unicarapp.R;
import com.example.unicarapp.data.model.User;
import com.example.unicarapp.databinding.ActivityMapBinding;
import com.example.unicarapp.ui.map.MapFragment;
import com.example.unicarapp.viewmodels.MapViewModel;
import com.google.android.material.navigation.NavigationView;

public class MapActivity extends AppCompatActivity {

    MapViewModel mapViewModel;
    private ActivityMapBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();

        if (!mapViewModel.isAuthenticated()) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActivityMapBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;

        mapViewModel.getCurrentUserLiveData().observe(this, new CurrentUserObserver());

        binding.fabProfile.setOnClickListener(new ClickHandler());
        binding.fabSignout.setOnClickListener(new ClickHandler());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mapFragmentContainer, new MapFragment())
                    .commit();
        }
    }

    private class CurrentUserObserver implements Observer<User> {
        @Override
        public void onChanged(User user) {
            if(user == null) {
                startActivity(new Intent(MapActivity.this, LoginActivity.class));
            }
        }
    }

    private class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if(btnId == R.id.fab_profile) {
                drawerLayout.openDrawer(navigationView);
            } else if( btnId == R.id.fab_signout) {
                 mapViewModel.signOut();
            }
        }
    }
}