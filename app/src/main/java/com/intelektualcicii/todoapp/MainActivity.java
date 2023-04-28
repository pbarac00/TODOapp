package com.intelektualcicii.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean passwordShow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final TextView forgotPassword = findViewById(R.id.tv_forgotPassword);
        final TextView signUp = findViewById(R.id.tv_logIn);
        final EditText email=findViewById(R.id.et_email);
        final EditText password=findViewById(R.id.et_password);
        final Button logIn=findViewById(R.id.bt_signUp);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final ImageView showPassword=findViewById(R.id.iv_showPassword);

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

    }


}