package com.hfad.dawrlygp.views.Fragments.NotificationTabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.viewModel.NotificationViewModel;
import com.hfad.dawrlygp.views.Adapters.NotificationAdapter;
import com.hfad.dawrlygp.views.Helpers.DataCalled;


public class AdminNotificationTab extends Fragment  implements DataCalled {

    private NotificationAdapter notificationAdapter ;

    private RecyclerView recyclerView ;

    private View view ;

    private NotificationViewModel model ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_notification_tab, container,
                false);

        recyclerView = view.findViewById(R.id.notification_view);

        final ProgressBar progressBar = view.findViewById(R.id.progNotify);

        progressBar.setVisibility(View.VISIBLE);

        model = ViewModelProviders.of(this).get(NotificationViewModel.class);

        model.init(this);

        model.setSuccess(isSuccess -> {
            if (isSuccess){
                getAdapter();
            }
            progressBar.setVisibility(View.GONE);

        });

        return view;
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

    private void getAdapter(){
        notificationAdapter = new NotificationAdapter(model.getData().getValue() ,view.getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onDataCalled() {

    }
}