package com.hfad.dawrlygp.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.BendingPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminViewModel extends ViewModel {

    private FirebaseAuth auth;

    private long notificationNumber =0;

    private NotificationNumber number ;

    private Name name ;

    private List<Posts> list = new ArrayList<>();

    public void setName(Name name) {
        this.name = name;
    }

    public void setNotificationNumber(NotificationNumber number) {
        this.number = number;
    }

    public void getNumOfNotifications(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Query reference = FirebaseDatabase.getInstance()
                    .getReference("Notification")
                    .orderByChild("postType")
                    .equalTo("Admin");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                            notificationNumber = dataSnapshot.getChildrenCount();
                            number.getNotificationNumber(notificationNumber);
                            Log.v("AdminViewModel","Notification True "+ notificationNumber);

                    }
                    else {
                        number.getNotificationNumber(notificationNumber);
                        Log.v("AdminViewModel","Notification False "+ notificationNumber);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void getSenderName(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            Log.v("AdminViewModel","1");
            Query reference = FirebaseDatabase.getInstance()
                    .getReference("Notification").orderByChild("postType").equalTo("Admin");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Notification notification = snapshot.getValue(Notification.class);
                            String postBendingId = notification.getPostId();
                            Log.v("AdminViewModel","2");
                            Query query = FirebaseDatabase.getInstance().getReference("Bending_Posts")
                                    .orderByChild("id").equalTo(postBendingId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                            Log.v("AdminViewModel","3");
                                            BendingPosts bendingPosts = snapshot1.getValue(BendingPosts.class);
                                            int postItemId = bendingPosts.getPostItemsId();
                                            String postId = bendingPosts.getPostId();
                                            if (postItemId == 1){
                                                Log.v("AdminViewModel","4");
                                                Query query = FirebaseDatabase.getInstance()
                                                        .getReference("Item_Posts")
                                                        .orderByChild("postId")
                                                        .equalTo(postId);
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                                final ItemPosts itemPosts = dataSnapshot1.getValue(ItemPosts.class);
                                                                String userId = itemPosts.getUserId();
                                                                Query query = FirebaseDatabase.getInstance().getReference("User")
                                                                        .orderByChild("user_id").equalTo(userId);
                                                                Log.v("Names","5");

                                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                                                User user = snapshot2.getValue(User.class);
                                                                                String fname = user.getFname();
                                                                                String lname = user.getLname();
                                                                                String image = user.getImageId();
                                                                                Posts requests = new Posts();
                                                                                requests.setFname(fname);
                                                                                requests.setLname(lname);
                                                                                requests.setUserImageUrl(image);
                                                                                requests.setTextBrandNameName(itemPosts.getTitle());
                                                                                requests.setTextAgeModelName(itemPosts.getModelName());
                                                                                requests.setImageUrl(itemPosts.getImageUrl());
                                                                                requests.setLocation(itemPosts.getLocation());
                                                                                requests.setDecription(itemPosts.getDescription());
                                                                                requests.setTime(itemPosts.getTime());
                                                                                requests.setPostDate(itemPosts.getPostDate());
                                                                                requests.setTextColorGender(itemPosts.getColor());
                                                                                requests.setTypeId(itemPosts.getTypeId());
                                                                                requests.setPostId(itemPosts.getPostId());
                                                                                requests.setPostItemsId(itemPosts.getPostItemsId());
                                                                                list.add(requests);
                                                                            }
                                                                            name.getNames(list);

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

                                            if (postItemId == 9){
                                                Log.v("Names","4");
                                                Query query = FirebaseDatabase.getInstance().getReference("Human_Posts")
                                                        .orderByChild("postId").equalTo(postId);
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                                final HumanPosts  humanPosts = dataSnapshot1.getValue(HumanPosts.class);
                                                                String userId = humanPosts.getUserId();
                                                                Query query = FirebaseDatabase.getInstance().getReference("User")
                                                                        .orderByChild("user_id").equalTo(userId);
                                                                Log.v("Names","5");

                                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                                                User user = snapshot2.getValue(User.class);
                                                                                String fname = user.getFname();
                                                                                String lname = user.getLname();
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
                                                                                list.add(requests);
                                                                            }
                                                                            name.getNames(list);

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

    public interface NotificationNumber{
        public void getNotificationNumber(long notificationNum);
    }

    public interface Name {
        public void getNames(List<Posts> items );
    }
}
