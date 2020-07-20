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

public class MessageInfo  implements Parcelable {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private String fname ;

    private String lname ;

    private String image;

    private String lastMessage ;

    private String receiverId;

    private boolean seen ;

    private String date ;

    private String senderId ;

    public MessageInfo() {
    }

    public MessageInfo(Parcel in) {
        fname = in.readString();
        lname = in.readString();
        image = in.readString();
        lastMessage = in.readString();
        receiverId = in.readString();
        seen = in.readByte() != 0;
        date = in.readString();
        senderId = in.readString();
    }

    public static final Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {
        @Override
        public MessageInfo createFromParcel(Parcel in) {
            return new MessageInfo(in);
        }

        @Override
        public MessageInfo[] newArray(int size) {
            return new MessageInfo[size];
        }
    };

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(image);
        dest.writeString(lastMessage);
        dest.writeString(receiverId);
        dest.writeByte((byte) (seen ? 1 : 0));
        dest.writeString(date);
        dest.writeString(senderId);
    }

    private static final String TAG = "MessageInfo";
    @BindingAdapter({"android:imageUser"})
    public static void load(ImageView view, String image) {


        Log.d(TAG, "load: "+image);
        Picasso.get()
                .load(image)
                .transform(new CircleTransformation())
                .placeholder(R.drawable.pp)
                .into(view);


    }
}
