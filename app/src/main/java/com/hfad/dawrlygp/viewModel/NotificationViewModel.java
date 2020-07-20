package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.model.User;
import com.hfad.dawrlygp.repository.NotificationRepository;
import com.hfad.dawrlygp.repository.RequestsRepository;
import com.hfad.dawrlygp.views.Fragments.AdminFragment.RequestsFragment;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.AdminNotificationTab;
import com.hfad.dawrlygp.views.Fragments.UserFragment.NotificationFragment;

import java.util.List;

public class NotificationViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private NotificationRepository repoistory ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public  void init(AdminNotificationTab context){
        if (data != null){

        }
        repoistory = NotificationRepository.getInstance(context);
        data = repoistory.getData();
        repoistory.setSuccess(new NotificationRepository.Success() {
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
