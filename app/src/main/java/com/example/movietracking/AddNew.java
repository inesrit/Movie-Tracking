package com.example.movietracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddNew extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MovieNameModel> md;
     //private static String JSON_URL_DIS = "https://imdb-api.com/en/API/SearchMovie/k_ot7372p6/";
   //private static String JSON_URL_RATINGS = "https://imdb-api.com/en/API/Ratings/k_ot7372p6/";
//Secound IMBD Account
    private static String JSON_URL_DIS = "https://imdb-api.com/en/API/SearchMovie/k_za1wch72/";
    private static String JSON_URL_RATINGS = "https://imdb-api.com/en/API/Ratings/k_za1wch72/";
    MovienameAdapter adapter;
    private static final String TAG = "AddNew";
    private StringBuilder stringBuilder;

    String id;


    String resultType;
    String title;
    String name;
    String image;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        getSupportActionBar().hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNew.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        TextView tv_search_movie =findViewById(R.id.tv_search_movie);
         searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: " + "\n" + query);
                stringBuilder = new StringBuilder(query.toString());
                if(query != null){
                    tv_search_movie.setVisibility(View.GONE);
                    extractData(stringBuilder);
                    md.clear();
                }

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + "\n" + newText);
                return false;
            }

        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.i(TAG, "onClose: OnCloseListner" + "\n" + md.size());
                md.remove(md);
                return true;
            }
        });



        recyclerView = findViewById(R.id.recycleview_movie);
        md = new ArrayList<>();
    }

    private void extractData(StringBuilder stringBuilder) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.i(TAG, "extractData: completeUrl" + "\n" + JSON_URL_DIS + stringBuilder);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_DIS + stringBuilder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i(TAG, "extractData: Jsonresopnse" + "\n" + response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("asdsds", "adsds");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getString("id");
                        resultType = jsonObject.getString("resultType");
                        title = jsonObject.getString("description");
                        name = jsonObject.getString("title");
                        image = jsonObject.getString("image");

                        extractRating(id, resultType, title, name, image);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("fata", "" + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i(TAG, "onErrorResponse: ErrorException" + "\n" + error.getLocalizedMessage());
            }
        });

        queue.add(jsonArrayRequest);


    }

    private void extractRating(String id, String resultType, String title, String name, String image) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.i(TAG, "extractData: completeUrlraating" + "\n" + JSON_URL_RATINGS + id);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_RATINGS + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i(TAG, "extractData: resopnse" + "\n" + response.toString());
                try {

                    String imDbId = response.getString("imDbId");
                    String titel = response.getString("title");
                    String fullTitle = response.getString("fullTitle");
                    String type = response.getString("type");
                    String year = response.getString("year");
                    String rating = response.getString("imDb");
                    String metacritic = response.getString("metacritic");
                    String theMovieDb = response.getString("theMovieDb");
                    String rottenTomatoes = response.getString("rottenTomatoes");
                    String tV_com = response.getString("tV_com");
                    String filmAffinity = response.getString("filmAffinity");

                    md.add(new MovieNameModel(id, resultType, title, name, image, rating));
                    Log.d("fata", "" + id);

                    Log.i(TAG, "extractData: completeRating" + "\n" + rating);

                    //md.add(new MovieNameModel(rating, percent, votes));


                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new MovienameAdapter(getApplicationContext(), md);
                    recyclerView.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("fata", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i(TAG, "onErrorResponse: ErrorException" + "\n" + error.getLocalizedMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }
}