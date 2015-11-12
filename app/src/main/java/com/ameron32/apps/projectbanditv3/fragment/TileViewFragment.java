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
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class TileViewFragment extends AbsContentFragment
    implements View.OnTouchListener {

  @Optional @InjectView(R.id.tileview) TileView mTileView;
  @Optional @InjectView(R.id.mapview) MapView mMapView;

  public TileViewFragment() {}

  MarkerLayout objectLayer;
  MarkerLayout npcLayer;
  RevealView fog;
  RevealView black;
  MarkerLayout playerLayer;

  private static final int imageWidthPx = 1920;
  private static final int imageHeightPx = 1920;
  private static final int tiles = 16;
  private static final int subTiles = 6;

  private static final String imageUrl = "https://i.imgur.com/OAbtGaM.jpg";

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    setPadding(false);
//    setDemoTileView();
  }

  private void setDemoTileView() {
    try {
//      Bitmap downsample;
//      downsample = BitmapFactory.decodeStream(getActivity().getAssets().open("Basic.png"));
      final View downsampleLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_downsample_view, mTileView, false);
      final ImageView downsampleImageView = (ImageView) downsampleLayer.findViewById(R.id.downsample);
//      downsampleImageView.setImageBitmap(downsample);
      Picasso.with(getContext()).load(imageUrl).into(downsampleImageView);
      mTileView.addScalingViewGroup((ViewGroup) downsampleLayer);
      mTileView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
        @Override
        public boolean onGenericMotion(View v, MotionEvent event) {
          if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
              case MotionEvent.ACTION_SCROLL:
                if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f)
                  zoomIn(event);
                else
//                  zoomOut();
                  return true;
            }
          }
          return false;
        }
      });

      // size of original image at 100% mScale
      mTileView.setSize(imageWidthPx * 8, imageHeightPx * 8);
      // allow scaling past original size
      mTileView.setScaleLimits(0, 2);

      // lets center all markers both horizontally and vertically
      mTileView.setMarkerAnchorPoints(-0.5f, -0.5f);

      mTileView.getScalingLayout().setOnTouchListener(new View.OnTouchListener() {

        GestureDetector gestureDetector;
        final GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {
          @Override public boolean onDown(MotionEvent e) { return false; }
          @Override public void onShowPress(MotionEvent e) {}
          @Override public boolean onSingleTapUp(MotionEvent e) { return false; }
          @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
          @Override public void onLongPress(MotionEvent e) {
//            float scale = mTileView.getScale();
            float scale = mTileView.getScalingLayout().getScale();
            fog.resetReveal();
            fog.reveal(e, scale, 1);
            black.reveal(e, scale, 2);
          }
          @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }
        };

        @Override
        public boolean onTouch(View v, MotionEvent event) {
          if (gestureDetector == null) {
            gestureDetector = new GestureDetector(v.getContext(), listener);
          }
          gestureDetector.onTouchEvent(event);

          float scale = mTileView.getScalingLayout().getScale();
//          if (event.getAction() == MotionEvent.AC) {
//            black.squareRevealStart(event, scale);
//          } else if(event.getAction() == MotionEvent.ACTION_POINTER_UP) {
//            black.squareRevealEnd(event, scale);
//            black.revealSquare();
//          }
          return false;
        }
      });
    } finally {

    }

    drawObjectArcs();
    drawNPCArcs();
    drawPlayerArcs();

    drawObjects();
    drawNPCs();

    drawFog();
    drawBlack();

    drawPlayerTokens();
  }

  private void zoomIn(MotionEvent event) {
    mTileView.onDoubleTap(event);
  }

  private void drawObjectArcs() {
    // TODO circles for range
  }

  private void drawNPCArcs() {
    // TODO circles for range
  }

  private void drawPlayerArcs() {
    // TODO circles for range
  }

  private void drawObjects() {
    objectLayer = new MarkerLayout(getContext());
    objectLayer.setAnchors(-0.5f, -0.5f);
    mTileView.addScalingViewGroup(objectLayer);
  }

  private void drawNPCs() {
    npcLayer = new MarkerLayout(getContext());
    npcLayer.setAnchors(-0.5f, -0.5f);
    mTileView.addScalingViewGroup(npcLayer);
  }

  private void drawFog() {
    final View fogLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, mTileView, false);
    fog = (RevealView) fogLayer.findViewById(R.id.reveal_view);
    fog.setTiling(tiles * subTiles, tiles * subTiles, true);
    fog.setColor(Color.DKGRAY, 192);
    mTileView.addScalingViewGroup((ViewGroup) fogLayer);
  }

  private void drawBlack() {
    final View blackLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, mTileView, false);
    black = (RevealView) blackLayer.findViewById(R.id.reveal_view);
    black.setTiling(tiles * subTiles, tiles * subTiles, true);
    mTileView.addScalingViewGroup((ViewGroup) blackLayer);
  }

  private void drawPlayerTokens() {
    playerLayer = new MarkerLayout(getContext());
    playerLayer.setAnchors(-0.5f, -0.5f);
    mTileView.addScalingViewGroup(playerLayer);
    placeMarker(objectLayer, "max2b.png", 1000, 1000);
    placeMarker(playerLayer, "wizbang.png", 1300, 1000);
    placeMarker(playerLayer, "shield.png", 1000, 1300);
    frameTo(1000, 1000);
  }

  private void placeMarker(MarkerLayout layer, String path, int x, int y) {
    ImageView imageView = new ImageView(getContext());
    Picasso.with(getContext())
        .load("file:///android_asset/"+path)
        .resize(256, 256)
        .rotate(90f * new Random().nextInt(4))
        .into(imageView);
    layer.addMarker(imageView, x, y, null, null);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
//    mTileView.onTouchEvent(event);
    mMapView.onTouchEvent(event);
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
