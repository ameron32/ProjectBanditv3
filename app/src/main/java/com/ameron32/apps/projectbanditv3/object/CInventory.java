package com.ameron32.apps.projectbanditv3.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import org.json.JSONArray;

@ParseClassName("CInventory") 
public class CInventory
  extends AbsBanditObject<AbsBanditObject.Column>
{
  
  private int baseValue;
  private int currentDurability;
  private String name;
  private String type;
  private JSONArray characterOwners;
  
  public CInventory() {}
  
  public static CInventory assignItemToCharacter(
      ParseObject item,
      ParseObject character,
      int quantity) {
    CInventory link = new CInventory();
    link.addOwner(character);
    link.addItem(item);
    link.setQuantity(quantity);
    return link;
  }
  
  public CInventory setQuantity(
      int quantity) {
    this.put("quantity", quantity);
    return this;
  }
  
  public CInventory addOwner(
      ParseObject character) {
    ParseRelation<ParseObject> relation = this.getRelation("owner");
    relation.add(character);
    
    String characterName = character.getString("name");
    characterOwners = this.getJSONArray("characterOwners");
    if (characterOwners == null) {
      characterOwners = new JSONArray();
    }
    characterOwners.put(characterName);
    
    this.put("characterOwners", characterOwners);
    return this;
  }
  
  public CInventory addItem(
      ParseObject item) {
    baseValue = item.getInt("baseValue");
    currentDurability = item.getInt("durabilityUses");
    name = item.getString("name");
    type = item.getString("type");
    
    this.put("currentDurability", currentDurability);
    this.put("baseValue", baseValue);
    this.put("name", name);
    this.put("type", type);
    
    this.put("item", item);
    return this;
  }
  
//  String[] columns = new String[] {
//      "name", "quantity", "baseValue",
//      "currentDurability",
//      "characterOwners" };
//  
//  @Override public String get(
//      int columnPosition) {
//    switch (columnPosition) {
//    case 0:
//      return this.getString(columns[columnPosition]);
//    case 1:
//    case 2:
//    case 3:
//      return this.getInt(columns[columnPosition]) + "";
//    default:
//      return "N/A";
//    }
//  }
//  
//  private boolean isHeader = false;
//  @Override public void useAsHeaderView(boolean b) {
//    isHeader = b;
//  }
//  
//  @Override public boolean isHeaderView() {
//    return isHeader;
//  }
//
//  @Override public int getColumnCount() {
//    return columns.length;
//  }
//
//  @Override public String getColumnHeader(
//      int columnPosition) {
//    return columns[columnPosition];
//  }
  
  private static final AbsBanditObject.Column[] COLUMNS = {
    new Column("name", DataType.String)
  };
  
  @Override public AbsBanditObject.Column get(
      int columnPosition) {
    return COLUMNS[columnPosition];
  }
  
  @Override public int getColumnCount() {
    return COLUMNS.length;
  }
}
