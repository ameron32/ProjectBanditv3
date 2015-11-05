package com.ameron32.apps.projectbanditv3.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.fragment.AbsContentFragment;
import com.qozix.tileview.TileView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TileViewFragment extends AbsContentFragment
    implements View.OnTouchListener {

  @InjectView(R.id.tileview) TileView mTileView;

  public TileViewFragment() {}

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

    try {
      Bitmap downsample = BitmapFactory.decodeStream(getActivity().getAssets().open("map.png"));
      ImageView downsampleView = (ImageView) view.findViewById(R.id.downsample);
      downsampleView.setImageBitmap(downsample);

      mTileView.setSize(downsample.getWidth() * 8, downsample.getHeight() * 8);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
  public void onDestroyView() {
    super.onDestroy();
    ButterKnife.reset(this);
    mTileView.destroy();
    mTileView = null;
  }

  public TileView getTileView() {
    return mTileView;
  }

  /**
   * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
   * see https://github.com/moagrius/TileView/wiki/FAQ
   */
  public void frameTo(final double x, final double y) {
    getTileView().post(new Runnable() {
      @Override
      public void run() {
        getTileView().scrollToAndCenter(x, y);
      }
    });
  }
}
