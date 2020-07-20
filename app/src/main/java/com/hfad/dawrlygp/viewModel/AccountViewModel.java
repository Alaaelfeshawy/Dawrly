package com.hfad.dawrlygp.viewModel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.dawrlygp.model.ApprovedPosts;
import com.hfad.dawrlygp.model.FoundPosts;
import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.ItemPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.AccountRepository;
import com.hfad.dawrlygp.views.Fragments.UserFragment.AccountFragment;

import java.util.List;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private AccountRepository repoistory ;

    private FirebaseAuth auth;

    private SetDataSuccess success;

    private Names names;

    private Success successCall ;

    public void setSuccessCall(Success successCall) {
        this.successCall = successCall;
    }

    public void setNames(Names names) {
        this.names = names;
    }

    public void setSuccess(SetDataSuccess success) {
        this.success = success;
    }

    public void init(AccountFragment context){
        repoistory = AccountRepository.getInstance(context);
        data = repoistory.getData();
        Log.v("Size",""+data.toString());
        repoistory.setSuccess(new AccountRepository.Success() {
            @Override
            public void success(boolean isSuccess) {
                success.setDataSuccess(isSuccess);

            }
        });
        repoistory.setData(new AccountRepository.Data() {
            @Override
            public void onSendData(String lname, String fname, String image) {
                Log.v("AccountViewModel",lname+" "+fname+" "+image);

                names.getNames(fname , lname , image);

            }
        });
    }

    public boolean logout(){
       auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!= null) {
            auth.signOut();
            return true;

        }
        return false ;

    }

    public void delete(final String postId , final int postItemsId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Approved_posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("SavedViewModel","3");
                            snapshot.getRef().removeValue();
                            removeData(postId , postItemsId);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Query query1 = FirebaseDatabase.getInstance()
                    .getReference("Found_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.v("SavedViewModel","3");
                            snapshot.getRef().removeValue();
                            removeData(postId , postItemsId);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Query query2 = FirebaseDatabase.getInstance()
                    .getReference("Notification")
                    .orderByChild("postId")
                    .equalTo(postId);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void removeData(String postId , int postItemsId){
        if (postItemsId >=1 && postItemsId <=8){
            Query query2 = FirebaseDatabase.getInstance()
                    .getReference("Item_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            ItemPosts itemPosts = snapshot1.getValue(ItemPosts.class);
                            String imagePost = itemPosts.getImageUrl();
                            snapshot1.getRef().removeValue();
                            if (imagePost !=null)
                            {
                                StorageReference photoRef = FirebaseStorage.getInstance()
                                        .getReferenceFromUrl(imagePost);
                                photoRef.delete().addOnSuccessListener(aVoid -> Log.d("Delete", "onSuccess: deleted file"));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (postItemsId ==9){
            Query query1 = FirebaseDatabase.getInstance()
                    .getReference("Human_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            HumanPosts humanPosts = snapshot1.getValue(HumanPosts.class);
                            String imagePost = humanPosts.getImageUrl();
                            snapshot1.getRef().removeValue();
                            if (imagePost !=null) {
                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imagePost);

                                photoRef.delete().addOnSuccessListener(aVoid -> Log.d("Delete", "onSuccess: deleted file"));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Query query3 = FirebaseDatabase.getInstance()
                .getReference("Location")
                .orderByChild("id")
                .equalTo(postId);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        snapshot1.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void found(String postId){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Approved_posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ApprovedPosts approvedPosts = snapshot.getValue(ApprovedPosts.class);
                            String postId = approvedPosts.getPostId();
                            int postItemsId = approvedPosts.getPostItemsId();
                            snapshot.getRef().removeValue();
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("Found_Posts");
                            FoundPosts posts = new FoundPosts();
                            String foundPostId = reference.push().getKey();
                            posts.setId(foundPostId);
                            posts.setPostId(postId);
                            posts.setPostItemsId(postItemsId);
                            reference.push().setValue(posts);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public  void checkFound(String postId){

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Query query = FirebaseDatabase.getInstance()
                    .getReference("Found_Posts")
                    .orderByChild("postId")
                    .equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        successCall.setSuccess(true);

                    }
                    else {
                        successCall.setSuccess(false);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface SetDataSuccess{
        public void setDataSuccess(boolean isSuccess);
    }

    public interface Names {
        public void getNames(String fname , String lname , String image);
    }

    public interface Success{
        public void setSuccess(boolean isSuccess);
    }


}
