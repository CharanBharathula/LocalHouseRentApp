package com.example.localhouserentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpScreen extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Button register, getOtp, setOtp;
    TextInputLayout nameLayout, addressLayout, mobileLayout, emailLayout, passwordLayout, cpwdLayout, otpField;
    String name, address, mobile, email, pwd, cpwd, otp;
    LinearLayout otpLayout, remainingControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up_screen);

        initialize();
        register.setOnClickListener(view -> {
            validateData();
        });
        getOtp.setOnClickListener(v -> {
            mobile = mobileLayout.getEditText().getText().toString();
            if(mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 10)
                mobileLayout.setError("Enter Mobile Number please");
            else
                otpLayout.setVisibility(View.VISIBLE);
        });
        setOtp.setOnClickListener(v -> {
            otp = otpField.getEditText().getText().toString();
            if(otp.isEmpty())
                otpField.setError("Enter OTP Please");
            else {
                //compare otp and set visibility of remaining to visible
                authenticateOtp();
            }
        });
    }

    private void authenticateOtp() {
        remainingControls.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);
    }

    private void validateData() {
        getData();
        if(name.isEmpty())
            nameLayout.setError("Enter name please");
        else if(address.isEmpty())
            addressLayout.setError("Enter Address Please");
        else if(email.isEmpty())
            emailLayout.setError("Enter Valid Email");
        else if(pwd.isEmpty())
            passwordLayout.setError("Enter Password");
        else if(cpwd.isEmpty())
            cpwdLayout.setError("Confirm Your Password with password");
        else if(pwd.equals(cpwd) || cpwd.equals(pwd))
            cpwdLayout.setError("Passwords Does not match");
        else
            registerUser();
    }

    private void registerUser() {
        //register user
    }

    private void getData() {
        name = nameLayout.getEditText().getText().toString();
        email = emailLayout.getEditText().getText().toString();
        pwd = passwordLayout.getEditText().getText().toString();
        cpwd = cpwdLayout.getEditText().getText().toString();
        address = addressLayout.getEditText().getText().toString();
    }

    private void initialize() {
        //TextInputLayouts
        nameLayout = findViewById(R.id.name);
        emailLayout = findViewById(R.id.email);
        mobileLayout = findViewById(R.id.mobile);
        passwordLayout = findViewById(R.id.password);
        cpwdLayout = findViewById(R.id.confirm_password);
        addressLayout = findViewById(R.id.address);
        otpField = findViewById(R.id.otp_field);

        //Buttons
        register = findViewById(R.id.register);
        setOtp = findViewById(R.id.enter_otp);
        getOtp = findViewById(R.id.request_otp);
        //Layouts
        otpLayout = findViewById(R.id.otp_layout);
        remainingControls = findViewById(R.id.registerLayout);

        firebaseAuth = FirebaseAuth.getInstance();

    }
}