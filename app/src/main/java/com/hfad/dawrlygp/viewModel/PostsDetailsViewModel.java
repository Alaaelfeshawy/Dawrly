package com.hfad.dawrlygp.viewModel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.dawrlygp.model.ApprovedPosts;
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.PostItems;
import com.hfad.dawrlygp.model.ReportPosts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostsDetailsViewModel extends ViewModel {

   private FirebaseAuth auth ;

   private Data data ;

    private static final String TAG = "PostsDetailsViewModel";

   private Query query1 ,query2 ,query3;

   private Success success;

   private DatabaseReference ref ;

   private ConfirmSuccess confirmSuccess;

   private String approvedPostsId ;

   private SuccessReport successReport ;

   private ReportUser reportUser;

   private String postReportId ;

    public void setReportUser(ReportUser reportUser) {
        this.reportUser = reportUser;
    }

    public void setSuccessReport(SuccessReport successReport) {
        this.successReport = successReport;
    }

    public void setConfirmSuccess(ConfirmSuccess confirmSuccess) {
        this.confirmSuccess = confirmSuccess;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void delete(final String postId , final int postItemsId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Log.v("Validation","Done");
            query1 = FirebaseDatabase.getInstance()
                    .getReference("Bending_Posts")
                    .orderByChild("postItemsId")
                    .equalTo(postItemsId);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            BendingPosts posts = snapshot.getValue(BendingPosts.class);
                            String postIdDB = posts.getPostId();
                            int postItemsDB = posts.getPostItemsId();
                            if (postIdDB.equals(postId) && postItemsDB == postItemsId){
                                snapshot.getRef().removeValue();
                                Log.v(TAG,"Bending Deleted");
                                if (postItemsId>=1 && postItemsId<=8){
                                    query2 = FirebaseDatabase.getInstance()
                                            .getReference("Item_Posts")
                                            .orderByChild("postId")
                                            .equalTo(postId);
                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren())
                                                {
                                                    ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                                                    String imagePost = itemPosts.getImageUrl();
                                                    if (imagePost !=null)
                                                      {
                                                          Log.d(TAG, "onDataChange: "+imagePost);

                                                          StorageReference photoRef = FirebaseStorage.getInstance()
                                                                  .getReferenceFromUrl(imagePost);

                                                          photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                                                    }
                                                    snapshot1.getRef().removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (postItemsId==9){
                                    query2 = FirebaseDatabase.getInstance()
                                            .getReference("Human_Posts")
                                            .orderByChild("postId")
                                            .equalTo(postId);
                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren())
                                                {
                                                    HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                                                    String imagePost = humanPosts.getImageUrl();
                                                    if (imagePost !=null)
                                                        {
                                                            Log.d(TAG, "onDataChange: "+imagePost);

                                                            StorageReference photoRef = FirebaseStorage.getInstance()
                                                                    .getReferenceFromUrl(imagePost);

                                                            photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                                                        }
                                                    snapshot1.getRef().removeValue();

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                 query3 = FirebaseDatabase.getInstance()
                                        .getReference("Location")
                                        .orderByChild("id")
                                         .equalTo(postId);
                                 query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                                snapshot1.getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                 success.onSuccess(true);
                            }
                        }
                    }else {
                        Log.v("Validation","Failed");
                        success.onSuccess(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void confirm(final String postId , final int postItemsId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Bending_Posts")
                    .orderByChild("postItemsId")
                    .equalTo(postItemsId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            BendingPosts posts = snapshot.getValue(BendingPosts.class);
                            String postIdDB = posts.getPostId();
                            if (postIdDB.equals(postId)){
                                snapshot.getRef().removeValue();
                                ref = FirebaseDatabase.getInstance()
                                        .getReference("Approved_posts");
                                ApprovedPosts approvedPosts = new ApprovedPosts();
                                approvedPostsId = ref.push().getKey();
                                approvedPosts.setId( approvedPostsId);
                                approvedPosts.setPostId(postId);
                                approvedPosts.setPostItemsId(postItemsId);
                                ref.push().setValue(approvedPosts);
                                ref = FirebaseDatabase.getInstance().getReference("Notification");
                                Notification notification = new Notification();
                                String confirmationId = ref.push().getKey();
                                notification.setPostId(approvedPostsId);
                                notification.setNotificationPostId(confirmationId);
                                notification.setPostType("User");
                                ref.push().setValue(notification);
                                confirmSuccess.confirmSuccess(true);

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void seen(final String postId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Log.v("PostDetails",""+1);
            Query query = FirebaseDatabase.getInstance().getReference("Bending_Posts")
                    .orderByChild("postId").equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("PostDetails",""+2);
                            BendingPosts posts = snapshot.getValue(BendingPosts.class);
                            final String bendingPostId = posts.getId();
                            Query query = FirebaseDatabase.getInstance().getReference("Notification")
                                        .orderByChild("postType").equalTo("Admin");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                                Log.v("PostDetails",""+3);
                                                Notification notification = snapshot1.getValue(Notification.class);
                                                String postId = notification.getPostId();
                                                if (postId.equals(bendingPostId)){
                                                    snapshot.child("seen").getRef().setValue(true);
                                                    snapshot1.getRef().removeValue();
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    public void seenHome(final String postId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Log.v("PostDetails",""+1);
            Query query = FirebaseDatabase.getInstance().getReference("Approved_posts")
                    .orderByChild("postId").equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("PostDetails",""+2);
                            ApprovedPosts posts = snapshot.getValue(ApprovedPosts.class);
                            final String bendingPostId = posts.getId();
                            Query query = FirebaseDatabase.getInstance().getReference("Notification")
                                    .orderByChild("postType").equalTo("User");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                            Log.v("PostDetails",""+3);
                                            Notification notification = snapshot1.getValue(Notification.class);
                                            String postId = notification.getPostId();
                                            snapshot.child("seen").getRef().setValue(true);
                                            if (postId.equals(bendingPostId)){
                                                snapshot1.getRef().removeValue();
                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    public void seenSimilarity(final String postId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("SimilarityResults")
                    .child(auth.getCurrentUser().getUid())
                    .orderByChild("postId2")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                            snapshot.getRef().child("seen").setValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }


    public String userId(){
       auth = FirebaseAuth.getInstance();
       String currentUserId = auth.getCurrentUser().getUid();
       Log.v("USER_ID",""+currentUserId);
       return currentUserId ;
   }

    public void reportPost(final int postItemsId , final String postId){
        ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("ReportPosts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        ReportPosts posts = snapshot.getValue(ReportPosts.class);

                        if (posts.getUserId().equals(userId())
                                && posts.getPostId().equals(postId)
                                && posts.getPostItemsId()==postItemsId)
                        {
                            successReport.setSuccess(false);
                        }
                        else {

                            if (postItemsId >=1 && postItemsId<=8){

                                postItems(postItemsId,postId);
                            }
                            else {
                                postHuman(postItemsId,postId);
                            }


                        }
                    }
                }
                else {
                   savePost(postItemsId,postId);
                    successReport.setSuccess(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void savePost(int postItemsId , String postId){
        ReportPosts posts = new ReportPosts();
        posts.setPostItemsId(postItemsId);
        postReportId = ref.push().getKey();
        posts.setPostId(postId);
        posts.setId(postReportId);
        posts.setUserId(userId());
        ref.push().setValue(posts);
        NotificationTable();
    }

    private void postItems(final int postItemsId , final String postId){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("PostItems")
                .orderByChild(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ItemPosts itemPosts = snapshot.getValue(ItemPosts.class);
                        if (itemPosts.getUserId().equals(userId())){
                            reportUser.setUserReport(false);
                        }
                        else {
                            savePost(postItemsId,postId);
                            successReport.setSuccess(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postHuman(final int postItemsId , final String postId){

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Human_Posts")
                .orderByChild(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        HumanPosts humanPosts = snapshot.getValue(HumanPosts.class);
                        if (humanPosts.getUserId().equals(userId())){
                            reportUser.setUserReport(false);
                        }
                        else {
                            savePost(postItemsId,postId);
                            successReport.setSuccess(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void NotificationTable(){
        ref = FirebaseDatabase.getInstance().getReference().child("Notification");
        Notification notification = new Notification();
         String notificationId = ref.push().getKey();
        notification.setNotificationPostId(notificationId);
        notification.setPostId(postReportId);
        notification.setPostType("Admin");
        ref.push().setValue(notification);

    }

    public interface Data{
       public void getData(String fname , String lname);
    }

    public interface Success{
        public void onSuccess(boolean isSuccess);
   }

    public interface ConfirmSuccess{
        public void confirmSuccess(boolean isSuccess);
   }

   public interface SuccessReport{
        public void setSuccess(boolean isSuccess);
   }

   public interface ReportUser{
        public void setUserReport(boolean isSuccess);
   }
}

