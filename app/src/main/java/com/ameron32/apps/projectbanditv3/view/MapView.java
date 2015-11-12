package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.projectbanditv3.R;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by klemeilleur on 11/10/2015.
 */
public class MapView extends TileView {

  int tiles;
  int subTiles;

  List<Token> objectTokens;
  MarkerLayout objectLayer;
  List<Token> npcTokens;
  MarkerLayout npcLayer;
  RevealView fog;
  RevealView black;
  List<Token> playerTokens;
  MarkerLayout playerLayer;

  public MapView(Context context) {
    super(context);
    initialize();
  }

  public MapView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  private void initialize() {
    // sample settings
    String sampleDownsample = "https://i.imgur.com/OAbtGaM.jpg";
    String baseUrl250 = "https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/";
    int sdsWidth = 1920; int sdsHeight = 1920;
    int percent = 250;
    int tvSizeScale = 10;
    int tvWidth = 1920*10; int tvHeight = 1920*10;
    int tileSizeSq = 300;

    setDefaults(16, 6);
    setDownsample(sampleDownsample);
    setDetail(baseUrl250, percent, tileSizeSq);
    setTouchInterceptors();
    setMapSettings(sdsWidth, sdsHeight, tvSizeScale, 0.0f, 1.0f, true);

    drawObjectArcs();
    drawNPCArcs();
    drawPlayerArcs();

    drawObjects();
    drawNPCs();
      drawFog(true);
      drawBlack(true);
    drawPlayerTokens();
  }

  private void setDefaults(int tiles, int subTiles) {
    this.tiles = tiles;
    this.subTiles = subTiles;
    defineBounds(0, 0, tiles * subTiles, tiles * subTiles);
  }

  private void setMapSettings(int imageWidthPx, int imageHeightPx, int sizeMultiplier,
      float scaleMin, float scaleMax, boolean centerMarkers) {
    // size of original image at 100% mScale
    setSize(imageWidthPx * sizeMultiplier, imageHeightPx * sizeMultiplier);
    // allow scaling past original size
    setScaleLimits(scaleMin, scaleMax);
    // lets center all markers both horizontally and vertically
    if (centerMarkers) {
      setMarkerAnchorPoints(-0.5f, -0.5f);
    }
  }

  private void setDownsample(String imageUrl) {
//    final View downsampleLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_downsample_view, this, false);
//    final ImageView downsampleImageView = (ImageView) downsampleLayer.findViewById(R.id.downsample);
    final ImageView downsampleImageView = new ImageView(getContext());
    Picasso.with(getContext())
        .load(imageUrl)
        .into(downsampleImageView);
//    addScalingViewGroup((ViewGroup) downsampleLayer);
    addView(downsampleImageView, 0);
  }


  private void setDetail(String baseUrl, int size, int tileSize) {
    setBitmapProvider(new BitmapProvider() {
      @Override
      public Bitmap getBitmap(Tile tile, Context context) {
        Object data = tile.getData();
        if( data instanceof String ) {
          String unformattedFileName = (String) tile.getData();
          String formattedFileName = String.format(unformattedFileName, tile.getColumn(), tile.getRow());
          try {
            return Picasso.with(getContext())
                .load(formattedFileName)
                .get();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    });
    addDetailLevel(0.25f, baseUrl + size + "/%d_%d.png", tileSize, tileSize);
  }

  private void setTouchInterceptors() {
    setOnGenericMotionListener(new View.OnGenericMotionListener() {
      @Override
      public boolean onGenericMotion(View v, MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
          switch (event.getAction()) {
            case MotionEvent.ACTION_SCROLL:
              if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                zoomIn(event);
                return true;
              } else if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0.0f) {
                zoomOut(event);
                return true;
              }
          }
        }
        return false;
      }
    });

    getScalingLayout().setOnTouchListener(new View.OnTouchListener() {

      GestureDetector gestureDetector;
      final GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
          return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        boolean isStart = true;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
          float scale = getScale();
          if (isStart) {
            fog.squareRevealStart(e, scale);
            black.squareRevealStart(e, scale);
          } else {
            fog.squareRevealEnd(e, scale);
            fog.resetReveal();
            fog.revealSquare(0);
            black.squareRevealEnd(e, scale);
            black.revealSquare(1);
          }
          isStart = !isStart;
          return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
          float scale = getScale();
          fog.resetReveal();
          fog.reveal(e, scale, 1);
          black.reveal(e, scale, 2);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
          return false;
        }
      };

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector == null) {
          gestureDetector = new GestureDetector(v.getContext(), listener);
        }
        return gestureDetector.onTouchEvent(event);
      }
    });
  }

  private void zoomIn(MotionEvent e) {
    float destination = getScale() * 2;
    smoothScaleFromFocalPoint((int) e.getX(), (int) e.getY(), destination);
  }

  private void zoomOut(MotionEvent e) {
    float destination = getScale() / 2;
    smoothScaleFromFocalPoint((int) e.getX(), (int) e.getY(), destination);
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
    addScalingViewGroup(objectLayer);
  }

  private void drawNPCs() {
    npcLayer = new MarkerLayout(getContext());
    npcLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(npcLayer);
  }

  private void drawFog(boolean halfTileOffset) {
    final View fogLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, this, false);
    fog = (RevealView) fogLayer.findViewById(R.id.reveal_view);
    fog.setTiling(tiles * subTiles, tiles * subTiles, halfTileOffset);
    fog.setColor(Color.DKGRAY, 192);
    addScalingViewGroup((ViewGroup) fogLayer);
  }

  private void drawBlack(boolean halfTileOffset) {
    final View blackLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, this, false);
    black = (RevealView) blackLayer.findViewById(R.id.reveal_view);
    black.setTiling(tiles * subTiles, tiles * subTiles, halfTileOffset);
    addScalingViewGroup((ViewGroup) blackLayer);
  }

  private void drawPlayerTokens() {
    playerLayer = new MarkerLayout(getContext());
    playerLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(playerLayer);
    placeMarker(objectLayer, "max2b.png", 1000, 1000);
    placeMarker(playerLayer, "wizbang.png", 1300, 1000);
    placeMarker(playerLayer, "shield.png", 1000, 1300);
    scrollToAndCenter(1000, 1000);
  }

  private void placeMarker(MarkerLayout layer,
                           String path, int x, int y) {
    ImageView imageView = new ImageView(getContext());
    Picasso.with(getContext())
        .load("file:///android_asset/" + path)
        .resize(256, 256)
        .rotate(90f * new Random().nextInt(4))
        .into(imageView);
    layer.addMarker(imageView, x, y, null, null);
  }



  public static class Token {

  }
}
