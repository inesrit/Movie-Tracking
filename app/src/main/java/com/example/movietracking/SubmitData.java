package com.example.movietracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubmitData extends AppCompatActivity {
    private static final String TAG = "Submit Data";
    ArrayList<MovieNameModel> md;
    private Switch aSwitch;
    private RatingBar ratingBar;
    float UserRatingValue;
    private EditText comment;
    private String switchButtonStatus="No",userComment;
    private Button saveBtn;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    String ivMovieImg,tvmovieName,tvDecription,tvGetRating;
    String currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_data);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        db = FirebaseFirestore.getInstance();
         currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "onCreate: GetCurrentUerId" + "\n" + currentuser);
        saveBtn =findViewById(R.id.saveBtnaddData);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        comment = findViewById(R.id.edComment);

        aSwitch =findViewById(R.id.switchButton);
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.i(TAG, "onRatingChanged: userSwitchButton" + "\n" + isChecked);
                if(isChecked){
                    switchButtonStatus="Yes";
                }else{
                    switchButtonStatus="No";
                }
            }
        });

        ratingBar = findViewById(R.id.getratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                UserRatingValue = rating;
                Log.i(TAG, "onRatingChanged: userRating" + "\n" + rating);



            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubmitData.this, AddNew.class);
                startActivity(i);
                finish();
            }
        });

        Intent intent = getIntent();
        MovieNameModel model = intent.getParcelableExtra("ExampleItem");
         ivMovieImg = model.getCoverImage();
         tvmovieName = model.getName();
         tvDecription = model.getTitle();
         tvGetRating = model.getRating();


        Log.i(TAG, "onCreate: objectData " + "\n" + ivMovieImg + "\n" + tvmovieName + "\n" + tvDecription);

        ImageView imageView = findViewById(R.id.ivMovieId);
        Picasso.get()
                .load(ivMovieImg)
                .into(imageView);
        Log.i(TAG, "onCreate: ImageView " + "\n"  + ivMovieImg);
        TextView tvName = findViewById(R.id.tvMovieNameId);
        tvName.setText(tvmovieName);
        TextView tvDec = findViewById(R.id.tvMovieDescriptionId);
        tvDec.setText(tvDecription);
        TextView tvRating = findViewById(R.id.tvRatingId);
        tvRating.setText(tvGetRating);


    }

   private void saveData(){


        userComment = comment.getText().toString();
       if (TextUtils.isEmpty(userComment)) {
           comment.setError("Please Enter Comment");
       }else {

           addDataToFirestore();
       }
   }
    private void addDataToFirestore() {
        ProgressDialog progressDialog = new ProgressDialog(SubmitData.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String documentId = db.collection("Current User Movie List").document().getId();
        Log.i(TAG, "addDataToFirestore: documentIdCheck" + "\n" + documentId);
        Map<String, Object> user = new HashMap<>();
        user.put("movieName", tvmovieName);
        user.put("movieDescription",tvDecription);
        user.put("movieRating",tvGetRating);
        user.put("movieImg",ivMovieImg);
        user.put("userComment", userComment);
        user.put("userWatchMovie", switchButtonStatus);
        user.put("userRating", UserRatingValue);
        user.put("documentId", documentId);

        db.collection("All Movie List")
                .document(currentuser)
                .collection("Current User Movie List")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "onSuccess: documentReferenceId" + "\n" + documentReference.getId());
                        progressDialog.dismiss();
                        Toast.makeText(SubmitData.this, "Data Save successfully!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SubmitData.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i(TAG, "onFailuer: userdatalistError" + "\n" + e.getLocalizedMessage());

                    }
                });

    }
}