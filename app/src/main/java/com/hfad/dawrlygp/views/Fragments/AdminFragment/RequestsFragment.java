package com.hfad.dawrlygp.views.Fragments.AdminFragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.viewModel.RequestsViewModel;
import com.hfad.dawrlygp.views.Adapters.RequestAdapter;
import com.hfad.dawrlygp.views.Helpers.DataCalled;


public class RequestsFragment extends Fragment implements DataCalled {

    private View view ;

    private RequestsViewModel model ;

    private RecyclerView recyclerView ;

    private RequestAdapter adapter ;

    private TextView textView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_requests, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (view!=null){
            model = ViewModelProviders.of(this).get(RequestsViewModel.class);
            model.init(this);
            recyclerView = view.findViewById(R.id.request);
            final ProgressBar progressBar = view.findViewById(R.id.progressRequest);
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            textView = view.findViewById(R.id.warning);
            model.setSuccess(new RequestsViewModel.Success() {
                @Override
                public void onSuccess(boolean isSuccess) {
                    if (isSuccess){
                        Log.v("Request","True");
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        data();

                    }
                    else {
                        Log.v("Request","False");
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("request to add this post");
                    }
                }
            });


        }
    }

    public void data(){

            adapter = new RequestAdapter(getContext(),model.getData().getValue(),2);

             RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),
                            1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter.notifyDataSetChanged();

    }

    @Override
    public void onDataCalled() {
       //adapter.notifyDataSetChanged();
    }
}
