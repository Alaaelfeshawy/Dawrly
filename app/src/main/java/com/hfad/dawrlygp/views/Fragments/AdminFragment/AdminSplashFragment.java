package com.hfad.dawrlygp.views.Fragments.AdminFragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hfad.dawrlygp.R;

public class AdminSplashFragment extends Fragment {


  private View view;

  private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_splash, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (view !=null){
           new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    navController.navigate(R.id.action_adminSplashFragment_to_admin);
                    getActivity().finish();
                }
            }, 5000);

        }

        }

    }

