package com.example.movietracking;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfileModel implements Parcelable {
    private String userId;
    private String firstname;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


    public UserProfileModel(){}

    public UserProfileModel(String userId, String firstname, String email, String password) {
        this.userId = userId;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
    }

    protected UserProfileModel(Parcel in) {
        userId = in.readString();
        firstname = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<UserProfileModel> CREATOR = new Creator<UserProfileModel>() {
        @Override
        public UserProfileModel createFromParcel(Parcel in) {
            return new UserProfileModel(in);
        }

        @Override
        public UserProfileModel[] newArray(int size) {
            return new UserProfileModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstname);
        dest.writeString(email);
        dest.writeString(password);
    }
}