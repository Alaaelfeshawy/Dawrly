package com.hfad.dawrlygp.views.Fragments.UserFragment;



import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.hfad.dawrlygp.model.Location;
import com.hfad.dawrlygp.model.Options;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.SearchViewModel;
import com.hfad.dawrlygp.views.Adapters.SearchAdapter;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment implements DataCalled {

   private SearchAdapter adapter;

   private Spinner category , location , date ;

   private RecyclerView recyclerView;

   private SearchViewModel model ;

   private View view ;

   private ArrayAdapter aa ;

   private ProgressBar progressBar ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_search, container, false);

        setHasOptionsMenu(true);

        return view ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(SearchViewModel.class);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_search);

        category = view.findViewById(R.id.category);

        location = view.findViewById(R.id.Location);

        date = view.findViewById(R.id.date_);

        Toolbar toolbar = view.findViewById(R.id.search_toolbar);

        progressBar = view.findViewById(R.id.progressSearch);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");

        progressBar.setVisibility(View.VISIBLE);

        model.init(this);

           model.setSuccess(new SearchViewModel.Success() {
                @Override
                public void Success(boolean isSuccess) {

                    progressBar.setVisibility(View.GONE);
                }
            });

        setAdapter();

        model.initLocation(this);

        model.initOptions(this);

        Button button = view.findViewById(R.id.reSet);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.init(SearchFragment.this);

                setAdapter();
            }
        });

        getDate();

    }

    public void setAdapter() {
        adapter = new SearchAdapter(model.getData().getValue(), getActivity());
        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }

    private void getDate(){

        String[] months = {"Date by month", "January", "February", "March", "April", "May", "June",
                "July", "August", "September ", "October", "November", "December"};

        aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, months);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        date.setAdapter(aa);

        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position ==0){

                    return;
                }
                else {

                    adapter.getDatePosition(position);

                    adapter.filterByAll();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void getCategory(){

        String[] arrOptions = new String[model.getCategory().getValue().size() + 1];

        for (int k = 0; k < arrOptions.length; k++) {

            if (k == 0) {
                arrOptions[k] = "Category";
            } else {

                arrOptions[k] = model.getCategory().getValue().get(k - 1).getName();
            }
        }

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrOptions);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(aa);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position ==0){
                    return;
                }
                else {

                    adapter.getCategoryPosition(position);

                    adapter.filterByAll();

                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getLocation(){

        String[] arrLocations = new String[model.getLocation().getValue().size()+ 1];

        for (int i =0 ; i<arrLocations.length ; i++){

            if (i == 0) {

                arrLocations[i] = "Location";
            }
            else {

                arrLocations[i] = getCity(model.getLocation().getValue().get(i-1).getLat()
                        ,model.getLocation().getValue().get(i-1).getLon());
            }
        }

        final List<String> arrList = new ArrayList<String>();
        int cnt= 0;
        List<String> lenList = new ArrayList<String>();
        for(int i=0;i<arrLocations.length;i++){
            for(int j=i+1;j<arrLocations.length;j++){
                if(arrLocations[i].equals(arrLocations[j])){
                    cnt+=1;
                }
            }
            if(cnt<1){
                arrList.add(arrLocations[i]);
            }
            cnt=0;
        }

        aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrList);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(aa);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position ==0){

                    return;
                }
                else {

                    adapter.getLocationCity(arrList.get(position));

                    adapter.filterByAll();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public String getCity(double lat , double lon) {

        Geocoder geocoder;

        List<Address> addresses;

        String city = null;

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            city = addresses.get(0).getLocality();


        } catch (IOException e) {

            Log.v("OptionAdapter","Exception "+e.getMessage());
        }
        return city ;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenu);

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

        model.getData().observe(this, new Observer<List<Posts>>() {
            @Override
            public void onChanged(List<Posts> posts) {

                adapter.notifyDataSetChanged();

                getDate();
            }
        });

        model.getCategory().observe(this, new Observer<List<Options>>() {
            @Override
            public void onChanged(List<Options> options) {

                 getCategory();
            }
        });

        model.getLocation().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {

                getLocation();
            }
        });

    }
}
