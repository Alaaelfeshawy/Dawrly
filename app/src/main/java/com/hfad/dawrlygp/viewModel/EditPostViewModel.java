package com.hfad.dawrlygp.viewModel;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.views.EditPost;

public class EditPostViewModel extends ViewModel {
    public MutableLiveData<String> nameBrandName = new MutableLiveData<>();
    public MutableLiveData<String> colorValue = new MutableLiveData<>();
    public MutableLiveData<String> ageModelName = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    public MutableLiveData<String> desc = new MutableLiveData<>();
    public MutableLiveData<String> date = new MutableLiveData<>();
    public MutableLiveData<String> type = new MutableLiveData<>();
    private int typeValue =0;
    private MutableLiveData<Posts> mPosts ;
    private Posts post ;
    private String gender = null ;
    private FirebaseAuth auth ;
    private StorageReference storageReference;
       private DataItems dataItems ;
       private DataHuman dataHuman ;
    private static final String TAG = "EditPostViewModel";

    public void setDataHuman(DataHuman dataHuman) {
        this.dataHuman = dataHuman;
    }

    public void setDataItems(DataItems dataItems) {
        this.dataItems = dataItems;
    }

    private SuccessCall call ;

    private Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public void setCall(SuccessCall call) {
        this.call = call;
    }

    public MutableLiveData<Posts> getData(){
        if (mPosts == null){
            mPosts = new MutableLiveData<>();
        }
        return mPosts;
    }

    public void Onclick(View view){

        post = new Posts();
        post.setTextBrandNameName(nameBrandName.getValue());
        if (colorValue.getValue() != null){
            post.setTextColorGender(colorValue.getValue());

        }
        if (gender!=null){
            post.setTextColorGender(gender);

        }
        post.setTypeId(typeValue);
        post.setTextAgeModelName(ageModelName.getValue());
        post.setDecription(desc.getValue());
        post.setPostDate(date.getValue());
        post.setLocation(location.getValue());
        mPosts.setValue(post);

    }

