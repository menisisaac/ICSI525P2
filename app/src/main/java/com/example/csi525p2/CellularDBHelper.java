package com.example.csi525p2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to manage the SQLite database for storing cellular network data.
 */
public class CellularDBHelper extends SQLiteOpenHelper {

    // Name of the SQLite database file
    private static final String DATABASE_NAME = "wireless_network.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor for the CellularDBHelper.
     *
     * @param context The context to use to open or create the database.
     */
    public CellularDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db The SQLite database being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the 'cellular_data' table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS cellular_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp DATETIME, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "cell_id INTEGER, " +
                "rssi INTEGER, " +
                "technology TEXT, " +
                "frequency INTEGER)");
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Create the 'cellular_data' table if it doesn't exist
//        db.execSQL("CREATE TABLE IF NOT EXISTS cellular_data (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "cell_id INTEGER, " +
//                "rssi INTEGER, " +
//                "technology TEXT, " +
//                "frequency INTEGER)");
//    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db         The SQLite database to be upgraded.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is not implemented as there's no upgrade scenario defined for the database version changes
    }
}
