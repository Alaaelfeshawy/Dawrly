package com.hfad.dawrlygp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.PostItems;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;
import java.util.ArrayList;
import java.util.List;

public class MainDialogRepository {

    public static MainDialogRepository instance ;

    private List<PostItems> postItems = new ArrayList<>();

    private DatabaseReference firebaseDatabase ;

    private static MainDialogFragment mContext;

    private static DataCalled dataCalled ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static MainDialogRepository getInstance(MainDialogFragment context){
        mContext = context ;
        if (instance == null){
            instance = new MainDialogRepository();
        }
        dataCalled = (DataCalled) mContext;
        return instance ;
    }

    public  MutableLiveData<List<PostItems>> getData(){
        setData();
        MutableLiveData<List<PostItems>> liveData = new MutableLiveData<>();
        liveData.setValue( postItems);
        return liveData ;

    }

    private void setData(){
        postItems.clear();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = firebaseDatabase.child("PostItems");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        postItems.add(snapshot.getValue(PostItems.class));
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

   public interface Success{
        public void onSuccess(boolean isSuccess);
    }


}
