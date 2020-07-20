package com.hfad.dawrlygp.views.Fragments.UserFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hfad.dawrlygp.viewModel.AccountViewModel;
import com.hfad.dawrlygp.views.Adapters.MyPostsAdapter;
import com.hfad.dawrlygp.views.ChangeMail;
import com.hfad.dawrlygp.views.ChangePassword;
import com.hfad.dawrlygp.views.EditProfile;
import com.hfad.dawrlygp.views.Helpers.DataCalled;
import com.hfad.dawrlygp.views.MainActivity;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Saved;

public class AccountFragment extends Fragment implements DataCalled {

    public static String ID = "AccountFragmentPage";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView lastName , firstName;
    private Toolbar toolbar ;
    private View view ;
    private MyPostsAdapter adapter ;
    private RecyclerView recyclerView;
    private AccountViewModel model ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, container ,false);

         model = ViewModelProviders.of(this).get(AccountViewModel.class);

         final ProgressBar progressBar = view.findViewById(R.id.progress_account);

         toolbar = view.findViewById(R.id.account_toolbar);

         recyclerView = view.findViewById(R.id.my_posts);

         progressBar.setVisibility(View.VISIBLE);

         recyclerView.setVisibility(View.GONE);

         final TextView textView = view.findViewById(R.id.warnAccount);

         model.init(this);

         model.setSuccess(new AccountViewModel.SetDataSuccess() {
             @Override
             public void setDataSuccess(boolean isSuccess) {
                 if (isSuccess){

                     Log.v("AccFragment","Success");

                     textView.setVisibility(View.GONE);

                     getData();


                 }
                 else {
                     Log.v("AccFragment","False");

                     recyclerView.setVisibility(View.GONE);

                     textView.setVisibility(View.VISIBLE);

                 }
                 recyclerView.setVisibility(View.VISIBLE);

                  progressBar.setVisibility(View.GONE);

             }
         });

         final SwipeRefreshLayout swipeAccount = view.findViewById(R.id.swipeAccount);

        swipeAccount.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.getData().getValue().clear();

                model.init(AccountFragment.this);

                model.setSuccess(new AccountViewModel.SetDataSuccess() {
                    @Override
                    public void setDataSuccess(boolean isSuccess) {
                        if (isSuccess){
                            Log.v("AccFragment","Success");

                            progressBar.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                           textView.setVisibility(View.GONE);

                            getData();

                        }
                        else {
                            Log.v("AccFragment","False");

                            progressBar.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);

                            textView.setText("you haven't posted yet");
                        }
                    }
                });
                data();
              swipeAccount.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeAccount.setRefreshing(false);
                    }
                },3000);
            }
        });



        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");

        data();


        return view ;
    }

    public  void data() {
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        firstName =  headerView.findViewById(R.id.text_fname);
        lastName =  headerView.findViewById(R.id.text_lname);
        model.setNames(new AccountViewModel.Names() {
            @Override
            public void getNames(String fname, String lname, String image) {
                Log.v("Names",""+fname+" "+lname);
                firstName.setText(fname+" ");
                lastName.setText(lname);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar,
                R.string.Open,
                R.string.Close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                drawerLayout.closeDrawers();

                int id = item.getItemId();

                switch (id) {

                    case R.id.editProfile:
                        Intent intent1 = new Intent(view.getContext(), EditProfile.class);
                        startActivity(intent1);
                        break;
                    case R.id.changemail:
                        Intent intent2 = new Intent(view.getContext(), ChangeMail.class);
                        startActivity(intent2);
                        break;
                    case R.id.chnagepassword:
                        Intent intent3 = new Intent(view.getContext(), ChangePassword.class);
                        startActivity(intent3);
                        break;
                    case R.id.savedmenu:
                        Intent intent4 = new Intent(view.getContext(), Saved.class);
                        startActivity(intent4);
                        break;
                    case R.id.logout:
                        if (model.logout()){
                            Intent intent = new Intent(view.getContext(),MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        break;



                }
                return true;

            }
        });

    }

    public void  getData(){

        adapter = new MyPostsAdapter(model.getData().getValue(), view.getContext(),ID ,model);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),
                1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();


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


