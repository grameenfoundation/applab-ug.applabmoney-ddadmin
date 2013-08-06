package org.applab.digdata.client.mob.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.applab.digdata.client.mob.admin.database.DatabaseHandler;
import org.applab.digdata.client.mob.admin.model.Vsla;


/**
 * Activity for vsla_search
 */
public class FindVslaActivity extends DashboardActivity {

    private DatabaseHandler dbHandler;
    private Button searchButton;
    private Vsla vsla;
    private EditText vslaIdText;
    private boolean isValid;

    /**
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_search);
        setHeader(true, false);

        try {
            dbHandler = new DatabaseHandler(getApplicationContext());
            int vslaCount = dbHandler.getVslaCount();
            Log.i("FindVsla vsla count", "" + String.valueOf(vslaCount));
            if (vslaCount < 4) {
                dbHandler.insertDefaultData();
            }

            // Importing all assets like buttons, text fields
            this.vslaIdText = (EditText) findViewById(R.id.vslaIdText);
            this.searchButton = (Button) findViewById(R.id.search_button);

            // Add a click listener to the "Search" button
            this.searchButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    validate();
                    if (isValid) {
                        Intent openDashboard = new Intent(FindVslaActivity.this,
                                VslaAdminDashboardActivity.class);
                        openDashboard.putExtra("vsla", vsla);
                        startActivity(openDashboard);
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("FindVsla vsla count", "" + ex.toString());
        }
    }

    private void validate() {
        if (vslaIdText.getText().toString().trim().isEmpty()) {
            showToast(FindVslaActivity.this, "Please enter the VSLA ID!!");
            isValid = false;
        } else {
            vsla = dbHandler.getVslaFromDbById(vslaIdText.getText().toString().trim());
            if (null == vsla.getId()) {
                isValid = false;
                showToast(FindVslaActivity.this, "Invalid VSLAID!!");
            } else {
                isValid = true;
            }
        }
    }
}