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
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.SimilarityTable;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.AdminNotificationTab;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.SimilarityNotificationTab;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.util.ArrayList;
import java.util.List;

public class NotificationSimilarityRepository {

    private static final String TAG = "NotificationSimilarityR";

    public static NotificationSimilarityRepository instance ;

    private List<Posts> items = new ArrayList<>() ;

    private FirebaseAuth auth;

    private DatabaseReference reference ;

    private static SimilarityNotificationTab mContext;

    private static DataCalled dataCalled ;

    private String currentUser ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static NotificationSimilarityRepository getInstance(SimilarityNotificationTab context){
        mContext = context ;
        if (instance == null){
            instance = new NotificationSimilarityRepository();
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

            currentUser = auth.getCurrentUser().getUid();

            reference = FirebaseDatabase.getInstance().getReference("SimilarityResults");

            reference.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                            final SimilarityTable similarityTable = snapshot.getValue(SimilarityTable.class);

                            final String postId = similarityTable.getPostId2();

                            int postItemsId = similarityTable.getPostItemsId();

                            double similarity = similarityTable.getSimilarity();

                           // final boolean seen = similarityTable.isSeen();

                            if (postItemsId == 9){

                                Query query = FirebaseDatabase.getInstance()
                                        .getReference("Human_Posts")
                                        .orderByChild("postId")
                                        .equalTo(postId);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){

                                            getData(postItemsId,postId/*,false */, similarity);

                                          /*  if (!seen){

                                                getData(postItemsId,postId,false , similarity);
                                            }
                                            else {
                                                getData(postItemsId,postId,true, similarity);

                                            }*/

                                        }
                                       /* else {
                                            getData(postItemsId,postId,true, similarity);

                                            success.onSuccess(false);

                                        }*/

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else {
                                Query query = FirebaseDatabase.getInstance()
                                        .getReference("Item_Posts")
                                        .orderByChild("postId")
                                        .equalTo(postId);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){

                                            getData(postItemsId,postId/*,false */, similarity);

                                           /* if (!seen){
                                                getData(postItemsId,postId,false , similarity);
                                            }
                                            else {
                                                getData(postItemsId,postId,true , similarity);

                                            }*/

                                        }
                                        /*else {

                                            getData(postItemsId,postId,true , similarity);
                                            success.onSuccess(false);

                                        }*/

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
    }

    public void getData(final int postItemsId , final String postId /*, final boolean statue*/ , double similarity){

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
                            String user_id = itemPosts.getUserId();
                            String contactViaMobile = itemPosts.getCommunicationByMob();
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
                                            String userNumber = user.getPhoneNumber();
                                            Posts requests = new Posts();
                                            requests.setFname(fname);
                                            requests.setLname(lname);
                                            requests.setUserImageUrl(image);
                                            requests.setUserNumber(userNumber);
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
                                          //  requests.setStatus(statue);
                                            requests.setCity(itemPosts.getCity());
                                            requests.setPostTypeId("similarity");
                                            requests.setUserId(user.getUser_id());
                                            requests.setCommunicationByNumber(contactViaMobile);
                                            requests.setSimilarity(similarity);

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
                        dataCalled.onDataCalled();
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

        if (postItemsId == 9) {
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                            String user_id = humanPosts.getUserId();
                            String connectViaMobile = humanPosts.getCommunicationByMob();
                                if (!user_id.equals(currentUser)){
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
                                                    String userNumber = user.getPhoneNumber();
                                                    Posts requests = new Posts();
                                                    requests.setUserId(user.getUser_id());
                                                    requests.setFname(fname);
                                                    requests.setLname(lname);
                                                    requests.setUserImageUrl(image);
                                                    requests.setTextBrandNameName(humanPosts.getName());
                                                    requests.setImageUrl(humanPosts.getImageUrl());
                                                    requests.setLocation(humanPosts.getLocation());
                                                    requests.setDecription(humanPosts.getDescription());
                                                    requests.setTime(humanPosts.getTime());
                                                    requests.setPostDate(humanPosts.getPostDate());
                                                    requests.setTextColorGender(humanPosts.getGender());
                                                    requests.setTextAgeModelName(String.valueOf(humanPosts.getAge()));
                                                    requests.setTypeId(humanPosts.getTypeId());
                                                    requests.setPostId(humanPosts.getPostId());
                                                    requests.setPostItemsId(humanPosts.getPostItemsId());
                                                  //  requests.setStatus(statue);
                                                    requests.setPostTypeId("similarity");
                                                    requests.setCity(humanPosts.getCity());
                                                    requests.setCommunicationByNumber(connectViaMobile);
                                                    requests.setUserNumber(userNumber);
                                                    items.add(requests);
                                                    requests.setSimilarity(similarity);
                                                    success.onSuccess(true);
                                                }
                                            }

                                            else {
                                                Log.v(TAG,"No User Table");

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
                    else {
                        success.onSuccess(false);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else {

        }

    }

    public interface  Success{
        public void onSuccess(boolean isSuccess);
    }

}
