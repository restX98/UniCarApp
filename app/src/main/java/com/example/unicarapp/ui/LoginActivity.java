package com.example.unicarapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.unicarapp.R;
import com.example.unicarapp.databinding.ActivityLoginBinding;
import com.example.unicarapp.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // binding.setLifecycleOwner(this); // For LiveData to observe changes in this lifecycle

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);

        binding.btnSignup.setOnClickListener(new ClickHandler());
        binding.btnLogin.setOnClickListener(new ClickHandler());
    }

    private class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if(btnId == R.id.btn_login) {

            } else if(btnId == R.id.btn_signup) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        }
    }
}
