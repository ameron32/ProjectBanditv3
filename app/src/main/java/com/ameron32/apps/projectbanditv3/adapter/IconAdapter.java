package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.ContentManager;

import butterknife.ButterKnife;



public class IconAdapter
    extends
    RecyclerView.Adapter<IconAdapter.ViewHolder> {
  
  private int mSelectedPosition;
  private int mTouchedPosition = -1;
  
  private LayoutInflater mInflater;
  private int mLayoutResource;
  private int mImageViewId;
  private int[] mImageResources;
  
  public IconAdapter(
      Context context,
      int layoutResource,
      int imageViewId,
      int[] imageResources) {
    this.mLayoutResource = layoutResource;
    this.mImageViewId = imageViewId;
    this.mImageResources = imageResources;
    this.mInflater = LayoutInflater.from(context);
  }
  
  public static class ViewHolder extends
      RecyclerView.ViewHolder {
    
    ImageView mImageView;
    
    public ViewHolder(View itemView,
        int imageViewResId) {
      super(itemView);
      ButterKnife.inject(this, itemView);
      
      mImageView = (ImageView) itemView.findViewById(imageViewResId);
    }
  }
  
  public int getItemResource(
      int position) {
    return mImageResources[position];
  }
  
  @Override public int getItemCount() {
    return mImageResources.length;
  }
  
  @Override public ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    View inflatedView = mInflater.inflate(mLayoutResource, parent, false);
    return new ViewHolder(inflatedView, mImageViewId);
  }
  
  @Override public void onBindViewHolder(
      final ViewHolder holder,
      final int position) {
    holder.mImageView.setImageResource(getItemResource(position));
    
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
    if (mSelectedPosition == position
        || mTouchedPosition == position) {
      holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selected_gray));
    } else {
      holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }
  }
  
  private void touchPosition(
      int position) {
    int lastPosition = mTouchedPosition;
    mTouchedPosition = position;
    if (lastPosition >= 0)
      notifyItemChanged(lastPosition);
    if (position >= 0)
      notifyItemChanged(position);
  }
  
  public void selectPosition(
      int position) {
    int lastPosition = mSelectedPosition;
    mSelectedPosition = position;
    notifyItemChanged(lastPosition);
    notifyItemChanged(position);
  }
}
