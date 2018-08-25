package interfaces;

/**
 * Created by flo on 11/04/2018.
 */

public interface ATWeShop {
    //list of methods in AT local interface
    public void setName(String userName);
    public void setID(String id);
    public void createShoppingList(String listID,String title);
    public void joinShoppingList(String listID);
   // public void containsShoppingList(String listID);
    //public void getShoppingList(String listID);
    public void addItemToShoppingList(String listID,String item, Integer amount);
    public void freezeShoppingList (String listID) ;
    public void stop();



}
