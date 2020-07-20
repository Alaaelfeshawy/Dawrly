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
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Options;
import com.hfad.dawrlygp.model.PostItems;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.UserFragment.SearchFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository {

    public static SearchRepository instance ;

    private List<Posts> items = new ArrayList<>();

    private FirebaseAuth auth ;

    private DatabaseReference reference ;

    private static SearchFragment mContext ;

    private static DataCalled dataCalled ;

    private Success success;

    private List<Options> optionsList = new ArrayList<>();

    private List<Options> date = new ArrayList<>();

    private List<Location> locationList = new ArrayList<>();

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static SearchRepository getInstance(SearchFragment context){
        mContext = context ;
        if (instance == null){
            instance = new SearchRepository();
        }
        dataCalled = (DataCalled) mContext;

        return instance ;
    }

    public MutableLiveData getData(){

        setData();

        MutableLiveData<List<Posts>> liveData = new MediatorLiveData<>();

        liveData.setValue(items);

        return liveData ;

    }

    public MutableLiveData getOptions(){

        setOptions();

        MutableLiveData<List<Options>> liveData = new MediatorLiveData<>();

        liveData.setValue(optionsList);

        return liveData ;

    }

    public MutableLiveData getLocation(){

        setLocation();

        MutableLiveData<List<Location>> liveData = new MediatorLiveData<>();

        liveData.setValue(locationList);

        return liveData ;

    }

    public MutableLiveData getDate(){

        setDate();

        MutableLiveData<List<Options>> liveData = new MediatorLiveData<>();

        liveData.setValue(date);

        return liveData ;

    }

    private void setDate(){
        items.clear();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() !=null){

            reference = FirebaseDatabase.getInstance()
                    .getReference("Approved_posts");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);

                            String postId = approvedPosts.getPostId();

                            int postItems = approvedPosts.getPostItemsId();

                            getPost(postItems , postId);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setOptions(){

        optionsList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostItems");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren() ){

                        PostItems postItems = snapshot.getValue(PostItems.class);

                        optionsList.add(new Options(postItems.getTitle()));

                       // successList.isSuccess(true);

                        dataCalled.onDataCalled();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setLocation(){

        locationList.clear();

        Query reference = FirebaseDatabase.getInstance()
                .getReference("Location")
                .orderByChild("sendBy")
                .equalTo("Post");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren() ){

                        Location location = snapshot.getValue(Location.class);

                        assert location != null;

                        locationList.add(new Location(location.getLat(),location.getLon()));

                        Log.v("SearchRepo","Location Size "+locationList.size());

                      //  successList.isSuccess(true);

                        dataCalled.onDataCalled();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setData(){

        items.clear();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() !=null){

            Log.v("SearchRepo","1");

            reference = FirebaseDatabase.getInstance()
                    .getReference("Approved_posts");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);

                            String postId = approvedPosts.getPostId();

                            int postItems = approvedPosts.getPostItemsId();

                            Log.v("SearchRepo","2");

                            getUser(postItems , postId);
                        }
                    }
                    {
                        success.onSuccess(false);

                    }
                    Log.v("PostsAllRepo","No Approved Posts");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getPost(int postItems , String postId){

        if (postItems >=1 && postItems <=8){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                            final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);

                            date.add(new Options(itemPosts.getTime()));

                        }
                        dataCalled.onDataCalled();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (postItems ==9){
            Query query1 = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                            final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);

                            date.add(new Options(humanPosts.getTime()));

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

    private void getUser(int postItems , String postId){

        if (postItems >=1 && postItems <=8){
            Log.v("SearchRepo","3");
            getMobileData(postId);
        }

        if (postItems ==9){
            getHumanData(postId);
        }


    }

    private void getMobileData(String postId){
        Query query = FirebaseDatabase.getInstance()
                .getReference("Item_Posts")
                .orderByChild("postId")
                .equalTo(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                        final String userId = itemPosts.getUserId();
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
                                        posts.setUserId(user.getUser_id());
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
                                        items.add(posts);
                                        Log.v("SearchRepo","4");
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getHumanData(String postId){
        Log.v("SearchRepo","5");
        Query query = FirebaseDatabase.getInstance().getReference("Human_Posts")
                .orderByChild("postId").equalTo(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                        String userId = humanPosts.getUserId();
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
                                        posts.setUserId(user.getUser_id());
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
                                        items.add(posts);
                                        Log.v("SearchRepo","6");
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface  Success{
        public  void onSuccess(boolean isSuccess);
    }
}
