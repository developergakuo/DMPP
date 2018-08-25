package vub.edu.weshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by gakuo on 8/4/2018.
 */

public class WeshopAddItemToList extends AppCompatActivity {

    Button submitnewItem;
    EditText name;
    EditText amount;
    String listID;
    private WeShopProcessor communicationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);

        name=(EditText)findViewById(R.id.itemName);
        amount=(EditText)findViewById(R.id.itemAmount);
        submitnewItem= (Button) findViewById(R.id.newItem);
        Intent list = getIntent();
        listID=list.getExtras().getString("listID");
        communicationLayer = list.getParcelableExtra("communicationLayer");



        submitnewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty()  ){
                    Toast.makeText(WeshopAddItemToList.this, "Enter an item name", Toast.LENGTH_SHORT).show();}
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(WeshopAddItemToList.this, "Enter an item amount", Toast.LENGTH_SHORT).show();}
                else {
                    if(WeShop.shoppingLists.get(listID).containsKey(name.getText().toString()))
                    {
                        WeShop.shoppingLists.get(listID).put(name.getText().toString(),Integer.parseInt(amount.getText()
                            .toString())+ WeShop.shoppingLists.get(listID).get(name.getText().toString()));
                    }
                    else
                    {
                        WeShop.shoppingLists.get(listID).put(name.getText().toString(),Integer.parseInt(amount.getText()
                                .toString()));
                    }
                    if (communicationLayer != null) {
                        communicationLayer.addItemToShoppingList(listID, name.getText().toString(), Integer.parseInt(amount.getText()
                                .toString()));
                        Log.v("AmbientTalk","Item added Notoification");
                    }
                }


                    Intent list = new Intent( WeshopAddItemToList.this,WeShopListItems.class);
                    list.putExtra("listID",listID);
                    list.putExtra("communicationLayer",communicationLayer);
                    startActivity(list);



            }
        });


    }
}


