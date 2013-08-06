package org.applab.digdata.client.mob.admin;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;
import org.applab.digdata.client.mob.admin.database.DatabaseHandler;
import org.applab.digdata.client.mob.admin.model.Vsla;

import java.util.ArrayList;

/**
 *
 */
public class ListVslaActivity extends ListActivity {

    DashboardActivity dashboardActivity = new DashboardActivity();
    private DatabaseHandler dbHandler;
    private Vsla vsla = new Vsla();
    private ArrayList<String> vslas;
    private ArrayList<Vsla> vslaArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_list);
        //setListAdapter(new ArrayAdapter<String>(this,
        //      android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names)));
        try {
            dbHandler = new DatabaseHandler(getApplicationContext());
            int vslaCount = dbHandler.getVslaCount();
            Log.i("FindVsla vsla count", "" + String.valueOf(vslaCount));
            if (vslaCount < 4) {
                dbHandler.insertDefaultData();
            }

            vslaArray = dbHandler.getAllVslasFromDb();
            VslaListArrayAdapter adapter = new VslaListArrayAdapter(ListVslaActivity.this, vslaArray);

            setListAdapter(adapter);
            //ListView vslaListView = (ListView)findViewById(R.id.vsla_list_view);
            //vslaListView.setAdapter(adapter);
            registerForContextMenu(getListView());

        } catch (Exception ex) {
            Log.e("get All Vslas", "" + ex.toString());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an Action");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Vsla vsla = vslaArray.get(info.position);
        switch (item.getItemId()) {
            case R.id.deliverKit:
                Intent deliverKitIntent = new Intent(this, VslaKitDeliveryActivity.class);
                Log.i("Case deliverkit", vsla.getId());
                deliverKitIntent.putExtra("vsla", vsla);
                startActivity(deliverKitIntent);
                return true;
            case R.id.generatePasskey:
                Intent generatePasskeyIntent = new Intent(this, VslaPassKeyGenerateActivity.class);
                generatePasskeyIntent.putExtra("vsla", vsla);
                startActivity(generatePasskeyIntent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public Vsla getVsla(DatabaseHandler dbHandler, String selectedItem){
        try {
            //dbHandler = new DatabaseHandler(getApplicationContext());
            vsla = dbHandler.getVslaFromDbByName(selectedItem);
        }
        catch (Exception ex){
            Log.e("get All Vslas", "" + ex.toString());
        }
       return vsla;
    }

}