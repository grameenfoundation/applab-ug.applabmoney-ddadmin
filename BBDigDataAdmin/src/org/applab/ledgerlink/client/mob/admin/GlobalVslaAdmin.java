package org.applab.ledgerlink.client.mob.admin;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class containing universal functions to be performed by the VSLA Administrator
 */
public class GlobalVslaAdmin {


    public static String getImei(Activity activity) {

        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static void activateDDAdminUser(String imei, String code) {


    }

    private void connect() {
        String URL = "http://74.208.213.214:9905/DigitizingDataRestfulService.svc/admin/getvslas/7";

        JSONObject json = null;

        DefaultHttpClient httpclient = new DefaultHttpClient();

        try {

            // Prepare a request object
            HttpGet httpget = new HttpGet(URL);

            // Execute the request
            HttpResponse response = httpclient.execute(httpget);
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }

    // Method to check that user is valid and credentials provided are legit
    public JSONObject loginUser(String email, String password) {


        return null;
    }

    public void logoutUser(Context applicationContext) {
    }

    private class Connection extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

    }

    public static final class ActivationCodes {

        public static final int ACTIVATION_SUCCESSFUL = 0;
        public static final int UNREGISTERED_CBT = 1;
        public static final int WRONG_SECURITY_TOKEN = 2;
        public static final int DIFFERENT_PHONE_USED = 4;
        public static final int DATA_VALIDATION_FAILURE = 8;
        public static final int UNKNOWN_CAUSE = 99;
    }

    public static final class ActivationStatus {

        public static final String ACTIVATION_SUCCESSFUL = "Activation was Successful";
        public static final String UNREGISTERED_CBT = "This CBT’s username is not registered on the back-office system.";
        public static final String WRONG_SECURITY_TOKEN = "The CBT’s username exists on the back-office system but, he entered the wrong Security Token";
        public static final String DIFFERENT_PHONE_USED = "The CBT used a different phone handset to activate instead of the one issued to him.";
        public static final String DATA_VALIDATION_FAILURE = "Data validation failed e.g. invalid or null values were sent for mandatory fields like Password";
        public static final String UNKNOWN_CAUSE = "Activation failed with unknown technical error e.g. invalid JSON String or request parameters";
    }


    public static final class KitDeliveryStatus {


    }


}