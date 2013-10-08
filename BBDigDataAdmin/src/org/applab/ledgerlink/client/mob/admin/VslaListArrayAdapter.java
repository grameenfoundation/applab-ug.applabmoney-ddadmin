package org.applab.ledgerlink.client.mob.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.model.Vsla;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * VslaAdminUser: Sarahk
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

    public int getCount() {
        if (values != null)
            return values.size();
        return 0;
    }

    public Vsla getValue(int position) {
        if (values != null)
            return values.get(position);
        return null;
    }

    public long getValuesId(int position) {

        if (values != null)

            return values.get(position).hashCode();

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            //Here I populate the ListView Row with data.
            //I will handle the itemClick event in the ListView view on the actual fragment
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.row_vsla_list, parent, false);

            TextView txtVslaId = (TextView) rowView.findViewById(R.id.txtRVLVslaId);
            TextView txtVslaName = (TextView) rowView.findViewById(R.id.txtRVLVslaName);

            txtVslaId.setText(String.format("CODE: %s", values.get(position).getVslaCode()));
            txtVslaName.setText(String.format("NAME: %s", values.get(position).getName()));

            return rowView;
        } catch (Exception ex) {
            Log.e("Errors:", "getView:D " + ((ex.getMessage() == null) ? "Generic Exception" : ex.getMessage()));
            return null;
        }
    }

    public ArrayList<Vsla> getValues() {
        return values;
    }

    public void setValues(List<Vsla> values) {
        this.values = (ArrayList<Vsla>) values;
    }
}
