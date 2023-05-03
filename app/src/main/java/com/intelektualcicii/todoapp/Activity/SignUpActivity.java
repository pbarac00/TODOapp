package com.intelektualcicii.todoapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
import com.intelektualcicii.todoapp.DataHolder.User;
import com.intelektualcicii.todoapp.R;

public class SignUpActivity extends AppCompatActivity {

    private boolean passwordShow=false;
    private FirebaseAuth firebaseAuth;
    EditText et_email, et_password, et_name;
    TextView tv_logIn;
    Button bt_signUp;
    ImageView iv_showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        et_name= findViewById(R.id.et_SU_name);
        tv_logIn = findViewById(R.id.tv_SU_logIn);
        et_email=findViewById(R.id.et_SU_email);
        et_password=findViewById(R.id.et_SU_password);
        bt_signUp=findViewById(R.id.bt_SU_signUp);
        iv_showPassword=findViewById(R.id.iv_SU_showPassword);

        firebaseAuth=FirebaseAuth.getInstance();

        iv_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow){
                    passwordShow=false;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_showPassword.setImageResource(R.drawable.hide);
                }
                else{
                    passwordShow=true;
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_showPassword.setImageResource(R.drawable.show);
                }
                //move cursor to end of password
                et_password.setSelection(et_password.length());
            }
        });

        tv_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });


        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp()
    {
        String password = et_password.getText().toString();
        String email=et_email.getText().toString();
        String name= et_name.getText().toString();

        if (name.isEmpty())
        {
            et_name.setError("Full name is required");
            et_name.requestFocus();
            return;
        }

        if (email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et_email.setError("Please enter valid email");
            et_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }

        if(password.length()<8)
        {
            et_password.setError("Password must contain minimum 8 characters");
            et_password.requestFocus();
            return;
        }

        User user = new User(name,email);

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this, "Registered sucessfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}