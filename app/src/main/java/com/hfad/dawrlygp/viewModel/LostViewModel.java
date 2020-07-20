package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.PostsLostRepository;
import com.hfad.dawrlygp.views.Fragments.TabFragments.LostFragment;

import java.util.List;

public class LostViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private PostsLostRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(LostFragment lostFragment){
        if (data != null){
        }
        repository = PostsLostRepository.getInstance(lostFragment);
        data = repository.getData();
        repository.setSuccess(new PostsLostRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                success.onSuccess(isSuccess);
            }
        });

    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface Success {
        public void onSuccess(boolean isSuccess);
    }
}
