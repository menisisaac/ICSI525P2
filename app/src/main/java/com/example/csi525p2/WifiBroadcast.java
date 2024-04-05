package com.example.csi525p2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {

        }
    };

}
