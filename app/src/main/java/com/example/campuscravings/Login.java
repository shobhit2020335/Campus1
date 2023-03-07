package com.example.campuscravings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.campuscravings.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    TextInputEditText mail, password;
    LoadingDialog loadingDialog = new LoadingDialog(Login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntent();

        auth = FirebaseAuth.getInstance();
        mail = (TextInputEditText) findViewById(R.id.inputemaillogin);
        password = (TextInputEditText) findViewById(R.id.inputpasswordlogin);

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString().isEmpty()) {
                    mail.setError("Required");
                    mail.requestFocus();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Required");
                    password.requestFocus();
                } else {
                    loadingDialog.startLoading();

//                  else{
                    auth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (!user.isEmailVerified()) {
                                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(Login.this, "Verification link has been sent to the registered email. kindly verify it.", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismissdialog();

                                            } else {
                                                Toast.makeText(Login.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismissdialog();
                                            }
                                        }
                                    });
//                        Toast.makeText(login.this, "Please check your email to verify and login.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                    loadingDialog.dismissdialog();
                                }


                            } else {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissdialog();
                            }
                        }

                    });
//                }

                }
            }
        });

        binding.signupbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}