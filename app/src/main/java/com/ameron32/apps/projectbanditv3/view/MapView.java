package com.ameron32.apps.projectbanditv3.view;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.TileMap;
import com.ameron32.apps.projectbanditv3.object.Token;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.markers.MarkerLayout;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.Picasso;
import com.wingjay.blurimageviewlib.BlurImageView;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 11/10/2015.
 */
public class MapView extends TileView
    implements MarkerLayout.MarkerTapListener {

  int tileCols, tileRows;
  int subTiles;
  boolean isFogOffset;
  float revealMultiplier;
  SparseArray<Integer> details = new SparseArray<>();

  // Object tokens hide in the black, but remain visible in the fog. They never move.
  List<Token> objectTokens;
  MarkerLayout objectLayer;

  // NPC tokens hide in the black and the fog. They
  List<Token> npcTokens;
  MarkerLayout npcLayer;

  // primary fog/black channel
  @Nullable RevealView fog;
  @Nullable RevealView black;

  // Player Tokens should always correspond to characters that are always visible
  List<Token> playerTokens;
  MarkerLayout playerLayer;


  Token currentToken;
  TileMap hostTileMap;

  boolean gmView;

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
    // nothing
    objectTokens = new ArrayList<>();
    npcTokens = new ArrayList<>();
    playerTokens = new ArrayList<>();
  }

  public void applyTileMap(TileMap currentTileMap) {
    this.hostTileMap = currentTileMap;
    this.setDownsample(currentTileMap.getDownsampleUrl());
    this.setDownsampleSize(currentTileMap.getDownsampleWidth(),
        currentTileMap.getDownsampleHeight());
    this.isFogOffset = currentTileMap.isFogOffset();
    this.revealMultiplier = currentTileMap.getRevealMultiplier();
    this.setTiles(currentTileMap.getTilesWidth(), currentTileMap.getTilesHeight(),
        currentTileMap.getSubTiles());
    this.setFullSizeMultiplier(currentTileMap.getFullSizeMultiplier());
    this.setMinMaxScale(currentTileMap.getMinScale(), currentTileMap.getMaxScale());
    final JSONObject detailLevels = currentTileMap.getDetailLevels();
    if (detailLevels != null) {
      for (int i = 0; i < detailLevels.length(); i++) {
        try {
          final JSONObject detailLevel = detailLevels.getJSONObject(
              i + "_" + MapView.DetailLevels.DETAIL_LEVEL);
          this.setBaseUrl(detailLevel.getString(MapView.DetailLevel.BASE_URL));
          final int tilePxX = detailLevel.getInt(MapView.DetailLevel.TILE_SIZE_X);
          final int tilePxY = detailLevel.getInt(MapView.DetailLevel.TILE_SIZE_Y);
          if (tilePxX != tilePxY) {
            throw new IllegalStateException("Cannot use non-square tiles yet.");
          }
          final int tilePxSq = tilePxY; // TODO make rect tiles?
          this.addDetailLevel(detailLevel.getInt(MapView.DetailLevel.SCALE_1000), tilePxSq);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
    this.start();
  }

  public void setGMView(boolean gmView) {
    this.gmView = gmView;
    if (gmView) {
      this.black.setColor(Color.BLACK, 64);
    } else {
      this.black.setColor(Color.BLACK, 255);
    }
  }



  String sampleDownsample;
  int sdsWidth, sdsHeight;
  int tvSizeScale;
  String baseUrl;
  float minScale, maxScale;
  public void setDownsample(String url) {
    sampleDownsample = url;
  }

  public void setDownsampleSize(int widthPx, int heightPx) {
    sdsWidth = widthPx;
    sdsHeight = heightPx;
  }

  public void setTilesSq(int tiles, int subTiles) {
    setDefaults(tiles, tiles, subTiles);
  }

  public void setTiles(int tilesX, int tilesY, int subTiles) {
    setDefaults(tilesX, tilesY, subTiles);
  }

  public void setFullSizeMultiplier(int timesBiggerThanDownsample) {
    tvSizeScale = timesBiggerThanDownsample;
  }

  public void setMinMaxScale(float min, float max) {
    this.minScale = min;
    this.maxScale = max;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void addDetailLevel(int scale1000, int tilePxSq) {
    details.append(scale1000, tilePxSq);
  }

  public void start() {
    applyDownsample(sampleDownsample);
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
    for(int i = 0; i < details.size(); i++) {
      int key = details.keyAt(i);
      // get the object by the key.
      Integer value = details.get(key);
      setDetail(baseUrl, key, value);
    }
    applyMapSettings(sdsWidth, sdsHeight, tvSizeScale, minScale, maxScale, true);

    prepareObjects();
    prepareNPCs();
    prepareFog(isFogOffset);
    prepareBlack(isFogOffset);
    preparePlayerTokens();

    setTouchInterceptors();
  }

  public Token addToken(TokenLayer layer, Token token) {
    MarkerLayout markerLayout;
    List<Token> tokenList;
    switch(layer) {
      case Object:
        markerLayout = objectLayer;
        tokenList = objectTokens;
        break;
      case NPC:
        markerLayout = npcLayer;
        tokenList = npcTokens;
        break;
      case Player:
      default:
        markerLayout = playerLayer;
        tokenList = playerTokens;
        break;
    }
    placeMarker(markerLayout, token);
    tokenList.add(token);
    return token;
  }

  public void setCurrentToken(Token token) {
    this.currentToken = token;
  }

  private void setDefaults(int tilesX, int tilesY, int subTiles) {
    this.tileCols = tilesX;
    this.tileRows = tilesY;
    this.subTiles = subTiles;
  }

  private void applyMapSettings(
      int imageWidthPx, int imageHeightPx, int sizeMultiplier,
      float scaleMin, float scaleMax, boolean centerMarkers) {
    // size of original image at 100% mScale
    setSize(imageWidthPx * sizeMultiplier, imageHeightPx * sizeMultiplier);
    defineBounds(0, 0, tileCols * subTiles, tileRows * subTiles);
    // allow scaling past original size
    setShouldScaleToFit(false);
    setScaleLimits(scaleMin, scaleMax);
    // lets center all markers both horizontally and vertically
    if (centerMarkers) {
      setMarkerAnchorPoints(-0.5f, -0.5f);
    }
  }

  private void applyDownsample(String imageUrl) {
    if (hostTileMap.hasDownsampleBlur()) {
      // load the blur view
      final BlurImageView downsampleImageView = new BlurImageView(getContext());
      addView(downsampleImageView, 0);
      downsampleImageView.setBlurFactor(3);
      downsampleImageView.setBlurImageByUrl(imageUrl);
    } else {
      // load a standard imageview
      final ImageView downsampleImageView = new ImageView(getContext());
      addView(downsampleImageView, 0);
      Picasso.with(getContext())
          .load(imageUrl)
          .into(downsampleImageView);
    }
  }

  private void setDetail(String baseUrl, int size, int tileSize) {
    addDetailLevel(((float) size / 1000.0f), baseUrl + size + "/%d_%d.png", tileSize, tileSize);
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

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector == null) {
          gestureDetector = new GestureDetector(v.getContext(), listener);
        }
        return gestureDetector.onTouchEvent(event);
      }

      final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
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
      };
    });

    objectLayer.setMarkerTapListener(this);
    npcLayer.setMarkerTapListener(this);
    playerLayer.setMarkerTapListener(this);
  }

  public void onMarkerTap(View v, int x, int y) {
    Token token = Token.fromView(v);
    if (token != null) {
      // TODO token pressed
      Snackbar.make(v, "Token: " + token.getTag(), Snackbar.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent event) {
    int x = (int) (getScrollX() + event.getX());
    int y = (int) (getScrollY() + event.getY());
    objectLayer.processHit(x, y);
    npcLayer.processHit(x, y);
    playerLayer.processHit(x, y);
    return super.onSingleTapConfirmed(event);
  }

  private TouchType touchType = TouchType.TokenTouch;
  public void setTouchType(TouchType type) {
    this.touchType = type;
  }

  boolean isStart = true;
  private void squareReveal(MotionEvent e) {
    float scale = getScale();
    if (isStart) {
      fog.squareRevealStart(e, scale);
      black.squareRevealStart(e, scale);
    } else {
      fog.squareRevealEnd(e, scale);
      fog.revealSquare(Math.round(0.0f * revealMultiplier));
      black.squareRevealEnd(e, scale);
      black.revealSquare(Math.round(0.5f * revealMultiplier));
      revealNPCs();
    }
    isStart = !isStart;
  }

  private void pointReveal(MotionEvent e) {
    float scale = getScale();
    fog.reveal(e, scale, Math.round(1.0f * revealMultiplier));
    black.reveal(e, scale, Math.round(1.5f * revealMultiplier));
    revealNPCs();
  }

  private void zoomIn(MotionEvent e) {
    float destination = getScale() * 2;
    smoothScaleFromFocalPoint((int) e.getX(), (int) e.getY(), destination);
  }

  private void zoomOut(MotionEvent e) {
    float destination = getScale() / 2;
    smoothScaleFromFocalPoint((int) e.getX(), (int) e.getY(), destination);
  }



  private void prepareObjects() {
    objectLayer = new MarkerLayout(getContext());
    objectLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(objectLayer);
  }

  private void prepareNPCs() {
    npcLayer = new MarkerLayout(getContext());
    npcLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(npcLayer);
  }

  private void prepareFog(boolean halfTileOffset) {
    if (hostTileMap.hasFog()) {
      final View fogLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, this, false);
      fog = (RevealView) fogLayer.findViewById(R.id.reveal_view);
      fog.setTiling(tileCols * subTiles, tileRows * subTiles, halfTileOffset);
      fog.setColor(Color.DKGRAY, 192);
      addScalingViewGroup((ViewGroup) fogLayer);
    } else {
      // NO FOG
      fog = null;
    }
  }

  private void prepareBlack(boolean halfTileOffset) {
    if (hostTileMap.hasBlack()) {
      final View blackLayer = LayoutInflater.from(getContext()).inflate(R.layout.merg_reveal_frame, this, false);
      black = (RevealView) blackLayer.findViewById(R.id.reveal_view);
      black.setTiling(tileCols * subTiles, tileRows * subTiles, halfTileOffset);
      if (gmView) {
        black.setColor(Color.BLACK, 64);
      } else {
        black.setColor(Color.BLACK, 0);
      }
      addScalingViewGroup((ViewGroup) blackLayer);
    } else {
       // NO BLACK
      black = null;
    }
  }

  private void preparePlayerTokens() {
    playerLayer = new MarkerLayout(getContext());
    playerLayer.setAnchors(-0.5f, -0.5f);
    addScalingViewGroup(playerLayer);
  }

  private void moveToken(MotionEvent e) {
    // TODO demo only
    fog.resetReveal();
    hideNPCs();
    Token token = moveToken(npcLayer, currentToken, e);
    revealNPCs();
    token.thenSave();
  }

  private void hideNPCs() {
    for (Token npc : npcTokens) {
      View marker = npc.findMarkerIn(npcLayer);
      marker.setVisibility(INVISIBLE);
    }
  }

  private void revealNPCs() {
    for (Token npc : npcTokens) {
      RevealView.Tile npcTileLocation = npc.getTile();
      if (fog.isShown(npcTileLocation) || gmView) {
        View marker = npc.findMarkerIn(npcLayer);
        marker.setVisibility(VISIBLE);
      }
    }
  }

  private Token moveToken(MarkerLayout layer, Token token, MotionEvent e) {
    RevealView.Tile tile = fog.getTileAt(e, getScale());
    token.move(tile.column(), tile.row());
    final View marker = token.findMarkerIn(layer);
    layer.moveMarker(marker, (int) (e.getX() / getScale()), (int) (e.getY() / getScale()));
    applyImage(token, marker);
    return token;
  }

  private void placeMarker(MarkerLayout layer, Token token) {
    ViewGroup tokenView = (ViewGroup) LayoutInflater.from(layer.getContext()).inflate(R.layout.token, layer, false);
    token.includeInTileMap(hostTileMap);
    token.attachTo(tokenView);
    final Drawable tokenBottom = ContextCompat.getDrawable(getContext(), R.drawable.circle_token);
    tokenBottom.mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.ADD);
    ImageView baseView = (ImageView) tokenView.findViewById(R.id.imageview_token_base);
    baseView.setImageDrawable(tokenBottom);
    layer.addMarker(tokenView,
        getCoordinateTranslater().translateX(token.getTileCol()),
        getCoordinateTranslater().translateY(token.getTileRow()), null, null);
    applyImage(token, tokenView);
    token.thenSave();
  }

  private void applyImage(Token token, View marker) {
    ImageView imageView = (ImageView) marker.findViewById(R.id.imageview_token_image);
    Picasso.with(getContext())
        .load(token.getUrl())
        .resize(token.getSizeX(), token.getSizeY())
        .rotate(token.getRotation())
        .into(imageView);
  }



  public enum TouchType {
    MoveViewportTouch, TokenTouch, RevealTouch, SquareRevealTouch;
  }

  public enum TokenLayer {
    Object, NPC, Player;
  }

  public static class DetailLevels extends JSONObject {
    public static final String DETAIL_LEVEL = "DETAIL_LEVEL";

    public DetailLevels put(DetailLevel... detailLevels) throws JSONException {
      for (int i = 0, detailLevelsLength = detailLevels.length; i < detailLevelsLength; i++) {
        DetailLevel level = detailLevels[i];
        put(i + "_" + DetailLevels.DETAIL_LEVEL, level);
      }
      return this;
    }

    public int getLevelsCount() {
      return this.length();
    }

    public DetailLevel get(int index) throws JSONException {
      if (index >= getLevelsCount()) {
        return null;
      }
      return (DetailLevel) this.getJSONObject(index + "_" + DETAIL_LEVEL);
    }
  }

  public static class DetailLevel extends JSONObject {
    public static final String BASE_URL = "BASE_URL";
    public static final String SCALE_1000 = "SCALE_1000";
    public static final String TILE_SIZE_X = "TILE_SIZE_X";
    public static final String TILE_SIZE_Y = "TILE_SIZE_Y";
    public static final String PATTERN = "PATTERN";
    public static final String FILE_EXTENSION = "FILE_EXTENSION";

    public DetailLevel put(String baseUrl, int scale1000, int tileSizeX,
            int tileSizeY, String pattern, String fileExtension) throws JSONException {
      put(DetailLevel.BASE_URL, baseUrl);
      put(DetailLevel.SCALE_1000, scale1000);
      put(DetailLevel.TILE_SIZE_X, tileSizeX);
      put(DetailLevel.TILE_SIZE_Y, tileSizeY);
      put(DetailLevel.PATTERN, pattern);
      put(DetailLevel.FILE_EXTENSION, fileExtension);
      return this;
    }

    public String getBaseUrl() throws JSONException {
      return getString(BASE_URL);
    }

    public int getScale1000() throws JSONException {
      return getInt(SCALE_1000);
    }

    public int getTileSizeX() throws JSONException {
      return getInt(TILE_SIZE_X);
    }

    public int getTileSizeY() throws JSONException {
      return getInt(TILE_SIZE_Y);
    }

    public String getPattern() throws JSONException {
      return getString(PATTERN);
    }

    public String getFileExtension() throws JSONException {
      return getString(FILE_EXTENSION);
    }
  }

  public View getView() {
    return this;
  }
}
