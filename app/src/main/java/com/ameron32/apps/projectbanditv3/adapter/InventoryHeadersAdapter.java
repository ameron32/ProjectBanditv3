package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
//import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;


public class InventoryHeadersAdapter
        extends ParseQueryAdapter<ParseObject>
//        implements StickyGridHeadersSimpleAdapter
{

  private Context context;

  public InventoryHeadersAdapter(
      Context context,
      int itemViewResource) {
    super(context, makeQuery(), itemViewResource);
    this.context = context;
    this.setPaginationEnabled(false);
  }

  private static ParseQueryAdapter.QueryFactory<ParseObject> makeQuery() {
    return new ParseQueryAdapter.QueryFactory<ParseObject>() {

      @Override public ParseQuery<ParseObject> create() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CInventory");
        query.include("item");

        // We group by type, for the StickyHeaders
        query.orderByAscending("type");

        // within groups, we put equipped on top
        query.addDescendingOrder("isEquipped");

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
//      if (header.equalsIgnoreCase(type)) {
//        return i;
//      }
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
      holder.itemImage.setColorFilter(Color.YELLOW, Mode.MULTIPLY);
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

    return v;
  }

  static class ViewHolder {

    @InjectView(R.id.imagebutton_inventory_item) ImageButton itemImage;
    @InjectView(R.id.textview_inventory_item_name) TextView itemName;
    @InjectView(R.id.textview_inventory_item_value) TextView itemValue;
    @InjectView(R.id.textview_inventory_item_quantity) TextView itemQuantity;

    public ViewHolder(View v) {
      ButterKnife.inject(this, v);
    }
  }

  static class OnInventoryItemClick
      implements OnClickListener {

    private final ParseObject object;
    private final ParseQueryAdapter<ParseObject> adapter;

    public OnInventoryItemClick(
        ParseObject object,
        ParseQueryAdapter<ParseObject> adapter) {
      this.object = object;
      this.adapter = adapter;
    }

    @Override public void onClick(View v) {
      boolean isEquipped = object.getBoolean("isEquipped");
      object.put("isEquipped", !isEquipped);
      object.saveInBackground();

      adapter.loadObjects();
    }
  }

}
