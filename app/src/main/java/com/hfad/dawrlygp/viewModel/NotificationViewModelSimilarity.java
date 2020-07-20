package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.NotificationRepository;
import com.hfad.dawrlygp.repository.NotificationSimilarityRepository;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.AdminNotificationTab;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.SimilarityNotificationTab;

import java.util.List;

public class NotificationViewModelSimilarity extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private NotificationSimilarityRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public  void init(SimilarityNotificationTab context){
        if (data != null){

        }
        repository = NotificationSimilarityRepository.getInstance(context);
        data = repository.getData();
        repository.setSuccess(isSuccess -> success.onSuccess(isSuccess));
    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface Success{
        public void onSuccess(boolean isSuccess);
    }

}
