package com.example.unicarapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.ActivityMapBinding;
import com.example.unicarapp.viewmodels.MapViewModel;
import com.google.android.material.navigation.NavigationView;

public class MapActivity extends AppCompatActivity {
    private ActivityMapBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActivityMapBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        MapViewModel viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding.setViewModel(viewModel);

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;

        binding.fabProfile.setOnClickListener(new ClickHandler());
    }

    private class ClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if(btnId == R.id.fab_profile) {
                binding.drawerLayout.openDrawer(binding.navView);
            }
        }
    }
}