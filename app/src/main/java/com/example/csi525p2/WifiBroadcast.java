package com.example.csi525p2;

import static android.content.Context.WIFI_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class WifiBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        on(context);
    };
    protected void on(Context context) {
        Log.d("Error", "Intent Happened");
        WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details;
            return;
        }
        Log.d("Error", "Passed it");
        wifi.startScan();
        for(ScanResult scanResult : wifi.getScanResults()) {
            Log.d("Creation", scanResult.BSSID);
        }
    }
}
