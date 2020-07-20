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
import com.hfad.dawrlygp.views.ChangePassword;

public class ChangePasswordViewModel extends ViewModel {

    public MutableLiveData<String> currentPassword = new MutableLiveData<>();
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> confirmationNewPassword = new MutableLiveData<>();
    private User user ;
    private MutableLiveData<User> data ;
    private boolean isSuccess;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public MutableLiveData<User> getData() {
        if (data == null){
            data = new MutableLiveData<>();
        }
        return data;
    }

    public  void onClick(View view){
        user = new User();
        user.setPassword(currentPassword.getValue());
        user.setNewPassword(newPassword.getValue());
        user.setConfirmPassword(confirmationNewPassword.getValue());
        data.setValue(user);
    }

    public void updatePassword(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            String id = auth.getCurrentUser().getUid();
            Query query = FirebaseDatabase.getInstance().getReference("User")
                    .orderByChild("user_id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren())
                        {

                            User user1 = snapshot.getValue(User.class);
                            if (user1.getPassword().equals(user.getPassword())
                            ){
                                isSuccess = true ;
                                callBack.onCallBackPassword(isSuccess);
                                Log.v("Tag","Success Password "+isSuccess);
                                auth.getCurrentUser().updatePassword(user.getNewPassword())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    snapshot.getRef().child("password").setValue(user.getNewPassword());
                                                    snapshot.getRef().child("confirmPassword").setValue(user.getNewPassword());
                                                    isSuccess = true ;
                                                    callBack.onCallBack(isSuccess);
                                                    Log.v("Tag","Success updated "+isSuccess);



                                                }
                                                else {
                                                    isSuccess = false ;
                                                    callBack.onCallBack(isSuccess);
                                                    Log.v("Tag","fail updated "+isSuccess);
                                                    Log.v("Error",task.getException().toString());
                                                }
                                            }
                                        });

                            }else {

                                isSuccess = false ;
                                callBack.onCallBackPassword(isSuccess);
                                Log.v("Tag","fail Password "+isSuccess);


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

    public interface CallBack{
        public void onCallBackPassword(boolean isSuccess);
        public void onCallBack(boolean isSuccess);
    }

}
