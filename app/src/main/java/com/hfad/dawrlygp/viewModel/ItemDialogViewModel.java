package com.hfad.dawrlygp.viewModel;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.ItemPosts;

public class ItemDialogViewModel extends ViewModel {

    public MutableLiveData<String> brandName = new MutableLiveData<>();

    public MutableLiveData<String> modelName = new MutableLiveData<>();

    public MutableLiveData<String> color = new MutableLiveData<>();

    public MutableLiveData<String> desc = new MutableLiveData<>();

    public MutableLiveData<String> time = new MutableLiveData<>();

    public MutableLiveData<String> location = new MutableLiveData<>();

    public MutableLiveData<String> image = new MutableLiveData<>();

    private MutableLiveData<ItemPosts> data ;

    private String checkedRadio ;

    public MutableLiveData<ItemPosts> getData() {
        if (data == null){
            data = new MutableLiveData<>();
        }
        return data;
    }

    public void onClick(View view){
        SharedPreferences pref = view.getContext().getSharedPreferences("MyPref", 0);
        // 0 - for private mode
        int id = pref.getInt("Type_Id",-1);

        Log.v("Type_Id","2-- "+id);

        ItemPosts posts = new ItemPosts();

        posts.setModelName(modelName.getValue());

        posts.setColor(color.getValue());

        posts.setDescription(desc.getValue());

        posts.setLocation(location.getValue());

        posts.setTypeId(id);

        posts.setImageUrl(image.getValue());

        posts.setTitle(brandName.getValue());

        posts.setTime(time.getValue());

        Log.v("MobileChecked","MobileViewModel " + checkedRadio);

        posts.setCommunicationByMob(checkedRadio);

        data.setValue(posts);
    }
    public void  onRadioButtonClickedPhoneNumber(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioYesMobile:
                if (checked)
                    checkedRadio = "true" ;
                break;
            case R.id.radioNoMobile:
                if (checked)
                    checkedRadio = "false" ;
                break;
        }






    }

}
