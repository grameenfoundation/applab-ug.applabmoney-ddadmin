package org.applab.digdata.client.mob.admin;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import org.applab.digdata.client.mob.admin.model.Vsla;
import org.applab.digdata.client.mob.admin.model.VslaKit;
import org.applab.digdata.client.mob.admin.provider.VslaKitProviderAPI;
import org.applab.digdata.client.mob.admin.provider.VslaProviderAPI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Repository {

    /**
     * the storage class to act as singleton
     */
    private static Repository REPO = null;
    /**
     * the application context in which we are working
     */
    private final Context context;

    public Repository(Context context) {
        this.context = context;
        REPO = this;
    }

    public static Vsla getVslaFromDb(String vslaId) {

        List<Vsla> vslas = new ArrayList<Vsla>();
        Vsla vsla = new Vsla();

        String selection = VslaProviderAPI.VslaColumns.VSLA_ID + "=?";
        String[] selectionArgs = {vslaId};
        Cursor vslaCursor = null;
        try {
            vslaCursor = DigitizingDataApplication.getInstance().getContentResolver().query(VslaProviderAPI.VslaColumns.CONTENT_URI, null, selection, selectionArgs, null);
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
            if (vslaCursor != null) {
                vslaCursor.close();
            }
        }
    }

    public boolean addVslaKitDeliveryInfo(VslaKit vslaKit) {
        try {
            ContentValues values = new ContentValues();
            values.put(VslaKitProviderAPI.VslaKitColumns.VSLA_ID, vslaKit.getVslaId());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_NUMBER, vslaKit.getPhoneNumber());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_IMEI, vslaKit.getPhoneImei());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_MODEL, vslaKit.getPhoneModel());
            values.put(VslaKitProviderAPI.VslaKitColumns.PHONE_MANUFACTURER, vslaKit.getPhoneManufacturer());
            values.put(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_MANUFACTURER, vslaKit.getChargeSetSerialNumber());
            values.put(VslaKitProviderAPI.VslaKitColumns.CHARGE_SET_SERIAL_NUMBER, vslaKit.getChargeSetManufacturer());
            values.put(VslaKitProviderAPI.VslaKitColumns.RECEIVED_BY, vslaKit.getReceivedBy());
            Uri retVal = DigitizingDataApplication.getInstance().getContentResolver().insert(VslaProviderAPI.VslaColumns.CONTENT_URI, values);

            if (retVal != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("Repository.addVsla", "" + ex.getMessage());
            return false;
        }
    }

    public boolean addVslaInfo(@NotNull Vsla vsla) {
        try {
            ContentValues values = new ContentValues();
            values.put(VslaProviderAPI.VslaColumns.VSLA_ID, vsla.getId());
            values.put(VslaProviderAPI.VslaColumns.VSLA_NAME, vsla.getName());
            values.put(VslaProviderAPI.VslaColumns.VSLA_PASS_KEY, vsla.getPasskey());
            Uri retVal = DigitizingDataApplication.getInstance().getContentResolver().insert(VslaProviderAPI.VslaColumns.CONTENT_URI, values);

            if (retVal != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.e("Repository.addVsla", "" + ex.getMessage());
            return false;
        }
    }

    // Getting FinancialPledges Count
    public int getVslaCount() {
        Cursor vslaCursor = null;

        try {
            Log.i("VSLA DB COUNT", "getting vslaCursor mi");

            ContentResolver resolver = DigitizingDataApplication.getInstance().getContentResolver();
            Log.i("VSLA DB COUNT", "resolver CHECK");

            Uri uri = VslaProviderAPI.VslaColumns.CONTENT_URI;
            Log.i("VSLA DB COUNT", "URI CHECK");

            vslaCursor = resolver.query(uri, null, null, null, null);
            Log.i("VSLA DB COUNT", "cursor CHECK");
            //vslaCursor = DigitizingDataApplication.getInstance().getContentResolver().query(VslaProviderAPI.VslaColumns.CONTENT_URI, null, null, null, null);
            Log.i("VSLA DB COUNT", String.valueOf(vslaCursor.getCount()));

            // return count
            return vslaCursor.getCount();
        } catch (Exception ex) {
            Log.e("Repository.VslaCount", "" + ex.toString());
            return 0;
        } finally {
            if (vslaCursor != null) {
                vslaCursor.close();
            }
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
        vsla.setName("God gives");
        vsla.setPasskey("3576tyw");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BC1345");
        vsla.setName("Hardwork Pays");
        vsla.setPasskey("w476tyw");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BB3437");
        vsla.setName("Invest wisely");
        vsla.setPasskey("1276trg");
        addVslaInfo(vsla);

        vsla = new Vsla();
        vsla.setId("BC7845");
        vsla.setName("We are humble");
        vsla.setPasskey("ty6t37");
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