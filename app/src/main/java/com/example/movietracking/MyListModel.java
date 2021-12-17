package com.example.movietracking;

import android.os.Parcel;
import android.os.Parcelable;

public class MyListModel implements Parcelable {
    private String documentId, movieRating, movieName, movieDescription , movieImg, userWatchMovie, userComment;
    private double userRating;
    public MyListModel() {
    }

    public MyListModel(String documentId, String movieRating, String movieName, String movieDescription, String movieImg, String userWatchMovie, String userComment, double userRating) {
        this.documentId = documentId;
        this.movieRating = movieRating;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieImg = movieImg;
        this.userWatchMovie = userWatchMovie;
        this.userComment = userComment;
        this.userRating = userRating;
    }

    protected MyListModel(Parcel in) {
        documentId = in.readString();
        movieRating = in.readString();
        movieName = in.readString();
        movieDescription = in.readString();
        movieImg = in.readString();
        userWatchMovie = in.readString();
        userComment = in.readString();
        userRating = in.readDouble();
    }

    public static final Creator<MyListModel> CREATOR = new Creator<MyListModel>() {
        @Override
        public MyListModel createFromParcel(Parcel in) {
            return new MyListModel(in);
        }

        @Override
        public MyListModel[] newArray(int size) {
            return new MyListModel[size];
        }
    };

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getUserWatchMovie() {
        return userWatchMovie;
    }

    public void setUserWatchMovie(String userWatchMovie) {
        this.userWatchMovie = userWatchMovie;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(movieRating);
        dest.writeString(movieName);
        dest.writeString(movieDescription);
        dest.writeString(movieImg);
        dest.writeString(userWatchMovie);
        dest.writeString(userComment);
        dest.writeDouble(userRating);
    }
}
