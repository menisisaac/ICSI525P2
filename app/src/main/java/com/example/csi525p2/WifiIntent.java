package com.example.csi525p2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class WifiIntent extends IntentService {
    WifiManager wifi;
    public WifiIntent() {
        super("WifiCollection");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Event", "Wifi Intent Sent");
    }
}
