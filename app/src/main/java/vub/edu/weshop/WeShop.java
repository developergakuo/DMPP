package vub.edu.weshop;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import edu.vub.at.IAT;
import edu.vub.at.android.util.IATAndroid;
import edu.vub.at.exceptions.InterpreterException;
import interfaces.ATWeShop;
import interfaces.JATWeShop;
import interfaces.JWeShop;

public class WeShop extends AppCompatActivity implements JWeShop {
    private static WeShopProcessor weShopProcessor;
    public static String applicationID;
    public static IAT iat;
    public static ATWeShop atWS;
    private EditText username;
    private Button regBtn;
    private TextView regTxt;
    public static String userName;
    public static HashMap<String,HashMap<String,Integer>> shoppingLists = new HashMap<>();
    public static HashMap<String,Vector> listFollowers = new HashMap<>();
    public static Vector frozenLists = new Vector();
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this,WeShopAssetInstaller.class);
        startActivityForResult(i,0);
        username = (EditText) findViewById(R.id.username);
        regBtn = (Button)findViewById(R.id.regBtn);
        regTxt = (TextView)findViewById(R.id.regTxt);

        applicationID =id(this);

        weShopProcessor = new WeShopProcessor(userName,applicationID,this);

        regBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
            Toast.makeText(WeShop.this, "Enter username as user", Toast.LENGTH_SHORT).show();
                if (username.getText().toString().isEmpty()){
                    Toast.makeText(WeShop.this, "Enter username as user", Toast.LENGTH_SHORT).show();
                }else {
                    userName =username.getText().toString();

                    Intent lists = new Intent(WeShop.this, WeShopShoppingLists.class);
                    lists.putExtra("prompt","userName");
                    lists.putExtra("userName", userName);
                    lists.putExtra("communicationLayer",weShopProcessor);
                    startActivity(lists);
                    finish();

                }
            }
        });



        // my test shopping list.
        shoppingLists.put("Test", new HashMap<String, Integer>());
        shoppingLists.get("Test").put("rice",10);
        listFollowers.put("Test", new Vector());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            Log.v("WeShop","Starting Asset Installer");
            new StartIATTask().execute((Void) null);
        }
    }
    public JATWeShop registerATApp(ATWeShop atpp){
            weShopProcessor.setATPP(atpp);
            return weShopProcessor;
        }

    class StartIATTask extends AsyncTask<Void,String,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                iat = IATAndroid.create(WeShop.this);
                iat.evalAndPrint("import /.WeShop.WeShop.makeWeShop()", System.err);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterpreterException e) {
                Log.e("AmbientTalk","Could not start IAT",e);
            }
            return null;
        }

    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void showNotification(String notification_title,String notification_text ) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, WeShopShoppingLists.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(notification_title)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(notification_title)
                .setContentText(notification_text)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }

        return uniqueID;
    }



}
