package com.example.csi525p2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CellularDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wireless_network.db";
    private static final int DATABASE_VERSION = 1;

    public CellularDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS cellular_data (id INTEGER PRIMARY KEY AUTOINCREMENT, cell_id INTEGER, rssi INTEGER, technology TEXT, frequency INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
