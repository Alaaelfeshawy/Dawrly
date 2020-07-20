package com.hfad.dawrlygp.viewModel;

import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.model.User;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> password = new MutableLiveData<>();

    private FirebaseAuth auth;

    private myCallBack back ;

    private AdminCallback adminCallback;

    public void setAdminCallback(AdminCallback adminCallback) {
        this.adminCallback = adminCallback;
    }

    public boolean userExist(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!= null) {
            String id = auth.getCurrentUser().getUid();
            if(!id.equals("iISUW7RSppfkRZzNAz17ItdWc252")){
                return true;
            }
        }
            return false ;
    }

    public boolean adminExist(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!= null) {
            String id = auth.getCurrentUser().getUid();
            if(id.equals("iISUW7RSppfkRZzNAz17ItdWc252")){
                return true;
            }
        }
        return false;
    }

    public void setBack(myCallBack back) {
        this.back = back;
    }

    public interface myCallBack {

        void onCallback(Boolean success);

    }

    public interface AdminCallback{
        public void AdminCallBack(boolean isSuccess);
    }

    public void login(final String mail , final String password){
        auth = FirebaseAuth.getInstance();
        Log.v("False","1");

        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.v("False","2");

                if (task.isSuccessful()){

                    if (auth.getCurrentUser()!= null) {
                        String id = auth.getCurrentUser().getUid();
                        Query query = FirebaseDatabase.getInstance()
                                .getReference("User")
                                .orderByChild("user_id").equalTo(id);
                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        User user = snapshot.getValue(User.class);
                                        if (mail.equals("admin123@yahoo.com")&&password.equals("Adminapp123")){
                                            adminCallback.AdminCallBack(true);
                                        }
                                        else if (mail.equals(user.getEmail())){
                                            snapshot.getRef().child("password").setValue(password);
                                            snapshot.getRef().child("confirmPassword").setValue(password);
                                            back.onCallback(true);
                                        }

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
                else {
                    Log.v("False","4");
                    back.onCallback(false);
                }
            }
        });

    }

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

       User loginUser = new User(email.getValue(), password.getValue());

        userMutableLiveData.setValue(loginUser);

    }



}
