package com.hfad.dawrlygp.views;



import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hfad.dawrlygp.databinding.ActivitySavedBinding;
import com.hfad.dawrlygp.viewModel.SavedViewModel;
import com.hfad.dawrlygp.views.Adapters.MyPostsAdapter;
import com.hfad.dawrlygp.views.Fragments.DialogFragments.MainDialogFragment;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Helpers.DataCalled;

public class Saved extends AppCompatActivity implements DataCalled {

    public static String ID = "SavedPage";

    private Toolbar toolbar;

    private MyPostsAdapter adapter;

    private DialogFragment newFragment ;

    private FragmentManager fragmentManager;

    private SavedViewModel model ;

    private ActivitySavedBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved);

        binding.savedToolbar.setTitle("Saved Posts");

        toolbar = findViewById(R.id.saved_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.scrollViewPosts.setVisibility(View.GONE);

        binding.proBarPosts.setVisibility(View.VISIBLE);

        model = ViewModelProviders.of(this).get(SavedViewModel.class);

        getInit();

        data();

        /*  binding.swipeRefresh.setOnRefreshListener(() -> {

            model.getData().getValue().clear();

            getInit();

            data();

            binding.swipeRefresh.postDelayed(new Runnable() {
               @Override
               public void run() {
                   binding.swipeRefresh.setRefreshing(false);
               }

           },3000);
        });*/

        button();


    }
    public void data(){
        adapter = new MyPostsAdapter(model.getData().getValue(), Saved.this,ID, model);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Saved.this,
                1);
        binding.scrollViewPosts.setLayoutManager(mLayoutManager);
        binding.scrollViewPosts.setItemAnimator(new DefaultItemAnimator());
        binding.scrollViewPosts.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void button(){

        binding.scrollViewPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.floatbuttonSave.getVisibility() == View.VISIBLE)
                {
                    binding.floatbuttonSave.hide();
                }
                else if (dy < 0 && binding.floatbuttonSave.getVisibility() != View.VISIBLE)
                {
                    binding.floatbuttonSave.show();
                }
            }
        });

        binding.floatbuttonSave.setOnClickListener(view -> {
            newFragment = new MainDialogFragment();
            fragmentManager = getSupportFragmentManager();
            newFragment.show(fragmentManager,MainDialogFragment.Title);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getInit(){

        model.init(this);

        model.setSuccess(new SavedViewModel.Success() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess){
                    binding.scrollViewPosts.setVisibility(View.VISIBLE);
                    binding.proBarPosts.setVisibility(View.GONE);
                    binding.messagePosts.setVisibility(View.GONE);
                }
                else {
                    binding.scrollViewPosts.setVisibility(View.GONE);
                    binding.proBarPosts.setVisibility(View.GONE);
                    binding.messagePosts.setText("No posts yet");
                }

            }
        });
    }

    @Override
    public void onDataCalled() {
       // adapter.notifyDataSetChanged();
    }
}
