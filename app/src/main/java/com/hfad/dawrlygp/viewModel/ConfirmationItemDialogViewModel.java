package com.hfad.dawrlygp.viewModel;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.SavedPosts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationItemDialogViewModel extends ViewModel {

    private FirebaseAuth auth ;
    private DatabaseReference ref ;
    private Location postLocation ;
    private SavedPosts savedPosts ;
    private StorageReference storageReference ;
    private String postId , postLocationId ,savePostId , bendingPostId
            , bendingNotificationId,  userId;

    private static final String TAG = "ConfirmationItemDialogV";

    public boolean save(ItemPosts itemPosts , double lat , double lon , String fileEx) {
        auth = FirebaseAuth.getInstance();
       userId = auth.getCurrentUser().getUid();
        if (auth.getCurrentUser() != null) {

            mobilePostsTable(itemPosts,fileEx);

            int delay = 6000;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (postId != null){
                        
                        postLocationTable(lat,lon,itemPosts.getPostItemsId()+1);

                        savedPostTable(itemPosts.getPostItemsId());
                    }
                    else handler.postDelayed(this, delay);


                }
            },delay);

            return true ;

        }
        else {
            return false;
        }
    }

    public boolean post (ItemPosts itemPosts , double lat , double lon , String fileEx)
    {

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        if (auth.getCurrentUser() != null){

            mobilePostsTable(itemPosts,fileEx);
            int delay = 6000;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (postId != null){
                        postLocationTable(lat,lon,itemPosts.getPostItemsId()+1);
                        bendingPostTable(itemPosts.getPostItemsId());
                        NotificationTable();
                    }
                    else handler.postDelayed(this, delay);


                }
            },delay);


            return true ;
        }
        return false ;
    }


    private void mobilePostsTable(ItemPosts posts , String fileEx)
    {
        ref = FirebaseDatabase.getInstance().getReference().child("Item_Posts");
        if (posts.getImageUrl() != null){
            Uri uri =Uri.parse(posts.getImageUrl());
            Log.d(TAG, "post: "+uri);
            storageReference = FirebaseStorage.getInstance().getReference()
                    .child("PostImages/"+System.currentTimeMillis()
                            + "." + fileEx);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: 1 ");
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
                    Date date1 = new Date();
                    final String currentTime =  df.format(date1);
                    storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                        String postImage=task.getResult().toString();
                        Log.d(TAG,"PostImage"+postImage);
                        posts.setUserId(userId);
                        postId = ref.push().getKey();
                        posts.setPostId(postId);
                        posts.setImageUrl(postImage);
                        posts.setPostDate(currentTime);
                        ref.push().setValue(posts);

                    });
                }
            });

        }
        else {
            posts.setUserId(userId);
            postId = ref.push().getKey();
            posts.setPostId(postId);
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
            Date date1 = new Date();
            final String currentTime =  df.format(date1);
            posts.setPostDate(currentTime);
            ref.push().setValue(posts);
        }



    }

    private void postLocationTable(double lat,double lon, int position)
    {
        ref = FirebaseDatabase.getInstance().getReference().child("Location");
        postLocation = new Location();
        postLocation.setId(postId);
        postLocationId = ref.push().getKey();
        postLocation.setLocationId(postLocationId);
        postLocation.setLat(lat);
        postLocation.setLon(lon);
        postLocation.setPostItemsId(position);
        postLocation.setSendBy("Post");
        ref.push().setValue(postLocation);
    }

    private void savedPostTable(int position)
    {
        ref = FirebaseDatabase.getInstance().getReference().child("Saved_Posts");
        savedPosts = new SavedPosts();
        savePostId = ref.push().getKey();
        savedPosts.setId(savePostId);
        savedPosts.setPostId(postId);
        savedPosts.setPostItemsId(position);
        ref.push().setValue(savedPosts);
    }

    private void bendingPostTable(int position)
    {
        ref = FirebaseDatabase.getInstance().getReference().child("Bending_Posts");
        BendingPosts bendingPosts = new BendingPosts();
        bendingPostId = ref.push().getKey();
        bendingPosts.setId(bendingPostId);
        bendingPosts.setPostId(postId);
        bendingPosts.setPostItemsId(position);
        bendingPosts.setSeen(false);
        ref.push().setValue(bendingPosts);

    }

    private void NotificationTable()
    {
        ref = FirebaseDatabase.getInstance().getReference().child("Notification");
        Notification notification = new Notification();
        bendingNotificationId = ref.push().getKey();
        notification.setNotificationPostId(bendingNotificationId);
        notification.setPostId(bendingPostId);
        notification.setPostType("Admin");
        ref.push().setValue(notification);

    }

}
