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
import com.hfad.dawrlygp.model.ApprovedPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.TabFragments.AllFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;
/** Singleton Pattern**/
public class PostsAllRepository {

    private static PostsAllRepository instance ;

    private List<Posts> items = new ArrayList<>();

    private FirebaseAuth auth ;

    private DatabaseReference reference ;

    private static AllFragment mContext ;

    private static DataCalled dataCalled ;

    private String currentUserId ;

    private Success success;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static PostsAllRepository getInstance(AllFragment context){
        mContext = context ;
        if (instance == null){
            instance = new PostsAllRepository();
        }
        dataCalled = (DataCalled) mContext;
        return instance ;
    }

    public MutableLiveData<List<Posts>> getItemsList(){
        setListItems();
        MutableLiveData<List<Posts>> data =  new MutableLiveData<>();
        data.setValue(items);
        return data ;
    }


    private void setListItems(){
        items.clear();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){

            Log.v("PostsAllRepo","1");

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
                            Log.v("PostsAllRepo","2");
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

    private   void getUser(int postItems , final String postId){

        if (postItems >= 1 && postItems <=8){
            Log.v("PostsAllRepo","3");
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
                            Query query1 = FirebaseDatabase.getInstance()
                                    .getReference("User")
                                    .orderByChild("user_id")
                                    .equalTo(userId);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                            User user = snapshot2.getValue(User.class);
                                            final String image = user.getImageId();
                                            final String fname = user.getFname();
                                            final String lname = user.getLname();
                                            final String userNumber = user.getPhoneNumber();
                                            final String userId = user.getUser_id();
                                            Query query2 = FirebaseDatabase.getInstance().getReference("Location")
                                                    .orderByChild("id").equalTo(userId);
                                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            Location location = snapshot.getValue(Location.class);
                                                            double userLat = location.getLat();
                                                            double userLon = location.getLon();
                                                                Posts posts = new Posts();
                                                                posts.setFname(fname);
                                                                posts.setLname(lname);
                                                                posts.setUserImageUrl(image);
                                                                posts.setUserId(userId);
                                                                posts.setTextBrandNameName(itemPosts.getTitle());
                                                                posts.setTextColorGender(itemPosts.getColor());
                                                                posts.setLocation(itemPosts.getLocation());
                                                                posts.setDecription(itemPosts.getDescription());
                                                                posts.setPostDate(itemPosts.getPostDate());
                                                                posts.setTypeId(itemPosts.getTypeId());
                                                                posts.setTextAgeModelName(itemPosts.getModelName());
                                                                posts.setPostId(itemPosts.getPostId());
                                                                posts.setPostItemsId(itemPosts.getPostItemsId());
                                                                posts.setTime(itemPosts.getTime());
                                                                posts.setImageUrl(itemPosts.getImageUrl());
                                                                posts.setCity(itemPosts.getCity());
                                                                posts.setUserNumber(userNumber);
                                                                posts.setCommunicationByNumber(itemPosts.getCommunicationByMob());
                                                                items.add(posts);
                                                                Log.v("PostsAllRepo","6");
                                                                success.onSuccess(true);


                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

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

        if (postItems ==9){
            Log.v("PostsAllRepo","5");
            Query query = FirebaseDatabase.getInstance().getReference("Human_Posts")
                    .orderByChild("postId").equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                            final String userId = humanPosts.getUserId();
                            Query query1 = FirebaseDatabase.getInstance().getReference("User")
                                    .orderByChild("user_id").equalTo(userId);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                            final User user = snapshot2.getValue(User.class);
                                            final String image = user.getImageId();
                                            final String fname = user.getFname();
                                            final String lname = user.getLname();
                                            final String userId = user.getUser_id();
                                            Query query2 = FirebaseDatabase.getInstance().getReference("Location")
                                                    .orderByChild("id").equalTo(userId);
                                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            Location location = snapshot.getValue(Location.class);
                                                            double userLat = location.getLat();
                                                            double userLon = location.getLon();
                                                                Posts posts = new Posts();
                                                                posts.setFname(fname);
                                                                posts.setLname(lname);
                                                                posts.setUserImageUrl(image);
                                                                posts.setUserId(userId);
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
                                                                posts.setUserNumber(user.getPhoneNumber());
                                                                posts.setCommunicationByNumber(humanPosts.getCommunicationByMob());
                                                                items.add(posts);
                                                                Log.v("PostsAllRepo","6");
                                                                success.onSuccess(true);

                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

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


    }
    private float distance(double lat1, double lon1, double lat2, double lon2)
    {

        /* double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;*/
        float[] distance = new float[1];
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        Log.v("Distance",""+(distance[0]*0.001));
         /*  android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        float distanceInMeters = loc1.distanceTo(loc2);

        Log.v("Distance",""+distanceInMeters);*/

      float finalDistance = (float) (distance[0]*0.001);

        return finalDistance ;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



public interface  Success{
        public  void onSuccess(boolean isSuccess);
}
}
