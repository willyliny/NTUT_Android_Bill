package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText signup_email,signup_password;
    private Button btn_signup;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //connect
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        btn_signup = findViewById(R.id.btn_SignUp);
        mAuth = FirebaseAuth.getInstance();
        //btn click
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = signup_email.getText().toString();
                password = signup_password.getText().toString();
                if(!account.equals("") && !password.equals("")) {
                    mAuth.createUserWithEmailAndPassword(account,password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    if(account.isEmpty()){
                        Toast.makeText(SignUpActivity.this,"請輸入帳號",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignUpActivity.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
