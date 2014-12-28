package com.ameron32.apps.projectbanditv3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.ameron32.apps.projectbanditv3.object.Item;
import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;

import java.util.List;

public class InitialHeaderAdapter implements StickyHeadersAdapter<InitialHeaderAdapter.ViewHolder> {

  private static final String[] HEADERS = Item.Type.nameValues();
  private List<CInventory> items;

  public InitialHeaderAdapter(List<CInventory> items) {
    replaceItems(items);
  }

  public void replaceItems(List<CInventory> items) {
    this.items = items;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent) {
//    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.letter_header, parent, false);
    final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stickyheaders_header, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
//    headerViewHolder.letter.setText(items.get(position).subSequence(0, 1));
    holder.textView.setText(HEADERS[(int) getHeaderId(position)]);
  }

  @Override
  public long getHeaderId(int position) {
    CInventory object = getItem(position);
    if (object == null) {
      return 0;
    }

    Item item = (Item) object.getParseObject("item");
    String type = item.getString("type");

    for (int i = 0; i < HEADERS.length; i++) {
      String header = HEADERS[i];
      if (header.equalsIgnoreCase(type)) { return i; }
    }

    return 0;
  }

  public CInventory getItem(int position) {
    if (items != null && items.size() > 0) {
      return items.get(position);
    }
    return null;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.textview);
    }
  }
}
