package com.ameron32.apps.projectbanditv3.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ameron32.apps.projectbanditv3.adapter.EquipmentAdapter;
import com.ameron32.apps.projectbanditv3.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EquipmentTestFragment
    extends
    SectionContainerTestFragment {
  
  @InjectView(R.id.listView1) ListView listView1;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
    listView1.setAdapter(new EquipmentAdapter(getActivity(), R.layout.row_equipment));
  }
}
