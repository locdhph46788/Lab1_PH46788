package com.example.lab1_ph46788;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginMailActivity extends AppCompatActivity {
    private TextInputEditText edtMail, edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_mail);
        edtMail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtSignUp = findViewById(R.id.txt_signup);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                edtMail.setText(bundle.getString("email"));
                edtPassword.setText(bundle.getString("password"));
            }
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtMail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginMailActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(LoginMailActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginMailActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginMailActivity.this, "Đăng Nhập Thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginMailActivity.this,HomeActivity.class);
                                    startActivity(intent);

                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginMailActivity.this, "Sai Tài Khoản Hoặc Mật khẩu!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginMailActivity.this, SignUpActivity.class);
                startActivity(in);
            }
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}