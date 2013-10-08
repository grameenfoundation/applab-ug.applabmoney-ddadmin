package org.applab.ledgerlink.client.mob.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;
import org.applab.ledgerlink.client.mob.admin.tasks.RecordLocationTask;
import org.json.JSONException;
import org.json.JSONObject;

public class VslaLocationRecordTabFragment extends SherlockFragment implements LocationListener {

    ActionBar actionBar;
    TextView vslaNameText;
    TextView vslaCodeText;
    EditText vslaGPS;
    private DatabaseHandler dbHandler;
    private LocationManager locationManager;
    private Location vslaLocation;
    private String provider;
    private Vsla vsla;
    private String userName;
    private String request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.vsla_location_record_fragtab, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        actionBar = getSherlockActivity().getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(R.string.recordLocationTxt);


        vslaGPS = (EditText) getSherlockActivity().findViewById(R.id.vslaGPSTxt);
        vslaCodeText = (TextView) getSherlockActivity().findViewById(R.id.vslaCodeTxt);
        vslaNameText = (TextView) getSherlockActivity().findViewById(R.id.vslaNameTxt);

        vslaGPS.setText("null location");
        vsla = (Vsla) getSherlockActivity().getIntent().getExtras().get("vsla");
        userName = getSherlockActivity().getIntent().getStringExtra("userName");

        vslaCodeText.setText(vsla.getVslaCode());
        vslaNameText.setText(vsla.getName());

        // Getting LocationManager object
        locationManager = (LocationManager) getSherlockActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            locationManager.requestLocationUpdates(provider, 60000, 1, this);

            // Get the vslaLocation from the given provider
            vslaLocation = locationManager.getLastKnownLocation(provider);

            if (vslaLocation == null) {
                vslaGPS.setText("Location Not found yet. Please wait");
                //onLocationChanged(vslaLocation);
            } else {
                vslaGPS.setText("Latitude: " + String.valueOf(vslaLocation.getLatitude()) + "\r\n"
                        + "Longitude: " + String.valueOf(vslaLocation.getLongitude()) + "\r\n"
                        + "Altitude: " + String.valueOf(vslaLocation.getAltitude()) + "\r\n"
                        + "Accuracy: " + String.valueOf(vslaLocation.getAccuracy()));
            }
        } else {
            vslaGPS.setText("GPS Provider Not Found. Please turn on your GPS radio in case it is off and then try again");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitData:
                if (vslaGPS.getText().toString().trim() != null) {
                    vsla.setGpsLocation(vslaGPS.getText().toString().trim());
                    submitDataToLocalCache(vsla);
                    submitDataToServer(vsla.getGpsLocation());

                }
                return true;
            case R.id.cancel:
                Intent openDashboard = new Intent(getSherlockActivity(),
                        ListVslaActivity.class);
                startActivity(openDashboard);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submitDataToServer(String gpsLocation) {
        RecordLocationTask recordLocationTask = new RecordLocationTask();

        recordLocationTask.execute(request);
    }

    private void prepareServerRequest(String gpsLocation) {

        JSONObject locationRecordJSON = new JSONObject();
        try {
            locationRecordJSON.put("VslaCode", vsla.getVslaCode());
            locationRecordJSON.put("AdminUserName", userName);
            locationRecordJSON.put("SecurityToken", getSecurityToken(userName));
            locationRecordJSON.put("PhoneImei", getImei());
            locationRecordJSON.put("GpsLocation", gpsLocation);

            request = locationRecordJSON.toString();

        } catch (JSONException e) {
            request = "";
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //return request;
    }


    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getSherlockActivity().getBaseContext(), location.toString(), Toast.LENGTH_SHORT).show();
        vslaLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    /**
     * If form is valid then submit it
     */
    private void submitDataToLocalCache(Vsla vsla) {
        boolean submitSuccess;
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        try {
            if (dbHandler == null) {
                Toast.makeText(getSherlockActivity(), "DB Handler is NULL", Toast.LENGTH_LONG).show();
            }
            submitSuccess = dbHandler.updateVsla(vsla);

        } catch (Exception ex) {
            submitSuccess = false;
        }
        if (submitSuccess) {
            //alertMessage(submitSuccess);
            Intent vslaList = new Intent(getSherlockActivity(),
                    ListVslaActivity.class);
            // Get UserName
            vslaList.putExtra("userName", userName);

            // Get VSLA list from local cache
            vslaList.putExtra("vslaList", dbHandler.getAllVslasFromDb());
            startActivity(vslaList);

        } else {
            Toast.makeText(getSherlockActivity(), "Submitting form FAILED", Toast.LENGTH_LONG).show();
        }
    }

    private String getImei() {
        return GlobalVslaAdmin.getImei(getSherlockActivity());
    }

    private String getSecurityToken(String userName) {
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        return dbHandler.getSecurityTokenFromDB(userName);
    }

    public void alertMessage(boolean submitSuccess) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getSherlockActivity());

        if (submitSuccess) {
            // Set title
            alertDialogBuilder.setTitle("Confirm");

            // Set dialog message
            alertDialogBuilder
                    .setMessage("Confirm Vsla Location Info!")
                    .setCancelable(true)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent vslaList = new Intent(getSherlockActivity(),
                                    ListVslaActivity.class);
                            startActivity(vslaList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        } else {
            // Set title
            alertDialogBuilder.setTitle("Sending Failed");

            // Set Dialog Message for
            alertDialogBuilder
                    .setMessage("Vsla Location Data Not Sent!")
                    .setCancelable(false)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }

}