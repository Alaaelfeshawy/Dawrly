package com.hfad.dawrlygp.views;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.dawrlygp.R;
import com.hfad.dawrlygp.model.Constant;
import com.hfad.dawrlygp.model.Notification;
import com.hfad.dawrlygp.model.SimilarityTable;
import com.hfad.dawrlygp.viewModel.MainViewModel;
import com.hfad.dawrlygp.views.Helpers.Similarity;
import com.hfad.dawrlygp.views.Helpers.SimilarityService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference ref ;

    private FirebaseAuth auth ;

    private String currentUserId;

    private static final String TAG = "MainActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_main);

        navGraph.setStartDestination(R.id.loginfragment);

        navController.setGraph(navGraph);

       // startService(new Intent(this, SimilarityService.class));


        getFinalSim();

    }

    public void getFinalSim(){
        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference("SimilarityResults");

        if (auth.getCurrentUser() !=null) {

            currentUserId = auth.getCurrentUser().getUid();
        }

        MainViewModel model = new MainViewModel();

        model.init();


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


    public void getSim(List<SimilarityTable> similarityTables){

        if (similarityTables.isEmpty()){

            if (currentUserId !=null){
                ref.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            ref.child(currentUserId).removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        }
        double similarityTitle , similarityColor , similarityModelName , finalSimilarity = 0 , imageSimilarity = 0;

        for (int i = 0; i<similarityTables.size() ; i++) {

            double similarityDesc = Similarity.similarity(similarityTables.get(i).getDesc1()
                    , similarityTables.get(i).getDesc2());
            double similarityDate = Similarity.similarity(similarityTables.get(i).getDesc1()
                    , similarityTables.get(i).getDesc2());

            if (similarityTables.get(i).getPostItemsId() == 1){
                similarityTitle = Similarity.similarity(similarityTables.get(i).getTitleName1()
                        , similarityTables.get(i).getTitleName2());
                similarityModelName = Similarity.similarity(similarityTables.get(i).getModelName1()
                        , similarityTables.get(i).getModelName2());
                similarityColor = Similarity.similarity(similarityTables.get(i).getColor1()
                        , similarityTables.get(i).getColor2());

                finalSimilarity = (similarityDesc+similarityDate+similarityTitle+similarityModelName
                        +similarityColor)/5;

            }
            if (similarityTables.get(i).getPostItemsId() == 2){
                similarityColor = Similarity.similarity(similarityTables.get(i).getColor1()
                        , similarityTables.get(i).getColor2());

                finalSimilarity = (similarityDesc+similarityDate+similarityColor)/3;
            }
            if (similarityTables.get(i).getPostItemsId() >=3 && similarityTables.get(i).getPostItemsId() <=6){
                similarityTitle = Similarity.similarity(similarityTables.get(i).getTitleName1()
                        , similarityTables.get(i).getTitleName2());
                similarityColor = Similarity.similarity(similarityTables.get(i).getColor1()
                        , similarityTables.get(i).getColor2());

                finalSimilarity = (similarityDesc+similarityDate+similarityTitle+similarityColor)/4;

            }
            if (similarityTables.get(i).getPostItemsId() == 7 || similarityTables.get(i).getPostItemsId() == 8){
                similarityTitle = Similarity.similarity(similarityTables.get(i).getTitleName1()
                        , similarityTables.get(i).getTitleName2());

                finalSimilarity = (similarityDesc+similarityDate+similarityTitle)/3;
            }

            if (similarityTables.get(i).getPostItemsId() == 9){
                similarityTitle = Similarity.similarity(similarityTables.get(i).getTitleName1()
                        , similarityTables.get(i).getTitleName2());
                finalSimilarity = (similarityTitle + similarityDate + similarityDesc)/3;
            }

            if (similarityTables.get(i).getImageURL1() != null && similarityTables.get(i).getImageURL2() != null ){
                //TODO 7ot l function l htdelha l sewr de zy l text imageSimilarity = function(hena l sewr);
            }
                Log.d(TAG, "onCreate: Similarity is " + finalSimilarity);

                String id = ref.push().getKey();

                similarityTables.get(i).setSimId(id);

                DecimalFormat df = new DecimalFormat("####0.00");

                similarityTables.get(i).setSimilarity(Double.valueOf(df.format(finalSimilarity)));

               similarityTables.get(i).setImageSimilarity(Double.valueOf(df.format(imageSimilarity)));

                ref.child(currentUserId).removeValue();

                ref.child(currentUserId).setValue(similarityTables);


            }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        getFinalSim();
    }

}
