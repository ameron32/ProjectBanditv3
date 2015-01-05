package com.ameron32.apps.projectbanditv3.tileview;

import java.util.ArrayList;
import java.util.List;

import com.qozix.tileview.TileView;

import android.content.Context;
import android.util.Log;


public class TileMap {
  List<TilesetWrapper> tilesets;
  List<Layer> layers;
  
  Orientation orientation;
  int width, height;
  int tileWidth, tileHeight;

  
  public TileMap() {
    orientation = Orientation.orthogonal;
    width = height = 0;
    tileWidth = tileHeight = 0;
  }
  
  public void setAttribute(String tag, String name, String value) {
    if (tag.equalsIgnoreCase("map")) {
    if (name.equalsIgnoreCase("orientation")) {
      orientation = Orientation.valueOf(value);
    }
    
    if (name.equalsIgnoreCase("width")) {
      width = Integer.valueOf(value);
    }
    
    if (name.equalsIgnoreCase("height")) {
      height = Integer.valueOf(value);
    }
    
    if (name.equalsIgnoreCase("tileWidth")) {
      tileWidth = Integer.valueOf(value);
    }
    
    if (name.equalsIgnoreCase("tileHeight")) {
     tileHeight = Integer.valueOf(value); 
    }
    }
  }
  
  public void setLayerAttribute(String tag, String name, String value) {
    if (tag.equalsIgnoreCase("layer")) {
      Layer layer;
      if (name.equalsIgnoreCase("name")) {
        layer = createLayer();
        layer.setAttribute(name, value);
      } else {
        layer = lastLayer();
        layer.setAttribute(name, value);
      }
    }
  }
  
  public void setTileAttribute(String tag, String name, String value) {
    if (tag.equalsIgnoreCase("tile")) {
      Tile tile;
      if (name.equalsIgnoreCase("gid")) {
        tile = Tile.newTileFromGID(Long.valueOf(value));
        lastLayer().addTile(tile);
      }
    }
  }
  
  public void setTilesetAttribute(String tag, String name, String value, Tileset tileset) {
    if (tag.equalsIgnoreCase("tileset")) {
      TilesetWrapper tilesetWrapper;
      if (name.equalsIgnoreCase("firstgid")) {
        tilesetWrapper = createTilesetWrapper();
        tilesetWrapper.setAttribute(name, value);
      } 
      else 
      if (name.equalsIgnoreCase("source")) {
        tilesetWrapper = lastTilesetWrapper();
        tilesetWrapper.setAttribute(name, value);
        tilesetWrapper.setTileset(tileset);
      }
      else {
        
      }
    }
  }
  
  public static boolean usesTag(String name) {
    return (name.equalsIgnoreCase("map")
        || name.equalsIgnoreCase("layer")
        || name.equalsIgnoreCase("tileset")
        || name.equalsIgnoreCase("tile"));
  }
  
  private Layer createLayer() {
    if (layers == null) { layers = new ArrayList<Layer>(); }
    Layer l = new Layer();
    layers.add(l);
    return l;
  }
  
  private TilesetWrapper createTilesetWrapper() {
    if (tilesets == null) { tilesets = new ArrayList<TilesetWrapper>(); }
    TilesetWrapper tilesetWrapper = new TilesetWrapper();
    tilesets.add(tilesetWrapper);
    return tilesetWrapper;
  }
  
  private Layer lastLayer() {
    return layers.get(layers.size() - 1);
  }
  
  private Tileset lastTileset() {
    return lastTilesetWrapper().tileset;
  }
  
  private TilesetWrapper lastTilesetWrapper() {
    return tilesets.get(tilesets.size() - 1);
  }
  

