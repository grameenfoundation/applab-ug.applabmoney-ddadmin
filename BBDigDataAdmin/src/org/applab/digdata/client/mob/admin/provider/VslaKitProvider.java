package org.applab.digdata.client.mob.admin.provider;

/**
 *
 *
 */
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import org.applab.digdata.client.mob.admin.database.DigDataSQLiteOpenHelper;
import org.applab.digdata.client.mob.admin.provider.VslaProviderAPI.VslaColumns;

import java.util.HashMap;

public class VslaKitProvider extends ContentProvider {

    private static final String t = "VslaProvider";
    private static final String DATABASE_NAME = "digdata.db";
    private static final int DATABASE_VERSION = 1;
    private static final String VSLA_TABLE_NAME = "vslakit";
    private static final int VSLA = 1;
    private static final int VSLA_ID = 2;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> sInstancesProjectionMap;
    private DatabaseHelper vDbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        int count;

        switch (sUriMatcher.match(uri)) {
            case VSLA:
                count = db.delete(VSLA_TABLE_NAME, where, whereArgs);
                break;

            case VSLA_ID:
                String instanceId = uri.getPathSegments().get(1);

                count =
                        db.delete(VSLA_TABLE_NAME,
                                VslaColumns._ID + "=" + instanceId
                                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                                whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        // Validate the requested uri
        if (sUriMatcher.match(uri) != VSLA) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        long rowId = db.insert(VSLA_TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri instanceUri = ContentUris.withAppendedId(VslaColumns.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(instanceUri, null);
            return instanceUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        vDbHelper = new DatabaseHelper(DATABASE_NAME);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(VSLA_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case VSLA:
                qb.setProjectionMap(sInstancesProjectionMap);
                break;

            case VSLA_ID:
                qb.setProjectionMap(sInstancesProjectionMap);
                qb.appendWhere(VslaColumns._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and run the query
        SQLiteDatabase db = vDbHelper.getReadableDatabase();

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        // String queryString = qb.buildQuery(null, selection, null, null, sortOrder, null, null);
        // Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Tell the cursor what uri to watch, so it knows when its source data changes
        // String queryString = qb.buildQuery(null, selection, selectionArgs, null, null, sortOrder, null);
        // Log.i("QUERYSTRING", queryString);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(VslaProviderAPI.AUTHORITY, "vslabio", VSLA);
        sUriMatcher.addURI(VslaProviderAPI.AUTHORITY, "vsla/#", VSLA_ID);

        sInstancesProjectionMap = new HashMap<String, String>();
        sInstancesProjectionMap.put(VslaColumns._ID, VslaColumns._ID);
        sInstancesProjectionMap.put(VslaColumns.VSLA_ID, VslaColumns.VSLA_ID);
        sInstancesProjectionMap.put(VslaColumns.VSLA_NAME, VslaColumns.VSLA_NAME);
        sInstancesProjectionMap.put(VslaColumns.VSLA_PASS_KEY, VslaColumns.VSLA_PASS_KEY);

    }

    private static class DatabaseHelper extends DigDataSQLiteOpenHelper {

        public DatabaseHelper(String databaseName) {
            super("/sdcard/digdata/databases", databaseName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("DB CREATION", "creating Vsla database");
            db.execSQL("CREATE TABLE " + VSLA_TABLE_NAME + " ("
                    + VslaColumns._ID + " integer primary key, "
                    + VslaColumns.VSLA_ID + " text not null, "
                    + VslaColumns.VSLA_NAME + " text not null, "
                    + VslaColumns.VSLA_PASS_KEY + " text not null );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(t, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS districts");
            onCreate(db);
        }

    }
}
