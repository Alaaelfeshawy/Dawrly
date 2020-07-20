package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.ReportRepository;
import com.hfad.dawrlygp.views.Fragments.AdminFragment.ReportFragment;
import com.hfad.dawrlygp.views.Fragments.AdminFragment.RequestsFragment;

import java.util.List;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private ReportRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(ReportFragment context){
        repository = ReportRepository.getInstance(context);
        data = repository.getData();
        repository.setSuccess(new ReportRepository.Success() {
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
