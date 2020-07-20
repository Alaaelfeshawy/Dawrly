package com.hfad.dawrlygp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.HumanPosts;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.PostsAllRepository;
import com.hfad.dawrlygp.views.Fragments.TabFragments.AllFragment;

import java.util.List;

public class AllViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> listItems = new MutableLiveData<>();

    private PostsAllRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(AllFragment context){

            repository =  PostsAllRepository.getInstance(context);
            listItems = repository.getItemsList();
            repository.setSuccess(new PostsAllRepository.Success() {
                @Override
                public void onSuccess(boolean isSuccess) {
                    success.onSuccess(isSuccess);
                }
            });
    }





    public LiveData<List<Posts>> getListItems() {
        return listItems;
    }

public interface Success {
     public void onSuccess(boolean isSuccess);
}
}
