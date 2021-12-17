package com.example.movietracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class User_Profile extends AppCompatActivity {
    private static final String TAG = "User_Profile";
    private FirebaseFirestore db;
    private Button updateProfileBtn;
    EditText edUsername;
    TextView edEmail,edPassword;
    String SAVE_USER_DATA_INFO = "User_Profile";
    String userName, userEmail, userPass;
    UserProfileModel updateModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();


       String  currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "onComplete: CurrentUserId" + "\n" + currentuser);
        db = FirebaseFirestore.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_Profile.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
       /* SharedPreferences prefs = getSharedPreferences(SAVE_USER_DATA_INFO, MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
*/
        db.collection("userdata")
                .whereEqualTo("userId" ,currentuser)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i(TAG, "onComplete: CurrentUserIdDocumentSnapshot" + "\n" + queryDocumentSnapshots);

                Log.i(TAG, "onComplete: CurrentUserIdDAta" + "\n" + userName + "\n" +userEmail + "\n"+ userPass );
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                     updateModel =doc.toObject(UserProfileModel.class);
                     updateModel.setId(doc.getId());
                }
                edEmail.setText(updateModel.getEmail().toString());
                edUsername.setText(updateModel.getFirstname().toString());
                edPassword.setText(updateModel.getPassword().toString());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {

                    }
                });
        edEmail =findViewById(R.id.ed_email_rg_profile);
        edUsername = findViewById(R.id.ed_name_rg_profile);
        edPassword = findViewById(R.id.ed_pass_rg_profile);

        updateProfileBtn =findViewById(R.id.update_profile);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateModel.setFirstname(edUsername.getText().toString());
                db.collection("userdata")
                        .document(updateModel.getId())
                        .set(updateModel, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(User_Profile.this, "Details has been updated..", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {

                    }
                });
            }
        });

    }
}