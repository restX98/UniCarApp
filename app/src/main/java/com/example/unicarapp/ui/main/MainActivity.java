package com.example.unicarapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.unicarapp.ui.map.MapActivity;
import com.example.unicarapp.ui.welcome.LoginViewModel;
import com.example.unicarapp.ui.welcome.WelcomeActivity;

public class MainActivity extends AppCompatActivity {
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if(mainViewModel.isAuthenticated()) {
            startActivity(new Intent(this, MapActivity.class));
        } else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }
}