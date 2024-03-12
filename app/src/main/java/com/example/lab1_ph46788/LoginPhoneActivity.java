package com.example.lab1_ph46788;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {
    private EditText edtPhone, edtOTP;
    private Button btnLoginOTP, btnGetOTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String smsVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_phone);
        edtOTP = findViewById(R.id.edt_otp);
        edtPhone = findViewById(R.id.edt_phone);
        btnGetOTP = findViewById(R.id.btn_getotp);
        btnLoginOTP = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtOTP.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginPhoneActivity.this, "Đã vượt quá số lượt gửi mã. vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            }

            public void onCodeSent(@NonNull String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                smsVerify = verificationId;
            }
        };
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhone.getText().toString().trim();

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else if (!phoneNumber.matches("[0-9]+")) {
                    Toast.makeText(LoginPhoneActivity.this, "Số điện thoại chỉ được chứa các kí tự số", Toast.LENGTH_SHORT).show();
                } else {
                    getOTP(phoneNumber);
                }
            }
        });
        btnLoginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOTP = edtOTP.getText().toString();

                if (userOTP.isEmpty()) {
                    Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyOtp(userOTP);
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(smsVerify, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginPhoneActivity.this, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginPhoneActivity.this, HomeActivity.class);
                            startActivity(intent);

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            edtOTP.setError("OTP Không Đúng");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}