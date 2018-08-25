package interfaces;

/**
 * Created by gakuo on 8/10/2018.
 */

public interface JATWeShop {
    public void addShoppingList(String listID,String title);
    public  void addItemToShoppingList(String listID, String itemName, Integer amount);
    public void freezeShoppingList(String listID);
    public void addListFollower(String ListID, String userName);
    public void  memberResurrected(String member);
    public void  memberDied(String member);

}
