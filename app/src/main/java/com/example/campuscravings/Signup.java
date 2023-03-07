package com.example.campuscravings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuscravings.databinding.ActivityMainBinding;
import com.example.campuscravings.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 99;
    private TextView tv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextInputEditText mail, password, name;
    LoadingDialog loadingDialog = new LoadingDialog(Signup.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntent();
        binding.loginbtnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mail = (TextInputEditText) findViewById(R.id.inputemailsignup);
        name = (TextInputEditText) findViewById(R.id.inputnamesignup);
        password = (TextInputEditText) findViewById(R.id.inputpasswordsignup);


        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Required");
                    name.requestFocus();
                } else if (mail.getText().toString().isEmpty()) {
                    mail.setError("Required");
                    mail.requestFocus();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Required");
                    password.requestFocus();
                } else {
                    loadingDialog.startLoading();

                    String email = Objects.requireNonNull(mail.getText()).toString();
                    String pass = Objects.requireNonNull(password.getText()).toString();
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Signup.this, "REGISTERD SUCCESSFULLY. PLEASE LOGIN.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this, Login.class);
                                startActivity(intent);
                                loadingDialog.dismissdialog();
                                User u1 = new User(Objects.requireNonNull(name.getText()).toString(), email, pass);
                                String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                database.getReference().child("users").child(id).setValue(u1);
                            } else {
                                loadingDialog.dismissdialog();
                                Toast.makeText(Signup.this, "ERROR:" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        SignInButton signInButton = findViewById(R.id.googlesignupbtn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                signIn();

            }
        });


    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);


            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseauthwithgoogle:" + acct.getId());
                firebaseauthwithgoogle(acct.getIdToken());

            } catch (ApiException e) {
                loadingDialog.dismissdialog();
                Log.w("TAG", "GOOGLESIGNINFAILED", e);
            }
        }
    }

    private void firebaseauthwithgoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    loadingDialog.dismissdialog();
                    Log.d("TAG", "signinwithcredential success");
                    Intent intent = new Intent(Signup.this, Home.class);
                    startActivity(intent);
                    FirebaseUser user = auth.getCurrentUser();
                    User u = new User(user.getDisplayName(), user.getEmail(), user.getUid().toString());
                    database.getReference().child("users").child(user.getUid()).setValue(u);
                } else {
                    finish();
                    loadingDialog.dismissdialog();
                    Log.w("TAG", "signinwithcredential failed", task.getException());
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Signup.this, Home.class);
            startActivity(intent);
        }
    }

}