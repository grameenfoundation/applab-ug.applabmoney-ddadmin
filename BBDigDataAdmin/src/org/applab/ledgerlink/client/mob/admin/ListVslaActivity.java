package org.applab.ledgerlink.client.mob.admin;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;

import java.util.ArrayList;

/**
 *
 */
public class ListVslaActivity extends ListActivity {

    private DatabaseHandler dbHandler;
    private Vsla vsla = new Vsla();
    private ArrayList<String> vslas;
    private ArrayList<Vsla> vslaArray;
    String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_list);

        userName = getIntent().getStringExtra("userName");
        //setListAdapter(new ArrayAdapter<String>(this,
        //      android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names)));
        try {
            dbHandler = new DatabaseHandler(getApplicationContext());

            // vslaArray = (ArrayList<Vsla>) getIntent().getExtras().get("vslaList");
            vslaArray = dbHandler.getAllVslasFromDb();
            VslaListArrayAdapter adapter = new VslaListArrayAdapter(ListVslaActivity.this, vslaArray);

            setListAdapter(adapter);
            if (dbHandler.getAssetCount() == 0) {
                dbHandler.addAssets();
            }

        } catch (Exception ex) {
            Log.e("get All Vslas", "" + ex.toString());
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        // Get selected Vsla
        Vsla selectedVsla = (Vsla) getListAdapter().getItem(position);
        Intent adminFunctionsIntent = new Intent(this, VslaAdminFunctionsActivity.class);
        adminFunctionsIntent.putExtra("userName", userName);
        adminFunctionsIntent.putExtra("vsla", selectedVsla);
        startActivity(adminFunctionsIntent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an Action");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * @Override public boolean onContextItemSelected(MenuItem item) {
     * AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
     * Vsla vsla = vslaArray.get(info.position);
     * switch (item.getItemId()) {
     * case R.id.deliverKit:
     * Intent deliverKitIntent = new Intent(this, VslaKitDeliveryActivity.class);
     * Log.i("Case deliverkit", vsla.getId());
     * deliverKitIntent.putExtra("vsla", vsla);
     * startActivity(deliverKitIntent);
     * return true;
     * case R.id.generatePasskey:
     * Intent generatePasskeyIntent = new Intent(this, VslaPassKeyGenerateActivity.class);
     * generatePasskeyIntent.putExtra("vsla", vsla);
     * startActivity(generatePasskeyIntent);
     * return true;
     * default:
     * return super.onContextItemSelected(item);
     * }
     * }
     */


    public Vsla getVsla(DatabaseHandler dbHandler, String selectedItem) {
        dbHandler = new DatabaseHandler(getApplicationContext());
        try {
            vsla = dbHandler.getVslaFromDbByName(selectedItem);
        } catch (Exception ex) {
            Log.e("get All Vslas", "" + ex.toString());
        }
        return vsla;
    }

}