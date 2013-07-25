package org.applab.digdata.client.mob.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import org.applab.digdata.client.mob.admin.database.DatabaseHandler;
import org.applab.digdata.client.mob.admin.model.Vsla;
import org.applab.digdata.client.mob.admin.model.VslaKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for vsla_search
 */
public class VslaKitDeliveryActivity extends DashboardActivity {

    Button btnSubmit;
    EditText phoneNumber;
    EditText phoneImei;
    EditText chargeSetSN;
    Spinner phoneModel;
    Spinner phoneManufacturer;
    Spinner chargeSetManufacturer;
    EditText receivedBy;
    TextView vslaNameText;
    TextView vslaIdText;
    private Vsla vsla;
    private DatabaseHandler dbHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_kit_delivery);
        setHeader(true, false);

        // Importing all assets like buttons, text fields
        vslaIdText = (TextView) findViewById(R.id.vslaIdTxt);
        vslaNameText = (TextView) findViewById(R.id.vslaNameTxt);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        phoneImei = (EditText) findViewById(R.id.phoneImei);
        chargeSetSN = (EditText) findViewById(R.id.chargeSetSerial);
        chargeSetManufacturer = (Spinner) findViewById(R.id.chargeset_manufacturer_spinner);
        btnSubmit = (Button) findViewById(R.id.deliver_kit_button);
        receivedBy = (EditText) findViewById(R.id.receivedBy);
        phoneModel = (Spinner) findViewById(R.id.phone_model_spinner);
        phoneManufacturer = (Spinner) findViewById(R.id.phone_manufacturer_spinner);

        //String vslaId = ((String) getIntent().getExtras().get("vslaId")).trim();
        vsla = (Vsla) getIntent().getExtras().get("vsla");
        Log.i("ONCREATE", vsla.getId());
        vslaIdText.setText(vsla.getId());
        vslaNameText.setText(vsla.getName());
        Log.i("ONCREATE", vslaIdText.getText().toString());

        //loginErrorMsg = (TextView) findViewById(R.id.login_error);

        // Spinner click listener
        //phoneModel.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        //phoneManufacturer.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        this.addItemsOnPhoneManufacturerSpinner();
        this.addItemsOnPhoneModelSpinner();
        this.addItemsOnChargeSetManufacturerSpinner();

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /** String model = String.valueOf(phoneModel.getSelectedItem()).trim();
                 String manufacturer = String.valueOf(phoneManufacturer.getSelectedItem()).trim();
                 String phoneNo = String.valueOf(phoneNumber.getText()).trim();
                 String imei = String.valueOf(phoneImei.getText()).trim();
                 String chargeSetSerialNo = String.valueOf(chargeSetSN.getText()).trim();
                 String chargeSetManuf = String.valueOf(chargeSetManufacturer.getSelectedItem()).trim();
                 String recipientName = String.valueOf(receivedBy.getText());
                 */
                VslaKit vslaKit = new VslaKit();
                vslaKit.setVslaId(String.valueOf(vslaIdText.getText()).trim());
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

        phoneModel = (Spinner) findViewById(R.id.phone_model_spinner);

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

        chargeSetManufacturer = (Spinner) findViewById(R.id.chargeset_manufacturer_spinner);

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
     * If form is valid then submit it
     */
    private void submitForm(VslaKit vslaKit) {
        boolean submitSuccess;
        dbHandler = new DatabaseHandler(getApplicationContext());
        try {
            if (dbHandler == null) {
                Toast.makeText(this, "DB Handler is NULL", Toast.LENGTH_LONG).show();
            }
            submitSuccess = dbHandler.addVslaKitDeliveryData(vslaKit);
        } catch (Exception ex) {
            submitSuccess = false;
            Toast.makeText(this, String.format("ERR: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
        }
        if (submitSuccess) {
            // Submit data on form

            // Toast.makeText(this, "Submitting form...", Toast.LENGTH_SHORT).show();
            alertMessage();

        } else {
            showToast(this, "Submitting form FAILED");
        }
    }

    public void alertMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Set title
        alertDialogBuilder.setTitle("Confirm");

        // Set dialog message
        alertDialogBuilder
                .setMessage("Kit Delivery Done!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent openDashboard = new Intent(VslaKitDeliveryActivity.this,
                                FindVslaActivity.class);
                        startActivity(openDashboard);
                    }
                });
        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}