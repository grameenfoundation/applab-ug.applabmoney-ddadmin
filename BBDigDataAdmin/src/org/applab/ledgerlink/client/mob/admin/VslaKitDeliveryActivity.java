package org.applab.ledgerlink.client.mob.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;
import org.applab.ledgerlink.client.mob.admin.model.VslaKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for vsla_search
 */
public class VslaKitDeliveryActivity extends Activity implements LocationListener {

    LocationManager locationManager;
    Location location;
    String provider;
    Button btnSubmit;
    Button btnGPSCapture;
    EditText phoneNumber;
    EditText phoneImei;
    EditText chargeSetSN;
    Spinner phoneModel;
    Spinner phoneManufacturer;
    Spinner chargeSetManufacturer;
    EditText receivedBy;
    EditText vslaGPS;
    TextView vslaNameText;
    TextView vslaIdText;
    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * <p/>
     * menu.add("Save")
     * .setOnMenuItemClickListener(this.SubmitButtonClickListener)
     * .setIcon(R.drawable.ic_action_send)
     * .setTitle(R.string.labelSubmitForm)
     * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
     * menu.add("Cancel")
     * .setOnMenuItemClickListener(this.CancelButtonClickListener)
     * .setIcon(R.drawable.navigation_cancel)
     * .setTitle(R.string.labelCancel)
     * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
     * return super.onCreateOptionsMenu(menu);
     * }
     */

    // Capture submit action click
    OnMenuItemClickListener SubmitButtonClickListener = new OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {
            vslaKit = new VslaKit();
            vslaKit.setPhoneNumber(String.valueOf(phoneNumber.getText()).trim());
            vslaKit.setPhoneImei(String.valueOf(phoneImei.getText()).trim());
            vslaKit.setPhoneManufacturer(String.valueOf(phoneManufacturer.getSelectedItem()).trim());
            vslaKit.setPhoneModel(String.valueOf(phoneModel.getSelectedItem()).trim());
            vslaKit.setReceivedBy(String.valueOf(receivedBy.getText()));
            vslaKit.setChargeSetSerialNumber(String.valueOf(chargeSetSN.getText()).trim());
            vslaKit.setChargeSetManufacturer(String.valueOf(chargeSetManufacturer.getSelectedItem()).trim());

            if (validate()) {
                submitForm(vslaKit);
            } else {
                Toast.makeText(VslaKitDeliveryActivity.this, "Please Fill all the Required Fields", Toast.LENGTH_LONG).show();

            }
            // Do something else
            return false;
        }
    };
    // Capture cancel action click
    OnMenuItemClickListener CancelButtonClickListener = new OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {
            // Create a simple toast message
            Toast.makeText(VslaKitDeliveryActivity.this, "Cancel Button", Toast.LENGTH_SHORT)
                    .show();
            finish();
            // Do something else
            return false;
        }
    };
    private Vsla vsla;
    private VslaKit vslaKit;
    private DatabaseHandler dbHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_kit_delivery);
        getActionBar().setTitle(R.string.txtdeliverKitTitle);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Importing all assets like buttons, text fields
        vslaIdText = (TextView) findViewById(R.id.vslaCodeTxt);
        vslaNameText = (TextView) findViewById(R.id.vslaNameTxt);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        phoneImei = (EditText) findViewById(R.id.phoneImei);
        // chargeSetSN = (EditText) findViewById(R.id.chargeSetSerial);
        //chargeSetManufacturer = (Spinner) findViewById(R.id.chargeset_manufacturer_spinner);
        receivedBy = (EditText) findViewById(R.id.receivedBy);
        //phoneModel = (Spinner) findViewById(R.id.phone_model_spinner);
        phoneManufacturer = (Spinner) findViewById(R.id.phone_manufacturer_spinner);
        vslaGPS = (EditText) findViewById(R.id.vslaGPSTxt);


        btnSubmit = (Button) findViewById(R.id.deliver_kit_button);
        btnGPSCapture = (Button) findViewById(R.id.capture_gps_button);

        // Getting LocationManager object
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            // if (location != null) {
            //   onLocationChanged(location);
            //   } else {
            //     Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            //  }
        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }


        //String vslaId = ((String) getIntent().getExtras().get("vslaId")).trim();
        vsla = (Vsla) getIntent().getExtras().get("vsla");
        vslaIdText.setText(vsla.getVslaCode());
        vslaNameText.setText(vsla.getName());

        this.addItemsOnPhoneManufacturerSpinner();
        this.addItemsOnPhoneModelSpinner();
        this.addItemsOnChargeSetManufacturerSpinner();


        btnGPSCapture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                while (location == null) {
                    //          showToast(VslaKitDeliveryActivity.this, "Please wait while the GPS Location is captured");
                    onLocationChanged(location);
                }
                if (location != null) {
                    vslaGPS.setVisibility(View.VISIBLE);
                    vslaGPS.setText("Latitude: " + String.valueOf(location.getLatitude()) + " " + "Longitude: " + String.valueOf(location.getLongitude()) + " " + "Altitude: " + String.valueOf(location.getAltitude()) + " " + "Accuracy: " + String.valueOf(location.getAccuracy()));
                    btnGPSCapture.setVisibility(View.INVISIBLE);
                }
            }

        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /** String model = String.valueOf(phoneModel.getSelectedItem()).trim();
                 String manufacturer = String.valueOf(phoneManufacturer.getSelectedItem()).trim();
                 String phoneNo = String.valueOf(phoneNumber.getText()).trim();
                 String imei = String.valueOf(phoneVslaImei.getText()).trim();
                 String chargeSetSerialNo = String.valueOf(chargeSetSN.getText()).trim();
                 String chargeSetManuf = String.valueOf(chargeSetManufacturer.getSelectedItem()).trim();
                 String recipientName = String.valueOf(receivedBy.getText());
                 */
                vslaKit = new VslaKit();

                vslaKit.setPhoneNumber(String.valueOf(phoneNumber.getText()).trim());
                vslaKit.setPhoneImei(String.valueOf(phoneImei.getText()).trim());
                vslaKit.setPhoneManufacturer(String.valueOf(phoneManufacturer.getSelectedItem()).trim());
                vslaKit.setPhoneModel(String.valueOf(phoneModel.getSelectedItem()).trim());
                vslaKit.setReceivedBy(String.valueOf(receivedBy.getText()));
                vslaKit.setChargeSetSerialNumber(String.valueOf(chargeSetSN.getText()).trim());
                vslaKit.setChargeSetManufacturer(String.valueOf(chargeSetManufacturer.getSelectedItem()).trim());

                if (validate()) {
                    submitForm(vslaKit);
                } else {
                    Toast.makeText(VslaKitDeliveryActivity.this, "Please Fill all the Required Fields", Toast.LENGTH_LONG).show();

                }
            }

        });


    }

    // add items into spinner dynamically
    public void addItemsOnPhoneModelSpinner() {

        //phoneModel = (Spinner) findViewById(R.id.phone_model_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getPhoneModelItems());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneModel.setAdapter(dataAdapter);
    }

    private List<String> getPhoneModelItems() {
        List<String> phoneModelList = new ArrayList<String>();
        phoneModelList.add("IDEOS");
        phoneModelList.add("Samsung");
        phoneModelList.add("HTC");
        return phoneModelList;
    }

    ;

    // add items into spinner dynamically
    public void addItemsOnPhoneManufacturerSpinner() {

        phoneManufacturer = (Spinner) findViewById(R.id.phone_manufacturer_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getPhoneManufacturerItems());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneManufacturer.setAdapter(dataAdapter);
    }

    private List<String> getPhoneManufacturerItems() {
        List<String> phoneManufacturerList = new ArrayList<String>();
        phoneManufacturerList.add("U8150");
        phoneManufacturerList.add("SII");
        phoneManufacturerList.add("SIII");
        return phoneManufacturerList;
    }

    // add items into spinner dynamically
    public void addItemsOnChargeSetManufacturerSpinner() {

        //  chargeSetManufacturer = (Spinner) findViewById(R.id.chargeset_manufacturer_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getChargeSetManufacturerItems());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chargeSetManufacturer.setAdapter(dataAdapter);
    }

    private List<String> getChargeSetManufacturerItems() {
        List<String> chargeSetManufacturerList = new ArrayList<String>();
        chargeSetManufacturerList.add("FENIX");
        chargeSetManufacturerList.add("Katwe");
        return chargeSetManufacturerList;
    }

    /**
     * Check for empty form fields
     *
     * @return true if no empty field || false there is an empty field
     */
    private boolean validate() {
        boolean ret = true;
        if (!Validator.hasText(chargeSetSN)) ret = false;
        if (!Validator.hasText(phoneImei)) ret = false;
        if (!Validator.hasText(receivedBy)) ret = false;
        if (!Validator.isPhoneNumber(phoneNumber, false)) ret = false;

        return ret;
    }
    /**
     @Override public boolean onOptionsItemSelected(MenuItem item) {
     switch (item.getItemId()) {
     case android.R.id.home:

     Intent intent = new Intent(this, ListVslaActivity.class);
     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     startActivity(intent);
     break;
     }
     return super.onOptionsItemSelected(item);
     } */

    /**
     * If form is valid then submit it
     */
    private void submitForm(VslaKit vslaKit) {
        boolean submitSuccess;
        dbHandler = new DatabaseHandler(getApplicationContext());
        try {
            if (dbHandler == null) {
                Toast.makeText(this, "DB Handler is NULL", Toast.LENGTH_LONG).show();
            }
            submitSuccess = false;
        } catch (Exception ex) {
            submitSuccess = false;
            Toast.makeText(this, String.format("ERR: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
        }
        if (submitSuccess) {
            alertMessage();

        } else {
            // showToast(this, "Submitting form FAILED");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        location = location;
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

    public void alertMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Set title
        alertDialogBuilder.setTitle("Confirm");

        // Set dialog message
        alertDialogBuilder
                .setMessage("Confirm Kit Delivery Data!")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openDashboard = new Intent(VslaKitDeliveryActivity.this,
                                ListVslaActivity.class);
                        startActivity(openDashboard);
                    }
                });
        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}