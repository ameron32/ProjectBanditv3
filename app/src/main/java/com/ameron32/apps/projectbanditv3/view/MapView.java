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
import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by klemeilleur on 11/10/2015.
 */
public class MapView extends TileView {

  int tiles;
  int subTiles;

  CoordinateTranslater tileTranslater = new CoordinateTranslater();
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

  public View getView() {
    return this;
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
  }

  private void setMapSettings(int imageWidthPx, int imageHeightPx, int sizeMultiplier,
      float scaleMin, float scaleMax, boolean centerMarkers) {
    // size of original image at 100% mScale
    setSize(imageWidthPx * sizeMultiplier, imageHeightPx * sizeMultiplier);
    tileTranslater.setSize(imageWidthPx * sizeMultiplier, imageHeightPx * sizeMultiplier);
    tileTranslater.setBounds(0, 0, tiles * subTiles, tiles * subTiles);
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
        if (data instanceof String) {
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
          switch (touchType) {
            case MoveViewportTouch:
              return false;
            default:
              return true;
          }
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
          switch (touchType) {
            case TokenTouch:
              moveToken(e);
              break;
            case RevealTouch:
              pointReveal(e);
              break;
            case SquareRevealTouch:
              squareReveal(e);
              break;
            default:
              // do nothing
              break;
          }
          return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
//          pointReveal(e);
//          squareReveal(e);
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

  private TouchType touchType = TouchType.TokenTouch;
  public void setTouchType(TouchType type) {
    this.touchType = type;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  boolean isStart = true;
  private void squareReveal(MotionEvent e) {
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
  }

  private void pointReveal(MotionEvent e) {
    float scale = getScale();
    fog.resetReveal();
    fog.reveal(e, scale, 1);
    black.reveal(e, scale, 2);
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

  Token max;
  private void drawPlayerTokens() {
    playerLayer = new MarkerLayout(getContext());
    playerLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(playerLayer);
//    addToken(playerLayer)
    max = addToken(playerLayer, Token.create("Max2b",
        250, 250, "file:///android_asset/" + "max2b.png"));
//    moveToken(playerLayer, max, 12, 12);
//    placeMarker(objectLayer, "max2b.png", 12, 12);
//    placeMarker(playerLayer, "wizbang.png", 1300, 1000);
//    placeMarker(playerLayer, "shield.png", 13, 13);
    scrollToTileAndCenter(12, 12);
  }

  private Token addToken(MarkerLayout layer, Token token) {
    placeMarker(layer, token);
    return token;
  }

  private void moveToken(MotionEvent e) {
    // TODO demo only
    moveToken(playerLayer, max, e);
  }

  private Token moveToken(MarkerLayout layer, Token token, MotionEvent e) {
    RevealView.Tile tile = fog.getTileAt(e, getScale());
    pointReveal(e);

    token.move(tile.row, tile.col);
    final View marker = layer.findViewWithTag(token.tag);
    layer.moveMarker(marker, (int) (e.getX() / getScale()), (int) (e.getY() / getScale()));
    applyImage(token, marker);
    scrollToTileAndCenter(tile.row, tile.col);
    return token;
  }

  private void placeMarker(MarkerLayout layer, Token token) {
    ImageView imageView = new ImageView(getContext());
    imageView.setTag(token.tag);
    final View marker = layer.addMarker(imageView,
        tileTranslater.translateX(token.tileX),
        tileTranslater.translateY(token.tileY), null, null);
    applyImage(token, marker);
  }

  private void applyImage(Token token, View marker) {
    ImageView imageView = (ImageView) marker;
    Picasso.with(getContext())
        .load(token.url)
        .resize(token.sizeX, token.sizeY)
        .rotate(token.rotation)
        .into(imageView);
  }

  public void scrollToTileAndCenter(int tileX, int tileY) {
    scrollToAndCenter(
        tileTranslater.translateAndScaleX(tileX, getScale()),
        tileTranslater.translateAndScaleY(tileY, getScale())
    );
  }



  public enum TouchType {
    MoveViewportTouch, TokenTouch, RevealTouch, SquareRevealTouch;
  }

  public static class Token {
    String tag;
    int tileX;
    int tileY;
    int sizeX;
    int sizeY;
    String url;
    float rotation;

    static Token create(String tag, int sizeX, int sizeY, String url) {
      Token t = new Token();
      t.tag = tag;
      t.sizeX = sizeX;
      t.sizeY = sizeY;
      t.url = url;
      t.tileX = 0;
      t.tileY = 0;
      t.rotation = 0.0f;
      return t;
    }

    public Token move(int tileX, int tileY) {
      final int oldX = this.tileX;
      final int oldY = this.tileY;
      double theta = Math.atan2(tileY-oldY, tileX-oldX);
      theta += Math.PI/2.0;
      double angle = Math.toDegrees(theta);
      if (angle < 0) {
        angle += 360;
      }
      angle += 180;
      if (angle < 0) {
        angle += 360;
      }
      this.rotation = (float) angle;
      this.tileX = tileX;
      this.tileY = tileY;
      return this;
    }
  }
}
