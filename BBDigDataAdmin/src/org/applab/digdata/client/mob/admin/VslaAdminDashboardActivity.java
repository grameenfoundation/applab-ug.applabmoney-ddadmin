package org.applab.digdata.client.mob.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.model.Vsla;

/**
 * Tab Activity for the VSLA Admin Functions
 */
public class VslaAdminDashboardActivity extends DashboardActivity {

    private Vsla vsla;
    private TextView vslaIdText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(true, false);
        setContentView(R.layout.vsla_admin_dashboard);

        vslaIdText = (TextView) findViewById(R.id.vslaIdTxt);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vsla = (Vsla) getIntent().getExtras().get("vsla");
            vslaIdText.setText(vsla.getId());
        }
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onClickDashboard(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.deliver_kit_button:
                Intent deliverKitIntent = new Intent(this, VslaKitDeliveryActivity.class);
                deliverKitIntent.putExtra("vsla", vsla);
                startActivity(deliverKitIntent);
                break;

            case R.id.generate_passkey_button:
                Intent generatePasskeyIntent = new Intent(this, VslaPassKeyGenerateActivity.class);
                generatePasskeyIntent.putExtra("vsla", vsla);
                startActivity(generatePasskeyIntent);
                break;

            default:
                break;
        }
    }


}