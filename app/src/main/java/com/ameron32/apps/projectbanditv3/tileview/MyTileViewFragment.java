package com.ameron32.apps.projectbanditv3.tileview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.tileview.TileMap;
import com.ameron32.apps.projectbanditv3.tileview.TiledXMLParser;
import com.ameron32.apps.projectbanditv3.tileview.Tileset;
import com.qozix.tileview.TileView;
import com.qozix.tileview.TileView.TileViewEventListener;
import com.qozix.tileview.graphics.BitmapDecoderAssets;
import com.qozix.tileview.graphics.BitmapDecoderHttp;
import com.qozix.tileview.markers.MarkerEventListener;

public class MyTileViewFragment extends TileViewFragment {
  
  private static final boolean DEBUG = true;
  
  public static final int BASIC_TILEVIEW   = 0;
  public static final int FULL_TILEVIEW    = 1;
  public static final int GRIDLAYOUT       = 2;
  
  public static final int TILEVIEW_VERSION = GRIDLAYOUT;
  
  public static MyTileViewFragment newInstance() {
    return new MyTileViewFragment();
  }
  
  public MyTileViewFragment() {}
  
  private void initTileView() {
    
    // multiple references
    TileView tileView = getTileView();
    
    // size of original image at 100% scale
    tileView.setSize(15000, 9637);
    
    // detail levels
    tileView.addDetailLevel(1.000f, "tiles-old/fantasy/1000/%col%_%row%.jpg", "samples-old/middle-earth.jpg");
    tileView.addDetailLevel(0.500f, "tiles-old/fantasy/500/%col%_%row%.jpg", "samples-old/middle-earth.jpg");
    tileView.addDetailLevel(0.250f, "tiles-old/fantasy/250/%col%_%row%.jpg", "samples-old/middle-earth.jpg");
    tileView.addDetailLevel(0.125f, "tiles-old/fantasy/125/%col%_%row%.jpg", "samples-old/middle-earth.jpg");
    
    // allow scaling past original size
    tileView.setScaleLimits(0, 2);
    
    // lets center all markers both horizontally and vertically
    tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
    
    // individual markers
    placeMarker(R.drawable.fantasy_elves, 1616, 1353);
    placeMarker(R.drawable.fantasy_humans, 2311, 2637);
    placeMarker(R.drawable.fantasy_dwarves, 2104, 701);
    placeMarker(R.drawable.fantasy_rohan, 2108, 1832);
    placeMarker(R.drawable.fantasy_troll, 3267, 1896);
    
    // frame the troll
    frameTo(3267, 1896);
    
    // scale down a little
    tileView.setScale(0.5);
  }
  
  private void initTileViewNew() {
    
    // multiple references
    TileView tileView = getTileView();
    requestView = (RequestView) getView().findViewById(R.id.request_view);
    
    tileView.addTileViewEventListener(new TileView.TileViewEventListener() {
      
      @Override
      public void onDetailLevelChanged() {}
      
      @Override
      public void onDoubleTap(int x, int y) {}
      
      @Override
      public void onDrag(int x, int y) {}
      
      @Override
      public void onFingerDown(int x, int y) {}
      
      @Override
      public void onFingerUp(int x, int y) {}
      
      @Override
      public void onFling(int sx, int sy, int dx, int dy) {}
      
      @Override
      public void onFlingComplete(int x, int y) {}
      
      @Override
      public void onPinch(int x, int y) {}
      
      @Override
      public void onPinchComplete(int x, int y) {}
      
      @Override
      public void onPinchStart(int x, int y) {}
      
      @Override
      public void onRenderComplete() {}
      
      @Override
      public void onRenderStart() {}
      
      @Override
      public void onScaleChanged(double scale) {
        
        log("SCALE_CHANGED", "scale: " + scale);
        for (ImageView iv : lIV) {
          iv.setScaleX((float) scale);
          iv.setScaleY((float) scale);
          iv.invalidate();
        }
      }
      
      @Override
      public void onScrollChanged(int x, int y) {}
      
      @Override
      public void onTap(final int x, final int y) {
        
        requestView.setMessage("Create a New Marker @ " + x + "/" + y + " ?");
        requestView.setPositiveListener(new View.OnClickListener() {
          
          @Override
          public void onClick(View v) {
            double scale = getTileView().getScale();
            log("SCALE", "scale: " + scale);
            placeMarker(R.drawable.fantasy_dwarves, x / scale, y / scale);
            requestView.hide();
          }
        });
        requestView.setNegativeListener(new View.OnClickListener() {
          
          @Override
          public void onClick(View v) {
            requestView.hide();
          }
        });
        requestView.show();
      }
      
      @Override
      public void onZoomComplete(double scale) {}
      
      @Override
      public void onZoomStart(double scale) {}
    });
    
    tileView.addMarkerEventListener(new MarkerEventListener() {
      
      @Override
      public void onMarkerTap(View v, int x, int y) {
        String message = "Marker Tapped @: " + x + "/" + y;
//        TextView textView = ((TextView) getView().findViewById(R.id.tvHello));
//        textView.setText(message);
        log("MARKER_TAP", message);
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
      }
    });
    
    int[] size = { 15000, 9637 };
    
    // Set the minimum parameters
    tileView.setSize(size[0], size[1]);
    
    
    tileView.setDownsampleDecoder(new BitmapDecoderAssets());
    tileView.setTileDecoder(new BitmapDecoderHttp());
    String http = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/jhara/";
    String tiles = http + "tiles/";
    tileView.setCacheEnabled(true);
    
    // detail levels
    /*
     * tileView.addDetailLevel(1f, "tiles/1000_%col%_%row%.png",
     * "downsamples/map.png");
     */
    tileView.addDetailLevel(0.5f, tiles + "500_%col%_%row%.png", "map.png");
    tileView.addDetailLevel(0.25f, tiles + "250_%col%_%row%.png", "map.png");
    tileView.addDetailLevel(0.125f, tiles + "125_%col%_%row%.png", "map.png");
    /*
     * tileView.addDetailLevel(0.0625f, "tiles/62_%col%_%row%.png",
     * "downsamples/map.png");
     * tileView.addDetailLevel(0.03125f, "tiles/31_%col%_%row%.png",
     * "downsamples/map.png");
     * tileView.addDetailLevel(0.015625f, "tiles/15_%col%_%row%.png",
     * "downsamples/map.png");
     */
    
    // lets center all markers both horizontally and vertically
    tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
    
    // use pixel coordinates to roughly center it
    // they are calculated against the "full" size of the mapView
    // i.e., the largest zoom level as it would be rendered at a scale of 1.0f
    tileView.moveToAndCenter(size[0] / 2, size[1] / 2);
    tileView.slideToAndCenter(size[0] / 2, size[1] / 2);
    
    // scale down a little
    tileView.setScale(0.1);
    
  }
  
