package org.applab.ledgerlink.client.mob.admin;

import android.os.AsyncTask;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncListViewLoader extends AsyncTask<String, Void, List<Vsla>> {
    //private final ProgressDialog dialog = new ProgressDialog(ListVslaActivity.this);
    private VslaListArrayAdapter vslaListAdpt;

    @Override
    protected void onPostExecute(List<Vsla> result) {
        super.onPostExecute(result);
        //dialog.dismiss();
        vslaListAdpt.setValues(result);
        vslaListAdpt.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialog.setMessage("Downloading contacts...");
        //dialog.show();
    }

    @Override
    protected List<Vsla> doInBackground(String... params) {
        List<Vsla> result = new ArrayList<Vsla>();

        try {
            URL u = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();
            InputStream is = conn.getInputStream();

            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(b) != -1)
                baos.write(b);

            String JSONResp = new String(baos.toByteArray());

            JSONArray arr = new JSONArray(JSONResp);
            for (int i = 0; i < arr.length(); i++) {
                result.add(convertVsla(arr.getJSONObject(i)));
            }

            return result;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private Vsla convertVsla(JSONObject obj) throws JSONException {
        String name = obj.getString("vslaCode");
        String surname = obj.getString("vslaName");

        return new Vsla(name, surname);
    }

}