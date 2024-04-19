package com.example.csi525p2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * Service class responsible for starting the CellularWorker to perform background work.
 */
public class CellularIntentService extends Service {

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link android.content.Context#startService(Intent)}, providing the intent that was used to start the service.
     *
     * @param intent  The Intent supplied to {@link android.content.Context#startService(Intent)},
     *                representing the work to be done.
     * @param flags   Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return An integer representing the type of service's behavior and how the system should manage the service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("CellularIntentService", "onStartCommand: Starting work...");

        // Create input data for the Worker (currently empty)
        Data inputData = new Data.Builder().build();

        // Create a OneTimeWorkRequest for the CellularWorker
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CellularWorker.class)
                .setInputData(inputData)
                .build();

        // Enqueue the work request with a unique name and policy to replace existing work
        WorkManager.getInstance(this)
                .enqueueUniqueWork("CellularWorker", ExistingWorkPolicy.REPLACE, workRequest);

        // Return START_STICKY to ensure the service restarts if it's killed by the system
        return START_STICKY;
    }

    /**
     * No binding is needed for this service, so this method returns null.
     *
     * @param intent The Intent that was used to bind to this service.
     * @return null because clients can't bind to this service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
