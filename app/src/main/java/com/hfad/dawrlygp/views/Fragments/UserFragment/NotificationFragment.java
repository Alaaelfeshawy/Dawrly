package com.hfad.dawrlygp.views.Fragments.UserFragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.viewModel.NotificationViewModel;
import com.hfad.dawrlygp.views.Adapters.NotificationAdapter;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.views.Adapters.ViewPagerAdapter;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.AdminNotificationTab;
import com.hfad.dawrlygp.views.Fragments.NotificationTabs.SimilarityNotificationTab;
import com.hfad.dawrlygp.views.Fragments.TabFragments.AllFragment;
import com.hfad.dawrlygp.views.Fragments.TabFragments.FoundFragment;
import com.hfad.dawrlygp.views.Fragments.TabFragments.LostFragment;
import com.hfad.dawrlygp.views.Helpers.DataCalled;
import com.hfad.dawrlygp.views.Helpers.myBadgeView;

public class NotificationFragment extends Fragment {
    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private View view ;

    private DatabaseReference ref ;

    private FirebaseAuth auth ;

    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view ;
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view !=null){
            auth = FirebaseAuth.getInstance();

            ref = FirebaseDatabase.getInstance().getReference("SimilarityResults");

            if (auth.getCurrentUser() !=null) {

                currentUserId = auth.getCurrentUser().getUid();
            }

            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");
            viewPager =  view.findViewById(R.id.notificationViewPager);
            setupViewPager(viewPager);
            tabLayout = view.findViewById(R.id.notificationTab);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.getTabAt(1).setCustomView(R.layout.notify);

            TextView textView = (TextView) tabLayout.getTabAt(1).getCustomView()
                    .findViewById(R.id.notification_badge);

            ref.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){


                        textView.setText(dataSnapshot.getChildrenCount()+"");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }






        }


    private Drawable buildCounterDrawable(int count) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.notify, null);
       // view.setBackgroundResource(R.drawable.circlar_background);

        TextView textView = (TextView) view.findViewById(R.id.notification_badge);

        if (count == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(count));
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }



    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AdminNotificationTab(), "Approved Notify"  );
        adapter.addFragment(new SimilarityNotificationTab(), "Similarity Notify");
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
