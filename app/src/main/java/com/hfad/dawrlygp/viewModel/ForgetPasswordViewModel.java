package com.hfad.dawrlygp.viewModel;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hfad.dawrlygp.model.User;

public class ForgetPasswordViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();

    private MutableLiveData<User> data ;

    private User user ;

    private FirebaseAuth auth ;

    private Boolean isSuccess ;

    private CallBack callBack ;


    public MutableLiveData<User> getData() {
        if (data == null){
            data = new MutableLiveData<>() ;
        }
        return data;
    }

    public void onClick(View view){

        user = new User();
        user.setEmail(email.getValue());

        data.setValue(user);

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void forgetPassword(String mail){
        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isSuccess = true ;
                            callBack.onCallBack(isSuccess);

                        } else {
                            isSuccess = false ;
                            callBack.onCallBack(isSuccess);
                        }
                    }
                });
    }

    public interface CallBack{
        public void onCallBack(boolean isSuccess);
    }
}
