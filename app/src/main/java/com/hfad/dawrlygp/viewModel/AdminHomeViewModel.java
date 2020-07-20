package com.hfad.dawrlygp.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHomeViewModel extends ViewModel {

   private FirebaseAuth auth ;

   private DatabaseReference reference ;

   private Count countFun ;

   private long countApp , countFound , countReq , countRepo ;

    public void setCountFun(Count countFun) {
        this.countFun = countFun;
    }

    public void getCount(){
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            reference = FirebaseDatabase.getInstance().getReference("Approved_posts");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        countApp = dataSnapshot.getChildrenCount();
                     }
                    reference = FirebaseDatabase.getInstance().getReference("Bending_Posts");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                countReq = dataSnapshot.getChildrenCount();
                            }
                            reference = FirebaseDatabase.getInstance().getReference("Found_Posts");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){

                                        countFound = dataSnapshot.getChildrenCount();

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            reference = FirebaseDatabase.getInstance().getReference("Report_Posts");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){

                                        countRepo = dataSnapshot.getChildrenCount();

                                    }

                                    countFun.Count(countApp,countReq,countFound,countRepo);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






        }
    }

    public interface Count{
        public void Count(long countApp , long countReq , long countFound , long countReport);

    }
}
