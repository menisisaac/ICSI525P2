package com.example.csi525p2;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CellularAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CellularAlarmReceiver", "Alarm received, starting service...");
        Intent serviceIntent = new Intent(context, CellularIntentService.class);
        serviceIntent.putExtra("action", "start_scan");
        context.startService(serviceIntent);
    }
}