    public void showData(int postItemsId , String postId) {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){

            if (postItemsId >=1 && postItemsId <=8)
                 {
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Item_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                ItemPosts itemPosts = snapshot.getValue(ItemPosts.class);
                                String imagePost = itemPosts.getImageUrl();
                                Log.v("EditPostViewModel","Image Post"+imagePost);
                                if (imagePost == null || imagePost.equals("null") ){
                                    image.onSetImage(true);
                                }
                                else {
                                    image.onSetImage(false);
                                }
                                dataItems.getData(itemPosts);


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            if (postItemsId == 9)
                {
                Query query = FirebaseDatabase.getInstance().getReference("Human_Posts")
                        .orderByChild("postId").equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                HumanPosts humanPosts = snapshot.getValue(HumanPosts.class);
                                String imagePost = humanPosts.getImageUrl();
                                if (imagePost == null || imagePost.equals("null")){
                                    image.onSetImage(true);
                                }else {
                                    image.onSetImage(false);
                                }
                                dataHuman.getData(humanPosts);


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

    public boolean removeImage(int postItemsId , String postId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            if (postItemsId >=1 && postItemsId<=8){
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Item_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                ItemPosts itemPosts = snapshot.getValue(ItemPosts.class);
                                String imageUri = itemPosts.getImageUrl();
                                snapshot.getRef().child("imageUrl").setValue("null");
                                StorageReference photoRef = FirebaseStorage.getInstance()
                                        .getReference().child("PostsImages/"+ imageUri);
                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Delete", "onSuccess: deleted file");

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
            if (postItemsId == 9){
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Human_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                HumanPosts humanPosts = snapshot.getValue(HumanPosts.class);
                                String imageUri = humanPosts.getImageUrl();
                                snapshot.getRef().child("imageUrl").removeValue();
                                StorageReference photoRef = FirebaseStorage.getInstance()
                                        .getReference().child("PostsImages/"+ imageUri);
                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Delete", "onSuccess: deleted file");

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
        return true;

    }

    public void updateData(final Uri uri , final int postItemsId
            , final String postId , String fileEx){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Log.v("EditPostViewModel","1");
           if (postItemsId >=1 && postItemsId<=8){
               Query query = FirebaseDatabase.getInstance()
                       .getReference("Item_Posts")
                       .orderByChild("postId")
                       .equalTo(postId);
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                               Log.v("EditPostViewModel","2");
                               ItemPosts itemPosts = snapshot.getValue(ItemPosts.class);
                               String brandName = itemPosts.getTitle();
                               String color = itemPosts.getColor();
                               String modelName = itemPosts.getModelName();
                               String location = itemPosts.getLocation() ;
                               String description = itemPosts.getDescription();
                               String postDate = itemPosts.getPostDate();
                               String imagePost = itemPosts.getImageUrl();
                               int typeId = itemPosts.getTypeId();
                               if (brandName != null){
                                   if (!brandName.equals(post.getTextBrandNameName())){
                                       snapshot.getRef().child("title").setValue(nameBrandName.getValue());
                                   }
                               }
                               if (typeValue == 0){
                                   Log.v(TAG,"Type 1 "+typeId);
                                   snapshot.getRef().child("typeId").setValue(typeId);
                               }
                               else {
                                   Log.v(TAG,"Type 2 "+typeValue);
                                   snapshot.getRef().child("typeId").setValue(typeValue);

                               }

                              if (color != null){
                                  if (!color.equals(post.getTextColorGender())){
                                      snapshot.getRef().child("color").setValue(colorValue.getValue());
                                  }
                              }

                               if (!location.equals(post.getLocation())){
                                   snapshot.getRef().child("location").setValue(EditPost.address);
                                   Query ref = FirebaseDatabase.getInstance()
                                           .getReference("Location").orderByChild("id").equalTo(postId);
                                   ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           if (dataSnapshot.exists()){
                                               for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                   dataSnapshot1.getRef().child("lat").setValue(EditPost.lat);
                                                   dataSnapshot1.getRef().child("lon").setValue(EditPost.lon);
                                               }
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });

                               }

                               if (!description.equals(post.getDecription())){
                                   snapshot.getRef().child("description").setValue(desc.getValue());
                               }

                               if (modelName != null){
                                   if (!modelName.equals(post.getTextAgeModelName())){
                                       snapshot.getRef().child("modelName").setValue(ageModelName.getValue());

                                   }
                               }
                               if (!postDate.equals(post.getPostDate())){
                                   snapshot.getRef().child("time").setValue(date.getValue());
                               }
                               if (imagePost == null || !imagePost.equals(post.getImageUrl()) )
                               {
                                   uploadImage(uri,postId , postItemsId,fileEx);

                                   if (imagePost !=null)

                                      {
                                          StorageReference photoRef = FirebaseStorage.getInstance()
                                                  .getReferenceFromUrl(imagePost);

                                          photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                                      }
                               }
                              call.onSuccessCall(true);
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
           }

            if (postItemsId ==9){
                Query query = FirebaseDatabase.getInstance()
                        .getReference("Human_Posts")
                        .orderByChild("postId")
                        .equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Log.v("EditPostViewModel","2");
                                HumanPosts humanPosts = snapshot.getValue(HumanPosts.class);
                                String name = humanPosts.getName();
                                int age = humanPosts.getAge();
                                String genderDB = humanPosts.getGender();
                                String location = humanPosts.getLocation() ;
                                String description = humanPosts.getDescription();
                                String postDate = humanPosts.getPostDate();
                                String imagePost = humanPosts.getImageUrl();
                                int typeId = humanPosts.getTypeId();

                                Log.d(TAG, "onDataChange: TypeDB "+typeId);

                                Log.d(TAG, "onDataChange:  GenderDB "+ genderDB);

                                if (!name.equals(post.getTextBrandNameName())){
                                    snapshot.getRef().child("name").setValue(nameBrandName.getValue());
                                }
                                if (gender == null){
                                    Log.v(TAG,"Gender 1 "+genderDB);
                                    snapshot.getRef().child("gender").setValue(genderDB);
                                }
                                else {
                                    Log.v(TAG,"Gender 2 "+gender);
                                    snapshot.getRef().child("gender").setValue(gender);

                                }
                                if (typeValue == 0){
                                    Log.v(TAG,"Type 1 "+typeId);
                                    snapshot.getRef().child("typeId").setValue(typeId);
                                }
                                else {
                                    Log.v(TAG,"Type 2 "+typeValue);
                                    snapshot.getRef().child("typeId").setValue(typeValue);

                                }

                                if (!location.equals(post.getLocation())){
                                    snapshot.getRef().child("location").setValue(EditPost.address);
                                    Query ref = FirebaseDatabase.getInstance()
                                            .getReference("Location").orderByChild("id").equalTo(postId);
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                    dataSnapshot1.getRef().child("lat").setValue(EditPost.lat);
                                                    dataSnapshot1.getRef().child("lon").setValue(EditPost.lon);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (!description.equals(post.getDecription())){
                                    snapshot.getRef().child("description").setValue(desc.getValue());
                                }
                                int ageint = Integer.parseInt(post.getTextAgeModelName());
                                if (age != ageint){
                                    snapshot.getRef().child("age").setValue(ageint);

                                }
                                if (!postDate.equals(post.getPostDate()))
                                {
                                    snapshot.getRef().child("time").setValue(date.getValue());
                                }
                               if (imagePost == null || !imagePost.equals(post.getImageUrl()) )
                               {
                                   uploadImage(uri,postId , postItemsId , fileEx);

                                   if (imagePost !=null)
                                   {
                                      StorageReference photoRef = FirebaseStorage.getInstance()
                                              .getReference().child("PostsImages/"+imagePost);

                                      photoRef.delete().addOnSuccessListener(aVoid -> Log.d("Delete", "onSuccess: deleted file"));
                                  }


                               }

                                call.onSuccessCall(true);
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

    private void uploadImage(final Uri targetUri , final String id
            , final int postItemsId , String fileEx) {
        if(targetUri != null)
        {
            storageReference = FirebaseStorage.getInstance().getReference()
                    .child("PostImages/"+System.currentTimeMillis()
                            + "." + fileEx);
            storageReference.putFile(targetUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (auth.getCurrentUser()!= null) {

                                if (postItemsId >=1 && postItemsId <=8){
                                    Query query = FirebaseDatabase.getInstance()
                                            .getReference("Item_Posts")
                                            .orderByChild("postId")
                                            .equalTo(id);
                                    ValueEventListener postListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                            {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                                                        String postImage=task.getResult().toString();
                                                        Log.d(TAG,postImage);
                                                        snapshot.getRef().child("imageUrl").setValue(postImage);

                                                    });
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    query.addListenerForSingleValueEvent(postListener);
                                }

                                if (postItemsId ==9){
                                    Query query = FirebaseDatabase.getInstance()
                                            .getReference("Human_Posts")
                                            .orderByChild("postId")
                                            .equalTo(id);
                                    ValueEventListener postListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                            {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                                                        String postImage=task.getResult().toString();
                                                        Log.d(TAG,postImage);
                                                        snapshot.getRef().child("imageUrl").setValue(postImage);

                                                    });
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    query.addListenerForSingleValueEvent(postListener);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });


        }

    }

    public  void onSexCheck(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioMaleProfile:
                if (checked)
                    gender ="Male";
                break;
            case R.id.radioFemaleProfile:
                if (checked)
                    gender ="Female";
                break;
        }

    }

    public  void onTypeCheck(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioFoundCheck:
                if (checked)
                    typeValue =1;
                break;
            case R.id.radioLostCheck:
                if (checked)
                    typeValue =2;
                break;
        }

    }


    public interface SuccessCall{
        public void onSuccessCall(boolean isSuccess);
    }

    public interface Image{
        public void onSetImage (boolean isImageExist);
    }

    public interface DataItems{
        public void getData(ItemPosts itemPosts);
    }

    public interface DataHuman{
        public void getData(HumanPosts humanPosts);
    }

}
