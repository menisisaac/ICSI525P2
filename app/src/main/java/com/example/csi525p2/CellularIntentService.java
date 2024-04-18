package com.example.csi525p2;



import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthNr;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class CellularIntentService extends IntentService {

    private TelephonyManager telephonyManager;
    private CellularDBHelper dbHelper;

    public CellularIntentService() {
        super("CellularIntentService");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("CellularIntentService", "Intent Started");

        String action = intent.getStringExtra("action");
        if (action != null && action.equals("start_scan")) {
            Context context = getApplicationContext();
            this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            this.dbHelper = new CellularDBHelper(context);
        }

        if (telephonyManager == null) {
            Log.e("CellularIntentService", "TelephonyManager is null");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CellularIntentService", "Permission ACCESS_FINE_LOCATION not granted.");
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete("cellular_data", null, null);

        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        if (cellInfoList != null) {

            Log.i("CellularIntentService", "Analyze CellInfo");

            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoGsm) {
                    Log.i("CellularIntentService", "Analyzing CellInfoGsm");

                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    long cellId = cellInfoGsm.getCellIdentity().getCid();
                    int rssi = cellInfoGsm.getCellSignalStrength().getDbm();
                    String technology = "2G";
                    int frequency = cellInfoGsm.getCellIdentity().getArfcn();

                    saveDataToDatabase(cellId, rssi, technology, frequency);
                } else if (cellInfo instanceof CellInfoWcdma) {
                    Log.i("CellularIntentService", "Analyzing CellInfoWcdma");

                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    long cellId = cellInfoWcdma.getCellIdentity().getCid();
                    int rssi = cellInfoWcdma.getCellSignalStrength().getDbm();
                    String technology = "3G";
                    int frequency = cellInfoWcdma.getCellIdentity().getUarfcn();

                    saveDataToDatabase(cellId, rssi, technology, frequency);
                } else if (cellInfo instanceof CellInfoLte) {
                    Log.i("CellularIntentService", "Analyzing CellInfoLte");

                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    long cellId = cellInfoLte.getCellIdentity().getCi();
                    int rssi = cellInfoLte.getCellSignalStrength().getDbm();
                    String technology = "4G";
                    int frequency = cellInfoLte.getCellIdentity().getEarfcn();

                    saveDataToDatabase(cellId, rssi, technology, frequency);
                } else if (cellInfo instanceof CellInfoNr) {
                    Log.i("CellularIntentService", "Analyzing CellInfoNr");

                    CellIdentityNr cellIdentity = (CellIdentityNr) ((CellInfoNr) cellInfo).getCellIdentity();
                    CellSignalStrengthNr cellSignalStrength = (CellSignalStrengthNr) ((CellInfoNr) cellInfo).getCellSignalStrength();
                    long cellId = cellIdentity.getNci();
                    int rssi = cellSignalStrength.getDbm();
                    String technology = "5G";
                    int frequency = cellIdentity.getNrarfcn();

                    saveDataToDatabase(cellId, rssi, technology, frequency);
                }
            }
        }
        if (db != null) {
            try {
                db.close();
                Log.i("CellularIntentService", "Database closed successfully");
            } catch (Exception e) {
                Log.e("CellularIntentService", "Error closing database", e);
            }
        }
    }

    private void saveDataToDatabase(long cellId, int rssi, String technology, int frequency) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO cellular_data (cell_id, rssi, technology, frequency) VALUES (?, ?, ?, ?)", new Object[]{cellId, rssi, technology, frequency});
            Log.i("CellularIntentService", "Saved data to database");
            Log.d("CellularIntentService", "cellId: " + cellId + " rssi: " + rssi + " technology: " + technology + " frequency: " + frequency);
        } catch (Exception e) {
            Log.e("CellularIntentService", "Error saving data to database", e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e("CellularIntentService", "Error closing database", e);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
            Log.i("CellularIntentService", "DatabaseHelper closed successfully");
        }
    }
}

