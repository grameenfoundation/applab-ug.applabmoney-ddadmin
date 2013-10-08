package org.applab.ledgerlink.client.mob.admin.tasks;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.applab.ledgerlink.client.mob.admin.VslaKitDeliveryTabFragment;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RecordLocationTask extends AsyncTask<String, Void, Integer> {

    public static int responseCode = 0;
    private final String URL = "http://74.208.213.214:9905/DigitizingDataRestfulService.svc/admin/capturegps";
    JSONObject json = null;
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httpPost;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Integer doInBackground(String... arg0) {
        String result = "";
        try {
            // Prepare a request object
            httpPost = new HttpPost(URL);

            // Execute the request
            HttpResponse response;
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
                //responseCode = 0;
            } else {
                responseCode = 408;
            }

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
//        Log.i("AdminTask POSTEx", headerCode.toString() + " " + json.toString());
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