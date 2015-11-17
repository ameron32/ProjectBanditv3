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
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.ameron32.apps.projectbanditv3.object.*;
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

  MapView.Token max;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setPadding(false);

    final Character currentCharacter = CharacterManager.get().getCurrentCharacter();
    final Token token;

    // TODO import maps from Parse
    getMapView().setDownsample("https://i.imgur.com/OAbtGaM.jpg");
    getMapView().setDownsampleSize(1920, 1920);
    getMapView().setTilesSq(16, 6);
    getMapView().setFullSizeMultiplier(10);
    getMapView().setMinMaxScale(0.4f, 1.0f);
    getMapView().setBaseUrl("https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/");
    getMapView().addDetailLevel(0.25f, 300);
    getMapView().addDetailLevel(0.50f, 300);
    getMapView().addDetailLevel(1.00f, 300);
    getMapView().start();
//    getMapView().setDownsample("https://i.imgur.com/lMz7mcq.png");
//    getMapView().setDownsampleSize(803, 719);
//    getMapView().setTiles(57, 51, 1);
//    getMapView().setFullSizeMultiplier(15);
//    getMapView().setMinMaxScale(0.1f, 1.0f);
//    getMapView().start();

    max = getMapView().addToken(MapView.TokenLayer.Player,
        MapView.Token.create("Max2b", R.color.yellow,
        200, 200, "https://i.imgur.com/n5cM7gh.png"));
    getMapView().setCurrentToken(max);
  }

  @OnClick(R.id.button_reveal_one) void clickReveal() {
    getMapView().setTouchType(MapView.TouchType.RevealTouch);
  }

  @OnClick(R.id.button_reveal_square) void clickSquareReveal() {
    getMapView().setTouchType(MapView.TouchType.SquareRevealTouch);
  }

  @OnClick(R.id.button_move_token) void clickToken() {
    getMapView().setTouchType(MapView.TouchType.TokenTouch);
  }

  @OnClick(R.id.button_move_viewport) void clickMoveView() {
    getMapView().setTouchType(MapView.TouchType.MoveViewportTouch);
  }

  @Override
  protected int getCustomLayoutResource() {
    return R.layout.fragment_tileview;
  }

  @Override
  public void onPause() {
    super.onPause();
    getMapView().pause();
  }

  @Override
  public void onResume() {
    super.onResume();
    getMapView().resume();
  }

  @Override
  public void onDestroyView() {
    getMapView().destroy();
    ButterKnife.reset(this);
    super.onDestroy();
  }

  public MapView getMapView() {
    return mMapView;
  }
}
