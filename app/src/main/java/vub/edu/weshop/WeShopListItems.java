package vub.edu.weshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import vub.edu.weshop.weshop.listAdpters.ItemsListAdapter;

/**
 * Created by gakuo on 8/6/2018.
 */

public class WeShopListItems extends AppCompatActivity {
    private Button addNewItem;
    private ListView list;
    final int ITEM_ADDED = 1;
    public static String listID;
    private  ArrayList<String>names= new ArrayList<>();
    private ArrayList<String> amounts = new ArrayList<>();
    private ItemsListAdapter adapter;
    private WeShopProcessor communicationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        //steps to list items a list
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        this.listID = intent.getExtras().getString("listID");
        communicationLayer = intent.getParcelableExtra("communicationLayer");
        setTitle("List: "+ this.listID);
        if(WeShop.shoppingLists.get(listID).keySet()==null || WeShop.shoppingLists.values()==null){
            Toast.makeText(WeShopListItems.this, "The list " +listID+ " is Empty", Toast.LENGTH_SHORT).show();

        } else {
            createListAdapterArrays(listID);
            adapter = new ItemsListAdapter(this, names, amounts);
            list = (ListView) findViewById(R.id.listOfItems);
            list.setAdapter(adapter);
        }
        //logic for creation of new item
        addNewItem = (Button) findViewById(R.id.addNewItem);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(WeShop.frozenLists.contains(listID)){
                Toast.makeText(WeShopListItems.this, "The List " +listID +" is frozen", Toast.LENGTH_SHORT).show();

            } else {
                Intent addAnItem = new Intent(WeShopListItems.this, WeshopAddItemToList.class);
                addAnItem.putExtra("listID", listID);
                if (communicationLayer!=null) {
                    addAnItem.putExtra(" communicationLayer", communicationLayer);
                }

                startActivity(addAnItem); }
            }
        });
    }

    public void itemPlusOne(View v) {
        View parent = (View) v.getParent();
        TextView item_name = (TextView) parent.findViewById(R.id.item_name);


        if (item_name!= null) {
            String name = String.valueOf(item_name.getText());
            adjustItemAmount(name,1);
            createListAdapterArrays(listID);
            if(communicationLayer!=null){
                communicationLayer.addItemToShoppingList(listID,name,1);
                Log.v("AmbientTalk","Item plus one notification");
            }

            adapter.notifyDataSetChanged();

        }
    }
    public void itemMinusOne(View v) {
        View parent = (View) v.getParent();
        TextView item_name = (TextView) parent.findViewById(R.id.item_name);


        if (item_name!= null) {
            String name = String.valueOf(item_name.getText());
            adjustItemAmount(name,-1);
            createListAdapterArrays(listID);
            Log.v("AmbientTalk", names.toString() + amounts.toString());
            if(communicationLayer!=null) {
                communicationLayer.addItemToShoppingList(listID, name, -1);
                Log.v("AmbientTalk", "Item minus one notification");
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ITEM_ADDED){
            if(resultCode==RESULT_OK){
                super.onActivityResult(requestCode, resultCode, data);
                String newListTitle = data.getStringExtra("title");
            }
        }
    }
    public void  adjustItemAmount (String itemName, int x){
        names.clear();
        amounts.clear();
        int current_amount = WeShop.shoppingLists.get(listID).get(itemName);
        HashMap <String,Integer> items = WeShop.shoppingLists.get(listID);
        HashMap temporaryItems = items;
        items.put(itemName,items.get(itemName)+x);
        WeShop.shoppingLists.put(listID,items);
    }
    public Pair createListAdapterArrays(String listID){
         //catch empty hashmap exception
            HashMap<String,Integer> shoppingList = WeShop.shoppingLists.get(listID);

            int i = 0;
            for (String s : shoppingList.keySet())
                names.add(s);
            int j = 0;
            for (Integer s : shoppingList.values())
                amounts.add(s.toString());
            return new Pair(names, amounts);
    }

    public class Pair
    {
        private ArrayList array1;
        private ArrayList array2;
        public Pair(ArrayList array1, ArrayList array2){
            this.array1 = array1;
            this.array2 = array2;
        }
        public ArrayList getArray1() { return array1; }
        public ArrayList getArray2() { return array2; }
    }

}




