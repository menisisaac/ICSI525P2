package com.example.csi525p2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class WifiTable {
    public WifiTable() {};

    public static class WifiEntry extends SQLiteOpenHelper {
        public static final String SCANTABLE_NAME = "WIFIScan";
        public static final String timestamp = "datetime";
        public static final String longitude = "longitude";
        public static final String latitude = "latitude";

        public static final String WIFI_TABLE_NAME = "wifi";
        public static final String _ID = "_id";

        public static final String bssid = "bssid";
        public static final String ssid = "ssid";
        public static final String frequency = "frequency";
        public static final String channel_width = "channel_width";
        public static final String rssi_level = "rssi_level";
        public static final String scan_id  = "scan_id";
        static final int DB_VERSION = 1;
        static final String DB_NAME = "WIFISCAN.DB";
        public WifiEntry(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        private static final String CREATE_SCAN_TABLE = "create table " + SCANTABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + timestamp + " TEXT NOT NULL, "
                + longitude + " TEXT NOT NULL, " + latitude + " TEXT NOT NULL);";
        private static final String CREATE_TABLE = "create table " + WIFI_TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + bssid + " TEXT NOT NULL, "
                + ssid + " TEXT NOT NULL, " + frequency + " INTEGER, " + channel_width + " INTEGER, "
                + rssi_level + " INTEGER, "
                + scan_id + "INTEGER, "
                + " FOREIGN KEY ("+scan_id+") REFERENCES "+SCANTABLE_NAME+"("+_ID+"));";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SCAN_TABLE);
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + WIFI_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SCANTABLE_NAME);
        }
    }

}
