package com.hfad.dawrlygp.views.Fragments.UserFragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hfad.dawrlygp.views.Adapters.ViewPagerAdapter;
import com.hfad.dawrlygp.views.Fragments.MainFragment.LoginFragment;
import com.hfad.dawrlygp.views.Fragments.TabFragments.AllFragment;
import com.hfad.dawrlygp.views.Fragments.TabFragments.FoundFragment;
import com.hfad.dawrlygp.views.Fragments.TabFragments.LostFragment;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Home;

public class HomeFragment extends Fragment {

    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private View view ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view ;
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view !=null){
            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
            viewPager =  view.findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);


        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AllFragment(), "All"  );
        adapter.addFragment(new LostFragment(), "Lost");
        adapter.addFragment(new FoundFragment(), "Found");
        viewPager.setAdapter(adapter);

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

}
