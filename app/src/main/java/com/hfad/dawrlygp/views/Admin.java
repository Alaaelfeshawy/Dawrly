package com.hfad.dawrlygp.views;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.Posts;
import com.hfad.dawrlygp.viewModel.AdminViewModel;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {

    private   DrawerLayout drawerLayout ;

    private NavigationView navigationView;

    private AdminViewModel model ;

    private ArrayList<Posts> postsInfo = new ArrayList<>() ;

    private static final String TAG = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        model = ViewModelProviders.of(this).get(AdminViewModel.class);
        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.navigationViewAdmin);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        TextView firstName =  headerView.findViewById(R.id.text_fname);
        TextView lastName =  headerView.findViewById(R.id.text_lname);
        firstName.setText("Admin ");
        lastName.setText("Dashboard");
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                drawerLayout.closeDrawers();

                int id = item.getItemId();

                switch (id){
                    case R.id.navigation_home_admin:
                        navController.navigate(R.id.adminHomeFragment);
                        break;
                    case R.id.navigation_request:
                        navController.navigate(R.id.requestsFragment);
                        break;

                    case R.id.navigation_report:
                        navController.navigate(R.id.reportFragment);
                        break;
                    case R.id.admin_logout:
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent = new Intent(Admin.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }

                return false;
            }
        });

        model.getNumOfNotifications();

        model.setNotificationNumber(new AdminViewModel.NotificationNumber() {
            @Override
            public void getNotificationNumber(long notificationNum) {
                Log.d(TAG, "getNotificationNumber: "+notificationNum);
                setNavItemCount(R.id.navigation_request,notificationNum);
            }
        });
        model.getSenderName();

        model.setName(new AdminViewModel.Name() {
            @Override
            public void getNames(List<Posts> items ) {

                Log.v("Names","Final Position "+items.size());

                addNotification(items , items.size()-1);

            }
        });
        setNavItemCount(R.id.navigation_report,0);

    }

    private void setNavItemCount(@IdRes int itemId, long count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        if (count>=100){
            view.setText("+99");
        }
        else if (count==0){
            view.setVisibility(View.GONE);

        }
        else {
            view.setText( String.valueOf(count));

        }

    }

    private void addNotification(List<Posts> items , int i ) {
          NotificationCompat.Builder mBuilder =
                  new NotificationCompat.Builder(this)
                          .setSmallIcon(R.drawable.chat) //set icon for notification
                          .setContentTitle("Request") //set title of notification
                          .setContentText(items.get(i).getFname()+" "+ items.get(i).getLname()+" request to add post")//this is notification message
                          .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
            Intent notificationIntent = new Intent(this, PostDetails.class);

            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            String PageId = "Request" ;

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

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_admin), drawerLayout);
    }

}