  private void initZoomPanGridLayout() {
    
    TileView tileView = getTileView();

    requestView = (RequestView) getView().findViewById(R.id.request_view);
    
    tileView.addTileViewEventListener(new TileView.TileViewEventListener() {
      
      @Override
      public void onDetailLevelChanged() {}
      
      @Override
      public void onDoubleTap(int x, int y) {}
      
      @Override
      public void onDrag(int x, int y) {}
      
      @Override
      public void onFingerDown(int x, int y) {}
      
      @Override
      public void onFingerUp(int x, int y) {}
      
      @Override
      public void onFling(int sx, int sy, int dx, int dy) {}
      
      @Override
      public void onFlingComplete(int x, int y) {}
      
      @Override
      public void onPinch(int x, int y) {}
      
      @Override
      public void onPinchComplete(int x, int y) {}
      
      @Override
      public void onPinchStart(int x, int y) {}
      
      @Override
      public void onRenderComplete() {}
      
      @Override
      public void onRenderStart() {}
      
      @Override
      public void onScaleChanged(double scale) {
        
        log("SCALE_CHANGED", "scale: " + scale);
        for (ImageView iv : lIV) {
          iv.setScaleX((float) scale);
          iv.setScaleY((float) scale);
          iv.invalidate();
        }
      }
      
      @Override
      public void onScrollChanged(int x, int y) {}
      
      @Override
      public void onTap(final int x, final int y) {
        
        requestView.setMessage("Create a New Marker @ " + x + "/" + y + " ?");
        requestView.setPositiveListener(new View.OnClickListener() {
          
          @Override
          public void onClick(View v) {
            double scale = getTileView().getScale();
            log("SCALE", "scale: " + scale);
            placeMarker(R.drawable.fantasy_dwarves, x / scale, y / scale);
            requestView.hide();
          }
        });
        requestView.setNegativeListener(new View.OnClickListener() {
          
          @Override
          public void onClick(View v) {
            requestView.hide();
          }
        });
        requestView.show();
      }
      
      @Override
      public void onZoomComplete(double scale) {}
      
      @Override
      public void onZoomStart(double scale) {}
    });
    
    tileView.addMarkerEventListener(new MarkerEventListener() {
      
      @Override
      public void onMarkerTap(View v, int x, int y) {
        String message = "Marker Tapped @: " + x + "/" + y;
//        TextView textView = ((TextView) getView().findViewById(R.id.tvHello));
//        textView.setText(message);
        log("MARKER_TAP", message);
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
      }
    });
    
    // int width = 2730; int height = 2730;
    int[] tilesize = { 1200, 1200 };
    int width = tilesize[0] * 6;
    int height = tilesize[1] * 6;
    float scale = 0.2f;
    tileView.setSize(width, height);

    // LayoutInflater inflater = (LayoutInflater)
    // getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // GridLayout gridLayout = (GridLayout) inflater.inflate(R.layout.grid_view,
    // null);
    // tileView.addView(gridLayout);
    //
    // for (int i = 0; i < 16 * 16; i++) {
    // View v = inflater.inflate(R.layout.grid_view_tile_child, null);
    // gridLayout.addView(v);
    // }


    tileView.addTileViewEventListener(new MyTileViewEventListener() {
      
      @Override
      public void onScaleChanged(double scale) {
//        ((TextView) getView().findViewById(R.id.tvHello))
//            .setText("Current Scale: " + scale);
      }
    });


    float k = 2.0f;
    String httpPrefix = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/indoor/";
    // httpPrefix = "https://copy.com/s/2zeq8Eyhbgje/";
    String tilesPrefix = "tileset";
    String backdropPrefix = "backdrop";

    TileMap tmxResult = TiledXMLParser.parseTMX(getActivity(), "basic.tmx");
    Log.i("KrisTMX", tmxResult.toString());



    Tileset.ComponentHandler handler
      = new Tileset.ComponentHandler(httpPrefix + tilesPrefix,
          "Basic", 600, "png");

    tileView.setDownsampleDecoder(new BitmapDecoderAssets());
    /**
     * @note future downsample should smart function (like TileBitmapDecoder)
     */
    String downsampleImage = "Basic.png";
    downsampleImage = "trans.png";
    tileView.setTileDecoder(new MyFOWTileBitmapDecoder(getTileView(), tmxResult, getActivity()));

//    tileView.addDetailLevel(1.0f * k, "indoor/tiles/fantasy_rohan_1x.png", "indoor/backdrop/black.png", 128, 128);
//    tileView.addDetailLevel(0.5f * k, "indoor/tiles/fantasy_rohan_0_5x.png", "indoor/backdrop/black.png", 64, 64);
//    tileView.addDetailLevel(0.25f * k, "indoor/tiles/fantasy_rohan_0_25x.png", "indoor/backdrop/black.png", 32, 32);

//    tileView.addDetailLevel(1.0f * k, 
//        httpPrefix + tilesPrefix + "fantasy_rohan_1x.png",
//        "black.png", 128, 128);
//    tileView.addDetailLevel(0.5f * k, 
//        httpPrefix + tilesPrefix + "fantasy_rohan_0_5x.png",
//        "black.png", 64, 64);
//    tileView.addDetailLevel(0.25f * k, 
//        httpPrefix + tilesPrefix + "fantasy_rohan_0_25x.png",
//        "black.png", 32, 32);

    // FIXME: uncomment when OOM fixed.
//  tileView.addDetailLevel(1.000f, httpPrefix + tilesPrefix + "dt01_1200_%col%_-%row%.jpg", "black.png", 1200, 1200);
//    tileView.addDetailLevel(0.500f, handler.getDetailLevelUrl(600), downsampleImage, 600, 600);
    tileView.addDetailLevel(0.250f, handler.getDetailLevelUrl(300), downsampleImage, 300, 300);
    tileView.addDetailLevel(0.125f, handler.getDetailLevelUrl(150), downsampleImage, 150, 150);

    // lets center all markers both horizontally and vertically
    tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

    // use pixel coordinates to roughly center it
    // they are calculated against the "full" size of the mapView
    // i.e., the largest zoom level as it would be rendered at a scale of 1.0f

    // lets center all markers both horizontally and vertically


    tileView.moveToAndCenter(0, 0);

    tileView.setScale(scale);
    tileView.setScaleLimits(0.2d, 2.25d);
    tileView.setCacheEnabled(true);


    TileView overlayView = getOverlayView();

    overlayView.setSize(width, height);
    overlayView.setDownsampleDecoder(new BitmapDecoderAssets());
    overlayView.setTileDecoder(new MyFOW2TileBitmapDecoder(getOverlayView(), tmxResult, getActivity()));

    overlayView.addDetailLevel(0.125f, handler.getDetailLevelUrl(150), "trans.png", 25, 25);
    overlayView.setMarkerAnchorPoints(-0.5f, -0.5f);

    overlayView.moveToAndCenter(0, 0);

    overlayView.setScale(scale);
    overlayView.setScaleLimits(0.2d, 2.25d);
    overlayView.setCacheEnabled(true);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    
    super.onActivityCreated(savedInstanceState);
    
    switch (TILEVIEW_VERSION) {
    case BASIC_TILEVIEW:
      initTileView();
      break;
    case FULL_TILEVIEW:
      initTileViewNew();
      break;
    case GRIDLAYOUT:
      initZoomPanGridLayout();
      break;
    default:
      initTileView();
      break;
    }
  }
  
  private final List<ImageView> lIV = new ArrayList<ImageView>();
  private RequestView           requestView;
  
  private void placeMarker(int resId, double x, double y) {
    
    log("New Marker @ ", x + "/" + y);
    ImageView imageView = new ImageView(this.getActivity());
    imageView.setImageResource(resId);
    imageView.setScaleX((float) getTileView().getScale());
    imageView.setScaleY((float) getTileView().getScale());
    lIV.add(imageView);
    
    getTileView().addMarker(imageView, x, y);
  }
  
  private void log(String tag, String message) {
    if (DEBUG) {
      Log.i("MyTileViewFragment", tag + " " + message);
//      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
  }
}