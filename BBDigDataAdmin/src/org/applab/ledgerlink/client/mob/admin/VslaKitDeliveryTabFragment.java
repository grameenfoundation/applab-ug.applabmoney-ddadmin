package org.applab.ledgerlink.client.mob.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.*;
import org.applab.ledgerlink.client.mob.admin.tasks.DeliverKitTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VslaKitDeliveryTabFragment extends SherlockFragment {

    ActionBar actionBar;
    Vsla vsla;
    LocationManager locationManager;
    Location location;
    String provider;
    Button btnSubmit;
    Button btnGPSCapture;
    EditText phoneNumber;
    EditText phoneVslaImei;
    EditText chargeSetSN;
    EditText phoneModel;
    Spinner phoneManufacturer;
    EditText phoneSerialNumber;
    EditText receivedBy;
    EditText recipientRole;
    TextView vslaNameText;
    TextView vslaCodeText;
    ArrayAdapter<String> phoneMaunfacturerDataAdapter;
    VslaKit vslaKit;
    KitDelivery kitDelivery;
    String phoneImei;
    String request;
    private ArrayList<Phone> phonesDNAList;
    private ArrayList<ChargeSet> chargeSetDNAList;
    private String userName;
    private DatabaseHandler dbHandler;

    public static void postDeliverKit(JSONObject json) {

        Log.i("POSTDELIVERKIT", json.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.vsla_kit_delivery_fragtab, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setTitle(R.string.txtdeliverKitTitle);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        vslaCodeText = (TextView) getSherlockActivity().findViewById(R.id.vslaCodeTxt);
        vslaNameText = (TextView) getSherlockActivity().findViewById(R.id.vslaNameTxt);

        vsla = (Vsla) getSherlockActivity().getIntent().getExtras().get("vsla");

        vslaCodeText.setText(vsla.getVslaCode());
        vslaNameText.setText(vsla.getName());

        phoneNumber = (EditText) getSherlockActivity().findViewById(R.id.phoneNumberTxt);
        phoneVslaImei = (EditText) getSherlockActivity().findViewById(R.id.phoneImeiTxt);
        phoneModel = (EditText) getSherlockActivity().findViewById(R.id.phoneModelTxt);
        phoneManufacturer = (Spinner) getSherlockActivity().findViewById(R.id.phone_manufacturer_spinner);
        phoneSerialNumber = (EditText) getSherlockActivity().findViewById(R.id.phoneSerialNumberTxt);
        receivedBy = (EditText) getSherlockActivity().findViewById(R.id.receivedByTxt);
        recipientRole = (EditText) getSherlockActivity().findViewById(R.id.recipientRoleTxt);

        this.addItemsOnPhoneManufacturerSpinner();

        // Removed for the start as there are no chargesets available yet
        // chargeSetSN = (EditText) getSherlockActivity().findViewById(R.id.chargeSetSerial);
        // chargeSetManufacturer = (Spinner) getSherlockActivity().findViewById(R.id.chargeset_manufacturer_spinner);
        // chargeSetDNAList = this.pickChargeSetDNA();
        kitDelivery = new KitDelivery();
        kitDelivery = getKitDeliveryData(vsla);
        if (kitDelivery != null) {
            populateFields(kitDelivery);
            disableEdit();
        }
    }

    private KitDelivery getKitDeliveryData(Vsla vsla) {
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        try {
            kitDelivery = dbHandler.getKitDeliveryInfoByVsla(vsla.getVslaCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            kitDelivery = null;
        } finally {
            dbHandler.close();
        }
        return kitDelivery;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitData:
                kitDelivery = new KitDelivery();
                kitDelivery.setVslaPhoneMsisdn(String.valueOf(phoneNumber.getText()).trim());
                kitDelivery.setVslaPhoneImei(String.valueOf(phoneVslaImei.getText()).trim());
                kitDelivery.setVslaPhoneManufacturer(String.valueOf(phoneManufacturer.getSelectedItem()).trim());
                kitDelivery.setVslaPhoneSerialNumber(String.valueOf(phoneSerialNumber.getText()).trim());
                kitDelivery.setVslaPhoneModel(String.valueOf(phoneModel.getText()).trim());
                kitDelivery.setReceivedBy(String.valueOf(receivedBy.getText()));
                kitDelivery.setRecipientRole(String.valueOf(recipientRole.getText()).trim());
                kitDelivery.setVslaCode(String.valueOf(vslaCodeText.getText()).trim());

                userName = getSherlockActivity().getIntent().getStringExtra("userName");
                kitDelivery.setAdminUserName(userName);

                Date today = new Date();
                SimpleDateFormat ft =
                        new SimpleDateFormat("yyyy-MM-dd");
                kitDelivery.setDeliveryDate(ft.format(today));
                kitDelivery.setDeliveryStatus(1);

                phoneImei = GlobalVslaAdmin.getImei(getSherlockActivity());

                // Setup webservice request to deliver the phone
                request = setupRequestJSON(kitDelivery, phoneImei);

                if (validate()) {
                    submitForm(kitDelivery);
                }

                return true;
            case R.id.cancel:
                Intent vslaList = new Intent(getSherlockActivity(),
                        ListVslaActivity.class);
                startActivity(vslaList);
                return true;
            default:
                return false;
        }
    }

    private String setupRequestJSON(KitDelivery kitDelivery, String phoneImei) {

        JSONObject kitDeliveryJSON = new JSONObject();
        try {
            kitDeliveryJSON.put("VslaCode", kitDelivery.getVslaCode());
            kitDeliveryJSON.put("VslaPhoneMsisdn", kitDelivery.getVslaPhoneMsisdn());
            kitDeliveryJSON.put("AdminUserName", kitDelivery.getAdminUserName());
            kitDeliveryJSON.put("SecurityToken", getSecurityToken(kitDelivery.getAdminUserName()));
            kitDeliveryJSON.put("VslaPhoneImei", kitDelivery.getVslaPhoneImei());
            kitDeliveryJSON.put("PhoneImei", phoneImei);
            kitDeliveryJSON.put("PhoneSerialNumber", kitDelivery.getVslaPhoneSerialNumber());
            kitDeliveryJSON.put("PhoneManufacturer", kitDelivery.getVslaPhoneManufacturer());
            kitDeliveryJSON.put("PhoneModel", kitDelivery.getVslaPhoneModel());
            kitDeliveryJSON.put("ReceivedBy", kitDelivery.getReceivedBy());
            kitDeliveryJSON.put("RecipientRole", kitDelivery.getRecipientRole());
            kitDeliveryJSON.put("DeliveryDate", kitDelivery.getDeliveryDate());

            request = kitDeliveryJSON.toString();

        } catch (JSONException e) {
            //To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }

        return request;
        // return "";
    }

    private VslaKit pickVslaKitDeliveryInfo(Vsla vsla) {
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        return dbHandler.getKitByVsla(vsla.getVslaCode());
    }

    private String getImei() {
        return GlobalVslaAdmin.getImei(getSherlockActivity());
    }

    private String getSecurityToken(String userName) {
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        try {
            return dbHandler.getSecurityTokenFromDB(userName);
        } catch (Exception ex) {
            Log.e("Get Security Token", ex.getMessage());
            return "";
        }
    }

    /**
     * If form is valid then submit it
     */
    private void submitForm(KitDelivery kitDelivery) {
        boolean submitSuccess = false;
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        try {
            if (dbHandler == null) {
                Toast.makeText(getSherlockActivity(), "DB Handler is NULL", Toast.LENGTH_LONG).show();
            }
            if (kitDelivery.getDeliveryStatus() != 1) {
                submitSuccess = dbHandler.insertVslaKitDeliveryData(kitDelivery);
            }
            if (kitDelivery.getDeliveryStatus() != 2) {
                DeliverKitTask deliverKitTask = new DeliverKitTask();
                deliverKitTask.execute(request);
            }
            if (kitDelivery.getDeliveryStatus() == 1) {
                submitSuccess = true;
            }
        } catch (Exception ex) {
            Log.e("SubmitForm", ex.getMessage());
            submitSuccess = false;
        }
        if (submitSuccess) {
            try {
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                actionBar.setSelectedNavigationItem(1);
                //getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(1);


            } catch (Exception ex) {
                Log.i("AB", ex.getMessage());
            }

            //alertMessage(submitSuccess);

        } else {
            Toast.makeText(getSherlockActivity(), "Submitting form FAILED", Toast.LENGTH_LONG).show();
        }
    }

    public void alertMessage(boolean submitSuccess) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getSherlockActivity());

        if (submitSuccess) {

            // Set title
            alertDialogBuilder.setTitle("Confirm");

            // Set dialog message
            alertDialogBuilder
                    .setMessage("Confirm Kit Delivery Data!")
                    .setCancelable(true)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Log.i("TAG",actionBar.setSelectedNavigationItem(1)etTag().toString());
                            actionBar.setSelectedNavigationItem(1);

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
                    .setMessage("Kit Delivery Data Not Sent!")
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

    /**
     * Check for empty form fields
     *
     * @return true if no empty field || false there is an empty field
     */
    private boolean validate() {
        boolean ret = true;
        if (!Validator.hasText(phoneVslaImei)) ret = false;
        if (!Validator.hasText(receivedBy)) ret = false;
        if (!Validator.isPhoneNumber(phoneNumber, false)) ret = false;

        return ret;
    }

    // Add items into phone Manufacturer spinner dynamically
    public void addItemsOnPhoneManufacturerSpinner() {

        Log.i("addItemsOnPhoneManufacturerSpinner", "addItemsOnPhoneManufacturerSpinner");
        phoneManufacturer = (Spinner) getSherlockActivity().findViewById(R.id.phone_manufacturer_spinner);
        phoneMaunfacturerDataAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                android.R.layout.simple_spinner_item, getPhoneManufacturerItems());
        phoneMaunfacturerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneManufacturer.setAdapter(phoneMaunfacturerDataAdapter);
    }

    private List<String> getPhoneManufacturerItems() {
        phonesDNAList = this.pickPhoneDNA();
        List<String> phoneManufacturerList = new ArrayList<String>();
        Log.i("getPhoneManufacturerItems", "getPhoneManufacturerItems");
        Log.i("phoneList", String.valueOf(phonesDNAList.size()));
        if (phonesDNAList.size() != 0) {
            for (Phone phone : phonesDNAList) {
                phoneManufacturerList.add(phone.getPhoneManufacturer());
            }
            Log.i("getPhoneManufacturerItemsCOUNT", String.valueOf(phoneManufacturerList.size()));
        } else phoneManufacturerList.add("Watsupp");
        return phoneManufacturerList;
    }

    /**
     * Omitted for now as Chargesets are not available yet
     * // Add items into spinner dynamically
     * public void addItemsOnChargeSetManufacturerSpinner() {
     * <p/>
     * chargeSetManufacturer = (Spinner) getSherlockActivity().findViewById(R.id.chargeset_manufacturer_spinner);
     * <p/>
     * ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getSherlockActivity(),
     * android.R.layout.simple_spinner_item, getChargeSetManufacturerItems());
     * dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     * chargeSetManufacturer.setAdapter(dataAdapter);
     * }
     * <p/>
     * private List<String> getChargeSetManufacturerItems() {
     * List<String> chargeSetManufacturerList = new ArrayList<String>();
     * for (ChargeSet chargeSet : chargeSetDNAList) //use for-each loop
     * {
     * chargeSetManufacturerList.add(chargeSet.getChargeSetManufacturer());
     * }
     * <p/>
     * return chargeSetManufacturerList;
     * }
     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ArrayList<Phone> pickPhoneDNA() {
        Log.i("DBHandler.addPhone", "DBHandler.addPhone");
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        return dbHandler.getPhoneDNA();
    }

    public ArrayList<ChargeSet> pickChargeSetDNA() {
        dbHandler = new DatabaseHandler(getSherlockActivity().getApplicationContext());
        return dbHandler.getChargeSetDNA();
    }

    private void populateFields(KitDelivery kitDelivery) {
        phoneVslaImei.setText(kitDelivery.getVslaPhoneImei().toString());
        phoneManufacturer.setSelection(phoneMaunfacturerDataAdapter.getPosition(kitDelivery.getVslaPhoneManufacturer().toString()));
        phoneModel.setText(kitDelivery.getVslaPhoneModel().toString());
        phoneNumber.setText(kitDelivery.getVslaPhoneMsisdn().toString());
        phoneSerialNumber.setText(kitDelivery.getVslaPhoneSerialNumber().toString());
        receivedBy.setText(kitDelivery.getReceivedBy().toString());
        recipientRole.setText(kitDelivery.getRecipientRole().toString());
    }

    private void disableEdit() {
        phoneVslaImei.setEnabled(false);
        phoneVslaImei.setFocusable(false);
        phoneManufacturer.setEnabled(false);
        phoneManufacturer.setFocusable(false);
        phoneModel.setEnabled(false);
        phoneModel.setFocusable(false);
        phoneNumber.setEnabled(false);
        phoneNumber.setFocusable(false);
        phoneSerialNumber.setEnabled(false);
        phoneSerialNumber.setFocusable(false);
        receivedBy.setEnabled(false);
        receivedBy.setFocusable(false);
        recipientRole.setEnabled(false);
        recipientRole.setFocusable(false);
    }


}