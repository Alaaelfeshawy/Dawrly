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
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.TabFragments.LostFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class PostsLostRepository {

    public static PostsLostRepository instance ;

    private List<Posts> items = new ArrayList<>();

    private FirebaseAuth auth ;

    private DatabaseReference reference ;

    private static LostFragment mContext ;

    private static DataCalled dataCalled ;

    private Success success ;

    private String currentUserId ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static PostsLostRepository getInstance(LostFragment context){
        mContext = context ;
        if (instance == null){
            instance = new PostsLostRepository();
        }
        dataCalled = (DataCalled) mContext;
        return instance ;
    }

    private static final String TAG = "PostsLostRepository";
    public MutableLiveData<List<Posts>> getData(){
        setListItems();
        MutableLiveData<List<Posts>> liveData = new MediatorLiveData<>();
        liveData.setValue(items);
        return liveData;

    }

    private void setListItems(){
        items.clear();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){

            Log.v("FoundRepo","1");

            currentUserId = auth.getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference("Approved_posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);
                            String postId = approvedPosts.getPostId();
                            int postItems = approvedPosts.getPostItemsId();
                            Log.v("FoundRepo","2");
                            getUser(postItems , postId);
                        }
                    }
                    else {

                        success.onSuccess(false);

                        Log.v("FoundRepo","No Approved Posts");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getUser(int postItems , final String postId){

        if (postItems >= 1 && postItems <=8){
            Log.v("FoundRepo","3");
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("typeId")
                    .equalTo(2);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            final String userId = itemPosts.getUserId();
                            String postIdMobile = itemPosts.getPostId();
                            if (postId.equals(postIdMobile)){
                                Query query1 = FirebaseDatabase.getInstance().getReference("User")
                                        .orderByChild("user_id").equalTo(userId);
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                User user = snapshot2.getValue(User.class);
                                                String image = user.getImageId();
                                                String fname = user.getFname();
                                                String lname = user.getLname();
                                                Posts posts = new Posts();
                                                posts.setFname(fname);
                                                posts.setLname(lname);
                                                posts.setUserImageUrl(image);
                                                posts.setTextBrandNameName(itemPosts.getTitle());
                                                posts.setImageUrl(itemPosts.getImageUrl());
                                                posts.setLocation(itemPosts.getLocation());
                                                posts.setDecription(itemPosts.getDescription());
                                                posts.setTime(itemPosts.getTime());
                                                posts.setPostDate(itemPosts.getPostDate());
                                                posts.setTextAgeModelName(itemPosts.getModelName());
                                                posts.setTextColorGender(itemPosts.getColor());
                                                posts.setTypeId(itemPosts.getTypeId());
                                                posts.setPostId(itemPosts.getPostId());
                                                posts.setPostItemsId(itemPosts.getPostItemsId());
                                                posts.setImageUrl(itemPosts.getImageUrl());
                                                posts.setCity(itemPosts.getCity());
                                                posts.setUserId(userId);
                                                posts.setCommunicationByNumber(itemPosts.getCommunicationByMob());
                                                posts.setUserNumber(user.getPhoneNumber());



                                                items.add(posts);
                                                Log.v("FoundRepo","4");
                                                success.onSuccess(true);

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

        if (postItems ==9){
            Log.v("FoundRepo","5");
            Query query = FirebaseDatabase.getInstance().getReference("Human_Posts")
                    .orderByChild("typeId").equalTo(2);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            try {
                                final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                                final String userId = humanPosts.getUserId();
                                String id = humanPosts.getPostId();
                                if (id.equals(postId)) {

                                    Query query1 = FirebaseDatabase.getInstance().getReference("User")
                                            .orderByChild("user_id").equalTo(userId);
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                    User user = snapshot2.getValue(User.class);
                                                    String image = user.getImageId();
                                                    String fname = user.getFname();
                                                    String lname = user.getLname();
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
                                                    posts.setUserId(userId);
                                                    posts.setCommunicationByNumber(humanPosts.getCommunicationByMob());
                                                    posts.setUserNumber(user.getPhoneNumber());

                                                    items.add(posts);
                                                    Log.v("FoundRepo","6");
                                                    success.onSuccess(true);



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
                            catch (Exception e){
                                Log.v("Exception",""+e.getMessage());
                            }


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

    public interface  Success{
        public  void onSuccess(boolean isSuccess);
    }

}
