package com.hfad.dawrlygp.views.Fragments.TabFragments;


import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hfad.dawrlygp.viewModel.AllViewModel;
import com.hfad.dawrlygp.viewModel.FoundViewModel;
import com.hfad.dawrlygp.viewModel.LostViewModel;
import com.hfad.dawrlygp.views.Adapters.HomeAdapter;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

public class LostFragment extends Fragment implements DataCalled {

    private HomeAdapter adapter;

    private RecyclerView recyclerView;

    private CoordinatorLayout coordinatorLayout;

    private View ContentView;

    private DialogFragment newFragment;

    private FragmentManager fragmentManager;

    private LostViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ContentView =inflater.inflate(R.layout.fragment_losts_and_found, container, false);
                coordinatorLayout = ContentView.findViewById(R.id.coordinator_layout);

        viewModel = ViewModelProviders.of(getActivity()).get(LostViewModel.class);

        recyclerView = ContentView.findViewById(R.id.home_list);

        viewModel.init(this);

        viewModel.getData();

        final ProgressBar progressBar = ContentView.findViewById(R.id.progressBarHome);

        progressBar.setVisibility(View.VISIBLE);


        viewModel.setSuccess(new LostViewModel.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                    setAdapter();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = ContentView.findViewById(R.id.swipeHome);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                viewModel.getData().getValue().clear();

                viewModel.init(LostFragment.this);

                viewModel.setSuccess(new LostViewModel.Success() {
                    @Override
                    public void onSuccess(boolean isSuccess) {
                        if (isSuccess){
                            setAdapter();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }

        });

        button();

        return ContentView;
    }

    public void setAdapter() {
        adapter = new HomeAdapter(viewModel.getData().getValue(), getActivity());

        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(getActivity(), 1);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
    }

    public void button(){
        final FloatingActionButton mFloatingActionButton =
                ContentView.findViewById(R.id.floatbutton);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE)
                {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE)
                {
                    mFloatingActionButton.show();
                }
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment = new MainDialogFragment();
                fragmentManager = getFragmentManager();
                newFragment.show(fragmentManager,MainDialogFragment.Title);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ContentView != null) {
            ViewGroup parentViewGroup = (ViewGroup) ContentView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(ContentView);
            }
        }
    }

    @Override
    public void onDataCalled() {

    }
}
