package com.ameron32.apps.projectbanditv3.tileview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

import com.ameron32.apps.projectbanditv3.tileview.Tile;
import com.ameron32.apps.projectbanditv3.tileview.TileMap;
import com.ameron32.apps.projectbanditv3.tileview.Tileset;
import com.qozix.tileview.graphics.BitmapDecoderHttp;

/**
 * RETIRED IN FAVOR OF MyTileBitmapDecoderHttp
 * REMAINS FOR REFERENCE ONLY
 * @author klemeilleur
 *
 */
public abstract class MyBitmapDecoderHttp extends BitmapDecoderHttp {
  
  private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
  
  private final Map<Integer, HashMap<String, Bitmap>> sizesets = new HashMap<Integer, HashMap<String, Bitmap>>();
//  private final Map<String, Bitmap> tilesets = new HashMap<String, Bitmap>();
  private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
  private final int[][] tilesetBitmaps = new int[6][13];
  
  static {
    OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
  }
  
  TileMap tileMap = null;
  public MyBitmapDecoderHttp(TileMap tileMap) {
    firstRun = true;
    this.tileMap = tileMap;
  }
  
  public void start(Context context) {
    String httpPrefix = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/indoor/";
    // httpPrefix = "https://copy.com/s/2zeq8Eyhbgje/";
    String tilesPrefix = "tiles/";
    String backdropPrefix = "backdrop/";
    
    
    
    if (!other && !fancy) {
      // nothing
    }
    if (other && !fancy) {
      bitmaps.put(0, retrieveUnknownBitmap(httpPrefix + tilesPrefix + "dt01.jpg", context));
      bitmaps.put(1, retrieveUnknownBitmap(httpPrefix + tilesPrefix + "dt02.jpg", context));
      
      int c = 0;
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 6; j++) {
          tilesetBitmaps[i][j] = (c % 2);
          c++;
        }
      }
    }
    if (other && fancy) {
      // nothing
    }
    
  }
  
  boolean other = true;
  boolean fancy = true;
  @Override
  public Bitmap decode(String fileName, Context context) {
    
    // if don't already have it
    if (!other) {
      return retrieveUnknownBitmap(fileName, context);
    } else if (!fancy){
      return other(fileName, context);
    } else {
      return fancy(fileName, context);
    }
  }
  
  boolean firstRun;
  
  private Bitmap retrieveUnknownBitmap(String fileName, Context context) {
    
    try {
      URL url = new URL(fileName);
      try {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream input = connection.getInputStream();
        if ( input != null ) {
          try {
            return BitmapFactory.decodeStream( input, null, OPTIONS );                    
          } catch ( OutOfMemoryError oom ) {
            // oom - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
            Log.e("ERROR:", "Out of Memory");

          } catch ( Exception e ) {
            // unknown error decoding bitmap
          }
        }
      } catch ( IOException e ) {
        // io error
      }     
    } catch ( MalformedURLException e1 ) {
      // bad url
    }
    return null;
  }
  
  private Bitmap other(String fileName, Context context) {
    
    if (firstRun) { start(context); firstRun = false; }
    
    int[] colrow = extractColRow(fileName);
    int size = colrow[0];
    int col  = colrow[1];
    int row  = colrow[2];
    
    Bitmap bitmap = bitmaps.get(tilesetBitmaps[col][row]);
    
    if (bitmap == null) {
      Log.i("Kris", "load");
      String httpPrefix = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/indoor/";
      String tilesPrefix = "tiles/";
      fileName = httpPrefix + tilesPrefix + "fantasy_rohan_0_5x.png";
      bitmap = retrieveUnknownBitmap(fileName, context);
    } else {
      Log.i("Kris", "loaded");
    }
    return bitmap;
  }
  
  private Bitmap fancy(String fileName, Context context) {
    
    Bitmap bitmap = null;
    String tileset = "Basic";
    
    int[] colrow = extractColRow(fileName);
    
    HashMap<String, Bitmap> tilesets = null;
    if (!sizesets.containsKey(colrow[0])) {
      tilesets = new HashMap<String, Bitmap>();
      sizesets.put(colrow[0], tilesets);
    } else {
      tilesets = sizesets.get(colrow[0]);
    }
    
    if (!tilesets.containsKey(tileset)) {
      // download the bitmap
      String httpPrefix = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/indoor/";
      String tilesPrefix = "tileset/";
      fileName = httpPrefix + tilesPrefix + tileset + colrow[0] + ".png";
      bitmap = retrieveUnknownBitmap(fileName, context);
      tilesets.put(tileset, bitmap);
    } else {
      bitmap = tilesets.get(tileset);
    }
    
    try {
//    bitmap = tilesetInLine(colrow, tileset, bitmap);
      bitmap = tilesFromColRow(colrow, tileset, bitmap);
    }
    catch (NullPointerException npe) {
      npe.printStackTrace();
      bitmap = failSafe(context);
    }
    return bitmap;
  }
  
  private Bitmap tilesetInLine(int[] colrow, String tileset, Bitmap bitmap) {
    return tileMap.getTileset(tileset).getTileAt(colrow[0], colrow[1], colrow[2], bitmap);
  }
  
  private Bitmap tilesFromColRow(int[] colrow, String tileset, Bitmap bitmap) {
    
    Log.e("GID", "colrow: " + colrow[1] + "/" + colrow[2]);
    Tile tile = tileMap.getTiles(0)[colrow[1]][colrow[2]];
    Log.e("GID", "gid: " + tile.gid());
    Bitmap src = tileMap.getTileset(tileset).getTileFromGID(colrow[0], tile.gid(), bitmap);
    
    if (tile.x() || tile.y() || tile.d()) {
      if (tile.x()) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
      }
      if (tile.y()) {
        Matrix m = new Matrix();
        m.preScale(1, -1);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
      }
      if (tile.d()) {
        Matrix m = new Matrix();
        m.preScale(1, -1);
        m.preRotate(270);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
      }
//      src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
    }
    return src;
  }
  
  private int[] extractColRow(String fileName) {
    
    String[] split = fileName.split("_");
    Log.d("Kris", "fileName: " + fileName);
    
    int size = 0;
    int col = 0;
    int row = 0;
    Log.i("Kris", "" + split[1]);
    Log.i("Kris", "" + split[2]);
    Log.i("Kris", "" + split[3].substring(1,2));
    size = Integer.valueOf(split[1]);
    col = Integer.valueOf(split[2]);
    row = Integer.valueOf(split[3].substring(1,2));
    Log.i("Kris", "size:"+size+" col:"+col+" row:"+row);
    
    return new int[] { size, col, row };
  }
  
  Bitmap m = null;
  private Bitmap failSafe(Context context) {
    
    if (m == null) {
//      String httpPrefix = "https://dl.dropboxusercontent.com/u/949753/ANDROID/TileView/indoor/";
//      String tilesPrefix = "tiles/";
//      String fileName = httpPrefix + tilesPrefix + "fantasy_rohan_0_5x.png";
//      m = retrieveUnknownBitmap(fileName, context);
      try {
        m = BitmapFactory.decodeStream(context.getAssets().open("black.png"));
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return m;
  }
  
}
