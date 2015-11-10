package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;

public abstract class AbsFrameRecyclerView
    extends RelativeLayout
    implements AbsMessageAdapter.OnDataSetChangedListener
{

  private RecyclerView mRecyclerView;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private final Context context;
  private LinearLayoutManager mLayoutManager;
  private AbsMessageAdapter mAdapter;

  public AbsFrameRecyclerView(
          Context context,
          AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
  }

  public AbsFrameRecyclerView(
          Context context,
          AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public AbsFrameRecyclerView(
          Context context) {
    super(context);
    this.context = context;
  }

  public void create() {
    initListView(this);
    initTitle(this);
    onCreate();
  }

  private void initTitle(
      View viewContainingTitleView) {
    TextView titleView = (TextView) viewContainingTitleView.findViewById(R.id.textview_chat_title);
    onCreateTitleView(titleView);
  }

  protected void onCreateTitleView(
      TextView titleView) {
    titleView.setText(this.getClass().getSimpleName());
  }

  private void initListView(
      View viewContainingListView) {
    mSwipeRefreshLayout = (SwipeRefreshLayout) viewContainingListView.findViewById(R.id.swipeRefreshLayout);
    if (mSwipeRefreshLayout != null) {
      mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          mAdapter.loadObjects();
        }
      });
    }
    mRecyclerView = (RecyclerView) viewContainingListView.findViewById(R.id.recyclerlistView1);
    // mRecyclerView.setAdapter(createAdapter(context));


    mRecyclerView.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
//    mLayoutManager.setStackFromEnd(true);
    mRecyclerView.setLayoutManager(mLayoutManager);
//    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    mAdapter = createAdapter(context);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.addOnDataSetChangedListener(this);
  }

  @Override public void onDataSetChanged() {
    if (mSwipeRefreshLayout != null) {
      mSwipeRefreshLayout.setRefreshing(false);
    }
    mRecyclerView.scrollToPosition(0);
  }

  protected abstract AbsMessageAdapter createAdapter(
      Context context);

  protected void onDestroy() {}

  protected void onCreate() {}

  public void destroy() {
    mAdapter.removeOnDataSetChangedListener(this);
    onDestroy();
  }

  protected AbsMessageAdapter getAdapter() {
    return mAdapter;
  }
}
