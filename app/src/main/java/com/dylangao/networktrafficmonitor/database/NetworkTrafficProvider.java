package com.dylangao.networktrafficmonitor.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static com.dylangao.networktrafficmonitor.database.DataBaseConstants.*;

public class NetworkTrafficProvider extends ContentProvider {
    DatabaseHelper mpDBHelper;
    private static final UriMatcher sMatcher;

    static {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(AUTOHORITY, NETWORK_TRAFFIC_FOR_DAY_TABLE_NAME,
                URI_TYPE_NETWORK_TRAFFIC_FOR_DAY);
        sMatcher.addURI(AUTOHORITY, NETWORK_TRAFFIC_FOR_MONTH_TABLE_NAME,
                URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH);
        sMatcher.addURI(AUTOHORITY, NETWORK_TRAFFIC_CONFIG_TABLE_NAME,
                URI_TYPE_NETWORK_TRAFFIC_CONFIG);
    }

    @Override
    public boolean onCreate() {
        mpDBHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case URI_TYPE_NETWORK_TRAFFIC_FOR_DAY:
                return NETWORK_TRAFFIC_FOR_DAY_TABLE_NAME;
            case URI_TYPE_NETWORK_TRAFFIC_FOR_MONTH:
                return NETWORK_TRAFFIC_FOR_MONTH_TABLE_NAME;
            case URI_TYPE_NETWORK_TRAFFIC_CONFIG:
                return NETWORK_TRAFFIC_CONFIG_TABLE_NAME;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = getType(uri);
        if (table == null) {
            return null;
        }
        SQLiteDatabase db = mpDBHelper.getReadableDatabase();
        long id = db.insert(table, null, values);
        if (id < 0) {
            return null;
        }
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getType(uri);
        if (table == null) {
            return null;
        }
        SQLiteDatabase db = mpDBHelper.getReadableDatabase();
        return db.query(table, projection, selection, selectionArgs, null,
                sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = getType(uri);
        if (table == null) {
            return -1;
        }
        SQLiteDatabase db = mpDBHelper.getReadableDatabase();
        int count = db.update(table, values, selection, selectionArgs);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getType(uri);
        if (table == null) {
            return -1;
        }
        SQLiteDatabase db = mpDBHelper.getReadableDatabase();
        int count = db.delete(table, selection, selectionArgs);
        return count;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_FILE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NETWORK_TRAFFIC_FOR_DAY_TABLE);
            db.execSQL(CREATE_NETWORK_TRAFFIC_FOR_MONTH_TABLE);
            db.execSQL(CREATE_NETWORK_TRAFFIC_CONFIG_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

        }

    }
}
