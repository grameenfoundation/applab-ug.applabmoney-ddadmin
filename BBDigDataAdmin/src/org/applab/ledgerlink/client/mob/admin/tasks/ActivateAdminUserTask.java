package org.applab.ledgerlink.client.mob.admin.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.applab.ledgerlink.client.mob.admin.LoginActivity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ActivateAdminUserTask extends AsyncTask<String, Void, Integer> {

    private final String URL = "http://74.208.213.214:9905/DigitizingDataRestfulService.svc/admin/getvslas/7";
    JSONObject json = null;
    HttpClient httpclient = new DefaultHttpClient();
    private ProgressDialog progressDialog;
    private LoginActivity activity;
    private int id = -1;
    int executeCount = 0;
    StringBuilder sb = new StringBuilder();
    public static int responseCode = 0;


    public ActivateAdminUserTask(LoginActivity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(String... arg0) {

        String result = "";
        try {
            // Prepare a request object
            HttpGet httpget = new HttpGet(URL);

            // Execute the request
            HttpResponse response;


            //int executeCount = 0;
            do {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        progressDialog.setMessage("Activating in.. (" + (executeCount + 1) + "/5)");
                    }
                });

                // Execute HTTP Post Request
                executeCount++;
                response = httpclient.execute(httpget);

                // Get the response entity
                HttpEntity entity = response.getEntity();

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
                }
            } while (executeCount < 5 && responseCode == 408);
        } catch (Exception e) {
            responseCode = 408;
            e.printStackTrace();
            Log.i("AdminTasks", e.getMessage().toString());
        }
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer headerCode) {
        progressDialog.dismiss();
        Log.i("AdminTask", headerCode.toString());
        if (headerCode == 200) {
            /** try {
             activity.postActivateVslaAdminUser(json);
             } catch (JSONException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             Log.i("AdminTaskPost", e.getMessage().toString());
             } */
        } else {
            activity.errorMessaging();
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
