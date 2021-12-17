package com.example.movietracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyList extends AppCompatActivity implements MyListRVAdapter.MyListItemDelete {
    private static final String TAG = "MYLIST";
    private RecyclerView myListRV;
    private ArrayList<MyListModel> myListArrayList;
    private MyListRVAdapter myListRVAdapter;
    private FirebaseFirestore db;
    DocumentReference productIdRef;
    String documentId;
    ProgressDialog progressDialog;
    String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        getSupportActionBar().hide();

        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyList.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        myListRV = findViewById(R.id.rvMyList);
        db = FirebaseFirestore.getInstance();
        myListArrayList = new ArrayList<>();
        myListRV.setHasFixedSize(true);
        myListRV.setLayoutManager(new LinearLayoutManager(this));
        myListRVAdapter = new MyListRVAdapter(myListArrayList, this);
        myListRV.setAdapter(myListRVAdapter);
        getVideosDataFromDataBase();

    }

    private void getVideosDataFromDataBase() {
        documentId = db.collection("Current User Movie List").document().getId();
        Log.i(TAG, "onCreate: DocumentId" + "\n" + documentId);
        db.collection("All Movie List")
                .document(currentuser)
                .collection("Current User Movie List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String documentId, movieRating, movieName, movieDescription, movieImg, userWatchMovie, userComment;
                                double userRating;
                                documentId = documentSnapshot.getId();
                                Log.i(TAG, "onComplete: documentsnapshotId" + "\n" + documentSnapshot.getId() + "\n" + documentId);
                                movieRating = documentSnapshot.getString("movieRating");
                                movieName = documentSnapshot.getString("movieName");
                                movieDescription = documentSnapshot.getString("movieDescription");
                                movieImg = documentSnapshot.getString("movieImg");
                                userWatchMovie = documentSnapshot.getString("userWatchMovie");
                                userComment = documentSnapshot.getString("userComment");
                                userRating = documentSnapshot.getDouble("userRating");
                                MyListModel myListModel = new MyListModel(documentId, movieRating, movieName, movieDescription, movieImg, userWatchMovie, userComment, userRating);
//                                MyListModel myListModel = documentSnapshot.toObject(MyListModel.class);
//                                Log.i(TAG, "onComplete: modelDataCheck" + "\n" + myListModel.getMovieDescription() + "\n" + myListModel.getMovieImg() + "\n" + myListModel.getMovieName());
                                myListArrayList.add(myListModel);
                                myListRVAdapter.notifyDataSetChanged();

                                for (int i = 0; i < myListArrayList.size(); i++) {
                                    Log.i(TAG, "onComplete: documentIdCheckByList" + "\n" + myListArrayList.get(i).getDocumentId());
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MyList.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onItemClick(MyListModel myListModel) {
        Log.i(TAG, "onItemClick: DocumentId" + "\n" + myListModel.getDocumentId());
        new AlertDialog.Builder(MyList.this)
                .setTitle("Are you sure to delete?")
                .setMessage("Click yes to delete movie")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Positive");
                        productIdRef = db.collection("All Movie List")
                                .document(currentuser)
                                .collection("Current User Movie List")
                                .document(myListModel.getDocumentId());
                        productIdRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MyList.this, "Deleted successfully!!", Toast.LENGTH_SHORT).show();
                                        myListArrayList.clear();
                                        getVideosDataFromDataBase();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyList.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Negative");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}