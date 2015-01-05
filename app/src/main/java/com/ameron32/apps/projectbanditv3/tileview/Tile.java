package com.ameron32.apps.projectbanditv3.tileview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Tile {
  
  private boolean x;
  private boolean y;
  private boolean d;
  private int     gid;
  
  Tile() {
    x(false);
    y(false);
    d(false);
    setTileGID(0L);
  }
  
  public boolean x() {
    return x;
  }
  
  public boolean x(boolean x) {
    this.x = x;
    return x();
  }
  
  public boolean y() {
    return y;
  }
  
  public boolean y(boolean y) {
    this.y = y;
    return y();
  }
  
  public boolean d() {
    return d;
  }
  
  public boolean d(boolean d) {
    this.d = d;
    return d();
  }
  
  public int gid() {
    return gid;
  }
  
  public void setTileGID(Long gid) {
    setTileId(Integer.valueOf(gid.intValue()));
  }
  
  public void setTileId(int gid) {
    this.gid = gid;
  }
  
  public static TileDirection direction(Tile tile)
      throws TiledUnsignedIntDirectionException {
    if (tile.x() && tile.y() && tile.d()) { throw new TiledUnsignedIntDirectionException("Cannot be flipped X, Y, & D."); }
    
    if (tile.x() && !tile.y() && !tile.d()) { return TileDirection.MirrorX; }
    
    if (!tile.x() && tile.y() && !tile.d()) { return TileDirection.MirrorY; }
    
    if (!tile.x() && tile.y() && tile.d()) { return TileDirection.R90deg; }
    
    if (tile.x() && tile.y() && !tile.d()) { return TileDirection.R180deg; }
    
    if (tile.x() && !tile.y() && tile.d()) { return TileDirection.R270deg; }
    
    if (!tile.x() && !tile.y() && !tile.d()) { return TileDirection.Normal; }
    
    {
      throw new TiledUnsignedIntDirectionException("Unknown flip/rotate combination.");
    }
  }
  
  public enum TileDirection {
    Normal, MirrorX, MirrorY, R180deg, R90deg, R270deg
  }
  
  public static class TiledUnsignedIntDirectionException extends Exception {
    
    private static final long serialVersionUID = -1435003962762070307L;
    
    public TiledUnsignedIntDirectionException(String string) {
      super(string);
    }
  }
  
  public static Tile newTileFromGID(long gid) {
//    
//    final long[] idArray = gids;
//    Tile[] tiles = new Tile[idArray.length];
    
    long id = gid;
    Tile tile = new Tile();
    
    long flipYBit = 2147483648L; // Y
    long flipXBit = 1073741824L; // X
    long flipDBit =  536870912L; // D
    
    // TILE EXTRACTION
    // subtract flip bits from the front of the unsigned int (in long format)
    long integer = 0L;
//    for (int i = 0; i < idArray.length; i++) {
      integer = id;
      
//      final Tile tile = new Tile();
      if (integer > flipYBit) {
        tile.y(true);
        integer -= flipYBit;
      }
      if (integer > flipXBit) {
        tile.x(true);
        integer -= flipXBit;
      }
      if (integer > flipDBit) {
        tile.d(true);
        integer -= flipDBit;
      }
      tile.setTileGID(integer);
//      tiles[i] = tile;
//    }
    
    // display results of tile extraction, for testing
//    if (DEBUG) {
////      for (int i = 0; i < idArray.length; i++) {
////        Tile u = tiles[i];
//        boolean x, y, d;
//        long l;
//        x = u.x();
//        y = u.y();
//        d = u.d();
//        l = u.gid();
//        
//        try {
//          System.out.println("Start: [" // + quickLabel(idArray[i])
//              + "]\n" + "  End:  " + Helper.quickLabel(l) 
//              + "  with: " + Helper.quickLabel(x, "x") + "|"
//              + Helper.quickLabel(y, "y") + "|" 
//              + Helper.quickLabel(d, "d") + Tile.direction(u).name());
//        }
//        catch (TiledUnsignedIntDirectionException e) {
//          e.printStackTrace();
//        }
////      }
//    }
    
    return tile;
  }
  
  public Bitmap rotateFromTile(Bitmap tileImage) {
    Tile tile = this;
    if (tile.x() || tile.y() || tile.d()) {
      int sx, sy;
      sx = sy = 1; 
      Matrix m = new Matrix();
      if (tile.x()) {
        sx = sx * -1;
      }
      if (tile.y()) {
        sy = sy * -1;
      }
      if (tile.d()) {
        sy = sy * -1;
        m.preRotate(90);
      }
      m.preScale(sx, sy);
      return Bitmap.createBitmap(tileImage, 0, 0, tileImage.getWidth(), tileImage.getHeight(), m, false);
    }
    return tileImage;
  }

  @Override
  public String toString() {
    String toString = "Tile [gid=" + gid + " , rotation=";
    try {
      toString += direction(this);
    }
    catch (TiledUnsignedIntDirectionException e) {
      e.printStackTrace();
      toString += "EXCEPTION (see logcat)";
    }
    toString += "]";
    return toString;
  }
  
  
}