package vub.edu.weshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by gakuo on 8/4/2018.
 */

public class WeShopCreateNewList extends AppCompatActivity {

    private Button submitnewList;
    private EditText title;
    public static WeShop weShop;
    private WeShopProcessor communicationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createlistcontent);
        title=(EditText)findViewById(R.id.listTitle);
        submitnewList= (Button) findViewById(R.id.newList);
        Intent listing = getIntent();
        communicationLayer = listing.getParcelableExtra("communicationLayer");

        weShop = new WeShop();
        submitnewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (title.getText().toString().isEmpty()){
                    Toast.makeText(WeShopCreateNewList.this, "Enter a title", Toast.LENGTH_SHORT).show();}
                else {
                    String idExtension =Calendar.getInstance().getTime().toString(); //to give each list a unique id
                    WeShop.shoppingLists.put(title.getText().toString()+idExtension, new HashMap<String, Integer>());
                    //if(communicationLayer != null) {
                        communicationLayer.notifyNewListCreated(WeShop.applicationID+ idExtension, title.getText().toString());
                    //}
                    Intent lists = new Intent(WeShopCreateNewList.this,WeShopShoppingLists.class);
                    lists.putExtra("title", title.getText().toString());
                    setResult(RESULT_OK, lists);
                    startActivity(lists);
                }
            }
        });


    }
}


