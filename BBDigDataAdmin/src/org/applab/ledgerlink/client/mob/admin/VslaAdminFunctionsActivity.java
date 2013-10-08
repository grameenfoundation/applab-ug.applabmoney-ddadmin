package org.applab.ledgerlink.client.mob.admin;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;

/**
 * Tab Activity for the VSLA Admin Functions
 */
public class VslaAdminFunctionsActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    // Declare Variables
    private FragmentTabHost mTabHost;
    private SherlockFragment fragment;
    private String selectedTag;
    private static final String SELETED_TAB_INDEX = "tabIndex";
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the view from main_fragment.xml
        //setContentView(R.layout.vsla_admin_functions_fragment);
        //getActionBar().setTitle(R.string.deliverKitTitleText);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // add tabs
        Tab deliverKitTab = actionBar
                .newTab()
                .setTag("deliver kit")
                .setText("Deliver Kit")
                .setTabListener(this);
        actionBar.addTab(deliverKitTab);

        Tab recordLocationTab = actionBar
                .newTab()
                .setTag("record location")
                .setText("Record Location")
                .setTabListener(this);
        actionBar.addTab(recordLocationTab);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(SELETED_TAB_INDEX);
            actionBar.setSelectedNavigationItem(index);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);

        /** menu.add("Cancel")
         .setOnMenuItemClickListener(this.CancelButtonClickListener)
         .setIcon(R.drawable.navigation_cancel)
         .setTitle(R.string.labelCancel)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
         menu.add("Save")
         .setOnMenuItemClickListener(this.SubmitButtonClickListener)
         .setIcon(R.drawable.ic_action_done)
         .setTitle(R.string.labelSubmitForm)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  */

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        selectedTag = (String) tab.getTag();

        if (selectedTag.equalsIgnoreCase("deliver kit")) {
            fragment = new VslaKitDeliveryTabFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment).commit();
        }
        if (selectedTag.equalsIgnoreCase("record location")) {
            fragment = new VslaLocationRecordTabFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment).commit();
        }

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @Override protected void onSaveInstanceState(Bundle outState) {
     * super.onSaveInstanceState(outState);
     * outState.putInt(SELETED_TAB_INDEX,
     * <p/>
     * getSupportActionBar().getSelectedTab().getPosition());
     * }
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}