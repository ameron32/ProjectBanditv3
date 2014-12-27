package com.ameron32.apps.projectbanditv3.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ameron32.apps.projectbanditv3.R;

/**
 * 
 * TODO: fix SectionGridPagerAdapter with massive overhaul
 * 
 * @author klemeilleur
 *
 */
public class SectionGridPagerAdapter
    extends BaseAdapter {
  
  private LayoutInflater inflater;
  
  public SectionGridPagerAdapter(
      Context context) {
    inflater = LayoutInflater.from(context);
  }
  
  @Override public int getCount() {
    return 60;
  }
  
  @Override public Object getItem(
      int position) {
    return null;
  }
  
  @Override public long getItemId(
      int position) {
    return position;
  }
  
  @Override public View getView(
      int position, View convertView,
      ViewGroup parent) {
    return inflater.inflate(R.layout.section_character_stats, parent, false);
  }
  
}
