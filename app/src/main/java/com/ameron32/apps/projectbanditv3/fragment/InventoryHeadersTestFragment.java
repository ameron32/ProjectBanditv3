package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;


//import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class InventoryHeadersTestFragment
    extends SectionContainerTestFragment
{
  
//  @InjectView(R.id.gridlayout_inventory) StickyGridHeadersGridView gridView1;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
//    gridView1.setAdapter(new InventoryHeadersAdapter(getActivity(), R.layout.row_griditem_inventory));
  }
}
