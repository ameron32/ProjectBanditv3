package com.ameron32.apps.projectbanditv3.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.ameron32.apps.projectbanditv3.object.Item;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EquipmentRecyclerAdapter
    extends AbsParseSuperRecyclerQueryAdapter<CInventory, EquipmentRecyclerAdapter.ViewHolder>
{

  private final int mRowViewResource;

  private static ParseQueryAdapter.QueryFactory<CInventory> makeQuery() {
    return new ParseQueryAdapter.QueryFactory<CInventory>() {

      @Override public ParseQuery<CInventory> create() {
        ParseQuery<CInventory> query = new ParseQuery<CInventory>("CInventory");
        query.include("item");
        query.whereEqualTo("isEquipped", true);

        // We group by type, for the StickyHeaders
        query.orderByAscending("type");

        // And sort the rest by name, A to Z
        query.addAscendingOrder("name");

        Character currentCharacter = CharacterManager.get().getCurrentCharacter();
        query.whereEqualTo("owner", currentCharacter);

        return query;
      }
    };
  }

  public EquipmentRecyclerAdapter(final int rowViewResource) {
    super(makeQuery());
    mRowViewResource = rowViewResource;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View v = LayoutInflater.from(parent.getContext()).inflate(mRowViewResource, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final CInventory object = getItem(position);
    final Item item = (Item) object.getParseObject("item");

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
    holder.itemDurability.setText("[" + currentDurability + "/" + maxDurability + "]");
    holder.itemValue.setText("$" + baseValue);
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

  public class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.button_value)
    Button itemValue;
    @InjectView(R.id.textview_equipment_item_name)
    TextView itemName;
    @InjectView(R.id.textview_equipment_item_value)
    TextView itemDurability;
    @InjectView(R.id.textview_equipment_item_slot)
    TextView equipmentSlot;
    @InjectView(R.id.progressBar1)
    ProgressBar durabilityBar;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
      final int position = getPosition();
      Log.i("ERA:itemClick", "item " + position + " clicked");
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.i("ERA:itemClick", "item " + position + " clicked");
        }
      });
    }
  }
}
