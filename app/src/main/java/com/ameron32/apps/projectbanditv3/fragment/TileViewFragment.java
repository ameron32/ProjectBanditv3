package com.ameron32.apps.projectbanditv3.fragment;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.ameron32.apps.projectbanditv3.object.Game;
import com.ameron32.apps.projectbanditv3.object.TileMap;
import com.ameron32.apps.projectbanditv3.object.Token;
import com.ameron32.apps.projectbanditv3.view.MapView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

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

//    new AsyncTask() {
//      public static final String TAG = "Annon/AsyncTask";
//
//      @Override
//      protected Object doInBackground(Object[] params) {
//        Log.d(TAG, "doInBackground: start");
//
//
//        try {
//          ParseObject fourCornersMap = ParseQuery.getQuery("TileMap")
//                  .get("so82lqHrAi");
////          fourCornersMap.put("downsampleWidth", 1920);
////          fourCornersMap.put("downsampleHeight", 1920);
////          fourCornersMap.put("fullSizeMultiplier", 10);
////          fourCornersMap.put("tilesWidth", 16);
////          fourCornersMap.put("tilesHeight", 6);
////          fourCornersMap.put("subTiles", 6);
////          fourCornersMap.put("minScale", 0.4f);
////          fourCornersMap.put("maxScale", 1.0f);
//          fourCornersMap.put("detailLevels",
//                  new MapView.DetailLevels().put(
//                          new MapView.DetailLevel().put(
//                                  "https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/",
//                                  250,
//                                  300, 300,
//                                  "%d_%d",
//                                  ".png"
//                          ),
//                          new MapView.DetailLevel().put(
//                                  "https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/",
//                                  500,
//                                  300, 300,
//                                  "%d_%d",
//                                  ".png"
//                          ),
//                          new MapView.DetailLevel().put(
//                                  "https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/",
//                                  1000,
//                                  300, 300,
//                                  "%d_%d",
//                                  ".png"
//                          )));
//          fourCornersMap.save();
//        } catch (ParseException e) {
//          e.printStackTrace();
//        } catch (JSONException e) {
//          e.printStackTrace();
//        }
//
////        try {
////          ParseObject stoneTabletMap = ParseQuery.getQuery("TileMap")
////                  .get("dyT7mc6F7i");
////          stoneTabletMap.put("downsampleWidth", 803);
////          stoneTabletMap.put("downsampleHeight", 719);
////          stoneTabletMap.put("fullSizeMultiplier", 15);
////          stoneTabletMap.put("tilesWidth", 57);
////          stoneTabletMap.put("tilesHeight", 51);
////          stoneTabletMap.put("subTiles", 1);
////          stoneTabletMap.put("minScale", 0.1f);
////          stoneTabletMap.put("maxScale", 1.0f);
//////      stoneTabletMap.put("detailLevels",
//////              new MapView.DetailLevels().put(
//////                      new MapView.DetailLevel().put()));
////          stoneTabletMap.save();
////        } catch (ParseException e) {
////          e.printStackTrace();
////        }
//        return null;
//      }
//    }.execute();

    final Character currentCharacter = CharacterManager.get().getCurrentCharacter();
    final TileMap currentTileMap = GameManager.get().getCurrentMap();
    final Token token;

    getMapView().setDownsample(currentTileMap.getDownsampleUrl());
    getMapView().setDownsampleSize(currentTileMap.getDownsampleWidth(),
            currentTileMap.getDownsampleHeight());
    getMapView().setTiles(currentTileMap.getTilesWidth(), currentTileMap.getTilesHeight(),
            currentTileMap.getSubTiles());
    getMapView().setFullSizeMultiplier(currentTileMap.getFullSizeMultiplier());
    getMapView().setMinMaxScale(currentTileMap.getMinScale(), currentTileMap.getMaxScale());
      final JSONObject detailLevels = currentTileMap.getDetailLevels();
      if (detailLevels != null) {
          for (int i = 0; i < detailLevels.length(); i++) {
              try {
                  final JSONObject detailLevel = detailLevels.getJSONObject(
                          i + "_" + MapView.DetailLevels.DETAIL_LEVEL);
                  getMapView().setBaseUrl(detailLevel.getString(MapView.DetailLevel.BASE_URL));
                  final int tilePxX = detailLevel.getInt(MapView.DetailLevel.TILE_SIZE_X);
                  final int tilePxY = detailLevel.getInt(MapView.DetailLevel.TILE_SIZE_Y);
                  if (tilePxX != tilePxY) {
                      throw new IllegalStateException("Cannot use non-square tiles yet.");
                  }
                  final int tilePxSq = tilePxY; // TODO make rect tiles?
                  getMapView().addDetailLevel(detailLevel.getInt(MapView.DetailLevel.SCALE_1000), tilePxSq);
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
      }
      getMapView().start();

    // TODO import maps from Parse
//    getMapView().setDownsample("https://i.imgur.com/OAbtGaM.jpg");
//    getMapView().setDownsampleSize(1920, 1920);
//    getMapView().setTilesSq(16, 6);
//    getMapView().setFullSizeMultiplier(10);
//    getMapView().setMinMaxScale(0.4f, 1.0f);
//    getMapView().setBaseUrl("https://dl.dropboxusercontent.com/u/949753/android/TileView/4corners/");
//    getMapView().addDetailLevel(250, 300);
//    getMapView().addDetailLevel(500, 300);
//    getMapView().addDetailLevel(1000, 300);
//    getMapView().start();

//    getMapView().setDownsample("https://i.imgur.com/lMz7mcq.png");
//    getMapView().setDownsampleSize(803, 719);
//    getMapView().setTiles(57, 51, 1);
//    getMapView().setFullSizeMultiplier(15);
//    getMapView().setMinMaxScale(0.1f, 1.0f);
//    getMapView().start();



//    max = getMapView().addToken(MapView.TokenLayer.Player,
//        MapView.Token.create("Max2b", R.color.yellow,
//        200, 200, "https://i.imgur.com/n5cM7gh.png"));
//    getMapView().setCurrentToken(max);
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
