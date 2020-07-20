package com.hfad.dawrlygp.viewModel;

import android.annotation.SuppressLint;
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
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.SavedPosts;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationHumanDialogViewModel extends ViewModel {

 private FirebaseAuth auth ;
 private Location postLocation ;
 private SavedPosts savedPosts ;
 private StorageReference storageReference ;
 private String postId , postLocationId ,savePostId , bendingPostId , bendingNotificationId;
private static final String TAG = "ConfirmationHumanDialogViewModel";


    @SuppressLint("LongLogTag")
    public boolean save(HumanPosts humanPosts , double lat, double lon , String fileEx){

         auth = FirebaseAuth.getInstance();
        Log.d(TAG, "post: "+fileEx);
        int delay = 6000;
         final String userId = auth.getCurrentUser().getUid();
         if (auth.getCurrentUser() != null){

            humanPostsTable(humanPosts,userId , fileEx);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (postId != null){
                        postLocationTable(lat,lon,humanPosts.getPostItemsId());
                        savedPostTable(humanPosts.getPostItemsId());
                    }
                    else handler.postDelayed(this, delay);
                }
            },delay);

            return true ;
     }
     return false ;
 }

    @SuppressLint("LongLogTag")
    public boolean post (HumanPosts humanPosts , double lat, double lon , String fileEx ){
        auth = FirebaseAuth.getInstance();
        Log.d(TAG, "post: "+fileEx);
        int delay = 6000;
        final String userId = auth.getCurrentUser().getUid();
        if (auth.getCurrentUser() != null){
            humanPostsTable(humanPosts,userId,fileEx);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (postId != null){
                        postLocationTable(lat,lon,humanPosts.getPostItemsId());
                        bendingPostTable(humanPosts.getPostItemsId());
                        NotificationTable();
                    }
                    else handler.postDelayed(this, delay);


                }
            },delay);

            return true ;
        }
        return false ;
    }


   @SuppressLint("LongLogTag")
   private  void humanPostsTable(HumanPosts humanPosts , String userId , String fileEx) {
       DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Human_Posts");

       if (humanPosts.getImageUrl() != null){
           Uri uri =Uri.parse(humanPosts.getImageUrl());
           Log.d(TAG, "post: "+uri);
           storageReference = FirebaseStorage.getInstance().getReference()
                   .child("PostImages/"+System.currentTimeMillis()
                           + "." + fileEx);
           storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
               Log.d(TAG, "onSuccess: 1 ");
               DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
               Date date1 = new Date();
               final String currentTime =  df.format(date1);
               storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                   String postImage=task.getResult().toString();
                   Log.d(TAG,"PostImage"+postImage);
                   humanPosts.setUserId(userId);
                   postId = ref.push().getKey();
                   humanPosts.setPostId(postId);
                   humanPosts.setImageUrl(postImage);
                   humanPosts.setPostDate(currentTime);
                   ref.push().setValue(humanPosts);

               });
           });

       }
       else {
           DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy h:mm a");
           Date date1 = new Date();
           final String currentTime =  df.format(date1);
           humanPosts.setUserId(userId);
           postId = ref.push().getKey();
           humanPosts.setPostId(postId);
           humanPosts.setPostDate(currentTime);
           ref.push().setValue(humanPosts);
       }
    }

     private void postLocationTable(double lat,double lon, int position){
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Location");
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

     private void savedPostTable(int position){
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Saved_Posts");
     savedPosts = new SavedPosts();
     savePostId = ref.push().getKey();
     savedPosts.setId(savePostId);
     savedPosts.setPostId(postId);
     savedPosts.setPostItemsId(position);
     ref.push().setValue(savedPosts);
 }

     private void bendingPostTable(int position){
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Bending_Posts");
         BendingPosts bendingPosts = new BendingPosts();
         bendingPostId = ref.push().getKey();
         bendingPosts.setId(bendingPostId);
         bendingPosts.setPostId(postId);
         bendingPosts.setPostItemsId(position);
         bendingPosts.setSeen(false);
         ref.push().setValue(bendingPosts);

     }

     private void NotificationTable(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Notification");
        Notification notification = new Notification();
        bendingNotificationId = ref.push().getKey();
        notification.setNotificationPostId(bendingNotificationId);
        notification.setPostId(bendingPostId);
        notification.setPostType("Admin");
        ref.push().setValue(notification);

    }


}
