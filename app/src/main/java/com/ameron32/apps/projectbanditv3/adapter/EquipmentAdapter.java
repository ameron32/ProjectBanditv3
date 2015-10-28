package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EquipmentAdapter extends
    ParseQueryAdapter<ParseObject> {

  private final Context context;
  private final int itemViewResource;

  public EquipmentAdapter(
      Context context,
      int itemViewResource) {
    super(context, makeQuery(), itemViewResource);
    this.context = context;
    this.itemViewResource = itemViewResource;
  }

  private static ParseQueryAdapter.QueryFactory<ParseObject> makeQuery() {
    return new ParseQueryAdapter.QueryFactory<ParseObject>() {

      @Override public ParseQuery<ParseObject> create() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CInventory");
        query.include("item");
        query.whereEqualTo("isEquipped", true);
        query.orderByAscending("name");
        com.ameron32.apps.projectbanditv3.object.Character currentCharacter = CharacterManager.get().getCurrentCharacter();
        query.whereEqualTo("owner", currentCharacter);
        query.orderByDescending("type");
        query.addAscendingOrder("name");

        return query;
      }
    };
  }



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

}
