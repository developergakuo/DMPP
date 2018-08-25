package vub.edu.weshop;

/**
 * Created by gakuo on 8/1/2018.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import vub.edu.weshop.weshop.listAdpters.ShoppingListsListAdapter;


public class WeShopShoppingLists extends AppCompatActivity {
    private ListView list;
    public Button createnewlist;
    private WeShopProcessor communicationLayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listview);
        Intent intent = getIntent();
        communicationLayer = intent.getParcelableExtra("communicationLayer");
        //when user wants to create a new list
        createnewlist= (Button) findViewById(R.id.createList);
        createnewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int result = 1;
                Intent lists = new Intent(WeShopShoppingLists.this, WeShopCreateNewList.class);
                lists.putExtra("communicationLayer",communicationLayer);
                startActivityForResult(lists, result);
            }
        });

       //populate lists of shopping lists on ui
        try {// avoid empty hashmap exception

            String[] titles = new String[WeShop.shoppingLists.keySet().size()];
            int i = 0;
            for (String s : WeShop.shoppingLists.keySet())
                titles[i++] = s;

            ShoppingListsListAdapter adapter = new ShoppingListsListAdapter(this, titles);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);
        } catch (Exception e){
            Log.e("AmbientTalk", e.getMessage());
        }


        // display available lists.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();

                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                super.onActivityResult(requestCode, resultCode, data);
                String newListTitle = data.getStringExtra("title");
                Log.v("AmbientTalk","title"+newListTitle);
            }
        }
    }
    public void freezeList(View view){
        View parent = (View) view.getParent();

        TextView id =(TextView) parent.findViewById(R.id.listEntry);
        if (id !=null) {
            String listID = String.valueOf(id.getText());
            Intent viewList = new Intent(WeShopShoppingLists.this, WeShopListItems.class);
            //put userName into the Vector of list followers of the given List.
            WeShop.frozenLists.add(listID);
            if(communicationLayer != null){
                communicationLayer.notifyListFrozen(listID);
                Log.v("AmbientTalk", "freeze notification");
            }
            Toast.makeText(WeShopShoppingLists.this, "You have frozen list: " +listID , Toast.LENGTH_SHORT).show();
        }


    }
    public void joinList(View view){
        View parent = (View) view.getParent();
        TextView id =(TextView) parent.findViewById(R.id.listEntry);
        if (id !=null) {
            String listID = String.valueOf(id.getText());
            Intent viewList = new Intent(WeShopShoppingLists.this, WeShopListItems.class);
            //put userName into the Vector of list followers of the given List.
            WeShop.listFollowers.get(listID).add(WeShop.userName);
            if(communicationLayer!= null){
                communicationLayer.addListFollower(listID, WeShop.applicationID);
                Log.v("AmbientTalk", "follower notification");
            }

            Toast.makeText(WeShopShoppingLists.this, "The List " +listID +" sucessfully followed", Toast.LENGTH_SHORT).show();


        }

    }
    public void viewList(View view){
        View parent = (View) view.getParent();

        TextView id =(TextView) parent.findViewById(R.id.listEntry);
        if (id !=null) {
            String listID = String.valueOf(id.getText());
            Intent viewList = new Intent(WeShopShoppingLists.this, WeShopListItems.class);
            viewList.putExtra("listID",listID);
            viewList.putExtra("communicationLayer",communicationLayer);
            startActivity(viewList);
            finish();
        }
    }
}
