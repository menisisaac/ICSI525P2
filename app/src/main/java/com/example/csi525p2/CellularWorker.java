package com.example.csi525p2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Worker class responsible for gathering cellular network information and saving it to a SQLite database.
 */
public class CellularWorker extends Worker {

    // TelephonyManager instance to access cellular network information
    private final TelephonyManager telephonyManager;

    // Database helper instance for database operations
    private final CellularDBHelper dbHelper;

    // Context of the application
    private final Context context;

    // FusedLocationProviderClient for accessing device location
    private final FusedLocationProviderClient fusedLocationClient;

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
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
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

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                // Check for permission to access fine location
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("CellularWorker", "Permission ACCESS_FINE_LOCATION not granted.");
                    return;
                }
                fusedLocationClient.requestLocationUpdates(LocationRequest.create(), new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        if (locationResult.getLastLocation() != null) {
                            Location location = locationResult.getLastLocation();

                            // Check for permission to access fine location
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Log.e("CellularWorker", "Permission ACCESS_FINE_LOCATION not granted.");
                                return;
                            }

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

                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr) {
                                        // Retrieve NR cell information and save to database
                                        CellInfoNr cellInfoNr = (CellInfoNr) cellInfo;
                                        CellIdentityNr cellIdentity = (CellIdentityNr) cellInfoNr.getCellIdentity();
                                        CellSignalStrengthNr cellSignalStrength = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();

                                        long cellId = cellIdentity.getNci();
                                        int rssi = cellSignalStrength.getDbm();
                                        String technology = "5G";
                                        int frequency = cellIdentity.getNrarfcn();

                                        saveDataToDatabase(location, cellId, rssi, technology, frequency);
                                    }
                                }
                            }
                        } else {
                            Log.e("CellularWorker", "Current location is null");
                        }
                    }
                }, null);
            }
        });

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
        if (location != null) {
            // Get current timestamp
            long timestamp = System.currentTimeMillis();

            // Get writable database instance
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Insert data into the database table
            db.execSQL("INSERT INTO cellular_data (timestamp, latitude, longitude, cell_id, rssi, technology, frequency) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{timestamp, location.getLatitude(), location.getLongitude(), cellId, rssi, technology, frequency});

            // Log the inserted data for debugging or tracking purposes
            Log.d("CellularWorker",
                    "timestamp: " + timestamp +
                            " latitude: " + location.getLatitude() +
                            " longitude: " + location.getLongitude() +
                            " cellId: " + cellId +
                            " rssi: " + rssi +
                            " technology: " + technology +
                            " frequency: " + frequency);
        } else {
            Log.e("CellularWorker", "Location is null");
        }
    }

    // Enqueue work method
    public static void enqueueWork(Context context, Intent intent) {
        // Create WorkRequest
        androidx.work.OneTimeWorkRequest workRequest = new androidx.work.OneTimeWorkRequest.Builder(CellularWorker.class).build();
        // Enqueue work
        androidx.work.WorkManager.getInstance(context).enqueue(workRequest);
    }
}
