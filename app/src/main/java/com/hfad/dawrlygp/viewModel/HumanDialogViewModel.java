package com.hfad.dawrlygp.viewModel;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.HumanPosts;

public class HumanDialogViewModel extends ViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> time = new MutableLiveData<>();
    public MutableLiveData<String> age = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    public MutableLiveData<String> image = new MutableLiveData<>();
    private String gender ;
    private String checkedRadio ;

    private HumanPosts posts ;

    private MutableLiveData<HumanPosts> data ;

    public MutableLiveData<HumanPosts> getData(){
        if (data == null){
            data = new MutableLiveData<>();
        }
        return data ;
    }

    public  void onClickNext(View view){
        posts = new HumanPosts();
        SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", 0);
        // 0 - for private mode
        int id = pref.getInt("Type_Id",-1);

        Log.v("Type_Id","2-- "+id);

        posts.setName(name.getValue());
        try {
            posts.setAge(Integer.parseInt(age.getValue()));

        }catch (Exception e){
            e.getMessage();
        }
        posts.setDescription(description.getValue());
        posts.setGender(gender);
        posts.setTypeId(id);
        posts.setTime(time.getValue());
        posts.setLocation(location.getValue());
        posts.setImageUrl(image.getValue());
        posts.setCommunicationByMob(checkedRadio);
        data.setValue(posts);


    }

    public void  onRadioButtonClicked(View view){
        posts = new HumanPosts();
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioMaleHuman:
                if (checked)
                    gender = "Male" ;
                break;
            case R.id.radioFemaleHuman:
                if (checked)
                    gender ="Female";
                    break;
        }






    }

    public void  onRadioButtonClickedPhoneNumber(View view)
    {
        posts = new HumanPosts();
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioYes:
                if (checked)
                    checkedRadio = "true" ;
                break;
            case R.id.radioNo:
                if (checked)
                    checkedRadio = "false" ;
                break;
        }






    }





}
