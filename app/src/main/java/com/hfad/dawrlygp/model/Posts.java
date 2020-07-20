package com.hfad.dawrlygp.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Helpers.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class Posts  implements Parcelable {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    // for Adapters
    private String fname , lname , userImageUrl;
    private String imageUrl;
    private String location;
    private String decription ;
    private String time ;
    private String textBrandNameName ; //brand name and name
    private String textColorGender ; // color and gender
    private String textAgeModelName; // age and name model
    private int typeId ;
    private String userId;
    private String postId;
    private int PostItemsId ;
    private String postDate ;
    private boolean status ;
    private String postTypeId;
    private String city ;
    private String userNumber ;
    private String communicationByNumber ;
    private double similarity ;

    public Posts() {
    }

    protected Posts(Parcel in) {
        fname = in.readString();
        lname = in.readString();
        userImageUrl = in.readString();
        imageUrl = in.readString();
        location = in.readString();
        decription = in.readString();
        time = in.readString();
        textBrandNameName = in.readString();
        textColorGender = in.readString();
        textAgeModelName = in.readString();
        typeId = in.readInt();
        userId = in.readString();
        postId = in.readString();
        PostItemsId = in.readInt();
        postDate = in.readString();
        status = in.readByte() != 0;
        postTypeId = in.readString();
        city = in.readString();
        userNumber = in.readString();
        communicationByNumber = in.readString();
    }


    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        @Override
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        @Override
        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    public String getCommunicationByNumber() {
        return communicationByNumber;
    }

    public void setCommunicationByNumber(String communicationByNumber) {
        this.communicationByNumber = communicationByNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTextColorGender() {
        return textColorGender;
    }

    public void setTextColorGender(String textColorGender) {
        this.textColorGender = textColorGender;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getTextAgeModelName() {
        return textAgeModelName;
    }

    public void setTextAgeModelName(String textAgeModelName) {
        this.textAgeModelName = textAgeModelName;
    }

    public String getTextBrandNameName() {
        return textBrandNameName;
    }

    public void setTextBrandNameName(String textBrandNameName) {
        this.textBrandNameName = textBrandNameName;
    }


    public String getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPostItemsId() {
        return PostItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        PostItemsId = postItemsId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }



    @BindingAdapter({"android:imageUrl"})
    public static void loadImage(ImageView view, String image) {

        Picasso.get()
                .load(image)
                .fit()
                .placeholder(R.drawable.noimage)
                .into(view);

    }

    private static final String TAG = "Posts";

    @BindingAdapter({"android:imageUser"})
    public static void load(ImageView view, String image) {

        Log.d(TAG, "load: "+image);
        Picasso.get()
                .load(image)
                .transform(new CircleTransformation())
                .placeholder(R.drawable.pp)
                .into(view);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(userImageUrl);
        dest.writeString(imageUrl);
        dest.writeString(location);
        dest.writeString(decription);
        dest.writeString(time);
        dest.writeString(textBrandNameName);
        dest.writeString(textColorGender);
        dest.writeString(textAgeModelName);
        dest.writeInt(typeId);
        dest.writeString(userId);
        dest.writeString(postId);
        dest.writeInt(PostItemsId);
        dest.writeString(postDate);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(postTypeId);
        dest.writeString(city);
        dest.writeString(userNumber);
        dest.writeString(communicationByNumber);
        dest.writeDouble(similarity);
    }
}
