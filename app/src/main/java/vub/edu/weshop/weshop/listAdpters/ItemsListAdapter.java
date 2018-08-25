package vub.edu.weshop.weshop.listAdpters;

/**
 * Created by gakuo on 8/1/2018.
 */

import android.app.Activity;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vub.edu.weshop.R;

public class ItemsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String>names;
    private final ArrayList<String> amounts;


    public ItemsListAdapter (Activity context, ArrayList<String> names,ArrayList<String> amounts) {
        super(context, R.layout.items_in_list, names);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.names = names;
        this.amounts = amounts;



    }

    public View getView(int position,View view,ViewGroup parent) {


        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        // Lookup view for data population
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView amount = (TextView) view.findViewById(R.id.item_amount);



        itemName.setText(names.get(position) );
        amount.setText(amounts.get(position));



        // Return the completed view to render on screen
        return view;


    };
}
