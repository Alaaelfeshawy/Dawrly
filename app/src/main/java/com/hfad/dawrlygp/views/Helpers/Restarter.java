package com.hfad.dawrlygp.views.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent = new Intent(context, SimilarityService.class);
            ContextCompat.startForegroundService(context,serviceIntent);
        } else {
            Intent serviceIntent = new Intent(context, SimilarityService.class);
            ContextCompat.startForegroundService(context , serviceIntent);
        }
    }
}