package com.hfad.dawrlygp.repository;

import android.os.Handler;
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
import com.hfad.dawrlygp.model.SimilarityTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainRepository {

    public static MainRepository instance ;

    private FirebaseAuth auth ;

    private String currentUserId;

    private List<SimilarityTable> items1 = new ArrayList<>();
    private List<SimilarityTable> items2 = new ArrayList<>() ;
    private List<SimilarityTable> items3 = new ArrayList<>() ;
    private List<SimilarityTable> items4 = new ArrayList<>() ;

    List<SimilarityTable> newList1 = new ArrayList<>();
    List<SimilarityTable> newList2 = new ArrayList<>();
    List<SimilarityTable> newList3 = new ArrayList<>();
    List<SimilarityTable> newList4 = new ArrayList<>();


    private DatabaseReference reference ;

    private static final String TAG = "MainRepository";

    private ListInterface listInterface;

    public void setListInterface(ListInterface listInterface) {
        this.listInterface = listInterface;
    }

    public static MainRepository getInstance(){
        if (instance == null){
            instance = new MainRepository();
        }
        return instance ;
    }

    public MutableLiveData<List<SimilarityTable>> getData(){
        setListItems();
        MutableLiveData<List<SimilarityTable>> liveData = new MediatorLiveData<>();
        //liveData.setValue(items);
        return liveData;

    }

    private void setListItems(){

        items1.clear();
        items2.clear();
        items3.clear();
        items4.clear();
        newList1.clear();
        newList2.clear();
        newList3.clear();
        newList4.clear();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() !=null){

            currentUserId = auth.getCurrentUser().getUid();

            reference = FirebaseDatabase.getInstance().getReference("Approved_posts");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);

                            getUser(approvedPosts.getPostItemsId(),approvedPosts.getPostId());
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getUser(int postItems , String postId){

        if (postItems >= 1 && postItems <=8){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("userId")
                    .equalTo(currentUserId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                            final ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            final int typeId1 = itemPosts.getTypeId();
                            String dec1 = itemPosts.getDescription();
                            String postId1 = itemPosts.getPostId();
                            String color1 = itemPosts.getColor();
                            String modelName1 = itemPosts.getModelName();
                            String title1 = itemPosts.getTitle();
                            int postItemIdDB = itemPosts.getPostItemsId();
                            String date1 = itemPosts.getTime();
                            String imageURL1 = itemPosts.getImageUrl();


                            if (postId.equals(postId1)) {
                                if (typeId1 == 2) {
                                        Query query1 = FirebaseDatabase.getInstance()
                                                .getReference("Item_Posts")
                                                .orderByChild("typeId")
                                                .equalTo(1);
                                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    SimilarityTable posts = new SimilarityTable();
                                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                                        ItemPosts itemPosts1 = snapshot2.getValue(ItemPosts.class);
                                                        if (!itemPosts1.getUserId().equals(currentUserId)
                                                                && itemPosts1.getPostItemsId()== postItemIdDB) {
                                                            String postId2 = itemPosts1.getPostId();
                                                            String userId2 = itemPosts1.getUserId();
                                                            String desc2 = itemPosts1.getDescription();
                                                            String title2 = itemPosts1.getTitle();
                                                            String modelName2 = itemPosts1.getModelName();
                                                            String color2 = itemPosts1.getColor();
                                                            String date2 = itemPosts1.getTime();
                                                            String imageURL2 = itemPosts1.getImageUrl();
                                                            //   posts.setUserId1(currentUserId);
                                                            //    posts.setUserId2(userId);
                                                            if (!checkApproved(postId2)){
                                                               if (postItems == 1){
                                                                   posts.setTitleName1(title1);
                                                                   posts.setTitleName2(title2);
                                                                   posts.setModelName1(modelName1);
                                                                   posts.setModelName2(modelName2);
                                                                   posts.setColor1(color1);
                                                                   posts.setColor2(color2);
                                                               }
                                                                if (postItems == 2){
                                                                    posts.setColor1(color1);
                                                                    posts.setColor2(color2);
                                                                }
                                                                if (postItems >=3 && postItems <=6){
                                                                    posts.setTitleName1(title1);
                                                                    posts.setTitleName2(title2);
                                                                    posts.setColor1(color1);
                                                                    posts.setColor2(color2);
                                                                }
                                                                if (postItems == 7 || postItems == 8){
                                                                    posts.setTitleName1(title1);
                                                                    posts.setTitleName2(title2);
                                                                }
                                                                posts.setPostId1(postId1);
                                                                posts.setPostId2(postId2);
                                                                posts.setDesc1(dec1);
                                                                posts.setDesc2(desc2);
                                                                posts.setDate1(date1);
                                                                posts.setDate2(date2);
                                                                posts.setPostItemsId(postItems);
                                                                posts.setImageURL1(imageURL1);
                                                                posts.setImageURL2(imageURL2);
                                                                items1.add(posts);
                                                            }



                                                        }

                                                    }
                                                    Log.d(TAG, "onDataChange: List 1 " + items1.toString());

                                                    //  listInterface.getList(items);

                                                    setList1(items1);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                               }

                                else {
                                    Query query1 = FirebaseDatabase.getInstance()
                                            .getReference("Item_Posts")
                                            .orderByChild("typeId")
                                            .equalTo(2);
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                                    ItemPosts itemPosts1 = snapshot2.getValue(ItemPosts.class);
                                                    if (!itemPosts1.getUserId().equals(currentUserId)
                                                            && itemPosts1.getPostItemsId()== postItemIdDB) {
                                                        String postId2 = itemPosts1.getPostId();
                                                        String userId2 = itemPosts1.getUserId();
                                                        String desc2 = itemPosts1.getDescription();
                                                        String title2 = itemPosts1.getTitle();
                                                        String modelName2 = itemPosts1.getModelName();
                                                        String color2 = itemPosts1.getColor();
                                                        String date2 = itemPosts1.getTime();
                                                        String imageURL2 = itemPosts1.getImageUrl();
                                                        SimilarityTable posts = new SimilarityTable();
                                                      //  posts.setUserId1(currentUserId);
                                                      //  posts.setUserId2(userId);
                                                        if (!checkApproved(postId2)){
                                                            if (postItems == 1){
                                                                posts.setTitleName1(title1);
                                                                posts.setTitleName2(title2);
                                                                posts.setModelName1(modelName1);
                                                                posts.setModelName2(modelName2);
                                                                posts.setColor1(color1);
                                                                posts.setColor2(color2);
                                                            }
                                                            if (postItems == 2){
                                                                posts.setColor1(color1);
                                                                posts.setColor2(color2);
                                                            }
                                                            if (postItems >=3 && postItems <=6){
                                                                posts.setTitleName1(title1);
                                                                posts.setTitleName2(title2);
                                                                posts.setColor1(color1);
                                                                posts.setColor2(color2);
                                                            }
                                                            if (postItems == 7 || postItems == 8){
                                                                posts.setTitleName1(title1);
                                                                posts.setTitleName2(title2);
                                                            }
                                                            posts.setPostId1(postId1);
                                                            posts.setPostId2(postId2);
                                                            posts.setDesc1(dec1);
                                                            posts.setDesc2(desc2);
                                                            posts.setDate1(date1);
                                                            posts.setDate2(date2);
                                                            posts.setImageURL1(imageURL1);
                                                            posts.setImageURL2(imageURL2);
                                                            posts.setPostItemsId(postItems);
                                                            items2.add(posts);
                                                        }



                                                    }

                                                }
                                               // Log.d(TAG, "onDataChange: List 2 " + items2.toString());

                                                //listInterface.getList(items);

                                                setList2(items2);
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (postItems==9){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("userId")
                    .equalTo(currentUserId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                            final HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                            final int typeId = humanPosts.getTypeId();
                            String dec1 = humanPosts.getDescription();
                            String postId1 = humanPosts.getPostId();
                            String date1 = humanPosts.getTime();
                            String name1 = humanPosts.getName();
                            String imageURL1 = humanPosts.getImageUrl();
                            if (postId1.equals(postId)) {
                                if (typeId==2){
                                Query query1 = FirebaseDatabase.getInstance()
                                        .getReference("Human_Posts")
                                        .orderByChild("typeId")
                                        .equalTo(1);
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                HumanPosts humanPosts1 = snapshot2.getValue(HumanPosts.class);
                                                if (!humanPosts1.getUserId().equals(currentUserId)){
                                                    String postId2 = humanPosts1.getPostId();
                                                    String userId = humanPosts1.getUserId();
                                                    String desc2 = humanPosts1.getDescription();
                                                    String date2 = humanPosts1.getTime();
                                                    String name2 = humanPosts1.getName();
                                                    String imageURL2 = humanPosts1.getImageUrl();
                                                    SimilarityTable posts = new SimilarityTable();
                                                   // posts.setUserId1(currentUserId);
                                                  //  posts.setUserId2(userId);
                                                    if (!checkApproved(postId2)) {
                                                        posts.setPostId1(postId1);
                                                        posts.setPostId2(postId2);
                                                        posts.setDesc1(dec1);
                                                        posts.setDesc2(desc2);
                                                        posts.setPostItemsId(postItems);
                                                        posts.setDate1(date1);
                                                        posts.setDate2(date2);
                                                        posts.setTitleName1(name1);
                                                        posts.setTitleName2(name2);
                                                        posts.setImageURL1(imageURL1);
                                                        posts.setImageURL2(imageURL2);
                                                        items3.add(posts);
                                                    }


                                                }

                                            }
                                        //    Log.d(TAG, "onDataChange: List 3  "+items3.toString());

                                            setList3(items3);

                                            // listInterface.getList(items);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else {
                                    Query query1 = FirebaseDatabase.getInstance()
                                        .getReference("Human_Posts")
                                        .orderByChild("typeId")
                                        .equalTo(2);
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                                HumanPosts humanPosts1 = snapshot2.getValue(HumanPosts.class);
                                                if (!humanPosts1.getUserId().equals(currentUserId)){

                                                    String postId2 = humanPosts1.getPostId();
                                                    String userId = humanPosts1.getUserId();
                                                    String desc2 = humanPosts1.getDescription();
                                                    String date2 = humanPosts1.getTime();
                                                    String name2 = humanPosts1.getName();
                                                    String imageURL2 = humanPosts1.getImageUrl();
                                                    SimilarityTable posts = new SimilarityTable();
                                                    // posts.setUserId1(currentUserId);
                                                    //  posts.setUserId2(userId);
                                                    if (!checkApproved(postId2)) {
                                                        posts.setPostId1(postId1);
                                                        posts.setPostId2(postId2);
                                                        posts.setDesc1(dec1);
                                                        posts.setDesc2(desc2);
                                                        posts.setPostItemsId(postItems);
                                                        posts.setDate1(date1);
                                                        posts.setDate2(date2);
                                                        posts.setTitleName1(name1);
                                                        posts.setTitleName2(name2);
                                                        posts.setImageURL1(imageURL1);
                                                        posts.setImageURL2(imageURL2);
                                                        items4.add(posts);
                                                    }


                                                }

                                            }

                                        }
                                       // Log.d(TAG, "onDataChange: List 4 "+items4.toString());

                                        setList4(items4);

                                        //listInterface.getList(items);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }



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


    public void getListFinalList(){
        ArrayList<SimilarityTable> newList = new ArrayList<>();

        Handler handler = new Handler();

        int delay = 1000;


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SimilarityTable> list1 = getList1();
                List<SimilarityTable> list2 = getList2();
                List<SimilarityTable> list3 = getList3();
                List<SimilarityTable> list4 = getList4();
                Log.d(TAG, "run: 1 "+list1.toString());
                Log.d(TAG, "run: 2 "+list2.toString());
                Log.d(TAG, "run: 3 "+list3.toString());
                Log.d(TAG, "run: 4 "+list4.toString());

                newList.addAll(list1);
                newList.addAll(list2);
                newList.addAll(list3);
                newList.addAll(list4);

                ArrayList<SimilarityTable> duplicate = new ArrayList<>();
                duplicate.addAll(newList);
                HashSet<SimilarityTable> filter = new HashSet(duplicate);
                ArrayList<SimilarityTable> tables = new ArrayList<>(filter);
                Log.d(TAG, "run: "+tables.size());
                listInterface.getList(tables);
            }
        },delay);

    }

    private void setList1(List<SimilarityTable> list1) {
      //  Log.d(TAG, "setList1: "+newList1);
        newList1 = list1;
    }

    private void setList2(List<SimilarityTable> list2) {
      //  Log.d(TAG, "setList2: "+newList2);
        newList2 = list2;
    }

    private void setList3(List<SimilarityTable> list3) {
     //   Log.d(TAG, "setList3: "+newList3);
        newList3= list3;
    }

    private void setList4(List<SimilarityTable> list4) {
       // Log.d(TAG, "setList4: "+newList4);
        newList4 = list4;
    }

    private List<SimilarityTable> getList1() {
       // Log.d(TAG, "getList1: "+ newList1);

        return newList1;
    }

    private List<SimilarityTable> getList2() {
      //  Log.d(TAG, "getList2: "+ newList2);

        return newList2;
    }

    private List<SimilarityTable> getList3() {
     //   Log.d(TAG, "getList3: "+ newList3);

        return newList3;
    }

    private List<SimilarityTable> getList4()
    {
        //Log.d(TAG, "getList4: "+ newList4);
        return newList4;
    }

    public interface ListInterface {
        public void getList(List<SimilarityTable> similarityTables);
    }

    private boolean checkApproved(String postId){
        Query query = FirebaseDatabase.getInstance()
                .getReference("Approved_posts")
                .orderByChild("postId")
                .equalTo(postId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return false;
    }


}
