package com.hfad.dawrlygp.views.Fragments.AdminFragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.viewModel.AdminHomeViewModel;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class AdminHomeFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private View view ;

    private AdminHomeViewModel model;

    private TextView textFound , textRequest , textApproved , textReport ;

    private ProgressBar progressBar ;

    private LinearLayout linearLayout ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        textApproved = view.findViewById(R.id.approvedNum);

        textFound = view.findViewById(R.id.foundNum);

        textReport = view.findViewById(R.id.reportNum);

        textRequest = view.findViewById(R.id.requestNum);

        progressBar = view.findViewById(R.id.progressBarAdmin);

        linearLayout = view.findViewById(R.id.totalLinear);

        linearLayout.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

        model = ViewModelProviders.of(this).get(AdminHomeViewModel.class);

        model.getCount();

        model.setCountFun((countApp, countReq, countFound, countReport) -> getResults(countApp,countFound,countReq , countReport));

        return  view;
    }

    private void getResults(long countApp , long countFound , long countReq , long countRepo)
    {
        linearLayout.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);

        textRequest.setText(countReq+"");

        textFound.setText(countFound+"");

        textReport.setText(countRepo+"");

        textApproved.setText(countApp+"");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
}
