package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.PostsFoundRepository;
import com.hfad.dawrlygp.views.Fragments.TabFragments.FoundFragment;

import java.util.List;

public class FoundViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> listItems = new MutableLiveData<>();

    private PostsFoundRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(FoundFragment foundFragment){

        repository =  PostsFoundRepository.getInstance(foundFragment);

        listItems = repository.getItemsList();

        repository.setSuccess(new PostsFoundRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                success.onSuccess(isSuccess);
            }
        });

    }



    public LiveData<List<Posts>> getListItems() {
        return listItems;
    }

    public interface Success{
        public void onSuccess(boolean isSuccess);
    }
}
