package com.hfad.dawrlygp.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Options;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.repository.SearchRepository;
import com.hfad.dawrlygp.views.Fragments.UserFragment.SearchFragment;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Posts>> data = new MediatorLiveData<>();

    private MutableLiveData<List<Options>> category = new MediatorLiveData<>();

    private MutableLiveData<List<Location>> location = new MediatorLiveData<>();

    private MutableLiveData<List<Options>> date = new MediatorLiveData<>();

    private SearchRepository repository ;

    private Success success ;

    public void setSuccess(Success success) {
        this.success = success;
    }

    public void init(SearchFragment fragment) {

        repository = SearchRepository.getInstance(fragment);

        data = repository.getData();

        repository.setSuccess(new SearchRepository.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                success.Success(isSuccess);
            }
        });


    }

    public void initOptions(SearchFragment fragment) {

        repository = SearchRepository.getInstance(fragment);

        category = repository.getOptions();


    }

    public void initLocation(SearchFragment fragment) {

        repository = SearchRepository.getInstance(fragment);

        location = repository.getLocation();

       /* repository.setSuccessList(new SearchRepository.SuccessList() {
            @Override
            public void isSuccess(boolean isSuccess) {

                successList.isSuccess(isSuccess);
            }
        });*/

    }

    public void initDate(SearchFragment fragment) {

        repository = SearchRepository.getInstance(fragment);

        date = repository.getDate();

    }

    public MutableLiveData<List<Options>> getDate() {
        return date;
    }

    public MutableLiveData<List<Location>> getLocation() {
        return location;
    }

    public MutableLiveData<List<Options>> getCategory() {
        return category;
    }

    public LiveData<List<Posts>> getData() {
        return data;
    }

    public interface  Success{
        public void Success(boolean isSuccess);
    }

}
