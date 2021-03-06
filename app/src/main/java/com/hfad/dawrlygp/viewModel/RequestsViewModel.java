package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.RequestsRepository;
import com.hfad.dawrlygp.views.Fragments.AdminFragment.RequestsFragment;

import java.util.List;

public class RequestsViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private RequestsRepository repoistory ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(RequestsFragment context){
        repoistory = RequestsRepository.getInstance(context);
        data = repoistory.getData();
        repoistory.setSuccess(new RequestsRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {

                success.onSuccess(isSuccess);

            }
        });

    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface Success{
        public void onSuccess(boolean isSuccess);
    }

}
