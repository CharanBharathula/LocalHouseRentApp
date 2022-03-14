package com.example.localhouserentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginScreen extends AppCompatActivity {

    TextView gotoRegister;
    TextInputLayout emailLayout, passwordLayout;
    String email, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_screen);

        initialize();
        gotoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreen.this, SignUpScreen.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            validateData();
        });
    }

    private void validateData() {
        email = emailLayout.getEditText().getText().toString();
        password = passwordLayout.getEditText().getText().toString();

        if(email.isEmpty())
            emailLayout.setError("Enter Valid Email");
        else if(password.isEmpty())
            passwordLayout.setError("Enter Password");
        else
            authenticateUser();
    }

    private void authenticateUser() {
        //login User
    }

    private void initialize()
    {
        gotoRegister = findViewById(R.id.gotoRegister);
        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);
        login = findViewById(R.id.login);
    }
}