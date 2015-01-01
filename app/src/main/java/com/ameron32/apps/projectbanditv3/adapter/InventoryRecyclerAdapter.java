package com.ameron32.apps.projectbanditv3.adapter;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.AutoReloader;
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

public class InventoryRecyclerAdapter
    extends AbsParseSuperRecyclerQueryAdapter<CInventory, InventoryRecyclerAdapter.ViewHolder>
{

  private final int mRowViewResource;

  private static ParseQueryAdapter.QueryFactory<CInventory> makeQuery() {
    return new ParseQueryAdapter.QueryFactory<CInventory>() {

      @Override public ParseQuery<CInventory> create() {
        ParseQuery<CInventory> query = new ParseQuery<CInventory>("CInventory");
        query.include("item");

        // We group by type, for the StickyHeaders
        query.orderByAscending("type");

        // within groups, we put equipped on top
        query.addDescendingOrder("isEquipped");

        // And sort the rest by name, A to Z
        query.addAscendingOrder("name");

        Character currentCharacter = CharacterManager.get().getCurrentCharacter();
        query.whereEqualTo("owner", currentCharacter);

        return query;
      }
    };
  }

  public InventoryRecyclerAdapter(final int rowViewResource) {
    super(makeQuery(), true);
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

    int baseValue = object.getInt("baseValue");
    int quantity = object.getInt("quantity");
    holder.itemName.setText(object.getString("name"));
    if (quantity == 1) {
      holder.itemQuantity.setVisibility(View.INVISIBLE);
      holder.itemValue.setText("$ "
          + baseValue);
    } else {
      holder.itemQuantity.setVisibility(View.VISIBLE);
      holder.itemQuantity.setText(quantity
          + "");
      holder.itemValue.setText("$ "
          + baseValue + " / $ "
          + baseValue * quantity);
    }

    if (object.getBoolean("isEquipped")) {
      holder.itemImage.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
    } else {
      holder.itemImage.clearColorFilter();
    }

    boolean isArmor = item.getBoolean("isArmor");
    boolean isWeapon = item.getBoolean("isWeapon");
    if (isArmor || isWeapon) {
      holder.itemImage.setOnClickListener(new OnInventoryItemClick(object, this));
    } else {
      holder.itemImage.setOnClickListener(null);
    }
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

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.imagebutton_inventory_item)
    ImageButton itemImage;
    @InjectView(R.id.textview_inventory_item_name)
    TextView itemName;
    @InjectView(R.id.textview_inventory_item_value)
    TextView itemValue;
    @InjectView(R.id.textview_inventory_item_quantity)
    TextView itemQuantity;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }

  public static class OnInventoryItemClick
      implements View.OnClickListener {

    private final CInventory object;
    private final AutoReloader reloader;

    public OnInventoryItemClick(
        CInventory object,
        AutoReloader adapter) {
      this.object = object;
      this.reloader = adapter;
    }

    @Override public void onClick(View v) {
      boolean isEquipped = object.getBoolean("isEquipped");
      object.put("isEquipped", !isEquipped);
      object.saveInBackground();

      reloader.loadObjects();
    }
  }
}
