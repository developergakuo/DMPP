package vub.edu.weshop;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Vector;

import interfaces.ATWeShop;
import interfaces.JATWeShop;

/**
 * Created by gakuo on 8/10/2018.
 */

public class WeShopProcessor implements JATWeShop,Parcelable {
    private ATWeShop atpp;
    private String id;
    String userName;
    //Maps user id's to user names
    private HashMap<String,String> mapping;
    //Handler to the loop handling communication with AT
    private Handler mHandler;
    private WeShop context;
    //Message code send to looper thread
    private  int _CREATE_LIST_ = 0;
    private  int _FREEZE_LIST_ = 1;
    private  int _ADD_ITEM_TO_LIST_ = 2;
    private  int _NEW_LIST_FOLLOWER_ = 3;
    private  int _MSG_SHUTDOWN_ = 4;



    public WeShopProcessor(String userName,String id, WeShop c){
        this.id=id;
        this.context =c;
        this.userName =userName;
        LooperThread lt = new LooperThread();
        lt.start();
        mHandler = lt.mHandler;
    }

    protected WeShopProcessor(Parcel in) {
        id = in.readString();
        userName = in.readString();
        _CREATE_LIST_ = in.readInt();
        _FREEZE_LIST_ = in.readInt();
        _ADD_ITEM_TO_LIST_ = in.readInt();
        _NEW_LIST_FOLLOWER_ = in.readInt();
        _MSG_SHUTDOWN_ = in.readInt();
    }

    public static final Creator<WeShopProcessor> CREATOR = new Creator<WeShopProcessor>() {
        @Override
        public WeShopProcessor createFromParcel(Parcel in) {
            return new WeShopProcessor(in);
        }

        @Override
        public WeShopProcessor[] newArray(int size) {
            return new WeShopProcessor[size];
        }
    };

    public void setATPP(ATWeShop atpp){
        atpp.setID(id);
        atpp.setName(userName);
        this.atpp = atpp;
    }


    public void notifyNewListCreated(String listID, String title){
        //if(mHandler!= null){
        mHandler.sendMessage(Message.obtain(mHandler, _CREATE_LIST_,new CreateListMessage(listID)));
       // }
    };
    public void notifyNewListFollower(String ListID, String UserName){
        if(mHandler!= null) {
            mHandler.sendMessage(Message.obtain(mHandler, _NEW_LIST_FOLLOWER_, new ListFollowerMessage(ListID, userName)));
        }
        };
    public void notifyNewItemAdded(String listID, String itemName, Integer amount){
        if(mHandler != null){
        mHandler.sendMessage(Message.obtain(mHandler, _ADD_ITEM_TO_LIST_, new AddItemMessage(listID,itemName,amount)));}
    };
    public void notifyListFrozen(String listID){
        if (mHandler != null){
        mHandler.sendMessage(Message.obtain(mHandler, _FREEZE_LIST_, new FreezeListMessage(listID)));}
    };

    public void addShoppingList(String listID,String title){
        if(context.shoppingLists.containsKey(listID)){

        }
        else {
            context.shoppingLists.put(listID, new HashMap<String, Integer>());
            context.listFollowers.put(listID, new Vector());
            context.showNotification("Weshop notification "," New list " + listID);
        }
    }
    public void addItemToShoppingList(String listID, String itemName, Integer amount){
        if(context.shoppingLists.get(listID).containsKey(itemName))
        {
           context.shoppingLists.get(listID).put(itemName,amount+ context.shoppingLists.get(listID).get(itemName));
           // context.showNotification("Weshop notification","Item "+ itemName+" adjusted in list " + listID);
        }
        else
        {
           context.shoppingLists.get(listID).put(itemName,amount);
           context.showNotification("Weshop notification"," New item "+ itemName+" added in list " + listID);

        }
    }
    public void freezeShoppingList(String listID){
        context.frozenLists.add(listID);
        context.showNotification("Weshop notification","Lits "+ listID +"has been frozen ");


    }
    public void addListFollower(String listID, String userName){
        if (context !=null) {
            context.listFollowers.get(listID).add(userName);

            context.showNotification("Weshop notification", "New follower " + userName + " added to list " + listID);
        }

    }
    public void  memberResurrected(String member){
        if (context !=null) {
            context.showNotification("Weshop notification", "Member" + member + " is back online");
        }

    }
    public void  memberDied(String member){
        if (context !=null) {
            context.showNotification("Weshop notification", "Member" + member + " is back online  ");
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userName);
        parcel.writeInt(_CREATE_LIST_);
        parcel.writeInt(_FREEZE_LIST_);
        parcel.writeInt(_ADD_ITEM_TO_LIST_);
        parcel.writeInt(_NEW_LIST_FOLLOWER_);
        parcel.writeInt(_MSG_SHUTDOWN_);
    }

    private class LooperThread extends Thread {


        public Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (null == atpp)
                    return;
                switch (msg.what) {
                    case 1: {
                        FreezeListMessage em = (FreezeListMessage) msg.obj;
                        atpp.freezeShoppingList(em.listID);
                        break;
                    }
                    case 2: {
                        AddItemMessage em = (AddItemMessage) msg.obj;
                        atpp.addItemToShoppingList(em.listID,em.itemName,em.amount);
                        break;
                    }
                    case 0: {
                        CreateListMessage em = (CreateListMessage) msg.obj;
                        atpp.createShoppingList(em.listID,"Title");
                        break;
                    }
                    case 3: {
                         ListFollowerMessage em = (ListFollowerMessage) msg.obj;
                        atpp.joinShoppingList(em.listID);
                        break;
                    }

                    case 4:
                        atpp.stop();
                        break;
                }
            }
        };
        public void run() {
            Looper.prepare();
            Looper.loop();
        }
    }

    //messages for the handler

        public class AddItemMessage{
            public String listID;
            public String itemName;
            public Integer amount;

            public AddItemMessage(String listID,String itemName, Integer amount){
                this.listID = listID;
                this.itemName = itemName;
                this.amount = amount;
            }
        }

        public class FreezeListMessage{
            public String listID;

            public FreezeListMessage(String listID){
                this.listID = listID;

            }
        }
        public class CreateListMessage{
            public String listID;

            public CreateListMessage(String listID){
                this.listID = listID;

            }
        }
        public class ListFollowerMessage {
            public String listID;
            public String userName;

            public ListFollowerMessage(String listID,String userName) {
                this.listID = listID;
                this.userName = userName;

            }
        }


}
