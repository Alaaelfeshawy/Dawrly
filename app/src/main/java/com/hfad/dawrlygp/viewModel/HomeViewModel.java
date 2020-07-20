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
import com.hfad.dawrlygp.model.ApprovedPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Message;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private FirebaseAuth auth;

    private Name name ;

    private String user_id ;

    private Number number ;

    public void setNumber(Number number) {
        this.number = number;
    }

    private int messageUnseenCount = 0;

    private List<Posts> list = new ArrayList<>();

    public void getNotificationForMessages(){

        auth = FirebaseAuth.getInstance();

        final String currentUserId = auth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance()
                .getReference("User");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                        final User user = snapshot1.getValue(User.class);

                        final String userId = user.getUser_id();

                        Log.v("ChatRepo","1");
                        Query query1 = FirebaseDatabase.getInstance()
                                .getReference("Message")
                                .child(currentUserId +" To " + userId);

                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                        Message message = snapshot.getValue(Message.class);

                                        if (!message.isSeen()){

                                            messageUnseenCount++;

                                            Log.v("messageUnseenCount","Count "+
                                                    messageUnseenCount);

                                            number.getNumber(messageUnseenCount);
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

    public void setName(Name name) {
        this.name = name;
    }

    public void getSenderName(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            user_id = auth.getCurrentUser().getUid();
            Log.v("HomeViewModel","1");
            Query reference = FirebaseDatabase.getInstance()
                    .getReference("Notification")
                    .orderByChild("postType")
                    .equalTo("User");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Notification notification = snapshot.getValue(Notification.class);
                            String postId = notification.getPostId();
                            Log.v("HomeViewModel","2");
                            Query query = FirebaseDatabase.getInstance()
                                    .getReference("Approved_posts")
                                    .orderByChild("id")
                                    .equalTo(postId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                            Log.v("HomeViewModel","3");
                                            ApprovedPosts approvedPosts = snapshot1.getValue(ApprovedPosts.class);
                                            int postItemId = approvedPosts.getPostItemsId();
                                            String postId = approvedPosts.getPostId();

                                            if (postItemId == 9){
                                                Log.v("HomeViewModel","4");
                                                Query query = FirebaseDatabase.getInstance()
                                                        .getReference("Human_Posts")
                                                        .orderByChild("postId")
                                                        .equalTo(postId);
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                                final HumanPosts  humanPosts = dataSnapshot1.getValue(HumanPosts.class);
                                                                String userId = humanPosts.getUserId();
                                                                Query query = FirebaseDatabase.getInstance().getReference("User")
                                                                        .orderByChild("user_id").equalTo(userId);
                                                                Log.v("HomeViewModel","5");

                                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                                                User user = snapshot2.getValue(User.class);
                                                                                String fname = user.getFname();
                                                                                String lname = user.getLname();
                                                                                String image = user.getImageId();
                                                                                String userId = user.getUser_id();
                                                                                if (userId.equals(user_id)) {
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
                                                                                    name.getNames(list);
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

                                            else {
                                                Log.v("HomeViewModel","4");
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
                                                                Query query = FirebaseDatabase.getInstance()
                                                                        .getReference("User")
                                                                        .orderByChild("user_id")
                                                                        .equalTo(userId);
                                                                Log.v("HomeViewModel","5");

                                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                                                User user = snapshot2.getValue(User.class);
                                                                                String fname = user.getFname();
                                                                                String lname = user.getLname();
                                                                                String image = user.getImageId();
                                                                                String userId = user.getUser_id();
                                                                                if (userId.equals(user_id)){
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
                                                                                    requests.setTextColorGender(itemPosts.getColor());
                                                                                    requests.setTypeId(itemPosts.getTypeId());
                                                                                    requests.setPostId(itemPosts.getPostId());
                                                                                    requests.setPostItemsId(itemPosts.getPostItemsId());
                                                                                    list.add(requests);
                                                                                    name.getNames(list);

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

    public interface Name {
        public void getNames(List<Posts> items );
    }

    public interface Number{
        public void getNumber(int count);
    }


}
