package com.ameron32.apps.projectbanditv3.tileview;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.ameron32.apps.projectbanditv3.tileview.Tileset.ComponentHolder;
import com.qozix.tileview.graphics.BitmapDecoderHttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;


public abstract class SmartTileBitmapDecoder extends BitmapDecoderHttp {
  
  // <OPTIMIZATION SETTINGS>
  private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
  static {
    OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
  }
  // </OPTIMIZATION SETTINGS>
  
  protected static boolean DEBUG = true;
  
  BitmapCache bitmapCache;
  
  public SmartTileBitmapDecoder() {
    bitmapCache = new BitmapCache();
  }
  
  protected static class BitmapCache {
    // SparseArray<Integer>: Integer: SIZES
    // HashMap    <String>:  String:  TILESET NAME
    // HashMap    <Bitmap>:  Bitmap:  TILESET IMAGE
    private Map<Integer, Map<String, SoftReference<Bitmap>>> sizesMap;
    private Bitmap error;
    private Bitmap blank;
    private Bitmap clear;
    
    public BitmapCache() {
      sizesMap = new HashMap<Integer, Map<String, SoftReference<Bitmap>>>();
    }
    
    /**
     * Simple bitmap cache or download splitter
     */
    public Bitmap getTilesetImage(Context context, ComponentHolder holder, int size) {
      
      // find the HashMap of tileset bitmaps for the given size
      Map<String, SoftReference<Bitmap>> tilesetMapForSize;
      if (sizesMap.get(size) != null) {
        tilesetMapForSize = sizesMap.get(size);
      } else {
        // if needed, generate a new HashMap for this size
        tilesetMapForSize = new HashMap<String, SoftReference<Bitmap>>();
        sizesMap.put(size, tilesetMapForSize);
      }
      
      // retrieve the tileset bitmap
      SoftReference<Bitmap> bitmap;
      if (tilesetMapForSize.containsKey(holder.tileset)) {
        bitmap = tilesetMapForSize.get(holder.tileset);
      } else {
        bitmap = loadBitmapHttp(holder.url, context);
        tilesetMapForSize.put(holder.tileset, bitmap);
      }
      
      return bitmap.get();
    }
    
    /**
     * Simple bitmap downloader method
     */
    protected SoftReference<Bitmap> loadBitmapHttp(String fileName, Context context) {
      log("loadBitmapHttp", "fileName: " + fileName);
      try {
        URL url = new URL(fileName);
        try {
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          InputStream input = connection.getInputStream();
          if ( input != null ) {
            try {
              Bitmap b = BitmapFactory.decodeStream( input, null, OPTIONS );
              return new SoftReference<Bitmap>(b);
            } catch ( OutOfMemoryError oom ) {
              // oom - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
              log("ERROR:", "Out of Memory");
              oom.printStackTrace();
            } catch ( Exception e ) {
              // unknown error decoding bitmap
            }
          }
        } catch ( IOException e ) {
          // io error
          log("ERROR:", "IOException");
          e.printStackTrace();
        }     
      } catch ( MalformedURLException e1 ) {
        // bad url
        log("ERROR:", "Bad URL Structure");
        e1.printStackTrace();
      }
      return null;
    }
    
    /**
     * Simple bitmap asset loader method
     */
    protected Bitmap loadBitmapAssets(String fileName, int tileSizeX, int tileSizeY, Context context) {
      Bitmap assetBitmap = null;
      try {
        assetBitmap = BitmapFactory.decodeStream(context.getAssets().open(fileName));
        assetBitmap = Bitmap.createScaledBitmap(assetBitmap, tileSizeX, tileSizeY, false);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return assetBitmap;
    }
    
    /**
     * Load a basic cached bitmap
     */
    protected Bitmap loadErrorTile(int tileSizeX, int tileSizeY, Context context) {
      // load the error tile if it has never been loaded before
      if (error == null) {
        try {
          error = BitmapFactory.decodeStream(context.getAssets().open("tvstatic.png"));
          error = Bitmap.createScaledBitmap(error, tileSizeX, tileSizeY, false);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      return error;
    }

    /**
     * Load a basic cached bitmap
     */
    protected Bitmap loadBlankTile(int tileSizeX, int tileSizeY, Context context) {
      // load the error tile if it has never been loaded before
      if (blank == null) {
        try {
          blank = BitmapFactory.decodeStream(context.getAssets().open("black.png"));
          blank = Bitmap.createScaledBitmap(blank, tileSizeX, tileSizeY, false);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      return blank;
    }

    /**
     * Load a basic cached bitmap
     */
    protected Bitmap loadTransparentTile(int tileSizeX, int tileSizeY, Context context) {
      // load the error tile if it has never been loaded before
      if (clear == null) {
        try {
          clear = BitmapFactory.decodeStream(context.getAssets().open("trans.png"));
          clear = Bitmap.createScaledBitmap(clear, tileSizeX, tileSizeY, false);
          Canvas canvas = new Canvas(clear);

          Paint transPainter = new Paint();
          transPainter.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

          canvas.drawRect(0, 0, tileSizeX, tileSizeY, transPainter);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      return clear;
    }
  }







  protected static void log(String tag, String message) {
    if (DEBUG) {
      Log.i(tag, message);
    }
  }
  
  protected BitmapCache getBitmapCache() {
    return bitmapCache;
  }
}
