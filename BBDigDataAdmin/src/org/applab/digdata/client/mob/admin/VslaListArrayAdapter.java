package org.applab.digdata.client.mob.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.model.Vsla;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Sarahk
 * Date: 8/1/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class VslaListArrayAdapter extends ArrayAdapter<Vsla> {
    Context context;
    ArrayList<Vsla> values;

    public VslaListArrayAdapter(Context context, ArrayList<Vsla> values) {
        super(context, R.layout.row_vsla_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            //Here I populate the ListView Row with data.
            //I will handle the itemClick event in the ListView view on the actual fragment
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.row_vsla_list, parent, false);

            TextView txtVslaId = (TextView)rowView.findViewById(R.id.txtRVLVslaId)   ;
            TextView txtVslaName = (TextView)rowView.findViewById(R.id.txtRVLVslaName);

            txtVslaId.setText(String.format("ID: %s",values.get(position).getId()));
            txtVslaName.setText(String.format("Name: %s",values.get(position).getName()));

            return rowView;
        }
        catch (Exception ex) {
            Log.e("Errors:", "getView:D " + ((ex.getMessage() == null) ? "Generic Exception" : ex.getMessage()));
            return null;
        }
    }
}