  @Override
  public String toString() {
    String toString = "TileMap [orientation=" + orientation.name() + ", width=" + width + ", height=" + height
        + ", tileWidth=" + tileWidth + ", tileHeight=" + tileHeight;
    if (tilesets != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(toString);
      for (TilesetWrapper tileset : tilesets) {
        sb.append("\n");
        sb.append(tileset);
      }
      toString = sb.toString();
    }
    else {
      toString = toString.concat("\n--No Tilesets");
    }
    if (layers != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(toString);
      for (Layer layer : layers) {
        sb.append("\n");
        sb.append(layer);
      }
      toString = sb.toString();
    }
    else {
      toString = toString.concat("\n--No Layers");
    }
    toString += "]";
    return toString;
  }

  public Tile[][] getTiles(int layer) {
    return layers.get(layer).getTiles();
  }
  
  public Tileset getTileset(String name) {
    for (TilesetWrapper tw : tilesets) {
      if (tw.tileset.name.equalsIgnoreCase(name)) return tw.tileset;
    }
    return null;
  }

  public Tileset getTilesetFromGID(int gid) {
    for (TilesetWrapper tw : tilesets) {
      int first = tw.firstGID;
      int last = first + tw.getMaxTiles() -1;
      if (first <= gid && gid <= last) return tw.tileset;
    }
    return null;
  }











  public static class Layer {
    String name;
    int width, height;
    
    List<Tile> tileData;
    
    public Layer() {
      name = "";
      width = height = 0;
    }
    
    public void setAttribute(String name, String value) {
      if (name.equalsIgnoreCase("name")) {
        name = value;
      }
      
      if (name.equalsIgnoreCase("width")) {
        width = Integer.valueOf(value);
      }
      
      if (name.equalsIgnoreCase("height")) {
        height = Integer.valueOf(value);
      }
    }
    
    public void addTile(Tile tile) {
      if (tileData == null) { tileData = new ArrayList<Tile>(); }
      tileData.add(tile);
    }
    
    public void getTilesetFromGID(int gid) {
      
    }

    @Override
    public String toString() {
      String toString = "Layer [name=" + name + ", width=" + width + ", height=" + height + "]";
      if (tileData != null) {
        StringBuilder sb = new StringBuilder();
        sb.append(toString);
        for (Tile tile : tileData) {
          sb.append("\n");
          sb.append("   " + tile);
        }
        sb.append("\nTotal tiles: " + tileData.size());
        toString = sb.toString();
      } else {
        toString = toString.concat("\n--No Tiles");
      }
      return toString;
    }
    
    Tile[][] getTiles() {
      Tile[][] tileGrid = new Tile[width][height];
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          tileGrid[i][j] = tileData.get((j * width) + i);
        }
      }
      return tileGrid;
    }
    
    Tile getTileAtColRow(int col, int row) {
      Tile[][] tileGrid = getTiles();
      return tileGrid[col][row];
    }
    
    Tile getTileAtPosition(int position) {
      return tileData.get(position);
    }
  }
  
  public static class TilesetWrapper {
    Tileset tileset;
    private int firstGID;
    String source;
    
    public int getFirstGid() {
      return firstGID;
    }
    
    TilesetWrapper() {
      tileset = null;
      firstGID = 1;
      source = "";
    }
    
    public void setAttribute(String name, String value) {
      if (name.equalsIgnoreCase("firstgid")) {
        firstGID = Integer.valueOf(value);
      }
      
      if (name.equalsIgnoreCase("source")) {
        source = value;
      }
    }
    
    public void setTileset(Tileset tileset) {
      this.tileset = tileset;
      tileset.setWrapper(this);
    }
    
    int getMaxTiles() {
      return tileset.getMaxTiles();
    }

    @Override
    public String toString() {
      return "TilesetWrapper [firstGID=" + firstGID + ", source=" + source 
          + " tileset=[" + tileset + "]]";
    }
  }
  
  public enum Orientation {
    orthogonal
  }

  public TileMap(TileMap tileMap) {
    this.tilesets = tileMap.tilesets;
    this.layers = tileMap.layers;
    this.orientation = tileMap.orientation;
    this.width = tileMap.width;
    this.height = tileMap.height;
    this.tileWidth = tileMap.tileWidth;
    this.tileHeight = tileMap.tileHeight;
  }
}
