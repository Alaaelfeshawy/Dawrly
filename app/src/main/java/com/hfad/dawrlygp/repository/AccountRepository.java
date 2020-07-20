package com.hfad.dawrlygp.repository;

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
import com.hfad.dawrlygp.model.ApprovedPosts;
import com.hfad.dawrlygp.model.FoundPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.UserFragment.AccountFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    public static AccountRepository instance ;

    private List<Posts> postsList = new ArrayList<>() ;

    private FirebaseAuth auth ;

    private DatabaseReference reference ;

    private Data data ;

    private static AccountFragment mContext;

    private static DataCalled dataCalled ;

    private Success success ;

    public void setData(Data data) {
        this.data = data;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static AccountRepository getInstance(AccountFragment context) {
        mContext = context ;

        if (instance == null){
            instance = new AccountRepository();
        }
        dataCalled = (DataCalled) mContext;
        return instance ;
    }

    public MutableLiveData getData(){
        setItems();
        MutableLiveData<List<Posts>> liveData = new MediatorLiveData<>();
        liveData.setValue(postsList);
        return liveData;

    }

    private void setItems(){
        postsList.clear();
        auth = FirebaseAuth.getInstance();
        final String id = auth.getUid();
        if (auth.getCurrentUser()!= null) {

            Log.v("AccountRepo","1");

            getUser(id);

            reference = FirebaseDatabase.getInstance().getReference("Approved_posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("AccountRepo","2");
                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);
                            int postItemsId = approvedPosts.getPostItemsId();
                            String postId = approvedPosts.getPostId();
                            getData(postItemsId,postId,id);
                        }
                    }
                    else {
                        success.success(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reference = FirebaseDatabase.getInstance().getReference("Found_Posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("AccountRepo","2");
                            FoundPosts posts = snapshot.getValue(FoundPosts.class);
                            int postItemsId = posts.getPostItemsId();
                            String postId = posts.getPostId();
                            getData(postItemsId,postId,id);
                        }
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
                                                success.success(true);
                                                data.onSendData(lname , fname , image);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else {
                                success.success(false);
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
                                                    success.success(true);
                                                    data.onSendData(lname , fname , image);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else {
                                    success.success(false);

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

    private void getUser(String id){
        Query query = FirebaseDatabase.getInstance()
                .getReference("User").orderByChild("user_id")
                .equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        String lname = user.getLname();
                        String fname = user.getFname();
                        String image = user.getImageId();
                        Log.v("AccountRepo","5");
                        data.onSendData(lname , fname , image);
                    }
                }
                else {
                    Log.v("AccountRepo","6");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public interface Success{
        public void success(boolean isSuccess);
    }

    public interface Data{
        public void onSendData(String lname , String fname , String image);
    }

}
