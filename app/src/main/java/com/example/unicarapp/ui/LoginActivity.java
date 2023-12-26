package com.example.unicarapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.unicarapp.R;
import com.example.unicarapp.data.model.AuthenticationResult;
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

        loginViewModel.isUserAuthenticated().observe(this, new AuthenticationObserver());

        binding.btnSignup.setOnClickListener(new ClickHandler());
        binding.btnLogin.setOnClickListener(new ClickHandler());
    }

    private class AuthenticationObserver implements Observer<AuthenticationResult> {
        @Override
        public void onChanged(AuthenticationResult authenticationResult) {
            if(authenticationResult.isSuccess()) {
                startActivity(new Intent(LoginActivity.this, MapActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, authenticationResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int btnId = v.getId();
            if(btnId == R.id.btn_login) {
                String emailUsername = binding.etEmailUsername.getText().toString().trim();
                String emailDomain = binding.tvEmailDomain.getText().toString().trim();
                String email = emailUsername + emailDomain;
                String password = binding.etPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loginViewModel.onLoginButtonClicked(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
                }
            } else if(btnId == R.id.btn_signup) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        }
    }
}
