package com.hfad.dawrlygp.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.ChangeMail;

public class ChangeEmailViewModel extends ViewModel {

    public MutableLiveData<String> currentMail = new MutableLiveData<>();

    public MutableLiveData<String> newMail = new MutableLiveData<>();

    private User user ;

    private boolean isSuccess ;

    private CallBack back ;

    public void setBack(CallBack back) {
        this.back = back;
    }

    private MutableLiveData<User> mUser ;

    public void updateMail(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String id = auth.getCurrentUser().getUid();
            Query query = FirebaseDatabase.getInstance().getReference("User")
                    .orderByChild("user_id").equalTo(id);
            Log.v("Tag","Id "+id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren())
                         {
                            User user1 = snapshot.getValue(User.class);
                            if (user1.getEmail().equals(currentMail.getValue())
                            ) {
                                isSuccess= true;
                                back.onCallBackMail(isSuccess);
                                Log.v("Tag","MailSuccess"+isSuccess);
                                auth.getCurrentUser().updateEmail(newMail.getValue())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    snapshot.getRef().child("email").setValue(user.getNewEmail());
                                                    isSuccess = true ;
                                                    back.onCallBack(isSuccess);
                                                    Log.v("Tag","MailSuccessUpdated"+isSuccess);



                                                }
                                                else {
                                                    Log.v("Error",task.getException().toString());
                                                    isSuccess = false ;
                                                    back.onCallBack(isSuccess);
                                                    Log.v("Tag","MailSuccessFail"+isSuccess);

                                                }
                                            }
                                        });

                            }else {
                                isSuccess = false;
                                back.onCallBackMail(isSuccess);
                                Log.v("Tag","MailFail "+isSuccess);


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

    public MutableLiveData<User> getData(){
        if (mUser == null){
            mUser = new MutableLiveData<>();
        }
        return mUser;
    }

    public void Onclick(View view){
        user = new User();
        user.setNewEmail(newMail.getValue());
        user.setEmail(currentMail.getValue());
        mUser.setValue(user);
    }

    public interface CallBack{
        public void onCallBackMail(Boolean isSuccessMail);
        public void onCallBack(Boolean isSuccess);

    }
}
