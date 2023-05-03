package com.intelektualcicii.todoapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.intelektualcicii.todoapp.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private boolean passwordShow=false;
    EditText email_et, password_et;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        firebaseAuth=FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.tv_forgotPassword);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final TextView signUp = findViewById(R.id.tv_signUp);
        email_et=findViewById(R.id.et_email);
        password_et=findViewById(R.id.et_password);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button logIn=findViewById(R.id.bt_logIn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final ImageView showPassword=findViewById(R.id.iv_showPassword);



        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow){
                    passwordShow=false;
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword.setImageResource(R.drawable.hide);
                }
                else{
                    passwordShow=true;
                    password_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.drawable.show);
                }
                //move cursor to end of password
                password_et.setSelection(password_et.length());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                finish();
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
                finish();
            }
        });

    }

    private void logIn(){
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if (email.isEmpty()){
            email_et.setError("Email is required");
            email_et.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_et.setError("Please enter valid email");
            email_et.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            password_et.setError("Password is required");
            password_et.requestFocus();
            return;
        }

        if(password.length()<8)
        {
            password_et.setError("Password must contain minimum 8 characters");
            password_et.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}