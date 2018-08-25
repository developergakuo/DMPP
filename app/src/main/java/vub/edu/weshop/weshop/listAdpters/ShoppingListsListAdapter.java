package vub.edu.weshop.weshop.listAdpters;

/**
 * Created by gakuo on 8/1/2018.
 */

import android.app.Activity;

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

public class ShoppingListsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] arrayOfShoppingLists;

    public ShoppingListsListAdapter(Activity context, String[] arrayOfShoppingLists ) {
        super(context, R.layout.items_in_list, arrayOfShoppingLists);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.arrayOfShoppingLists=arrayOfShoppingLists;


    }

    public View getView(int position,View view,ViewGroup parent) {


        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_entry, parent, false);
        }
        // Lookup view for data population
        TextView listTitle = (TextView) view.findViewById(R.id.listEntry);
        TextView pos = (TextView) view.findViewById(R.id.listID);
        listTitle.setText(arrayOfShoppingLists[position] );

        pos.setText("id: "+position);



        // Return the completed view to render on screen
        return view;


    };
}
