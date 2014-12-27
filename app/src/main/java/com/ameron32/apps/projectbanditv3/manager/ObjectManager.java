package com.ameron32.apps.projectbanditv3.manager;


import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.ameron32.apps.projectbanditv3.Query;
import com.parse.FindCallback;
import com.parse.ParseQuery;

public class ObjectManager extends AbsManager {
  
  public static ObjectManager objectManager;
  
  public static ObjectManager get() {
    if (objectManager == null) {
      objectManager = new ObjectManager();
    }
    return objectManager;
  }
  
  private ObjectManager() {}
  
  public void initialize() {
    setInitialized(true);
  }
  
  public static void destroy() {
    objectManager = null;
  }
  
  public void queryAllInventory(FindCallback<CInventory> callback) {
    ParseQuery<CInventory> query = Query._Inventory.getAllInventory();
    query.findInBackground(callback);
  }
}
