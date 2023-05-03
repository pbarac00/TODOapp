package com.intelektualcicii.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

public class SignUpActivity extends AppCompatActivity {

    private boolean passwordShow=false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final TextView logIn = findViewById(R.id.tv_SU_logIn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText email=findViewById(R.id.et_SU_email);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText password=findViewById(R.id.et_SU_password);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button signUp=findViewById(R.id.bt_SU_signUp);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final ImageView showPassword=findViewById(R.id.iv_SU_showPassword);

        firebaseAuth=FirebaseAuth.getInstance();

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow){
                    passwordShow=false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword.setImageResource(R.drawable.hide);
                }
                else{
                    passwordShow=true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.drawable.show);
                }
                //move cursor to end of password
                password.setSelection(password.length());
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                finish();
            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "signedUp", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
}