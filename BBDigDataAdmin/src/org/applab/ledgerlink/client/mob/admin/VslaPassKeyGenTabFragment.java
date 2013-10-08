package org.applab.ledgerlink.client.mob.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import org.applab.digdata.client.mob.admin.R;

public class VslaPassKeyGenTabFragment extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vsla_passkey_gen_fragtab, container, false);
        return rootView;
    }
}