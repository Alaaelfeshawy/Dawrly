package com.hfad.dawrlygp.views.Fragments.MainFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.hfad.dawrlygp.R;


public class ChooserFragment extends Fragment  {
    private NavController navController ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //TODO add animation here with logo and progress bar to load data

        Button found , lost ;
        found = view.findViewById(R.id.foundBt);
        lost = view.findViewById(R.id.lostBt);

        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Id",1);

                navController.navigate(R.id.action_chooserFragment2_to_home2,bundle);
                getActivity().finish();



            }
        });
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Id",2);
                navController.navigate(R.id.action_chooserFragment2_to_home2,bundle);
                getActivity().finish();


            }
        });
    }



}
