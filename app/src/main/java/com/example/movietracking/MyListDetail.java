package com.example.movietracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MyListDetail extends AppCompatActivity {
    private static final String TAG = "MyListDetail";
    private FirebaseFirestore db;
    private Switch aSwitch;
    private RatingBar ratingBar;
    float UserRatingValue;
    private String switchButtonStatus = "No", userComment;
    private Button updateBtn;
    EditText comment;
    String ivMovieImg, tvmovieName, tvDecription, tvGetRating, tvGetUserComment, tvGetUserWatchMovie, documentId;
    double tvGetUserRating;
    String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_detail);
        getSupportActionBar().hide();

        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyListDetail.this, MyList.class);
                startActivity(i);
                finish();
            }
        });
        comment = findViewById(R.id.tvCommentMyListDetail);
        updateBtn = findViewById(R.id.btnEditMyListData);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        aSwitch = findViewById(R.id.switchButtonUpdateData);
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onRatingChanged: userSwitchButton" + "\n" + isChecked);
                if (isChecked) {
                    switchButtonStatus = "Yes";
                } else {
                    switchButtonStatus = "No";
                }
            }
        });

        ratingBar = findViewById(R.id.myRatingMyList);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                UserRatingValue = rating;
                Log.i(TAG, "onRatingChanged: userRating" + "\n" + rating);
            }
        });

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        MyListModel model = intent.getParcelableExtra("MyListItem");
        ivMovieImg = model.getMovieImg();
        tvmovieName = model.getMovieName();
        tvDecription = model.getMovieDescription();
        tvGetRating = model.getMovieRating();
        tvGetUserComment = model.getUserComment();
        tvGetUserRating = model.getUserRating();
        documentId = model.getDocumentId();
        tvGetUserWatchMovie = model.getUserWatchMovie();
        if (tvGetUserWatchMovie.equals("Yes")){
//            aSwitch.setTextOn("ON");
            aSwitch.setChecked(true);
        }
        Log.i(TAG, "onCreate: objectData " + "\n" + ivMovieImg + "\n" + tvmovieName + "\n" + tvGetUserWatchMovie);

        ImageView imageView = findViewById(R.id.ivMovieIdMyListDetail);
        Picasso.get()
                .load(ivMovieImg)
                .into(imageView);
        Log.i(TAG, "onCreate: ImageView " + "\n" + ivMovieImg);
        TextView tvName = findViewById(R.id.tvMovieNameIdMyListDetail);
        tvName.setText(tvmovieName);
        TextView tvDec = findViewById(R.id.tvMovieDescriptionIdMyListDetail);
        tvDec.setText(tvDecription);
        TextView tvRating = findViewById(R.id.tvRatingIdMyListDetail);
        tvRating.setText(tvGetRating);

        String userRating = Double.toString(tvGetUserRating);
        ratingBar.setRating(Float.parseFloat(userRating));

//        RatingBar ratingBar = new RatingBar(getApplicationContext());
//        ratingBar.setNumStars((int) tvGetUserRating);


        TextView tvUserComment = findViewById(R.id.tvCommentMyListDetail);
        tvUserComment.setText(tvGetUserComment);
     /*   TextView tvUserWatchmovie = findViewById(R.id.tvUserWatchMovieMyListDetail);
        tvUserWatchmovie.setText(tvGetUserWatchMovie);*/
    }





    private void updateData() {

        userComment = comment.getText().toString();
        if (TextUtils.isEmpty(userComment)) {
            comment.setError("Please Enter Comment");
        } else {

            updateDataToFirestore();

        }
    }

    private void updateDataToFirestore() {
        Map<String, Object> user = new HashMap<>();
        user.put("movieName", tvmovieName);
        user.put("movieDescription", tvDecription);
        user.put("movieRating", tvGetRating);
        user.put("movieImg", ivMovieImg);
        user.put("documentId", documentId);
        user.put("userComment", userComment);
        user.put("userWatchMovie", switchButtonStatus);
        user.put("userRating", UserRatingValue);

        db.collection("All Movie List")
                .document(currentuser)
                .collection("Current User Movie List")
                .document(documentId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MyListDetail.this, "Details has been updated..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailuer: userdatalistError" + "\n" + e.getLocalizedMessage());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(MyListDetail.this, MyList.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }

}