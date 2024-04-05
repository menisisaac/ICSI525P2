package com.example.csi525p2;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class WifiIntent extends IntentService {

    public WifiIntent() {
        super("WifiCollection");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WifiManager wifi = (WifiManager) getSystemService(WifiManager.class);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details;
            return;
        }
        for(ScanResult scanResult : wifi.getScanResults()) {
            System.out.println(scanResult);
        }
    }
}
