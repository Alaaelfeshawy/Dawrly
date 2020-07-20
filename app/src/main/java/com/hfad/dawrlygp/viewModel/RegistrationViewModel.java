package com.hfad.dawrlygp.viewModel;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.views.Fragments.MainFragment.RegistrationFragment;

public class RegistrationViewModel extends ViewModel{

    public MutableLiveData<String> mail = new MutableLiveData<>();
    public MutableLiveData<String> fname = new MutableLiveData<>();
    public MutableLiveData<String> lname = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public MutableLiveData<String> birthofDate = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    private FirebaseAuth mAuth ;
    private DatabaseReference ref ;
    private FirebaseUser firebaseUser;
    public static String MESSAGE ;
    private myCallBack back ;
    private String type ;
    private MutableLiveData<User> data ;
    private User user ;

    public MutableLiveData<User> getData() {
        if (data == null){
            data = new MutableLiveData<>();
        }
        return data;
    }


    public void setBack(myCallBack back) {
        this.back = back;
    }

    public void signIn(String mail , String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = mAuth.getCurrentUser();
                    ref = FirebaseDatabase.getInstance().getReference().child("User");
                    String firebase_id = firebaseUser.getUid() ;
                    user.setUser_id(firebase_id);
                    user.setImageId("null");
                    ref.child(firebase_id).setValue(user);

                    ref = FirebaseDatabase.getInstance().getReference().child("Location");
                    Location postLocation = new Location();
                    postLocation.setId(firebase_id);
                    String postLocationId = ref.push().getKey();
                    postLocation.setLocationId(postLocationId);
                    postLocation.setLat(RegistrationFragment.lat);
                    postLocation.setLon(RegistrationFragment.lon);
                    postLocation.setSendBy("User");
                    ref.push().setValue(postLocation);
                    back.onCallback(true);

                }
                else {
                    back.onCallback(false);
                    MESSAGE = task.getException().getMessage();
                    Log.w("failure", "createUserWithEmail:failure", task.getException());

                }
            }
        });
    }

    public void onClick(View view) {
        user = new User();
        user.setEmail(mail.getValue());
        user.setFname(fname.getValue());
        user.setLname(lname.getValue());
        user.setPassword(password.getValue());
        user.setConfirmPassword(confirmPassword.getValue());
        user.setDateOfBirth(birthofDate.getValue());
        user.setLocation(RegistrationFragment.address);
        user.setGender(type);
        user.setPhoneNumber(phoneNumber.getValue());
        data.setValue(user);


    }

    public void onClickChooser(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioMale :
                if (checked)
                   type = "Male" ;
                break;
            case R.id.radioFemale :
                if (checked)
                    type = "Female" ;
                break;

        }
    }

    public interface myCallBack {

        void onCallback(Boolean success);

    }


}
