package com.hfad.dawrlygp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.AdminFragment.RequestsFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class RequestsRepository {

    public static RequestsRepository instance ;

    private List<Posts> items = new ArrayList<>() ;

    private DatabaseReference reference ;

    private FirebaseAuth auth;

    private static RequestsFragment mContext;

    private static DataCalled dataCalled ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static RequestsRepository getInstance(RequestsFragment context){
        mContext = context ;
        if (instance == null){
            instance = new RequestsRepository();
        }
        dataCalled = (DataCalled) mContext;
        return instance ;
    }

    public MutableLiveData<List<Posts>> getData(){
        setItems();
        MutableLiveData<List<Posts>> liveData = new MutableLiveData<>();
        liveData.setValue(items);
        return liveData ;

    }

    private void setItems(){
        items.clear();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!= null) {
            Log.v("RequestRepository","1");
            reference = FirebaseDatabase.getInstance().getReference("Bending_Posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("RequestRepository","2");
                            final BendingPosts bendingPosts = snapshot.getValue(BendingPosts.class);
                            final int postItemsId = bendingPosts.getPostItemsId();
                            final String postId = bendingPosts.getPostId();
                            final boolean seen = bendingPosts.isSeen();
                            Query query = FirebaseDatabase.getInstance()
                                    .getReference("Notification")
                                    .orderByChild("postType")
                                    .equalTo("Admin");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists())
                                    {
                                        if (!seen){
                                            Log.v("RequestRepository","3");
                                            Log.v("RequestRepository","4");
                                            Log.v("RequestRepository","NOTIFICATION");
                                            getData(postItemsId,postId,false);
                                        //    snapshot.child("seen").getRef().setValue(true);
                                        }
                                        else {
                                            Log.v("RequestRepository","6");
                                            getData(postItemsId,postId,true);
                                            Log.v("RequestRepository","No NOTIFICATION");
                                        }


                                    }

                                    else {
                                        Log.v("RequestRepository","6");
                                        getData(postItemsId,postId,true);
                                        Log.v("RequestRepository","No NOTIFICATION");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                    else {
                        success.onSuccess(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void getData(final int postItemsId , final String postId , final boolean statue){

            Query query1 = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("postItemsId")
                    .equalTo(postItemsId);
            query1.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            int postItemId = itemPosts.getPostItemsId();
                            String post_id = itemPosts.getPostId();
                            String user_id = itemPosts.getUserId();
                            if (postItemsId == postItemId && postId.equals(post_id)){
                                Log.v("RequestRepository","7");
                                Query query = FirebaseDatabase.getInstance()
                                        .getReference("User").orderByChild("user_id")
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
                                                Posts requests = new Posts();
                                                requests.setFname(fname);
                                                requests.setLname(lname);
                                                requests.setUserImageUrl(image);
                                                requests.setTextBrandNameName(itemPosts.getTitle());
                                                requests.setImageUrl(itemPosts.getImageUrl());
                                                requests.setLocation(itemPosts.getLocation());
                                                requests.setDecription(itemPosts.getDescription());
                                                requests.setTime(itemPosts.getTime());
                                                requests.setPostDate(itemPosts.getPostDate());
                                                requests.setTextAgeModelName(itemPosts.getModelName());
                                                requests.setTextColorGender(itemPosts.getColor());
                                                requests.setTypeId(itemPosts.getTypeId());
                                                requests.setPostId(itemPosts.getPostId());
                                                requests.setPostItemsId(itemPosts.getPostItemsId());
                                                requests.setStatus(statue);
                                                Log.v("RequestRepository","8");
                                                items.add(requests);
                                                success.onSuccess(true);
                                            }
                                        }

                                        else {
                                            Log.v("RequestRepository","No User Table");

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        dataCalled.onDataCalled();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        if (postItemsId == 9) {
           Query query = FirebaseDatabase.getInstance()
                   .getReference("Human_Posts")
                   .orderByChild("postItemsId")
                   .equalTo(postItemsId);
           query.addListenerForSingleValueEvent(new ValueEventListener()
           {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                           final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                           int postItemId =humanPosts.getPostItemsId();
                           String post_id = humanPosts.getPostId();
                           String user_id = humanPosts.getUserId();
                           Log.d("RequestRepository", "onDataChange: "+post_id+" "+postId);
                           if (postItemsId == postItemId && postId.equals(post_id)){
                               Log.v("RequestRepository","9");
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
                                               Posts requests = new Posts();
                                               requests.setFname(fname);
                                               requests.setLname(lname);
                                               requests.setUserImageUrl(image);
                                               requests.setTextBrandNameName(humanPosts.getName());
                                               requests.setImageUrl(humanPosts.getImageUrl());
                                               requests.setLocation(humanPosts.getLocation());
                                               requests.setDecription(humanPosts.getDescription());
                                               requests.setTime(humanPosts.getTime());
                                               requests.setPostDate(humanPosts.getPostDate());
                                               requests.setTextAgeModelName(String.valueOf(humanPosts.getAge()));
                                               requests.setTextColorGender(humanPosts.getGender());
                                               requests.setTypeId(humanPosts.getTypeId());
                                               requests.setPostId(humanPosts.getPostId());
                                               requests.setPostItemsId(humanPosts.getPostItemsId());
                                               requests.setStatus(statue);
                                               Log.v("RequestRepository","10");
                                               items.add(requests);
                                               success.onSuccess(true);
                                           }
                                       }

                                       else {
                                           Log.v("RequestRepository","No User Table");

                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                           }
                       }
                       dataCalled.onDataCalled();
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
