package org.applab.ledgerlink.client.mob.admin.provider;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * VslaAdminUser: Sarahk
 * Date: 9/17/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class VslaProvider3 extends ContentProvider {

    // public constants for client development
    public static final String AUTHORITY = "org.applab.digdata.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + VslaColumns.CONTENT_PATH);

    // helper constants for use with the UriMatcher
    private static final int VSLA_LIST = 1;
    private static final int SINGLE_VSLA_ID = 2;

    public VslaProvider3() {
    }

    /**
     * Column and content type definitions for the LentItemsProvider.
     */
    public static interface VslaColumns extends BaseColumns {
        public static final Uri CONTENT_URI = VslaProvider3.CONTENT_URI;
        public static final String VSLA_NAME = "VSLA_NAME";
        public static final String VSLA_PASS_KEY = "VSLA_PASS_KEY";
        public static final String VSLA_LOCATION = "VSLA_LOCATION";
        public static final String VSLA_ID = "VSLA_CODE";
        public static final String CONTENT_PATH = "vsla";
        // public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.digdata.vsla";
        //public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.digdata.vsla";
        public static final String[] PROJECTION_ALL = {_ID, VSLA_ID, VSLA_NAME, VSLA_PASS_KEY};
        public static final String SORT_ORDER_DEFAULT = VSLA_NAME + " ASC";
    }

    private static HashMap<String, String> VSLA_LIST_PROJECTION_MAP;

    // prepare the UriMatcher
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, VslaColumns.CONTENT_PATH, VSLA_LIST);
        uriMatcher.addURI(AUTHORITY, VslaColumns.CONTENT_PATH + "/#", SINGLE_VSLA_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "DigData";
    static final String VSLA_TABLE_NAME = "vsla";
    static final int DATABASE_VERSION = 2;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + VSLA_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " VSLA_CODE TEXT NOT NULL, " +
                    " VSLA_NAME TEXT NOT NULL, " +
                    " VSLA_PASS_KEY TEXT NOT NULL, " +
                    " VSLA_LOCATION TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + VSLA_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("VslaProvider3.insert", VslaProviderAPI.VslaColumns.VSLA_PASS_KEY + "23");
        /**
         * Add a new vsla record
         */
        long rowID = db.insert(VSLA_TABLE_NAME, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Log.i("VslaProvider3.insert", VslaProviderAPI.VslaColumns.VSLA_PASS_KEY + "23" + CONTENT_URI);
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(VSLA_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case VSLA_LIST:
                qb.setProjectionMap(VSLA_LIST_PROJECTION_MAP);
                break;
            case SINGLE_VSLA_ID:
                qb.appendWhere(VslaColumns.VSLA_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on vsla names
             */
            sortOrder = VslaColumns.VSLA_NAME;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case VSLA_LIST:
                count = db.delete(VSLA_TABLE_NAME, selection, selectionArgs);
                break;
            case SINGLE_VSLA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(VSLA_TABLE_NAME, VslaColumns.VSLA_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case VSLA_LIST:
                count = db.update(VSLA_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case SINGLE_VSLA_ID:
                count = db.update(VSLA_TABLE_NAME, values, VslaColumns.VSLA_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all vsla records
             */
            case VSLA_LIST:
                return "vnd.android.cursor.dir/vnd.digdata.vsla";
            /**
             * Get a particular vsla
             */
            case SINGLE_VSLA_ID:
                return "vnd.android.cursor.item/vnd.digdata.vsla";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
