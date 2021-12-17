package com.example.movietracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText password, email;
    private FirebaseAuth auth;
    private static final String TAG = "Login";
    String SAVE_USER_DATA_INFO = "User_Profile";
    private FirebaseFirestore db;
    ImageView showPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        email = findViewById(R.id.ed_email);
        password = findViewById(R.id.ed_pass);

        showPass = findViewById(R.id.ivShowPasswordId);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    showPass.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showPass.setImageResource(R.drawable.ic_baseline_visibility_24);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        Button signin_btn = (Button) findViewById(R.id.sign_in_btn);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPass = password.getText().toString();

                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    email.setError("Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    password.setError("Enter Password");
                    return;

                }
                if (userPass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Enter minimum 6 digit pass", Toast.LENGTH_SHORT).show();
                    password.setError("Enter 6 digit Password");
                    return;
                }
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
                auth.signInWithEmailAndPassword(userEmail, userPass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                               // String firebaseUser = task.getResult().getUser().getUid();
                                //Log.i(TAG, "onComplete: UserId" + "\n" + firebaseUser);
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {


                                    Toast.makeText(LoginActivity.this, "Login successfully!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(LoginActivity.this, "Email Password Not Register" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.i(TAG, "onFailure: OnFaulier Login" + "\n" + e.getLocalizedMessage());
                            }
                        });
            }
        });
        TextView tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tv_forgotPass = (TextView) findViewById(R.id.tv_forgotPass);
        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}