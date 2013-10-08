package org.applab.ledgerlink.client.mob.admin.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.applab.ledgerlink.client.mob.admin.VslaAdminFunctionsActivity;
import org.applab.ledgerlink.client.mob.admin.VslaKitDeliveryTabFragment;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeliverKitTask extends AsyncTask<String, Void, Integer> {

    public static int responseCode = 0;
    private final String URL = "http://74.208.213.214:9905/DigitizingDataRestfulService.svc/admin/deliverkit";
    JSONObject json = null;
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httpPost;
    int executeCount = 0;
    StringBuilder sb = new StringBuilder();
    private ProgressDialog progressDialog;
    private VslaAdminFunctionsActivity activity;
    VslaKitDeliveryTabFragment kitDeliverFragment;
    private int id = -1;

    /**
     * public DeliverKitTask(VslaAdminFunctionsActivity activity, ProgressDialog progressDialog) {
     * this.activity = activity;
     * this.progressDialog = progressDialog;
     * }
     */

    @Override
    protected void onPreExecute() {
        //progressDialog.show();
    }


    @Override
    protected Integer doInBackground(String... arg0) {

        String result = "";
        try {
            // Prepare a request object
            httpPost = new HttpPost(URL);

            // Execute the request
            HttpResponse response;

            //int executeCount = 0;
            /** do {
             activity.runOnUiThread(new Runnable() {
            @Override public void run() {
            // This code will always run on the UI thread, therefore is safe to modify UI elements.
            progressDialog.setMessage("Activating.. (" + (executeCount + 1) + "/5)");
            }
            });

             // Execute HTTP Post Request
             executeCount++;  */

            StringEntity se = new StringEntity(arg0[0]);
            Log.i("AdminTaskARG", arg0[0]);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));
            httpPost.setEntity(se);

            // Execute HTTP Post Request
            response = httpclient.execute(httpPost);


                    /*Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                Log.i("AdminTaskIN", in.toString());
                result = convertStreamToString(in);
                json = new JSONObject(result);
                Log.i("AdminTaskRES", result.toString());
            }
            /** }

             // If response entity is not null
             if (entity != null) {
             // get entity contents and convert it to string
             InputStream instream = entity.getContent();
             result = convertStreamToString(instream);
             Log.i("AdminTask", result.toString());
             // construct a JSON object with result
             json = new JSONObject(result);
             responseCode = response.getStatusLine().getStatusCode();
             // If you want to see the response code, you can Log it
             // out here by calling:
             // Log.d("256 Design", "statusCode: " + responseCode)
             } */
            //} while (executeCount < 5 && responseCode == 408);
        } catch (Exception e) {
            responseCode = 408;
            e.printStackTrace();
            Log.i("AdminTasks", e.getMessage().toString());
        }
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer headerCode) {
        //progressDialog.dismiss();
        Log.i("AdminTask POSTEx", headerCode.toString() + " " + json.toString());
        if (headerCode == 0) {
            VslaKitDeliveryTabFragment.postDeliverKit(json);
        }
    }

    private String convertStreamToString(InputStream instream) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("GetVslasTask", sb.toString());
        return sb.toString();
    }
}