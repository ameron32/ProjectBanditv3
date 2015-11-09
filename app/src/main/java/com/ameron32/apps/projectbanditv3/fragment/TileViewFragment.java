package com.ameron32.apps.projectbanditv3.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.view.RevealView;
import com.qozix.tileview.TileView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TileViewFragment extends AbsContentFragment
    implements View.OnTouchListener {

  @InjectView(R.id.tileview) TileView mTileView;
  private RevealView revealView;

  public TileViewFragment() {}

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setPadding(false);

    try {
      Bitmap downsample = BitmapFactory.decodeStream(getActivity().getAssets().open("Basic.png"));
      final View downsampleLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_downsample_view, mTileView, false);
      final ImageView downsampleImageView = (ImageView) downsampleLayer.findViewById(R.id.downsample);
      downsampleImageView.setImageBitmap(downsample);
      mTileView.addScalingViewGroup((ViewGroup) downsampleLayer);
//      ImageView downsampleView = new ImageView(getContext());
//      downsampleView.setImageBitmap(downsample);
//
//      mTileView.addView(downsampleView, 0);
      mTileView.setSize(downsample.getWidth() * 8, downsample.getHeight() * 8);
      mTileView.setScaleLimits(0, 2);

      final View fogLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, mTileView, false);
      final View blackLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, mTileView, false);
      final RevealView fog = (RevealView) fogLayer.findViewById(R.id.reveal_view);
      final RevealView black = (RevealView) blackLayer.findViewById(R.id.reveal_view);
      fog._randomizeVisiblity(true, true);
      black._randomizeVisiblity(false, false);
      mTileView.addScalingViewGroup((ViewGroup) fogLayer);
      mTileView.addScalingViewGroup((ViewGroup) blackLayer);
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
    mTileView.destroy();
    ButterKnife.reset(this);
    super.onDestroy();
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
