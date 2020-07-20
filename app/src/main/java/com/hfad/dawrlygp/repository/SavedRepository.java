package com.hfad.dawrlygp.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.SavedPosts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class SavedRepository {

    public static SavedRepository instance ;

    private List<Posts> postsList = new ArrayList<>() ;

    private FirebaseAuth auth ;

    private DatabaseReference reference ;

    private Success success;

    private static Context mContext;

    private static DataCalled dataCalled ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static SavedRepository getInstance(Context context){
        mContext = context ;

        if (instance == null){
            instance = new SavedRepository();
        }
        dataCalled = (DataCalled) mContext;

        return instance ;
    }

    public MutableLiveData getItems(){
        setItems();
        MutableLiveData<List<Posts>> liveData = new MediatorLiveData<>();
        liveData.setValue(postsList);
        return liveData ;
    }

    private void setItems(){
       postsList.clear();
       auth = FirebaseAuth.getInstance();
       final String id = auth.getUid();
        if (auth.getCurrentUser()!= null) {
            reference = FirebaseDatabase.getInstance().getReference("Saved_Posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            SavedPosts savedPosts = snapshot.getValue(SavedPosts.class);
                            int postItemsId = savedPosts.getPostItemsId();
                            String postId = savedPosts.getPostId();
                            getData(postItemsId,postId,id);
                        }
                    }else {
                        success.onSuccess(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void getData(final int postItemsId , final String postId , final String userId){
        if (postItemsId >= 1 && postItemsId <=8){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            int postItemId = itemPosts.getPostItemsId();
                            String post_id = itemPosts.getPostId();
                            String user_id = itemPosts.getUserId();
                            if (postItemsId == postItemId && postId.equals(post_id) &&
                                    userId.equals(user_id)){
                                Query query = FirebaseDatabase.getInstance()
                                        .getReference("User")
                                        .orderByChild("user_id")
                                        .equalTo(user_id);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                User user = snapshot.getValue(User.class);
                                                String lname = user.getLname();
                                                String fname = user.getFname();
                                                String image = user.getImageId();
                                                Posts posts = new Posts();
                                                posts.setFname(fname);
                                                posts.setLname(lname);
                                                posts.setUserImageUrl(image);
                                                posts.setTextBrandNameName(itemPosts.getTitle());
                                                posts.setTextColorGender(itemPosts.getColor());
                                                posts.setLocation(itemPosts.getLocation());
                                                posts.setDecription(itemPosts.getDescription());
                                                posts.setPostDate(itemPosts.getPostDate());
                                                posts.setTypeId(itemPosts.getTypeId());
                                                posts.setTextAgeModelName(String.valueOf(itemPosts.getModelName()));
                                                posts.setPostId(itemPosts.getPostId());
                                                posts.setPostItemsId(itemPosts.getPostItemsId());
                                                posts.setTime(itemPosts.getTime());
                                                posts.setImageUrl(itemPosts.getImageUrl());
                                                posts.setCity(itemPosts.getCity());
                                                postsList.add(posts);
                                                Log.v("Saved","7");
                                                success.onSuccess(true);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else {
                                success.onSuccess(false);
                            }
                            dataCalled.onDataCalled();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if (postItemsId == 9){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                           try {
                               final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                               int postItemId =humanPosts.getPostItemsId();
                               String post_id = humanPosts.getPostId();
                               String user_id = humanPosts.getUserId();
                               if (postItemsId == postItemId && postId.equals(post_id) &&
                                       userId.equals(user_id)){
                                   Query query = FirebaseDatabase.getInstance()
                                           .getReference("User")
                                           .orderByChild("user_id")
                                           .equalTo(user_id);
                                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           if (dataSnapshot.exists()){
                                               for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                   User user = snapshot.getValue(User.class);
                                                   String lname = user.getLname();
                                                   String fname = user.getFname();
                                                   String image = user.getImageId();
                                                   Posts posts = new Posts();
                                                   posts.setFname(fname);
                                                   posts.setLname(lname);
                                                   posts.setUserImageUrl(image);
                                                   posts.setTextBrandNameName(humanPosts.getName());
                                                   posts.setTextColorGender(humanPosts.getGender());
                                                   posts.setLocation(humanPosts.getLocation());
                                                   posts.setDecription(humanPosts.getDescription());
                                                   posts.setPostDate(humanPosts.getPostDate());
                                                   posts.setTypeId(humanPosts.getTypeId());
                                                   posts.setTextAgeModelName(String.valueOf(humanPosts.getAge()));
                                                   posts.setPostId(humanPosts.getPostId());
                                                   posts.setPostItemsId(humanPosts.getPostItemsId());
                                                   posts.setTime(humanPosts.getTime());
                                                   posts.setImageUrl(humanPosts.getImageUrl());
                                                   posts.setCity(humanPosts.getCity());
                                                   postsList.add(posts);
                                                   Log.v("Saved","7");
                                                   success.onSuccess(true);
                                               }
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });

                               }
                               else {
                                   success.onSuccess(false);

                                   Log.v("Saved","No Posts");

                               }
                           }
                           catch (Exception e){
                               Log.v("ExceptionSaved",""+e.getMessage());

                           }
                            dataCalled.onDataCalled();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }


    public interface  Success{
        public void onSuccess(boolean isSuccess);
    }


}
