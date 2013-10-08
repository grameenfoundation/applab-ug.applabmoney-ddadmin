package org.applab.ledgerlink.client.mob.admin;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;
import org.applab.ledgerlink.client.mob.admin.model.VslaAdminUser;
import org.applab.ledgerlink.client.mob.admin.tasks.ActivateAdminTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivateDDAdminActivity extends Activity {

    EditText userName;
    EditText activationToken;
    EditText password_text;
    EditText confirmPassword;
    TextView activationError;
    Button activate;
    Button activatePassword;
    String sec_token;
    private String user_Name;
    private String password;
    private String confirm_password;
    private String token;
    private DatabaseHandler dbHandler;
    private boolean nextScreen = false;
    private ArrayList<Vsla> vslaList;
    private JSONObject response;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextScreen = Boolean.valueOf(getIntent().getStringExtra("nextScreen"));
        if (!nextScreen) {
            setContentView(R.layout.admin_activate);
            activate = (Button) findViewById(R.id.btnActivate);


            userName = (EditText) findViewById(R.id.userName);
            activationToken = (EditText) findViewById(R.id.activationToken);
            activationError = (TextView) findViewById(R.id.activation_error);

            activate.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    user_Name = String.valueOf(userName.getText()).trim();
                    token = String.valueOf(activationToken.getText()).trim();
                    activate(user_Name, token);
                }
            });
        } else {
            setContentView(R.layout.admin_activate_password);
            activatePassword = (Button) findViewById(R.id.btnActivatePassword);
            password_text = (EditText) findViewById(R.id.password);
            confirmPassword = (EditText) findViewById(R.id.confirmPassword);
            activationError = (TextView) findViewById(R.id.activation_error);

            activatePassword.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    password = String.valueOf(password_text.getText()).trim();
                    confirm_password = String.valueOf(confirmPassword.getText()).trim();
                    if (confirmPasswordMatch(password, confirm_password)) {
                        try {
                            activateUserPassword();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("AdminActivity", e.getMessage().toString());
                        }
                    } else {
                        activationError.setText("Passwords do not Match");
                    }
                }
            });
        }
    }

    private void activate(String u_name, String a_token) {

        Intent activatePassword = new Intent(this, ActivateDDAdminActivity.class);
        activatePassword.putExtra("nextScreen", "true");
        activatePassword.putExtra("token", a_token);
        activatePassword.putExtra("userName", u_name);
        startActivity(activatePassword);
    }

    private void activateUserPassword() throws JSONException {

        user_Name = getIntent().getStringExtra("userName");
        sec_token = getIntent().getStringExtra("token");
        String imei = GlobalVslaAdmin.getImei(this);

        JSONObject request = new JSONObject();
        request.put("PhoneImei", imei);
        request.put("AdminUserName", user_Name);
        request.put("AdminPassword", password);
        request.put("SecurityToken", sec_token);

        ProgressDialog progressDialog = new ProgressDialog(ActivateDDAdminActivity.this);
        progressDialog.setMessage("Activating...");
        progressDialog.setCancelable(false);

        ActivateAdminTask activateAdminTask = new ActivateAdminTask(ActivateDDAdminActivity.this, progressDialog);

        activateAdminTask.execute(request.toString());
    }

    private boolean confirmPasswordMatch(String firstPassword, String secondPassword) {
        if (firstPassword.equals(secondPassword))
            return true;
        else
            return false;
    }

    public void postActivateVslaAdminUser(JSONObject response) throws JSONException {
        this.response = response;
        int activationStatus = getActivationStatus(response);
        processActivationStatus(activationStatus);
    }

    private void updateVslaAdminUserLocalCache() {
        int userActivationStatus = 1;
        dbHandler = new DatabaseHandler(getApplicationContext());
        VslaAdminUser vslaAdminUser = new VslaAdminUser(user_Name, password, sec_token, userActivationStatus);
        dbHandler.addAdminUser(vslaAdminUser);
    }

    private void processActivationStatus(int activationStatus) throws JSONException {
        switch (activationStatus) {
            case GlobalVslaAdmin.ActivationCodes.ACTIVATION_SUCCESSFUL:
                updateVslaAdminUserLocalCache();
                vslaList = prepareVslaList();
                populateLocalVslaCache(vslaList);
                launchVslaListing();
                break;
            case GlobalVslaAdmin.ActivationCodes.UNREGISTERED_CBT:
                activationError.setText(GlobalVslaAdmin.ActivationStatus.UNREGISTERED_CBT + "Activation not successful. Please try again!");
                break;
            case GlobalVslaAdmin.ActivationCodes.DATA_VALIDATION_FAILURE:
                activationError.setText(GlobalVslaAdmin.ActivationStatus.DATA_VALIDATION_FAILURE + "Activation not successful. Please try again!");
                break;
            case GlobalVslaAdmin.ActivationCodes.DIFFERENT_PHONE_USED:
                activationError.setText(GlobalVslaAdmin.ActivationStatus.DIFFERENT_PHONE_USED + "Activation not successful.");
                break;
            case GlobalVslaAdmin.ActivationCodes.WRONG_SECURITY_TOKEN:
                activationError.setText(GlobalVslaAdmin.ActivationStatus.WRONG_SECURITY_TOKEN + "Activation not successful. Please try again!");
                break;
            case GlobalVslaAdmin.ActivationCodes.UNKNOWN_CAUSE:
                activationError.setText(GlobalVslaAdmin.ActivationStatus.UNKNOWN_CAUSE + "Activation not successful. Please try again!");
                break;
        }
    }

    private void launchVslaListing() {
        dbHandler = new DatabaseHandler(getApplicationContext());
        Intent listVsla = new Intent(getApplicationContext(), ListVslaActivity.class);

        // Get UserName
        listVsla.putExtra("userName", user_Name);

        // Get VSLA list from local cache
        listVsla.putExtra("vslaList", dbHandler.getAllVslasFromDb());

        // Close all views before launching Search screen
        listVsla.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(listVsla);
    }


    private int getActivationStatus(JSONObject vslaJSONObject) throws JSONException {

        return (Integer) vslaJSONObject.get("ActivationStatus");
    }

    public ArrayList<Vsla> prepareVslaList() throws JSONException {

        vslaList = new ArrayList<Vsla>();
        JSONArray vslaJSONArray = (JSONArray) this.response.get("VslaList");

        for (int i = 0; i < vslaJSONArray.length(); i++) {
            JSONObject jsonObject = vslaJSONArray.getJSONObject(i);
            Vsla vsla = new Vsla();
            vsla.setName(jsonObject.get("VslaName").toString());
            vsla.setVslaCode(jsonObject.get("VslaCode").toString());
            vslaList.add(vsla);
        }
        return vslaList;
    }

    public void populateLocalVslaCache(ArrayList<Vsla> vslaListing) {
        dbHandler = new DatabaseHandler(getApplicationContext());
        for (Vsla vsla : vslaListing) {
            dbHandler.insertVslaInfo(vsla);
        }
    }

    public void errorMessaging() {

        Log.i("Error", "Error");
    }

}
