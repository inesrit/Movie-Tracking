package com.example.movietracking;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieNameModel implements Parcelable {
    private String id;
    private String resultType;
    private String title;
    private String name;
    private String image;
    private String rating;


    public  MovieNameModel(){}

    public MovieNameModel(String id, String resultType, String title, String name, String image, String rating) {
        this.id = id;
        this.resultType = resultType;
        this.title = title;
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    protected MovieNameModel(Parcel in) {
        id = in.readString();
        resultType = in.readString();
        title = in.readString();
        name = in.readString();
        image = in.readString();
        rating = in.readString();
    }

    public static final Creator<MovieNameModel> CREATOR = new Creator<MovieNameModel>() {
        @Override
        public MovieNameModel createFromParcel(Parcel in) {
            return new MovieNameModel(in);
        }

        @Override
        public MovieNameModel[] newArray(int size) {
            return new MovieNameModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String moviename) {
        this.name = moviename;
    }

    public String getCoverImage() {
        return image;
    }

    public void setCoverImage(String coverImage) {
        this.image = coverImage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(resultType);
        dest.writeString(title);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(rating);
    }
}