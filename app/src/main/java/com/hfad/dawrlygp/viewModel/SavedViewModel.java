package com.hfad.dawrlygp.viewModel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
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
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.SavedRepository;

import java.util.List;

public class SavedViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private SavedRepository repository ;

    private static final String TAG = "SavedViewModel";

    private Success success ;

    private FirebaseAuth auth;

    private DatabaseReference reference ;

    private String bendingNotificationId , bendingPostId ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(Context context){

        repository = SavedRepository.getInstance(context);

        data = repository.getItems();

        repository.setSuccess(new SavedRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                    success.onSuccess(true);
                }else {
                    success.onSuccess(false);
                }

            }
        });

    }

    public void postNow(final String postId , final int postItemsId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Log.v("SavedViewModel","1");
            if (postItemsId >=1 && postItemsId<=8){
                Log.v("SavedViewModel","2");
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Saved_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Log.v("SavedViewModel","3");
                                snapshot.getRef().removeValue();
                                BendingTable(postId,postItemsId);
                                NotificationTable();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if (postItemsId == 9){
                Log.v("SavedViewModel","4");
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Saved_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Log.v("SavedViewModel","3");
                                snapshot.getRef().removeValue();
                                BendingTable(postId,postItemsId);
                                NotificationTable();
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

    private void BendingTable(String postId , int postItemsId){
        reference = FirebaseDatabase.getInstance().getReference().child("Bending_Posts");
        BendingPosts bendingPosts = new BendingPosts();
        bendingPostId = reference.push().getKey();
        bendingPosts.setId(bendingPostId);
        bendingPosts.setPostId(postId);
        bendingPosts.setPostItemsId(postItemsId);
        bendingPosts.setSeen(false);
        reference.push().setValue(bendingPosts);
        success.onSuccess(true);

    }

    private void NotificationTable(){
        reference = FirebaseDatabase.getInstance().getReference().child("Notification");
        Notification notification = new Notification();
        bendingNotificationId = reference.push().getKey();
        notification.setNotificationPostId(bendingNotificationId);
        notification.setPostId(bendingPostId);
        notification.setPostType("Admin");
        reference.push().setValue(notification);

    }

    public void delete(final String postId , final int postItemsId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Log.v("SavedViewModel","1");
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Saved_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("SavedViewModel","3");
                            snapshot.getRef().removeValue();
                            removeData(postId,postItemsId);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void removeData(String postId , int postItemsId){
        if (postItemsId >=1 && postItemsId <=8){
            Query query2 = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            String imagePost = itemPosts.getImageUrl();
                            snapshot1.getRef().removeValue();
                            if (imagePost !=null)
                            {
                                StorageReference photoRef = FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(imagePost);

                                photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (postItemsId ==9){
            Query query1 = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                            String imagePost = humanPosts.getImageUrl();
                            snapshot1.getRef().removeValue();
                            if (imagePost !=null) {
                                StorageReference photoRef = FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(imagePost);

                                photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Query query3 = FirebaseDatabase.getInstance()
                .getReference("Location")
                .orderByChild("id")
                .equalTo(postId);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        snapshot1.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface Success{
        public void onSuccess(boolean isSuccess);
    }


}
