package com.hfad.dawrlygp.views.Fragments.UserFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.dawrlygp.viewModel.ChatViewModel;
import com.hfad.dawrlygp.viewModel.SearchViewModel;
import com.hfad.dawrlygp.views.Adapters.ChatAdapter;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Adapters.SearchAdapter;
import com.hfad.dawrlygp.views.Helpers.DataCalled;
import com.hfad.dawrlygp.views.Helpers.SwipeHandler.SwipeHelper;

import java.util.List;
import java.util.Objects;


public class ChatFragment extends Fragment implements DataCalled {


    private ChatAdapter adapter;

    private RecyclerView recyclerView ;

    private ChatViewModel model ;

    private View view ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_chat, container, false);

        setHasOptionsMenu(true);

        model = ViewModelProviders.of(this).get(ChatViewModel.class);

        model.init(this);

        recyclerView = view.findViewById(R.id.chat_list);

        Toolbar toolbar = view.findViewById(R.id.chat_toolbar);

        final ProgressBar progressBar = view.findViewById(R.id.progChat);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Chat");

        progressBar.setVisibility(View.VISIBLE);

        model.setSuccess(new ChatViewModel.Success() {
            @Override
            public void setSuccess(boolean isSuccess) {
                if (isSuccess){

                    setAdapter();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        return view ;
    }

    public void setAdapter() {
        adapter = new ChatAdapter(model.getData().getValue(),getContext(), model);
        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.left_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenuChat);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(view);
            }
        }
    }

    @Override
    public void onDataCalled() {

    }
}
