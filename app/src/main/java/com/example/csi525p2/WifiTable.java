package com.example.csi525p2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/*
SQLite specification for wifi, includes timestamp, longitude, latitude, and various radio specs
 */
public final class WifiTable {
    private WifiTable() {
    }

    public static class WifiEntry implements BaseColumns {
        public static final String WIFI_TABLE_NAME = "WIFIScan";
        public static final String timestamp = "datetime";
        public static final String longitude = "longitude";
        public static final String latitude = "latitude";

        public static final String _ID = "_id";

        public static final String bssid = "bssid";
        public static final String ssid = "ssid";
        public static final String frequency = "frequency";
        public static final String channel_width = "channel_width";
        public static final String rssi_level = "rssi_level";
        static final int DB_VERSION = 1;
        static final String DB_NAME = "WIFISCAN.DB";


        public static final String CREATE_WIFI_SCAN_TABLE = "create table " + WifiEntry.WIFI_TABLE_NAME + "("
                + WifiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WifiEntry.timestamp + " TEXT NOT NULL, "
                + WifiEntry.longitude + " TEXT NOT NULL, " + WifiEntry.latitude + " TEXT NOT NULL,"
                + WifiEntry.bssid + " TEXT NOT NULL, " + WifiEntry.ssid + " TEXT NOT NULL, "
                + WifiEntry.frequency + " INTEGER, " + WifiEntry.channel_width + " INTEGER, "
                + WifiEntry.rssi_level + " INTEGER)";
    }
}