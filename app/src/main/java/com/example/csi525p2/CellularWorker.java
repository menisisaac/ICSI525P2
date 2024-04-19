package com.example.csi525p2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

/**
 * Worker class responsible for gathering cellular network information and saving it to a SQLite database.
 */
public class CellularWorker extends Worker {

    // TelephonyManager instance to access cellular network information
    private final TelephonyManager telephonyManager;

    // LocationManager instance to access location information
    private final LocationManager locationManager;

    // Database helper instance for database operations
    private final CellularDBHelper dbHelper;

    // Context of the application
    private final Context context;

    /**
     * Constructor for CellularWorker class.
     *
     * @param context         The context in which the worker operates.
     * @param workerParams    Parameters for the worker.
     */
    public CellularWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.dbHelper = new CellularDBHelper(context);
    }

    /**
     * This method performs the actual work in the background.
     *
     * @return The result of the background work, indicating success or failure.
     */
    @NonNull
    @Override
    public Result doWork() {
        Log.i("CellularWorker", "Doing work in background");

        // Check if TelephonyManager is available
        if (telephonyManager == null) {
            Log.e("CellularWorker", "TelephonyManager is null");
            return Result.failure();
        }

        // Check for permission to access coarse location
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CellularWorker", "Permission ACCESS_COARSE_LOCATION not granted.");
            return Result.failure();
        }

        // Check for permission to access fine location
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CellularWorker", "Permission ACCESS_FINE_LOCATION not granted.");
            return Result.failure();
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Get the list of CellInfo objects representing the available cellular networks
        if (location != null) {
            Log.i("CellularWorker", "Location found: " + location.getLatitude() + ", " + location.getLongitude());
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        if (cellInfoList != null) {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoGsm) {
                    // Retrieve GSM cell information and save to database
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
                    CellSignalStrengthGsm cellSignalStrength = cellInfoGsm.getCellSignalStrength();

                    long cellId = cellIdentity.getCid();
                    int rssi = cellSignalStrength.getDbm();
                    String technology = "2G";
                    int frequency = cellIdentity.getArfcn();

                    saveDataToDatabase(location, cellId, rssi, technology, frequency);
//                    saveDataToDatabase(cellId, rssi, technology, frequency);

                } else if (cellInfo instanceof CellInfoWcdma) {
                    // Retrieve WCDMA cell information and save to database
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
                    CellSignalStrengthWcdma cellSignalStrength = cellInfoWcdma.getCellSignalStrength();

                    long cellId = cellIdentity.getCid();
                    int rssi = cellSignalStrength.getDbm();
                    String technology = "3G";
                    int frequency = cellIdentity.getUarfcn();

                    saveDataToDatabase(location, cellId, rssi, technology, frequency);
//                    saveDataToDatabase(cellId, rssi, technology, frequency);

                } else if (cellInfo instanceof CellInfoLte) {
                    // Retrieve LTE cell information and save to database
                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
                    CellSignalStrengthLte cellSignalStrength = cellInfoLte.getCellSignalStrength();

                    long cellId = cellIdentity.getCi();
                    int rssi = cellSignalStrength.getDbm();
                    String technology = "4G";
                    int frequency = cellIdentity.getEarfcn();

                    saveDataToDatabase(location, cellId, rssi, technology, frequency);
//                    saveDataToDatabase(cellId, rssi, technology, frequency);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr) { // Handle NR cell info
                    // Retrieve NR cell information and save to database
                    CellInfoNr cellInfoNr = (CellInfoNr) cellInfo;
                    CellIdentityNr cellIdentity = (CellIdentityNr) cellInfoNr.getCellIdentity();
                    CellSignalStrengthNr cellSignalStrength = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();

                    long cellId = cellIdentity.getNci();
                    int rssi = cellSignalStrength.getDbm();
                    String technology = "5G";
                    int frequency = cellIdentity.getNrarfcn();

                    saveDataToDatabase(location, cellId, rssi, technology, frequency);
//                    saveDataToDatabase(cellId, rssi, technology, frequency);
                }
            }
        }
        } else {
            Log.e("CellularWorker", "Location is null");
        }

        dbHelper.close();

        // Indicate successful completion of work
        return Result.success();
    }

    /**
     * Helper method to save cellular network data to the SQLite database.
     *
     * @param cellId     The cell ID of the network.
     * @param rssi       The signal strength (RSSI) of the network.
     * @param technology The technology (2G, 3G, 4G, 5G) of the network.
     * @param frequency  The frequency of the network.
     */
    private void saveDataToDatabase(Location location, long cellId, int rssi, String technology, int frequency) {
        // Get writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert data into the database table
        db.execSQL("INSERT INTO cellular_data (timestamp, latitude, longitude, cell_id, rssi, technology, frequency) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{location.getTime(), location.getLatitude(), location.getLongitude(), cellId, rssi, technology, frequency});

        // Log the inserted data for debugging or tracking purposes
        Log.d("CellularIntentService",
                "timestamp: " + location.getTime() +
                        " latitude: " + location.getLatitude() +
                        " longitude: " + location.getLongitude() +
                        " cellId: " + cellId +
                        " rssi: " + rssi +
                        " technology: " + technology +
                        " frequency: " + frequency);
    }

//    private void saveDataToDatabase(long cellId, int rssi, String technology, int frequency) {
//        // Get writable database instance
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Insert data into the database table
//        db.execSQL("INSERT INTO cellular_data (cell_id, rssi, technology, frequency) VALUES (?, ?, ?, ?)",
//                new Object[]{cellId, rssi, technology, frequency});
//
//        // Log the inserted data for debugging or tracking purposes
//        Log.d("CellularIntentService",
//                        " cellId: " + cellId +
//                        " rssi: " + rssi +
//                        " technology: " + technology +
//                        " frequency: " + frequency);
//    }
}