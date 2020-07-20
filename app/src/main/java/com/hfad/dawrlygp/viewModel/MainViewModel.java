package com.hfad.dawrlygp.viewModel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.hfad.dawrlygp.model.SimilarityTable;
import com.hfad.dawrlygp.repository.MainRepository;
import android.os.Handler;



import java.util.List;

public class MainViewModel {

    private static final String TAG = "MainViewModel";

    private MutableLiveData<List<SimilarityTable>> data = new MediatorLiveData<>();

    private MainRepository repository ;

    ListMove listMove ;

    public void setListMove(ListMove listMove) {
        this.listMove = listMove;
    }

    public void init(){

        repository = MainRepository.getInstance();

        data = repository.getData();



    }

    public boolean getData1(){
        Handler handler = new Handler();

        int delay = 9000;

        handler.postDelayed(new Runnable(){
            public void run(){
                repository.getListFinalList();
                repository.setListInterface(new MainRepository.ListInterface() {
                    @Override
                    public void getList(List<SimilarityTable> similarityTables) {

                        listMove.getList(similarityTables);

                        Log.d(TAG, "getList: "+similarityTables.size()+" "+similarityTables.toString());
                    }
                });

            }
        }, delay);

        return false ;

    }

    public LiveData<List<SimilarityTable>> getData() {
        return data;
    }

    public interface ListMove{
        public void getList(List<SimilarityTable> similarityTables);
    }

}

