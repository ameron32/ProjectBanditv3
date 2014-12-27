package com.ameron32.apps.projectbanditv3.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.ContentManager;
import com.ameron32.apps.projectbanditv3.manager.ContentManager.ContentItem;

import java.util.List;



public class ContentAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  
  private List<ContentItem> mData;
  private int mSelectedPosition;
  private int mTouchedPosition = -1;
  private boolean isClick = false;
  
  public ContentAdapter(
      List<ContentItem> data) {
    mData = data;
  }
  
  
  private static final int TYPE_SPACER_HEADER = 0;
  private static final int POSITION_SPACER_HEADER = 0;
  private static final int TYPE_ITEM = 1;
  
  @Override public RecyclerView.ViewHolder onCreateViewHolder(
      ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    if (viewType == TYPE_SPACER_HEADER) {
      View v = inflater.inflate(R.layout.view_spacer, viewGroup, false);
      return new HeaderViewHolder(v);
    }
    
    View v = inflater.inflate(R.layout.row_nav_text_drawer, viewGroup, false);
    return new ViewHolder(v);
  }
  
  @Override public int getItemViewType(
      int position) {
    if (isPositionHeader(position)) { return TYPE_SPACER_HEADER; }
    return TYPE_ITEM;
  }
  
  private boolean isPositionHeader(int position) {
      return position == POSITION_SPACER_HEADER;
  }

  private ContentItem getItem(int itemPosition) {
      return mData.get(itemPosition);
  }
  
  @Override public void onBindViewHolder(
      RecyclerView.ViewHolder viewHolder,
      int p) {
    if (viewHolder instanceof HeaderViewHolder) {
      return;
    }
    
    ContentAdapter.ViewHolder holder = (ViewHolder) viewHolder;
    final int position = p - 1;
    holder.textView.setText(getItem(position).title);
    Drawable d = holder.textView.getContext().getResources().getDrawable(getItem(position).imageResource);
    holder.textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    
    holder.itemView.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(
          View v, MotionEvent event) {
        
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          touchPosition(position);
          return false;
        case MotionEvent.ACTION_CANCEL:
          touchPosition(-1);
          return false;
        case MotionEvent.ACTION_MOVE:
          return false;
        case MotionEvent.ACTION_UP:
          touchPosition(-1);
          return false;
        }
        return true;
      }
    });
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(
          View v) {
        ContentManager.get().setCurrentSelectedFragmentPosition(position);
      }
    });
    
    // TODO: selected menu position, change layout accordingly
    if (mSelectedPosition == position || mTouchedPosition == position) {
      holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selected_gray));
    } else {
      holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }
  }
  
  private void touchPosition(
      int itemPosition) {
    int lastPosition = mTouchedPosition;
    mTouchedPosition = itemPosition;
    Log.d("itt", lastPosition + " / " + itemPosition);
    if (lastPosition >= 0)
      notifyItemChanged(lastPosition+1);
    if (itemPosition >= 0)
      notifyItemChanged(itemPosition+1);
  }
  
  public void selectPosition(
      int itemPosition) {
    int prevItemPosition = mSelectedPosition;
    mSelectedPosition = itemPosition;
    Log.d("its", prevItemPosition + " / " + itemPosition);
    notifyItemChanged(prevItemPosition+1);
    notifyItemChanged(itemPosition+1);
  }
  
  @Override public int getItemCount() {
    // +1 for header
    return (mData != null ? mData.size() + 1 : 1);
  }
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    
    public ViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.item_name);
    }
  }
  
  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    public HeaderViewHolder(View itemView) {
      super(itemView);
    }
  }
}