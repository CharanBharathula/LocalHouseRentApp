package com.example.localhouserentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignUpScreen extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Button register, getOtp, setOtp;
    TextInputLayout nameLayout, addressLayout, mobileLayout, emailLayout, passwordLayout, cpwdLayout, otpField;
    String name, address, mobile, email, pwd, cpwd;
    LinearLayout otpLayout, remainingControls;
    String otpGenerated;
    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;

    ProgressDialog progressDialog;
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
            else{
                sendOTP();
            }
        });
        setOtp.setOnClickListener(v -> {
            if (!inputcode1.getText().toString().trim().isEmpty()
                    && !inputcode2.getText().toString().trim().isEmpty()
                    && !inputcode3.getText().toString().trim().isEmpty()
                    && !inputcode4.getText().toString().trim().isEmpty()
                    && !inputcode5.getText().toString().trim().isEmpty()
                    && !inputcode6.getText().toString().trim().isEmpty()) {

                String code = inputcode1.getText().toString() +
                        inputcode2.getText().toString() +
                        inputcode3.getText().toString() +
                        inputcode4.getText().toString() +
                        inputcode5.getText().toString() +
                        inputcode6.getText().toString();

                if(otpGenerated != null)
                {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpGenerated, code);
                    /*FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    remainingControls.setVisibility(View.VISIBLE);
                                    otpLayout.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpScreen.this, "Enter the Correct otp", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                    if(phoneAuthCredential != null ){
                        remainingControls.setVisibility(View.VISIBLE);
                        otpLayout.setVisibility(View.GONE);
                        getOtp.setVisibility(View.GONE);
                        nameLayout.setWeightSum(1);
                        Toast.makeText(this, "OTP Verification Completed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void sendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                SignUpScreen.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(SignUpScreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String verficationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpGenerated = verficationid;
                        otpLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpScreen.this, "Code has been sent to "+mobile+" number", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void validateData() {
        getData();
        if(name.isEmpty())
            nameLayout.setError("Enter name please");
        else if(address.isEmpty())
            addressLayout.setError("Enter Address Please");
        else if(email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
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
        progressDialog=new ProgressDialog(SignUpScreen.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("We are adding you to our Database");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(SignUpScreen.this, task -> {
                    if(task.isSuccessful())
                    {
                        String encryptedPassword = null;
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        String userid=currentUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("name", name);
                        hashMap.put("email", email);

                        if(encryptedPassword != "Error")
                            encryptedPassword = encryptPassword(pwd);
                        hashMap.put("password", encryptedPassword);
                        hashMap.put("address" ,address);
                        hashMap.put("profile_icon", "https://www.kindpng.com/picc/m/495-4952535_create-digital-profile-icon-blue-user-profile-icon.png");
                        databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUpScreen.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                });

    }

    private String encryptPassword(String pwd) {
        //USed MD5 Algorithm to store the password
        try {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");
            /* Add plain-text password bytes to digest using MD5 update() method */
            m.update(pwd.getBytes());
            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();
            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i < bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            /* Complete hashed password in hexadecimal format */
            return s.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "Error";
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

        //Buttons
        register = findViewById(R.id.register);
        setOtp = findViewById(R.id.enter_otp);
        getOtp = findViewById(R.id.request_otp);

        //Layouts
        otpLayout = findViewById(R.id.otp_layout);
        remainingControls = findViewById(R.id.registerLayout);

        //otp fields
        inputcode1 = findViewById(R.id.inputotp1);
        inputcode2 = findViewById(R.id.inputotp2);
        inputcode3 = findViewById(R.id.inputotp3);
        inputcode4 = findViewById(R.id.inputotp4);
        inputcode5 = findViewById( R.id.inputotp5);
        inputcode6 = findViewById(R.id.inputotp6);

        firebaseAuth = FirebaseAuth.getInstance();

    }
}