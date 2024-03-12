package com.example.lab1_ph46788;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText edtEmail, edtPassword, edtRePassword;
    private Button btnSignUp;
    private TextView txtLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtRePassword = findViewById(R.id.edt_repassword);
        btnSignUp = findViewById(R.id.btn_signup);
        txtLogin = findViewById(R.id.txt_login);
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String rppassword = edtRePassword.getText().toString();
                if (email.equals("") || password.equals("") || rppassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(rppassword)) {
                    Toast.makeText(SignUpActivity.this, "mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 || !Character.isUpperCase(password.charAt(0))) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 6 kí tự và viết hoa chữ cái đầu tiên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent in = new Intent(SignUpActivity.this, LoginMailActivity.class);
                                    in.putExtra("email", email);
                                    in.putExtra("password", password);
                                    startActivity(in);
                                    Toast.makeText(SignUpActivity.this, "Đăng Kí Thành Công!",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginMailActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}