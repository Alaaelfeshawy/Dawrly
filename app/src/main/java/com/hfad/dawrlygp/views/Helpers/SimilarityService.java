package com.hfad.dawrlygp.views.Helpers;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.dawrlygp.model.Constant;
import com.hfad.dawrlygp.model.SimilarityTable;
import com.hfad.dawrlygp.viewModel.MainViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class SimilarityService extends Service {
    private static final String TAG = "MainActivityUser";

    private DatabaseReference ref ;

    private FirebaseAuth auth ;

    public static boolean isServiceRunning = false;

    private String currentUserId;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getFinalSim();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            getFinalSim();


        return START_STICKY;
    }

    public void getFinalSim(){
        if (isServiceRunning) return;
        isServiceRunning = true;
        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference("SimilarityResults");

        if (auth.getCurrentUser() !=null) {

            currentUserId = auth.getCurrentUser().getUid();
        }

        MainViewModel model = new MainViewModel();

        model.init();


        Log.d(TAG, "onStartCommand: ");
        try {
            Handler handler = new Handler();

            int delay = 1000;

            handler.postDelayed(new Runnable(){
                public void run(){
                    if(!model.getData1())
                    {
                        model.setListMove(similarityTables -> {

                            Log.d(TAG, "getList: "+similarityTables.size());

                            getSim(similarityTables);

                        });


                    }
                    else
                        handler.postDelayed(this, delay);
                }
            }, delay);
        }

        catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }
    public void getSim(List<SimilarityTable> similarityTables){

        if (similarityTables.isEmpty()){

            ref.child(currentUserId).removeValue();
        }

        for (int i = 0; i<similarityTables.size() ; i++) {

            double similarity = Similarity.similarity(similarityTables.get(i).getDesc1()
                    , similarityTables.get(i).getDesc2());

            String text1Space = similarityTables.get(i).getDesc1().replaceAll("\\s+", "");

            String text2Space = similarityTables.get(i).getDesc2().replaceAll("\\s+", "");

            int length1 = text1Space.length();

            int length2 = text2Space.length();

            if (length1 <= 5 && length2 <= 5) {

                Log.d(TAG, "onCreate: PLEASE you should write sentence !");
            }
            else {

                Log.d(TAG, "onCreate: Similarity is " + similarity);

                String id = ref.push().getKey();

                similarityTables.get(i).setSimId(id);

                DecimalFormat df = new DecimalFormat("####0.00");

                similarityTables.get(i).setSimilarity(Double.valueOf(df.format(similarity)));

                ref.child(currentUserId).removeValue();

                ref.child(currentUserId).setValue(similarityTables);


            }
        }


    }



}
