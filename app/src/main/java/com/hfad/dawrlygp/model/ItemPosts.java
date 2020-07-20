package com.hfad.dawrlygp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.dawrlygp.R;

import java.io.FileNotFoundException;
import java.io.Serializable;

public class ItemPosts implements Parcelable {

    private String modelName , color , title , description , location , time
            , imageUrl , userId , postDate, postId;

    private int typeId ,postItemsId;

    private String city ;

    private String communicationByMob;

    public ItemPosts() {
    }

    protected ItemPosts(Parcel in) {
        modelName = in.readString();
        color = in.readString();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        time = in.readString();
        imageUrl = in.readString();
        userId = in.readString();
        postDate = in.readString();
        postId = in.readString();
        typeId = in.readInt();
        postItemsId = in.readInt();
        city = in.readString();
        communicationByMob = in.readString();
    }

    public static final Creator<ItemPosts> CREATOR = new Creator<ItemPosts>() {
        @Override
        public ItemPosts createFromParcel(Parcel in) {
            return new ItemPosts(in);
        }

        @Override
        public ItemPosts[] newArray(int size) {
            return new ItemPosts[size];
        }
    };

    public String getCommunicationByMob() {
        return communicationByMob;
    }

    public void setCommunicationByMob(String communicationByMob) {
        this.communicationByMob = communicationByMob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getPostItemsId() {
        return postItemsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPostItemsId(int postItemsId) {
        this.postItemsId = postItemsId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modelName);
        dest.writeString(color);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(time);
        dest.writeString(imageUrl);
        dest.writeString(userId);
        dest.writeString(postDate);
        dest.writeString(postId);
        dest.writeInt(typeId);
        dest.writeInt(postItemsId);
        dest.writeString(city);
        dest.writeString(communicationByMob);
    }



}
