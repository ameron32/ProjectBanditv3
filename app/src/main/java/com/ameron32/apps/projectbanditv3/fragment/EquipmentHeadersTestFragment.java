package com.ameron32.apps.projectbanditv3.fragment;


import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class EquipmentHeadersTestFragment
    extends SectionContainerTestFragment
{
  
//  @InjectView(R.id.listView1) StickyListHeadersListView listView1;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
//    listView1.setAdapter(new EquipmentHeadersAdapter(getActivity(), R.layout.row_equipment));
  }
}
