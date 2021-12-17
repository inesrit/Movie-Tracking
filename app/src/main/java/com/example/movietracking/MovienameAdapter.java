package com.example.movietracking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovienameAdapter extends RecyclerView.Adapter<MovienameAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<MovieNameModel> modelData;
    private Context context;

    public MovienameAdapter(Context ctx, List<MovieNameModel> modelData) {
        this.inflater = LayoutInflater.from(ctx);
        this.modelData = modelData;
        this.context = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.searchmoviecard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(modelData.get(position).getTitle());
        holder.name.setText(modelData.get(position).getName());
        Picasso.get()
                .load(modelData.get(position)
                        .getCoverImage())
                .into(holder.image);
        holder.rating.setText(modelData.get(position).getRating());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SubmitData.class);
                i.putExtra("ExampleItem", modelData.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
              //  ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, name, rating;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.movie_description);
            name = itemView.findViewById(R.id.movie_name);
            image = itemView.findViewById(R.id.movie_image);
            rating = itemView.findViewById(R.id.get_rating);


        }
    }
}
