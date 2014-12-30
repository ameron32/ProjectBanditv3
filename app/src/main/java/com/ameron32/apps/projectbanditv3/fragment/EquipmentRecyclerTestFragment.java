package com.ameron32.apps.projectbanditv3.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.adapter.AbsParseSuperRecyclerQueryAdapter;
import com.ameron32.apps.projectbanditv3.adapter.EquipmentRecyclerAdapter;
import com.ameron32.apps.projectbanditv3.adapter.InitialHeaderAdapter;
import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EquipmentRecyclerTestFragment
    extends SectionContainerTestFragment
    implements SwipeRefreshLayout.OnRefreshListener,
    AbsParseSuperRecyclerQueryAdapter.OnQueryLoadListener<CInventory> {
  
  @InjectView(R.id.srv1)
  SuperRecyclerView list;

  private EquipmentRecyclerAdapter mAdapter;
  private InitialHeaderAdapter mDecoratorAdapter;
  private StickyHeadersItemDecoration mDecoration;

  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
//    listView1.setAdapter(new EquipmentHeadersAdapter(getActivity(), R.layout.row_equipment));

    mAdapter = new EquipmentRecyclerAdapter(R.layout.row_equipment);
//    mDecoratorAdapter = new InitialHeaderAdapter(mAdapter.getItems());

    list.setAdapter(mAdapter);
    list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    list.setRefreshListener(this);
    list.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);


//    // Build item decoration and add it to the RecyclerView
//    mDecoration = new StickyHeadersBuilder()
//        .setAdapter(mAdapter)
//        .setRecyclerView(list.getRecyclerView())
//        .setStickyHeadersAdapter(mDecoratorAdapter)
//        .build();
//
//    list.addItemDecoration(mDecoration);
    applyDecoration();

    mAdapter.addOnQueryLoadListener(this);
  }

  private void applyDecoration() {

    mDecoratorAdapter = new InitialHeaderAdapter(mAdapter.getItems());

    // Build item decoration and add it to the RecyclerView
    mDecoration = new StickyHeadersBuilder()
        .setAdapter(mAdapter)
        .setRecyclerView(list.getRecyclerView())
        .setStickyHeadersAdapter(mDecoratorAdapter)
        .build();

    list.addItemDecoration(mDecoration);
  }

  @Override
  public void onDestroyView() {
    mAdapter.removeOnQueryLoadListener(this);
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  @Override
  public void onRefresh() {
    mAdapter.loadObjects();
  }

  @Override
  public void onLoaded(List<CInventory> objects, Exception e) {
    if (e == null) {
      mDecoratorAdapter.replaceItems(objects);

//      list.removeItemDecoration(mDecoration);

//      // Build item decoration and add it to the RecyclerView
//      StickyHeadersItemDecoration decoration = new StickyHeadersBuilder()
//          .setAdapter(mAdapter)
//          .setRecyclerView(list.getRecyclerView())
//          .setStickyHeadersAdapter(mDecoratorAdapter)
//          .build();
//
//      list.addItemDecoration(decoration);
//      applyDecoration();
    }
  }

  @Override
  public void onLoading() {

  }
}
