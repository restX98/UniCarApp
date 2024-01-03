package com.example.unicarapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.unicarapp.R;
import com.example.unicarapp.ui.MapActivity;
import com.example.unicarapp.ui.main.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onStart() {
        super.onStart();

        if (loginViewModel.isAuthenticated()) {
            startActivity(new Intent(this, MapActivity.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}