package com.example.movietracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText forgotPassword;
    String forgotPass;
    Button send;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        forgotPassword =findViewById(R.id.ed_forgotPass);
        send =findViewById(R.id.send_email_forgotPass);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass = forgotPassword.getText().toString();
                if (TextUtils.isEmpty(forgotPass)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(ForgotPassword.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
                auth.sendPasswordResetEmail(forgotPass)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();
                            }
                        });
            }
        });
    }
}