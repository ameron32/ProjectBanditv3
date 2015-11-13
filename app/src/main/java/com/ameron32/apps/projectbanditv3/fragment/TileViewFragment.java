package com.ameron32.apps.projectbanditv3.fragment;

import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.view.MapView;
import com.ameron32.apps.projectbanditv3.view.RevealView;
import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.widgets.ZoomPanLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

public class TileViewFragment extends AbsContentFragment {

  @Optional @InjectView(R.id.mapview) MapView mMapView;

  public TileViewFragment() {}

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setPadding(false);
  }

  @OnClick(R.id.button_reveal_one) void clickReveal() {
    mMapView.setTouchType(MapView.TouchType.RevealTouch);
  }

  @OnClick(R.id.button_reveal_square) void clickSquareReveal() {
    mMapView.setTouchType(MapView.TouchType.SquareRevealTouch);
  }

  @OnClick(R.id.button_move_token) void clickToken() {
    mMapView.setTouchType(MapView.TouchType.TokenTouch);
  }

  @OnClick(R.id.button_move_viewport) void clickMoveView() {
    mMapView.setTouchType(MapView.TouchType.MoveViewportTouch);
  }

  @Override
  protected int getCustomLayoutResource() {
    return R.layout.fragment_tileview;
  }

  @Override
  public void onDestroyView() {
    mMapView.destroy();
    ButterKnife.reset(this);
    super.onDestroy();
  }

  public MapView getMapView() {
    return mMapView;
  }
}
