package com.example.movietracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    EditText name, password, email;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    String userName;
    String userEmail;
    String userPass;
    String currentuser;
    String SAVE_USER_DATA_INFO = "User_Profile";
    ImageView showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        name = findViewById(R.id.ed_name_rg);
        email = findViewById(R.id.ed_email_rg);
        password = findViewById(R.id.ed_pass_rg);
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
        Button sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userName = name.getText().toString();
                userEmail = email.getText().toString();
                userPass = password.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    name.setError("Enter Name");
                } else if (TextUtils.isEmpty(userEmail)) {

                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    email.setError("Enter Email");
                } else if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    password.setError("Enter Password");
                } else if (userPass.length() < 6) {

                    Toast.makeText(getApplicationContext(), "Enter minimum 6 digit pass", Toast.LENGTH_SHORT).show();
                    password.setError("Enter 6 digit Password");
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    Log.i(TAG, "onSuccess: userregistration" + "\n" + task);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("firstname", userName);
                                    user.put("email", userEmail);
                                    user.put("password", userPass);
                                    user.put("userId", auth.getUid());
                                    user.put("id", "");
                                    db.collection("userdata")
                                            .add(user)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.i(TAG, "onSuccess: userdatacollection" + "\n" + documentReference.getId());
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Registration successfully!!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Log.i(TAG, "onSuccess: userdatacollectionExecption" + "\n" + e.getLocalizedMessage());
                                                }
                                            });
//
                                }
                            });
                }

            }
        });
        TextView signin_btn = (TextView) findViewById(R.id.tv_signin);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}