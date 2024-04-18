package com.example.csi525p2;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.WIFI_SERVICE;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;

public class WifiBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        on(context);
    }

    ;

    protected void on(Context context) {
        Log.d("Event", "Intent Happened");
        WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
        wifi.startScan();
        WifiDBHelper wifidb = new WifiDBHelper(context);
        SQLiteDatabase db = wifidb.getWritableDatabase();
        for(ScanResult scanResult : wifi.getScanResults()) {
            ContentValues values = new ContentValues();
            values.put(WifiTable.WifiEntry.bssid, scanResult.BSSID);
            values.put(WifiTable.WifiEntry.timestamp, scanResult.timestamp);
            values.put(WifiTable.WifiEntry.latitude, location.getLatitude());
            values.put(WifiTable.WifiEntry.longitude, location.getLongitude());
            values.put(WifiTable.WifiEntry.ssid, scanResult.SSID);
            values.put(WifiTable.WifiEntry.frequency, scanResult.frequency);
            values.put(WifiTable.WifiEntry.channel_width, scanResult.channelWidth);
            values.put(WifiTable.WifiEntry.rssi_level, scanResult.level);
            long newRowId = db.insert(WifiTable.WifiEntry.WIFI_TABLE_NAME, null, values);
        }
    }
}
