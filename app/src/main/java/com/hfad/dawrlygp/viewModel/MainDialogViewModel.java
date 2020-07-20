package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.PostItems;
import com.hfad.dawrlygp.repository.MainDialogRepository;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;

import java.util.List;

public class MainDialogViewModel extends ViewModel {

    private MutableLiveData<List<PostItems>> data = new MutableLiveData<>();

    private MainDialogRepository  repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(MainDialogFragment context){

        repository = MainDialogRepository.getInstance(context);
        data = repository.getData();
        repository.setSuccess(new MainDialogRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                   success.onSuccess(true);

                }
            }
        });
    }

    public LiveData<List<PostItems>> getData() {
        return data;
    }
    public interface Success{
        public void onSuccess(boolean isSuccess);
    }
}
