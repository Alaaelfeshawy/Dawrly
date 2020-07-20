package com.hfad.dawrlygp.views;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.databinding.ActivityHomeBinding;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.HomeViewModel;
import com.hfad.dawrlygp.views.Fragments.UserFragment.ChatFragment;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity{

    private BottomNavigationView navigation;

    private NavController navController;

    private HomeViewModel model ;

    private ArrayList<Posts> postsInfo = new ArrayList<>() ;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        model = ViewModelProviders.of(this).get(HomeViewModel.class);

        Bundle bundle = getIntent().getExtras();

         int id = bundle.getInt("Id");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Type_Id",id);
        Log.v("Type_Id","1 -- "+id);
        editor.commit();

        navigation = findViewById(R.id.bottom_nav);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_home);

        //coordinatorLayout = findViewById(R.id.container);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navController.navigate(R.id.homeFragment);
                        return true;
                    case R.id.navigation_myAccount:
                        navController.navigate(R.id.accountFragment);
                        return true;
                    case R.id.navigation_chat:
                        navController.navigate(R.id.chatFragment );
                        return true;
                    case R.id.navigation_search:
                        navController.navigate(R.id.searchFragment);
                        return true;
                    case R.id.navigation_notifications:
                        navController.navigate(R.id.notificationFragment);
                        return true;
                }
                return false;
            }
        });

        //Hide the Bottom Navigation when the page is scrolled
        /*CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navigation.getLayoutParams();
        layoutParams.setBehavior(new ScrollHandler());*/

        model.getSenderName();

        model.setName(new HomeViewModel.Name() {
            @Override
            public void getNames(List<Posts> items) {

                Log.v("HomeActivity","Final Position "+items.size());

                addNotification(items , items.size()-1 );

                setUpBadge(items.size(),1);

            }
        });

        model.getNotificationForMessages();

        model.setNumber(new HomeViewModel.Number() {
            @Override
            public void getNumber(int count) {

                addNotification();


                setUpBadge(count , 0);
            }
        });





    }

    private void  setUpBadge(long num , int childNum){
        int [] child = {2,3};
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
            View v = bottomNavigationMenuView.getChildAt(child[childNum]);
            final BottomNavigationItemView itemView = (BottomNavigationItemView) v;
            final View badge = LayoutInflater.from(this)
                    .inflate(R.layout.notification_icon, bottomNavigationMenuView, false);
            final TextView tv = badge.findViewById(R.id.notification_badge);
            if (child[childNum]==2){
                tv.setText(""+ num);
                itemView.addView(badge);
            }
            if (child[childNum]==3){

                if (num>0){
                    tv.setText(""+num);
                    itemView.addView(badge);
                }
                if (num == 0){
                    tv.setVisibility(View.GONE);
                }
                if (num>100){
                    tv.setText("+99");
                    itemView.addView(badge);
                }


            }




    }

    private void addNotification(List<Posts> items , int i ) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.chat) //set icon for notification
                        .setContentTitle("Confirm") //set title of notification
                        .setContentText("Admin has approved your request to add this post")//this is notification message
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
        Intent notificationIntent = new Intent(this, PostDetails.class);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String PageId = "Home" ;

        postsInfo.add(items.get(i));

        notificationIntent.putParcelableArrayListExtra("LIST", postsInfo);

        notificationIntent.putExtra("PageId",PageId);

        notificationIntent.putExtras(notificationIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i, mBuilder.build());
    }

    private void addNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Check messages")
                        .setSmallIcon(R.drawable.chat) //set icon for notification
                        .setContentText("You maybe have some new messages check it")//this is notification message
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 , mBuilder.build());
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

}

