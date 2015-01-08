package com.ameron32.apps.projectbanditv3.tileview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.fragment.AbsContentFragment;
import com.qozix.tileview.TileView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TileViewFragment extends AbsContentFragment implements View.OnTouchListener {

  private TileView mTileView;
  private TileView mOverlayView;

  public TileViewFragment() {
  }

  @InjectView(R.id.loadview)
  LoadView loadView;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    ViewGroup viewGroup = (ViewGroup) view;
    mTileView = new TileView(getActivity());
    mTileView.setLayoutParams(
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    viewGroup.addView(mTileView, 0);

    mOverlayView = new TileView(getActivity());
    mOverlayView.setLayoutParams(
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    viewGroup.addView(mOverlayView, 1);
    mOverlayView.setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    mTileView.onTouchEvent(event);
    return false;
  }

  @Override
  protected int getCustomLayoutResource() {
    return R.layout.fragment_tileview;
  }

  @Override
  public void onPause() {
    super.onPause();
    mTileView.clear();
    mOverlayView.clear();
  }

  @Override
  public void onResume() {
    super.onResume();
    mTileView.resume();
    mOverlayView.resume();
  }

  @Override
  public void onDestroyView() {
    super.onDestroy();
    ButterKnife.reset(this);
    mTileView.destroy();
    mTileView = null;
    mOverlayView.destroy();
    mOverlayView = null;
  }

  public TileView getTileView() {
    return mTileView;
  }

  public TileView getOverlayView() {
    return mOverlayView;
  }

  /**
   * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
   * see https://github.com/moagrius/TileView/wiki/FAQ
   */
  public void frameTo(final double x, final double y) {
    getTileView().post(new Runnable() {
      @Override
      public void run() {
        getTileView().moveToAndCenter(x, y);
      }
    });

    getOverlayView().post(new Runnable() {
      @Override
      public void run() {
        getOverlayView().moveToAndCenter(x, y);
      }
    });
  }
}