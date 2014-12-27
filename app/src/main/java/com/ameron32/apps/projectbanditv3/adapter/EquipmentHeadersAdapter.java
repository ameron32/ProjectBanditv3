package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class EquipmentHeadersAdapter
    extends ParseQueryAdapter<ParseObject>
//    implements StickyListHeadersAdapter
{
  
  private final Context context;
  
  // private final int itemViewResource;
  
  public EquipmentHeadersAdapter(
      Context context,
      int itemViewResource) {
    super(context, makeQuery(), itemViewResource);
    this.context = context;
    // this.itemViewResource = itemViewResource;
    this.setPaginationEnabled(false);
  }
  
  private static ParseQueryAdapter.QueryFactory<ParseObject> makeQuery() {
    return new ParseQueryAdapter.QueryFactory<ParseObject>() {
      
      @Override public ParseQuery<ParseObject> create() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CInventory");
        query.include("item");
        query.whereEqualTo("isEquipped", true);
        
        // We group by type, for the StickyHeaders
        query.orderByAscending("type");
        
        // And sort the rest by name, A to Z
        query.addAscendingOrder("name");
        
        com.ameron32.apps.projectbanditv3.object.Character currentCharacter = CharacterManager.get().getCurrentCharacter();
        query.whereEqualTo("owner", currentCharacter);
        
        return query;
      }
    };
  }
  
  private static final String[] HEADERS = Item.Type.nameValues();
  
//  @Override public View getHeaderView(
//      int position, View convertView,
//      ViewGroup parent) {
//    View view = LayoutInflater.from(context).inflate(R.layout.stickyheaders_header, parent, false);
//    TextView textView = (TextView) view.findViewById(R.id.textview);
//    textView.setText(HEADERS[(int) getHeaderId(position)]);
//    return view;
//  }
  
  
//  @Override public long getHeaderId(
//      int position) {
//    ParseObject item = getItem(position);
//    String type = item.getString("type");
//
//    for (int i = 0; i < HEADERS.length; i++) {
//      String header = HEADERS[i];
//      if (header.equalsIgnoreCase(type)) { return i; }
//    }
//
//    return 0;
//  }
    
  @Override public View getItemView(
      ParseObject object, View v,
      ViewGroup parent) {
    v = super.getItemView(object, v, parent);
    
    ViewHolder holder;
    holder = (ViewHolder) v.getTag();
    if (holder == null) {
      holder = new ViewHolder(v);
      v.setTag(holder);
    }
    
    ParseObject item = object.getParseObject("item");
    
    String name = object.getString("name");
    int baseValue = object.getInt("baseValue");
    int currentDurability = object.getInt("currentDurability");
    int maxDurability = item.getInt("durabilityUses");
    String slots = "none";
    try {
      slots = getSlots(item);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    // int quantity = object.getInt("quantity");
    holder.itemName.setText(name);
    holder.itemDurability.setText("["
        + currentDurability + "/"
        + maxDurability + "]");
    holder.itemValue.setText("$"
        + baseValue);
    holder.equipmentSlot.setText(slots);
    // if (quantity < 2) {
    // holder.itemQuantity.setVisibility(View.INVISIBLE);
    // holder.itemValue.setText("$ "
    // + baseValue);
    // } else {
    // holder.itemQuantity.setVisibility(View.VISIBLE);
    // holder.itemQuantity.setText(quantity
    // + "");
    // holder.itemValue.setText("$ "
    // + baseValue + " / $ "
    // + baseValue * quantity);
    // }
    holder.durabilityBar.setMax(maxDurability);
    holder.durabilityBar.setProgress(currentDurability);
    
    return v;
  }
  
  private String getSlots(
      ParseObject item)
      throws JSONException {
    //  FIXME
    JSONArray armorSlots = item.getJSONArray("armorSlots");
    JSONArray weaponSlots = item.getJSONArray("weaponSlots");
    JSONArray slots = concatArray(armorSlots, weaponSlots);
    return slots.join(", ");
  }
  
  private JSONArray concatArray(
      JSONArray... arrs)
      throws JSONException {
    JSONArray result = new JSONArray();
    for (JSONArray arr : arrs) {
      if (arr != null) {
        for (int i = 0; i < arr.length(); i++) {
          result.put(arr.get(i));
        }
      }
    }
    return result;
  }
  
  static class ViewHolder {
    
    @InjectView(R.id.button_value) Button itemValue;
    @InjectView(R.id.textview_equipment_item_name) TextView itemName;
    @InjectView(R.id.textview_equipment_item_value) TextView itemDurability;
    @InjectView(R.id.textview_equipment_item_slot) TextView equipmentSlot;
    @InjectView(R.id.progressBar1) ProgressBar durabilityBar;
    
    public ViewHolder(View v) {
      ButterKnife.inject(this, v);
    }
  }
  //
  // @Override public View getHeaderView(
  // int position, View convertView,
  // ViewGroup parent) {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // @Override public long getHeaderId(
  // int position) {
  // // TODO Auto-generated method stub
  // return 0;
  // }
  
}
