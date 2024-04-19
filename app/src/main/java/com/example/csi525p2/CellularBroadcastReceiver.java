package com.example.csi525p2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver to handle alarms and start the CellularIntentService.
 */
public class CellularBroadcastReceiver extends BroadcastReceiver {

    /**
     * Called when the BroadcastReceiver receives an intent broadcast.
     *
     * @param context The context in which the receiver is running.
     * @param intent  The intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CellularAlarmReceiver", "Alarm received, starting service...");

        // Create an intent to start the CellularIntentService
        Intent serviceIntent = new Intent(context, CellularIntentService.class);

        // Start the CellularIntentService using the context
        context.startService(serviceIntent);
    }
}
