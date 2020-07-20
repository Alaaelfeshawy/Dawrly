package com.hfad.dawrlygp.viewModel;

import android.content.ContentResolver;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.views.EditPost;
import com.hfad.dawrlygp.views.EditProfile;
import java.io.IOException;

public class EditProfileViewModel extends ViewModel {

    public MutableLiveData<String> fname = new MutableLiveData<>();
    public MutableLiveData<String> lname = new MutableLiveData<>();
    public MutableLiveData<String> birthdate = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    private User user ;
    private MutableLiveData<User> mUser ;
    private String gender = null ;
    private FirebaseAuth auth ;
    private Data data ;
    private StorageReference storageReference;
    private SuccessCall call ;
    private SHowDataSuccess dataSuccess ;
    private Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public void setDataSuccess(SHowDataSuccess dataSuccess) {
        this.dataSuccess = dataSuccess;
    }

    public void setCall(SuccessCall call) {
        this.call = call;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public MutableLiveData<User> getData(){
        if (mUser == null){
            mUser = new MutableLiveData<>();
        }
        return mUser;
    }

    private static final String TAG = "EditProfileViewModel";

    public void Onclick(View view){

        user = new User();
        user.setDateOfBirth(birthdate.getValue());
        user.setFname(fname.getValue());
        user.setLname(lname.getValue());
        location.setValue(EditProfile.address);
        user.setLocation(location.getValue());
        user.setGender(gender);
        mUser.setValue(user);


    }

    public  void onRadioCheck(View view){
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

    public void showData(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            String id = auth.getCurrentUser().getUid();
            Query query = FirebaseDatabase.getInstance().getReference("User")
                    .orderByChild("user_id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user1 = snapshot.getValue(User.class);
                            String fname = user1.getFname();
                            String lname = user1.getLname();
                            String birthdate = user1.getDateOfBirth();
                            String location = user1.getLocation() ;
                            String imageURL = user1.getImageId();
                            String genderUser = user1.getGender();
                            if (!imageURL.equals("null")){
                                image.onSetImage(true);
                            }else {
                                image.onSetImage(false);
                            }
                            try {
                                data.getData(fname ,lname ,birthdate,location , imageURL , genderUser);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        dataSuccess.onShowDataSuccess(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean removeImage(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String id = auth.getCurrentUser().getUid();
            Query query = FirebaseDatabase.getInstance()
                    .getReference("User")
                    .orderByChild("user_id")
                    .equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user = snapshot.getValue(User.class);
                            String imageUri = user.getImageId();
                            snapshot.getRef().child("imageId").setValue("null");
                            Uri uri = Uri.parse(imageUri);
                            StorageReference photoRef = FirebaseStorage.getInstance()
                                    .getReferenceFromUrl(imageUri);
                            photoRef.delete().addOnSuccessListener(aVoid -> Log.d("Delete", "onSuccess: deleted file"));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return true;

    }

    public void updateData(Uri targetUri , String fileEx){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            final String id = auth.getCurrentUser().getUid();
            Query query = FirebaseDatabase.getInstance()
                    .getReference("User")
                    .orderByChild("user_id")
                    .equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user1 = snapshot.getValue(User.class);
                            String fnameUser = user1.getFname();
                            String lnameUser = user1.getLname();
                            String birthdateUser = user1.getDateOfBirth();
                            String locationUser = user1.getLocation() ;
                            String imageURLUser = user1.getImageId();
                            String genderUser = user1.getGender();
                            if (user.getLocation()==null){
                                Log.v("Location 1",locationUser);
                                snapshot.getRef().child("location").setValue(locationUser);
                            }
                            else {
                                snapshot.getRef().child("location").setValue(EditProfile.address);
                                Log.v("Location 2",EditProfile.address);
                                Query ref = FirebaseDatabase.getInstance()
                                        .getReference("Location").orderByChild("id").equalTo(id);
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                dataSnapshot1.getRef().child("lat").setValue(EditProfile.lat);
                                                dataSnapshot1.getRef().child("lon").setValue(EditProfile.lon);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            if (gender == null){
                                Log.v("Gender 1",genderUser);
                                snapshot.getRef().child("gender").setValue(genderUser);
                            }
                            else {
                                Log.v("Gender 2",gender);
                                snapshot.getRef().child("gender").setValue(gender);

                            }
                            if (!fnameUser.equals(user.getFname())){
                                snapshot.getRef().child("fname").setValue(fname.getValue());
                            }
                            if (!lnameUser.equals(user.getLname())){
                                snapshot.getRef().child("lname").setValue(lname.getValue());
                            }
                            if (!birthdateUser.equals(user.getDateOfBirth())){
                                snapshot.getRef().child("dateOfBirth").setValue(birthdate.getValue());
                            }
                            if (!imageURLUser.equals(user.getImageId())){

                                uploadImage(targetUri , fileEx);

                                Log.d(TAG, "onDataChange: "+imageURLUser);

                                if (!imageURLUser.equals("null")){
                                      StorageReference photoRef = FirebaseStorage.getInstance()
                                              .getReferenceFromUrl(imageURLUser);
                                      photoRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: deleted file"));
                                  }


                            }
                            call.onSucessCall(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void uploadImage(Uri uri , String fileEx) {
        if(uri != null)
        {
            storageReference = FirebaseStorage.getInstance().getReference()
                    .child("ProfilePictures/"+System.currentTimeMillis()
                            + "." + fileEx);
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        if (auth.getCurrentUser()!= null) {
                            String id = auth.getCurrentUser().getUid();
                            Query query = FirebaseDatabase.getInstance()
                                    .getReference("User")
                                    .orderByChild("user_id")
                                    .equalTo(id);
                            ValueEventListener postListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists())
                                    {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren())

                                        {
                                                storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                                                String profileImageUrl=task.getResult().toString();
                                                Log.i(TAG,profileImageUrl);
                                                snapshot.getRef().child("imageId").setValue(profileImageUrl);

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
                    })
                    .addOnFailureListener(e -> {
                    });


        }

    }



    public interface Data{
        public void getData(String fname , String lname ,
                            String birthofdate , String location , String URL
        ,String gender) throws IOException;
    }

    public interface SuccessCall{
        public void onSucessCall(boolean isSuccess);
    }

    public interface SHowDataSuccess{
        public void onShowDataSuccess(boolean isSuccess);
    }

    public interface Image{
        public void onSetImage (boolean isImageExist);
    }


}
