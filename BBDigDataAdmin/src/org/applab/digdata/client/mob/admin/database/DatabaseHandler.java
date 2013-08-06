package org.applab.digdata.client.mob.admin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.applab.digdata.client.mob.admin.model.Vsla;
import org.applab.digdata.client.mob.admin.model.VslaKit;
import org.applab.digdata.client.mob.admin.provider.VslaKitProviderAPI;
import org.applab.digdata.client.mob.admin.provider.VslaProviderAPI;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "digdata";
    // VSLA table name
    private static final String TABLE_VSLA = "vsla";
    // VSLAKIT table name
    private static final String TABLE_VSLA_KIT = "vslakit";
    private SQLiteDatabase db;

    // VSLA Table Columns names
    //private static final String _ID = "id";
    //private static final String VSLA_ID = "vslaid";
    //private static final String VSLA_NAME = "vslaname";
    //private static final String VSLA_PASS_KEY = "vslapasskey";

    public DatabaseHandler(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VSLA_TABLE = "CREATE TABLE " + TABLE_VSLA + "(" + VslaProviderAPI.VslaColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VslaProviderAPI.VslaColumns.VSLA_ID + " TEXT," + VslaProviderAPI.VslaColumns.VSLA_NAME + " TEXT,"
                + VslaProviderAPI.VslaColumns.VSLA_PASS_KEY + " TEXT" + ")";
        db.execSQL(CREATE_VSLA_TABLE);
        String CREATE_VSLA_KIT_TABLE = "CREATE TABLE " + TABLE_VSLA_KIT + "(" + VslaKitProviderAPI.VslaKitColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VslaKitProviderAPI.VslaKitColumns.VSLA_ID + " TEXT," + VslaKitProviderAPI.VslaKitColumns.PHONE_MANUFACTURER + " TEXT,"
                + VslaKitProviderAPI.VslaKitColumns.PHONE_NUMBER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.PHONE_IMEI + " TEXT," + VslaKitProviderAPI.VslaKitColumns.PHONE_MODEL + " TEXT," + VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_MANUFACTURER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_SERIAL_NUMBER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.RECEIVED_BY + " TEXT" + ")";
        db.execSQL(CREATE_VSLA_KIT_TABLE);
        Log.i("onCreate: DB Version", String.valueOf(db.getVersion()));
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VSLA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VSLA_KIT);

        // Create tables again
        onCreate(db);
    }

    public boolean addVslaInfo(Vsla vsla) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(VslaProviderAPI.VslaColumns.VSLA_ID, vsla.getId());
            values.put(VslaProviderAPI.VslaColumns.VSLA_NAME, vsla.getName());
            values.put(VslaProviderAPI.VslaColumns.VSLA_PASS_KEY, vsla.getPasskey());

            Log.i("DBHandler.addVsla", VslaProviderAPI.VslaColumns.VSLA_PASS_KEY + "23");

            long retVal = db.insert(TABLE_VSLA, null, values);
            if (retVal != -1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("DBHandler.addVsla", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean addVslaKitDeliveryData(VslaKit vslaKit) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(VslaKitProviderAPI.VslaKitColumns.VSLA_ID, vslaKit.getVslaId());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_MANUFACTURER, vslaKit.getPhoneManufacturer());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_MODEL, vslaKit.getPhoneModel());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_IMEI, vslaKit.getPhoneImei());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_NUMBER, vslaKit.getPhoneNumber());
            values.put(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_MANUFACTURER, vslaKit.getChargeSetManufacturer());
            values.put(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_SERIAL_NUMBER, vslaKit.getChargeSetSerialNumber());
            values.put(VslaKitProviderAPI.VslaKitColumns.RECEIVED_BY, vslaKit.getReceivedBy());

            long retVal = db.insert(TABLE_VSLA_KIT, null, values);
            if (retVal != -1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("DBHandler.addVsla", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean updateVsla(Vsla vsla) {
        db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(VslaProviderAPI.VslaColumns.VSLA_PASS_KEY, vsla.getPasskey());

            String selection = VslaProviderAPI.VslaColumns.VSLA_ID + "=?";
            String[] selectionArgs = {vsla.getId()};

            long retVal = db.update(TABLE_VSLA, values, selection, selectionArgs);
            if (retVal != -1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("DBHandler.addVsla", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    // Getting FinancialPledges Count
    public int getVslaCount() {
        Cursor vslaCursor = null;
        db = this.getWritableDatabase();

        try {
            vslaCursor = db.query(TABLE_VSLA, null, null, null, null, null, null);
            if (vslaCursor == null) {
                return 0;
            }
            return vslaCursor.getCount();
        } catch (Exception ex) {
            Log.e("Repository.VslaCount", "" + ex.toString());
            return 0;
        } finally {
            if (vslaCursor != null) {
                vslaCursor.close();
            }
        }
    }

    public void addUser(String string, String string1, String string2, String string3) {


    }

    public Vsla getVslaFromDbById(String vslaId) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Vsla> vslas = new ArrayList<Vsla>();
        Vsla vsla = new Vsla();

        String selection = VslaProviderAPI.VslaColumns.VSLA_ID + "=?";
        String[] selectionArgs = {vslaId};
        Cursor vslaCursor = null;
        try {
            vslaCursor = db.query(TABLE_VSLA, null, selection, selectionArgs, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));
            vslaCursor.moveToFirst();
            while (!vslaCursor.isAfterLast()) {
                vsla.setId(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_ID)));
                vsla.setName(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_NAME)));
                vslas.add(vsla);
                vslaCursor.moveToNext();
            }
            vslaCursor.close();
            return vslas.get(0);

        } catch (Exception ex) {
            Log.e("Repository.getVslafromDB", "" + ex.getStackTrace());
            return vsla;
        } finally {
            if (vslaCursor != null) {
                vslaCursor.close();
            }
        }
    }

    public Vsla getVslaFromDbByName(String selectedItem) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Vsla> vslas = new ArrayList<Vsla>();
        Vsla vsla = new Vsla();

        String selection = VslaProviderAPI.VslaColumns.VSLA_NAME + "=?";
        String[] selectionArgs = {selectedItem};
        Cursor vslaCursor = null;
        try {
            vslaCursor = db.query(TABLE_VSLA, null, selection, selectionArgs, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));
            vslaCursor.moveToFirst();
            while (!vslaCursor.isAfterLast()) {
                vsla.setId(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_ID)));
                vsla.setName(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_NAME)));
                vslas.add(vsla);
                vslaCursor.moveToNext();
            }
            vslaCursor.close();
            Log.i("VSLA", String.valueOf(vslas.get(0)));
            return vslas.get(0);

        } catch (Exception ex) {
            Log.e("Repository.getVslafromDB", "" + ex.toString());
            return vsla;
        } finally {
            if (vslaCursor != null) {
                vslaCursor.close();
            }
        }
    }

    //public ArrayList<String> getAllVslasFromDb() {
    public ArrayList<Vsla> getAllVslasFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Vsla> vslaArray= new ArrayList<Vsla>();

       // ArrayList<String> vslas = new ArrayList<String>();
        Cursor vslaCursor = null;
        try {
            vslaCursor = db.query(TABLE_VSLA, null, null, null, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));
            vslaCursor.moveToFirst();
            while (!vslaCursor.isAfterLast()) {
                Vsla vsla = new Vsla();
                vsla.setId(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_ID)));
                vsla.setName(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_NAME)));
                //vslas.add("Name: " + vsla.getName() + "\r\n" + "ID: " + vsla.getId());
                vslaArray.add(vsla);
                vslaCursor.moveToNext();
            }
            vslaCursor.close();
            //return vslas;
            return vslaArray;

        } catch (Exception ex) {
            Log.e("Repository.getAllVslasfromDB", "" + ex.getStackTrace());
            return vslaArray;
        } finally {
            if (vslaCursor != null) {
                vslaCursor.close();
            }
        }
    }

    public void insertDefaultData() {
        // Create a repo and pass in the current Application Repo for connecting to the database

        Vsla vsla;

        vsla = new Vsla();
        vsla.setId("BB2345");
        vsla.setName("God Gives");
        vsla.setPasskey("3576tyw");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BC1345");
        vsla.setName("Hardwork Pays");
        vsla.setPasskey("w476tyw");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BB3437");
        vsla.setName("Invest Wisely");
        vsla.setPasskey("176trg");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BC7845");
        vsla.setName("We are Humble");
        vsla.setPasskey("ty6t37");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BE7912");
        vsla.setName("What we have is Ours");
        vsla.setPasskey("tiy4er");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BG8812");
        vsla.setName("Welcome Please");
        vsla.setPasskey("hj34e1");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BA6722");
        vsla.setName("Old Man's Eye");
        vsla.setPasskey("hf34e7");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BG5426");
        vsla.setName("Early Risers");
        vsla.setPasskey("h35634");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BW1228");
        vsla.setName("To Build Each Other");
        vsla.setPasskey("h35634");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BY3217");
        vsla.setName("Use what you have");
        vsla.setPasskey("y3ui67");
        addVslaInfo(vsla);

        /** VslaKit vslaKit = null;

         vslaKit = new VslaKit();
         vslaKit =
         vslaKit.setPhoneNumber("0772102835");
         vslaKit.setPhoneImei("53637186919");
         vslaKit.setPhoneManufacturer("Samsung");
         vslaKit.setPhoneModel("wrt15763");
         vslaKit.setChargeSetSerialNumber("362487304401");
         vslaKit.setReceivedBy("Jon Jaran");
         addPledge(vslaKit); */
    }


    
}
