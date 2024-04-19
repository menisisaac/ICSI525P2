package com.example.csi525p2;

import android.Manifest;
import  android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

/**
 * MainActivity class to handle the user interface and interaction for starting
 * and stopping background services for collecting WiFi and cellular network information.
 */
public class MainActivity extends AppCompatActivity {

    // Request code for permissions
    private static final int PERMISSION_REQUEST_CODE = 100;

    // Scan interval for network data collection (1 minute)
    private static final long SCAN_INTERVAL = 60 * 1000;

    // PendingIntent for scheduling alarms
    private PendingIntent pendingIntent;

    // AlarmManager for managing alarm-based tasks
    private AlarmManager alarmManager;

    /**
     * Overrides the onCreate method to initialize the activity's UI.
     * @param savedState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.main);
    }

    /**
     * Starts WiFi network information collection by setting up an alarm using AlarmManager.
     * @param v The View object that triggered the method call.
     */
    public void startWifiCollection(View v) {
        Log.i("MainActivity", "Wifi scan started");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), WifiBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), SCAN_INTERVAL, pendingIntent);
    }

    /**
     * Starts the CellularIntentService for collecting cellular network information.
     * Uses AlarmManager to schedule service execution at regular intervals.
     * Requests necessary permissions if not already granted.
     * @param v The View object that triggered the method call.
     */
    public void startCellularIntentService(View v) {
        Log.i("MainActivity", "Cellular scan started");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (checkPermissions()) {
            Intent intent = new Intent(this, CellularBroadcastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), SCAN_INTERVAL, pendingIntent);
        } else {
            requestPermissions();
        }

    }

    /**
     * Stops WiFi network information collection by canceling the alarm.
     * @param v The View object that triggered the method call.
     */
    public void endWifiCollection(View v) {
        Log.i("MainActivity", "Wifi scan stopped");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, WifiBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Stops the CellularIntentService by stopping its corresponding service.
     * @param v The View object that triggered the method call.
     */
    public void stopCellularIntentService(View v) {
        Log.i("MainActivity", "Wifi scan stopped");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, CellularBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Checks if the required permissions are granted.
     * @return True if permissions are granted, false otherwise.
     */
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * Requests necessary permissions if they are not already granted.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSION_REQUEST_CODE);
    }

    /**
     * Overrides the onDestroy method to perform cleanup tasks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}