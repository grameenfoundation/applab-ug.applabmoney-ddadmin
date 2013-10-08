package org.applab.ledgerlink.client.mob.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;

/**
 *
 */
public class VslaPassKeyGenerateActivity extends Activity {

    DatabaseHandler dbHandler;
    private Button acceptPassKeyButton;
    private Vsla vsla;
    private TextView passKey;
    private TextView vslaCode;

    /**
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_pass_key);

        vsla = (Vsla) getIntent().getExtras().get("vsla");
        if (null != vsla) {
            Log.i("Passkey Activity", vsla.getName());
        }
        try {

            // Importing all assets like buttons, text fields
            this.passKey = (TextView) findViewById(R.id.passKeyTxt);
            this.vslaCode = (TextView) findViewById(R.id.vslaCodeTxt);
            this.acceptPassKeyButton = (Button) findViewById(R.id.accept_passkey_button);
            vslaCode.setText(vsla.getVslaCode().trim());
            if (null != generateVslaPasskey().trim()) {
                passKey.setText(generateVslaPasskey().trim());

                // add a click listener to the "Accept Passkey" button
                this.acceptPassKeyButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (null != vsla.getVslaCode()) {
                            vsla.setPasskey(String.valueOf(passKey.getText()).trim());
                            updateVslaPassKey(vsla);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("FindVsla vsla count", "" + ex.toString());
        }
    }

    private String generateVslaPasskey() {
        String newPassKey = PassKeyGenerator.randomAlphaNumeric(5);
        return newPassKey;
    }

    private void updateVslaPassKey(Vsla vsla) {
        boolean updateSuccess;
        try {
            dbHandler = new DatabaseHandler(getApplicationContext());
            updateSuccess = dbHandler.updateVsla(vsla);

            if (updateSuccess) {
                alertMessage();
                Toast.makeText(VslaPassKeyGenerateActivity.this, "Passkey Accepted", Toast.LENGTH_LONG).show();
                /**Intent openFindVsla = new Intent(VslaPassKeyGenerateActivity.this,
                 FindVslaActivity.class);
                 startActivity(openFindVsla); */
            } else {
                Toast.makeText(VslaPassKeyGenerateActivity.this, "Passkey not accepted", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.e("UPDATEPASSKEY", ex.toString());
        }
    }

    public void alertMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Set title
        alertDialogBuilder.setTitle("Confirm");

        // Set dialog message
        alertDialogBuilder
                .setMessage("Passkey Updated!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // If this button is clicked, close current activity
                        Intent openDashboard = new Intent(VslaPassKeyGenerateActivity.this,
                                ListVslaActivity.class);
                        startActivity(openDashboard);
                    }
                });
        /** .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {

         // If this button is clicked, just close the dialog box and do nothing
         dialog.cancel();
         }
         });  */

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}
