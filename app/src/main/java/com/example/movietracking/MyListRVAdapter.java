package com.example.movietracking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListRVAdapter extends RecyclerView.Adapter<MyListRVAdapter.ViewHolder> {

    private ArrayList<MyListModel> myListArrayList;
    private Context context;
    private static final String TAG = "MyListRVAdapter";
    public MyListItemDelete myListItemDelete;



    public MyListRVAdapter(ArrayList<MyListModel> myListArrayList, Context context) {
        this.myListArrayList = myListArrayList;
        this.context = context;
        this.myListItemDelete = (MyListItemDelete) context;
    }

    public interface MyListItemDelete {
        void onItemClick(MyListModel myListModel);
    }

    @NonNull
    @Override
    public MyListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mylistitemscard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyListRVAdapter.ViewHolder holder, int position) {

//        MyListModel myListModel = myListArrayList.get(position);

//        Log.i(TAG, "onBindViewHolder: myListRvAdapter" + "\n" + myListModel.getMovieImg() + "\n" + myListModel.getMovieDescription());
        holder.movie_name.setText(myListArrayList.get(position).getMovieName());
        holder.get_rating.setText(myListArrayList.get(position).getMovieRating());
        holder.movie_description.setText(myListArrayList.get(position).getMovieDescription());
        holder.watchYesorNo.setText(myListArrayList.get(position).getUserWatchMovie());
        holder.tv_MyRating.setText(String.valueOf(myListArrayList.get(position).getUserRating()));
        holder.tvYourComment.setText(myListArrayList.get(position).getUserComment());
        Picasso.get()
                .load(myListArrayList.get(position)
                        .getMovieImg())
                .into(holder.myListmovie_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MyListDetail.class);
                i.putExtra("MyListItem", myListArrayList.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                ((Activity)context).finish();
                //  ((Activity)context).finish();

            }
        });

        holder.ivDeleteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+myListArrayList.get(position).getDocumentId());
                myListItemDelete.onItemClick(myListArrayList.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myListArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView movie_name;
        private TextView get_rating;
        private TextView movie_description;
        private TextView watchYesorNo;
        private TextView tv_MyRating;
        private TextView tvYourComment;
        private ImageView myListmovie_image;
       private  ImageView ivDeleteMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movie_name = itemView.findViewById(R.id.movie_name);
            get_rating = itemView.findViewById(R.id.get_rating);
            movie_description = itemView.findViewById(R.id.movie_description);
            watchYesorNo = itemView.findViewById(R.id.watchYesorNo);
            tv_MyRating = itemView.findViewById(R.id.tv_MyRating);
            tvYourComment = itemView.findViewById(R.id.tvYourComment);
            ivDeleteMovie = itemView.findViewById(R.id.ivDeleteMovieId);
            myListmovie_image = itemView.findViewById(R.id.myListmovie_image);
        }
    }
}

