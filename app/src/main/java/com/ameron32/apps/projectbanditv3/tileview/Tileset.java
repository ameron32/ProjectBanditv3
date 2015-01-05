package com.ameron32.apps.projectbanditv3.tileview;

import com.ameron32.apps.projectbanditv3.tileview.TileMap.TilesetWrapper;

import android.graphics.Bitmap;

public class Tileset {
  
  private static final String TILESET_NAME = "name";
  private static final String TILE_WIDTH   = "tilewidth";
  private static final String TILE_HEIGHT  = "tileheight";
  private static final String IMAGE_NAME   = "source";
  private static final String IMAGE_WIDTH  = "width";
  private static final String IMAGE_HEIGHT = "height";
  
  public String               name;
  private int                 tileWidth;
  private int                 tileHeight;
  
  private String              imageSource;
  private int                 imgWidth;
  private int                 imgHeight;
  
  Tileset() {
    name = "";
    tileWidth = tileHeight = 0;
    
    imageSource = "";
    imgWidth = imgHeight = 0;
  }
  
  public void setAttribute(String tag, String name, String value) {
    if (usesTag(tag)) {
      if (name.equalsIgnoreCase(TILESET_NAME)) {
        this.name = value;
      }
      
      if (name.equalsIgnoreCase(TILE_WIDTH)) {
        tileWidth = Integer.valueOf(value);
      }
      
      if (name.equalsIgnoreCase(TILE_HEIGHT)) {
        tileHeight = Integer.valueOf(value);
      }
      
      if (name.equalsIgnoreCase(IMAGE_NAME)) {
        imageSource = value;
      }
      
      if (name.equalsIgnoreCase(IMAGE_WIDTH)) {
        imgWidth = Integer.valueOf(value);
      }
      
      if (name.equalsIgnoreCase(IMAGE_HEIGHT)) {
        imgHeight = Integer.valueOf(value);
      }
      
      {
        // do nothing }
      }
    }
    else {
      System.out.println("Tileset DOES NOT USE THIS TAG: [" + tag + "]");
    }
  }
  
  public static boolean usesTag(String name) {
    return (name.equalsIgnoreCase("tileset")
        || name.equalsIgnoreCase("image"));
  }
  
  @Override
  public String toString() {
    return "Tileset [name=" + name + ", tileWidth=" + tileWidth + ", tileHeight=" + tileHeight + ", imageSource="
        + imageSource + ", imgWidth=" + imgWidth + ", imgHeight=" + imgHeight + "]";
  }

  public Tileset(Tileset tileset) {
    this.name = tileset.name;
    this.tileWidth = tileset.tileWidth;
    this.tileHeight = tileset.tileHeight;
    this.imageSource = tileset.imageSource;
    this.imgWidth = tileset.imgWidth;
    this.imgHeight = tileset.imgHeight;
  }
  
  private TilesetWrapper tilesetWrapper = null;
  public void setWrapper(TilesetWrapper tilesetWrapper) {
    this.tilesetWrapper = tilesetWrapper;
  }
  
  public TilesetWrapper getWrapper() {
    return tilesetWrapper;
  }
  
  public int getMaxColumns() {
    return imgWidth / tileWidth;
  }
  
  public int getMaxRows() {
    return imgHeight / tileHeight;
  }
  
  public int getMaxTiles() {
    return getMaxColumns() * getMaxRows();
  }
  
  public Bitmap getTileAt(int size, int x, int y, Bitmap tileset) {
    
    // sanity check
    int maxX = getMaxColumns();
    int maxY = getMaxRows();
    if (x > maxX - 1 || y > maxY - 1) { return null; }
    
    int startX = x * tileWidth;
    int startY = y * tileHeight;
    
    float scaleX = ((float)size) / ((float)tileWidth);
    float scaleY = ((float)size) / ((float)tileHeight);
    startX = (int) (((float)startX) * scaleX);
    startY = (int) (((float)startY) * scaleY);
    int scaledWidth = (int) (tileWidth * scaleX);
    int scaledHeight = (int) (tileHeight * scaleY);
    
    return Bitmap.createBitmap(tileset, startX, startY, scaledWidth, scaledHeight);
  }
  
  public Bitmap getTileFromGID(int size, int gid, Bitmap tileset) {
    
    // sanity check
    int maxX = getMaxColumns();
    int maxY = getMaxRows();
    int maxGID = maxX * maxY;
    if (gid > maxGID - 1) { return null; }
    
    if (gid == 0) { return null; }
    
    gid = gid - 1;
    int colNumber = gid % maxX;
    int rowNumber = gid / maxX;
    
    return getTileAt(size, colNumber, rowNumber, tileset);
  }
  
  public static class ComponentHandler {
    
    String httpPrefix;
    String tileset;
    int maxSize;
    String extension;
    
    public ComponentHandler(String httpPrefix, String tileset, int maxSize, String extension) {
      
      this.httpPrefix = httpPrefix;
      this.tileset = tileset;
      this.maxSize = maxSize;
      this.extension = extension;
    }
    
    /**
     * 
     * @param size Width in pixels of unique tile size. Square tiles only.
     * @return Coded pseudo-url for DetailLevelUrl input. 
     * @note  not compatible as a standard url
     */
    public String getDetailLevelUrl(int size) {
      // http://www.myurl.com/whatever
      //     /
      //     #tileset_600_%col%_%row%_jpg
      String detailLevelUrl = httpPrefix + "/" + "#" + tileset + "_" + size 
          + "_" + "%col%" + "_" + "%row%" + "_" + extension;
      return detailLevelUrl;
    }
    
    public static ComponentHolder extractHolder(String decodeUrl) {

      String[] parts = decodeUrl.split("#");
      String httpPrefix = parts[0];
      String fileName = parts[1];
      
      String[] components = fileName.split("_");
      String tileset = components[0];
      String size = components[1];
      String column  = components[2];
      String row = components[3];
      String extension = components[4];
      
      ComponentHolder ch = ComponentHolder.newInstance(httpPrefix, 
          tileset, Integer.valueOf(size), 
          Integer.valueOf(column), Integer.valueOf(row), extension);
      
      return ch;
    }
  }
  
  public static class ComponentHolder {

    public String httpPrefix;
    public String filename;
    public String extension;
    public String url;
    
//  public String file;
    public String tileset;
    public int size;
    public int column;
    public int row;
    
    private ComponentHolder() {}
    
    public static ComponentHolder newInstance(String httpPrefix, 
        String tileset, int size,
        int column, int row,
        String extension) {
      ComponentHolder ch = new ComponentHolder();
      
      ch.httpPrefix = httpPrefix;
      ch.extension = extension;
      
      ch.tileset = tileset;
      ch.size = size;
      ch.column = column;
      ch.row = row;
      
      ch.makeUrl();
      
      return ch;
    }
    
    private void makeFilename() {
      this.filename = this.tileset + this.size + "." + this.extension;
    }
    
    private void makeUrl() {
      makeFilename();
      this.url = this.httpPrefix + this.filename;
    }

    @Override
    public String toString() {
      return "ComponentHolder [httpPrefix=" + httpPrefix + ", filename=" + filename + ", extension=" + extension
          + ", url=" + url + ", tileset=" + tileset + ", size=" + size + ", column=" + column + ", row=" + row + "]";
    }
  }
}
