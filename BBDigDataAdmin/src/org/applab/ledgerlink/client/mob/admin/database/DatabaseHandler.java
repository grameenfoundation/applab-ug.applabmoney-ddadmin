package org.applab.ledgerlink.client.mob.admin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.applab.ledgerlink.client.mob.admin.model.*;
import org.applab.ledgerlink.client.mob.admin.provider.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    // vsla table name
    private static final String TABLE_VSLA = "vsla";
    // vslakit table name
    private static final String TABLE_VSLA_KIT = "vslakit";
    // kitdelivery
    private static final String TABLE_KIT_DELIVERY = "kitdelivery";
    // vslaadminusers table name
    private static final String TABLE_VSLA_ADMIN_USERS = "vslaadminusers";
    // phones table name
    private static final String TABLE_PHONES = "phones";
    // chargesets table name
    private static final String TABLE_CHARGE_SET = "chargesets";
    private SQLiteDatabase db;
    private Cursor vslaAdminUserCursor;

    public DatabaseHandler(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VSLA_TABLE = "CREATE TABLE " + TABLE_VSLA + "(" + VslaProviderAPI.VslaColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VslaProviderAPI.VslaColumns.VSLA_CODE + " TEXT UNIQUE," + VslaProviderAPI.VslaColumns.VSLA_NAME + " TEXT,"
                + VslaProviderAPI.VslaColumns.VSLA_PASS_KEY + " TEXT NULL," + VslaProviderAPI.VslaColumns.KIT_DELIVERY_STATUS + " INTEGER," + VslaProviderAPI.VslaColumns.REGION_ID + " STRING," + VslaProviderAPI.VslaColumns.GPS_LOCATION + " STRING NULL" + " )";
        db.execSQL(CREATE_VSLA_TABLE);
        String CREATE_VSLA_KIT_TABLE = "CREATE TABLE " + TABLE_VSLA_KIT + "(" + VslaKitProviderAPI.VslaKitColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VslaKitProviderAPI.VslaKitColumns.PHONE_MANUFACTURER + " TEXT,"
                + VslaKitProviderAPI.VslaKitColumns.PHONE_NUMBER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.PHONE_IMEI + " TEXT UNIQUE," + VslaKitProviderAPI.VslaKitColumns.PHONE_MODEL + " TEXT," + VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_MANUFACTURER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_SERIAL_NUMBER + " TEXT," + VslaKitProviderAPI.VslaKitColumns.RECEIVED_BY + " TEXT" + ")";
        db.execSQL(CREATE_VSLA_KIT_TABLE);
        String CREATE_KIT_DELIVERY_TABLE = "CREATE TABLE " + TABLE_KIT_DELIVERY + "(" + KitDeliveryProviderAPI.KitDeliveryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KitDeliveryProviderAPI.KitDeliveryColumns.ADMIN_USER_NAME + " TEXT, " + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_CODE + " TEXT, " + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_IMEI + " TEXT, " + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MODEL + " TEXT, " + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MANUFACTURER + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MSISDN + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_SERIAL_NUMBER + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.RECIPIENT_ROLE + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.RECEIVED_BY + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.DELIVERY_DATE + " TEXT," + KitDeliveryProviderAPI.KitDeliveryColumns.DELIVERY_STATUS + " INT" + ")";
        db.execSQL(CREATE_KIT_DELIVERY_TABLE);
        String CREATE_VSLA_ADMIN_USERS_TABLE = "CREATE TABLE " + TABLE_VSLA_ADMIN_USERS + "(" + VslaAdminUserProviderAPI.VslaAdminUserColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME + " TEXT UNIQUE, " + VslaAdminUserProviderAPI.VslaAdminUserColumns.PASSWORD + " TEXT, " + VslaAdminUserProviderAPI.VslaAdminUserColumns.LAST_LOGIN + " TEXT, " + VslaAdminUserProviderAPI.VslaAdminUserColumns.REGION_CODE + " INT, " + VslaAdminUserProviderAPI.VslaAdminUserColumns.SECURITY_TOKEN + " TEXT, " + VslaAdminUserProviderAPI.VslaAdminUserColumns.ACTIVATION_STATUS + " INT" + ")";
        db.execSQL(CREATE_VSLA_ADMIN_USERS_TABLE);
        String CREATE_PHONES_TABLE = "CREATE TABLE " + TABLE_PHONES + "(" + PhoneProviderAPI.PhoneColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PhoneProviderAPI.PhoneColumns.PHONE_MANUFACTURER + " TEXT," + PhoneProviderAPI.PhoneColumns.PHONE_MODEL + " TEXT" + ")";
        db.execSQL(CREATE_PHONES_TABLE);
        String CREATE_CHARGESETS_TABLE = "CREATE TABLE " + TABLE_CHARGE_SET + "(" + ChargesetProviderAPI.ChargesetColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ChargesetProviderAPI.ChargesetColumns.CHARGESET_MANUFACTURER + " TEXT," + ChargesetProviderAPI.ChargesetColumns.CHARGESET_SERIAL_NUMBER + " TEXT UNIQUE" + ")";
        db.execSQL(CREATE_CHARGESETS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VSLA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VSLA_KIT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KIT_DELIVERY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VSLA_ADMIN_USERS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARGE_SET);

        // Create tables again
        onCreate(db);
    }

    public boolean insertVslaInfo(Vsla vsla) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(VslaProviderAPI.VslaColumns.VSLA_CODE, vsla.getVslaCode());
            values.put(VslaProviderAPI.VslaColumns.VSLA_NAME, vsla.getName());

            long retVal = db.insert(TABLE_VSLA, null, values);
            return retVal != -1;
        } catch (Exception ex) {
            Log.e("DBHandler.addVsla", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean insertVslaKitDeliveryData(KitDelivery kitDelivery) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_CODE, kitDelivery.getVslaCode());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.ADMIN_USER_NAME, kitDelivery.getAdminUserName());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_IMEI, kitDelivery.getVslaPhoneImei());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MODEL, kitDelivery.getVslaPhoneModel());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_SERIAL_NUMBER, kitDelivery.getVslaPhoneSerialNumber());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MANUFACTURER, kitDelivery.getVslaPhoneManufacturer());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MSISDN, kitDelivery.getVslaPhoneMsisdn());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.RECEIVED_BY, kitDelivery.getReceivedBy());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.RECIPIENT_ROLE, kitDelivery.getRecipientRole());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.DELIVERY_DATE, kitDelivery.getDeliveryDate());
            values.put(KitDeliveryProviderAPI.KitDeliveryColumns.DELIVERY_STATUS, kitDelivery.getDeliveryStatus());


            long retVal = db.insert(TABLE_KIT_DELIVERY, null, values);
            return retVal != -1;
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
            values.put(VslaProviderAPI.VslaColumns.GPS_LOCATION, vsla.getPasskey());

            String selection = VslaProviderAPI.VslaColumns.VSLA_CODE + "=?";
            String[] selectionArgs = {vsla.getVslaCode()};

            long retVal = db.update(TABLE_VSLA, values, selection, selectionArgs);
            return retVal != -1;
        } catch (Exception ex) {
            Log.e("DBHandler.addVsla", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    // Getting Vsla Count
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
                db.close();
            }
        }
    }

    public Vsla getVslaFromDbById(String vslaId) {
        db = this.getReadableDatabase();

        List<Vsla> vslas = new ArrayList<Vsla>();
        Vsla vsla = new Vsla();

        String selection = VslaProviderAPI.VslaColumns.VSLA_CODE + "=?";
        String[] selectionArgs = {vslaId};
        Cursor vslaCursor = null;
        try {
            vslaCursor = db.query(TABLE_VSLA, null, selection, selectionArgs, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));
            vslaCursor.moveToFirst();
            while (!vslaCursor.isAfterLast()) {
                vsla.setVslaCode(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_CODE)));
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
                db.close();
            }
        }
    }

    public Vsla getVslaFromDbByName(String selectedItem) {
        db = this.getReadableDatabase();

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
                vsla.setVslaCode(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_CODE)));
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
                db.close();
            }
        }
    }

    public ArrayList<Vsla> getAllVslasFromDb() {
        db = this.getReadableDatabase();

        ArrayList<Vsla> vslaArray = new ArrayList<Vsla>();
        /** Vsla vsla = new Vsla();
         vsla.setVslaCode("V004");
         vsla.setName("ABAKISA BAKHONYANA");
         vslaArray.add(vsla);


         return vslaArray;   */
        Cursor vslaCursor = null;
        try {
            vslaCursor = db.query(TABLE_VSLA, null, null, null, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));
            vslaCursor.moveToFirst();
            while (!vslaCursor.isAfterLast()) {
                Vsla vsla = new Vsla();
                vsla.setVslaCode(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_CODE)));
                vsla.setName(vslaCursor.getString(vslaCursor.getColumnIndex(VslaProviderAPI.VslaColumns.VSLA_NAME)));

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
                db.close();
            }
        }
    }

    public VslaKit getKitByVsla(String vslaId) {

        db = this.getReadableDatabase();

        List<VslaKit> vslaKits = new ArrayList<VslaKit>();
        VslaKit vslaKit = new VslaKit();

        String selection = VslaKitProviderAPI.VslaKitColumns._ID + "=?";
        String[] selectionArgs = {vslaId};
        String orderBy = VslaKitProviderAPI.VslaKitColumns.LAST_MODIFIED_DATE + " DESC";
        Cursor vslaKitCursor = null;
        try {
            vslaKitCursor = db.query(TABLE_VSLA, null, selection, selectionArgs, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaKitCursor.getCount()));
            vslaKitCursor.moveToFirst();
            while (!vslaKitCursor.isAfterLast()) {
                vslaKit.setKitId(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns._ID)));
                vslaKit.setPhoneImei(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.PHONE_IMEI)));
                vslaKit.setPhoneManufacturer(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.PHONE_MANUFACTURER)));
                vslaKit.setPhoneNumber(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.PHONE_NUMBER)));
                vslaKit.setPhoneModel(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.PHONE_MODEL)));
                vslaKit.setChargeSetManufacturer(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_MANUFACTURER)));
                vslaKit.setChargeSetSerialNumber(vslaKitCursor.getString(vslaKitCursor.getColumnIndex(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_SERIAL_NUMBER)));
                vslaKits.add(vslaKit);
                vslaKitCursor.moveToNext();
            }
            vslaKitCursor.close();
            Log.i("VSLA", String.valueOf(vslaKits.get(0)));
            return vslaKits.get(0);

        } catch (Exception ex) {
            Log.e("Repository.getVslafromDB", "" + ex.toString());
            return vslaKit;
        } finally {
            if (vslaKitCursor != null) {
                vslaKitCursor.close();
                db.close();
            }
        }
    }

    // Getting VslaAdminUserCount
    public int getVslaAdminUserCount() {
        Cursor vslaAdminUserCursor = null;
        db = this.getWritableDatabase();

        try {
            vslaAdminUserCursor = db.query(TABLE_VSLA_ADMIN_USERS, null, null, null, null, null, null);
            if (vslaAdminUserCursor == null) {
                return 0;
            }
            return vslaAdminUserCursor.getCount();
        } catch (Exception ex) {
            Log.e("Repository.VslaAdminUserCount", "" + ex.toString());
            return 0;
        } finally {
            if (vslaAdminUserCursor != null) {
                vslaAdminUserCursor.close();
                db.close();
            }
        }
    }

    public VslaAdminUser getVslaAdminUser(String userName) {
        db = this.getReadableDatabase();

        String selection = VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME + "=?";
        String[] selectionArgs = {userName};
        vslaAdminUserCursor = null;
        VslaAdminUser vslaAdminUser = new VslaAdminUser();

        try {
            vslaAdminUserCursor = db.query(TABLE_VSLA_ADMIN_USERS, null, selection, selectionArgs, null, null, null);
            if (vslaAdminUserCursor.getCount() == 0) {
                return null;
            }

            vslaAdminUserCursor.moveToFirst();
            while (!vslaAdminUserCursor.isAfterLast()) {
                vslaAdminUser.setUserName(vslaAdminUserCursor.getString(vslaAdminUserCursor.getColumnIndex(VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME)));
                vslaAdminUser.setPassword(vslaAdminUserCursor.getString(vslaAdminUserCursor.getColumnIndex(VslaAdminUserProviderAPI.VslaAdminUserColumns.PASSWORD)));
                vslaAdminUserCursor.moveToNext();
            }
            return vslaAdminUser;

        } catch (Exception ex) {
            Log.e("GetVslaAdminUser", ex.getMessage().toString());
            return null;
        } finally {
            if (vslaAdminUserCursor != null) {
                vslaAdminUserCursor.close();
                db.close();
            }
        }

    }

    public VslaAdminUser getVslaAdminUserByImei(String imei) {
        db = this.getWritableDatabase();

        String selection = VslaAdminUserProviderAPI.VslaAdminUserColumns.IMEI + "=?";
        String[] selectionArgs = {imei};
        Cursor vslaAdminUserCursor = null;
        VslaAdminUser vslaAdminUser = new VslaAdminUser();
        try {
            vslaAdminUserCursor = db.query(TABLE_VSLA_ADMIN_USERS, null, selection, selectionArgs, null, null, null);

            if (vslaAdminUserCursor == null) {
                return null;
            }
            vslaAdminUserCursor.moveToFirst();
            while (!vslaAdminUserCursor.isAfterLast()) {
                vslaAdminUser.setUserName(vslaAdminUserCursor.getString(vslaAdminUserCursor.getColumnIndex(VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME)));
                vslaAdminUser.setPassword(vslaAdminUserCursor.getString(vslaAdminUserCursor.getColumnIndex(VslaAdminUserProviderAPI.VslaAdminUserColumns.PASSWORD)));
            }
            return vslaAdminUser;

        } catch (Exception ex) {
            return null;
        } finally {
            if (vslaAdminUserCursor != null) {
                vslaAdminUserCursor.close();
                db.close();
            }
        }
    }

    public boolean addAdminUser(VslaAdminUser vslaAdminUser) {
        db = this.getWritableDatabase();

        //  Create object of ContentValues
        ContentValues values = new ContentValues();
        try {
            // Assign values for each Column.
            values.put(VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME, vslaAdminUser.getUserName());
            values.put(VslaAdminUserProviderAPI.VslaAdminUserColumns.PASSWORD, vslaAdminUser.getPassword());
            values.put(VslaAdminUserProviderAPI.VslaAdminUserColumns.SECURITY_TOKEN, vslaAdminUser.getSecurityToken());
            values.put(VslaAdminUserProviderAPI.VslaAdminUserColumns.ACTIVATION_STATUS, vslaAdminUser.getActivationStatus());

            long retVal = db.insert(TABLE_VSLA_ADMIN_USERS, null, values);

            return retVal != -1;
        } catch (Exception ex) {
            Log.e("DBHandler.addUser", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public ArrayList<Phone> getPhoneDNA() {
        db = this.getReadableDatabase();

        ArrayList<Phone> phoneArray = new ArrayList<Phone>();

        Cursor phoneCursor = null;
        try {
            phoneCursor = db.query(TABLE_PHONES, null, null, null, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(phoneCursor.getCount()));
            phoneCursor.moveToFirst();
            while (!phoneCursor.isAfterLast()) {
                Phone phone = new Phone();
                phone.setPhoneManufacturer(phoneCursor.getString(phoneCursor.getColumnIndex(PhoneProviderAPI.PhoneColumns.PHONE_MANUFACTURER)));
                phone.setPhoneModel(phoneCursor.getString(phoneCursor.getColumnIndex(PhoneProviderAPI.PhoneColumns.PHONE_MODEL)));
                phoneArray.add(phone);
                phoneCursor.moveToNext();
            }
            phoneCursor.close();
            return phoneArray;

        } catch (Exception ex) {
            Log.e("Repository.getAllVslasfromDB", "" + Arrays.toString(ex.getStackTrace()));
            return phoneArray;
        } finally {
            if (phoneCursor != null) {
                phoneCursor.close();
                db.close();
            }
        }
    }

    public ArrayList<ChargeSet> getChargeSetDNA() {

        db = this.getReadableDatabase();

        ArrayList<ChargeSet> chargesetArray = new ArrayList<ChargeSet>();

        Cursor chargeSetCursor = null;
        try {
            chargeSetCursor = db.query(TABLE_CHARGE_SET, null, null, null, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(chargeSetCursor.getCount()));
            chargeSetCursor.moveToFirst();
            while (!chargeSetCursor.isAfterLast()) {
                ChargeSet chargeSet = new ChargeSet();
                chargeSet.setChargeSetManufacturer(chargeSetCursor.getString(chargeSetCursor.getColumnIndex(ChargesetProviderAPI.ChargesetColumns.CHARGESET_MANUFACTURER)));
                chargesetArray.add(chargeSet);
                chargeSetCursor.moveToNext();
            }
            chargeSetCursor.close();
            return chargesetArray;

        } catch (Exception ex) {
            Log.e("Repository.getChargeSetDNA", "" + Arrays.toString(ex.getStackTrace()));
            return chargesetArray;
        } finally {
            if (chargeSetCursor != null) {
                chargeSetCursor.close();
                db.close();
            }
        }
    }

    private boolean insertPhoneInfo(Phone phone) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(PhoneProviderAPI.PhoneColumns.PHONE_MANUFACTURER, phone.getPhoneManufacturer());
            Log.i("DBHandler.addPhone", "DBHandler.addPhone");
            long retVal = db.insert(TABLE_PHONES, null, values);

            return retVal != -1;

        } catch (Exception ex) {
            Log.e("DBHandler.addPhone", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private boolean insertChargeSetInfo(ChargeSet chargeSet) {
        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(ChargesetProviderAPI.ChargesetColumns.CHARGESET_MANUFACTURER, chargeSet.getChargeSetManufacturer());
            long retVal = db.insert(TABLE_CHARGE_SET, null, values);

            return retVal != -1;
        } catch (Exception ex) {
            Log.e("DBHandler.addChargeset", ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void addAssets() {

        Phone phone = new Phone();
        phone.setPhoneManufacturer("Samsung");

        if (getPhoneDNA() != null) {
            // Prepopulate some tables
            insertPhoneInfo(phone);
        }


        /** ChargeSet chargeSet = new ChargeSet();
         chargeSet.setChargeSetManufacturer("Fenix");
         complete = insertChargeSetInfo(chargeSet); */

    }

    public String getSecurityTokenFromDB(String userName) {
        db = this.getReadableDatabase();

        String selection = VslaAdminUserProviderAPI.VslaAdminUserColumns.USER_NAME + "=?";
        String[] selectionArgs = {userName};
        Cursor userCursor = null;
        try {
            VslaAdminUser user = new VslaAdminUser();
            userCursor = db.query(TABLE_VSLA_ADMIN_USERS, null, selection, selectionArgs, null, null, null);
            userCursor.moveToFirst();
            while (!userCursor.isAfterLast()) {
                user.setSecurityToken(userCursor.getString(userCursor.getColumnIndex(VslaAdminUserProviderAPI.VslaAdminUserColumns.SECURITY_TOKEN)));
                userCursor.moveToNext();
            }
            userCursor.close();
            return user.getSecurityToken();

        } catch (Exception ex) {
            Log.e("Repository.getSecurityTokenFromDB", "" + ex.getMessage());
            return "";
        } finally {
            if (userCursor != null) {
                userCursor.close();
                db.close();
            }
        }
    }

    public int getAssetCount() {

        Cursor assetCursor = null;
        db = this.getWritableDatabase();

        try {
            assetCursor = db.query(TABLE_PHONES, null, null, null, null, null, null);
            if (assetCursor == null) {
                return 0;
            }
            return assetCursor.getCount();
        } catch (Exception ex) {
            Log.e("Repository.PhonesCount", "" + ex.toString());
            return 0;
        } finally {
            if (assetCursor != null) {
                assetCursor.close();
                db.close();
            }
        }
    }

    public KitDelivery getKitDeliveryInfoByVsla(String vslaCode) {
        db = this.getReadableDatabase();

        String selection = KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_CODE + "=?";
        String[] selectionArgs = {vslaCode};
        Cursor kitDeliveryCursor = null;
        try {
            kitDeliveryCursor = db.query(TABLE_KIT_DELIVERY, null, selection, selectionArgs, null, null, null);
            if (kitDeliveryCursor != null) {
                kitDeliveryCursor.moveToFirst();
                while (!kitDeliveryCursor.isAfterLast()) {
                    KitDelivery kitDelivery = new KitDelivery();
                    kitDelivery.setVslaPhoneMsisdn(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MSISDN)));
                    kitDelivery.setVslaPhoneManufacturer(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MANUFACTURER)));
                    kitDelivery.setVslaPhoneModel(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MODEL)));
                    kitDelivery.setVslaPhoneMsisdn(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_MSISDN)));
                    kitDelivery.setVslaPhoneImei(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_IMEI)));
                    kitDelivery.setVslaPhoneSerialNumber(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.VSLA_PHONE_SERIAL_NUMBER)));
                    kitDelivery.setRecipientRole(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.RECIPIENT_ROLE)));
                    kitDelivery.setReceivedBy(kitDeliveryCursor.getString(kitDeliveryCursor.getColumnIndex(KitDeliveryProviderAPI.KitDeliveryColumns.RECEIVED_BY)));
                    kitDeliveryCursor.moveToNext();
                    kitDeliveryCursor.close();
                    return kitDelivery;
                }
            }
            return null;
        } catch (Exception ex) {
            Log.e("DBHandler.getKitDeliveryInfoByVsla", "" + ex.getMessage());
            return null;
        } finally {
            if (kitDeliveryCursor != null) {
                kitDeliveryCursor.close();
                db.close();
            }
        }
    }
}