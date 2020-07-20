package com.hfad.dawrlygp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HumanPosts implements Parcelable {
    private String name ;
    private String imageUrl;
    private String location;
    private String description ;
    private String time ;
    private int age ;
    private String gender ;
    private int typeId ;
    private String userId;
    private String postId;
    private int PostItemsId ;
    private String postDate ;
    private String city ;
    private String communicationByMob ;


    protected HumanPosts(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        location = in.readString();
        description = in.readString();
        time = in.readString();
        age = in.readInt();
        gender = in.readString();
        typeId = in.readInt();
        userId = in.readString();
        postId = in.readString();
        PostItemsId = in.readInt();
        postDate = in.readString();
        city = in.readString();
        communicationByMob = in.readString();
    }

    public static final Creator<HumanPosts> CREATOR = new Creator<HumanPosts>() {
        @Override
        public HumanPosts createFromParcel(Parcel in) {
            return new HumanPosts(in);
        }

        @Override
        public HumanPosts[] newArray(int size) {
            return new HumanPosts[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getPostItemsId() {
        return PostItemsId;
    }

    public void setPostItemsId(int postItemsId) {
        PostItemsId = postItemsId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HumanPosts(String name, String imageUrl, String location, String decription,
                      String time) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.description = decription;
        this.time = time;
    }

    public HumanPosts(String imageId , String name) {
        this.name = name;
        this.imageUrl = imageId;
    }

    public HumanPosts(String name, String location, String decription) {
        this.name = name;
        this.location = location;
        this.description = decription;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HumanPosts(String name, String imageUrl, String location , String decription) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.description = decription;
    }

    public HumanPosts() {}

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageId) {
        this.imageUrl = imageId;
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
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(time);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeInt(typeId);
        dest.writeString(userId);
        dest.writeString(postId);
        dest.writeInt(PostItemsId);
        dest.writeString(postDate);
        dest.writeString(city);
        dest.writeString(communicationByMob);
    }
}
