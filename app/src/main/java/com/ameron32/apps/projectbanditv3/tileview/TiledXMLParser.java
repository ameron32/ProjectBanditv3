package com.ameron32.apps.projectbanditv3.tileview;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ameron32.apps.projectbanditv3.tileview.Tile.TiledUnsignedIntDirectionException;

import android.content.Context;

/**
 * NOT DESIGNED FOR MULTIPLE TILESETS IN ONE TSX FILE
 * 
 * @author klemeilleur
 * 
 */
public class TiledXMLParser {
  private static final int PARSE_TSX = 1;
  private static final int PARSE_TMX = 2;
  
  private static TileMap mTileMap = null;
  private static Tileset mTileset = null;
  private static boolean DEBUG = true;
  
  Context context;
  
  public TiledXMLParser(Context context) {
  
    this.context = context;
  }
  
  private static Tile[] convertGIDsToTiles(long[] gids) {
    
    final long[] idArray = gids;
    Tile[] tiles = new Tile[idArray.length];
    
    long flipYBit = 2147483648L; // Y
    long flipXBit = 1073741824L; // X
    long flipDBit =  536870912L; // D
    
    // TILE EXTRACTION
    // subtract flip bits from the front of the unsigned int (in long format)
    long integer = 0L;
    for (int i = 0; i < idArray.length; i++) {
      integer = idArray[i];
      
      final Tile tile = new Tile();
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
      tiles[i] = tile;
    }
    
    // display results of tile extraction, for testing
    if (DEBUG) {
      for (int i = 0; i < idArray.length; i++) {
        Tile u = tiles[i];
        boolean x, y, d;
        long l;
        x = u.x();
        y = u.y();
        d = u.d();
        l = u.gid();
        
        try {
          System.out.println("Start: [" // + quickLabel(idArray[i])
              + "]\n" + "  End:  " + Helper.quickLabel(l) 
              + "  with: " + Helper.quickLabel(x, "x") + "|"
              + Helper.quickLabel(y, "y") + "|" 
              + Helper.quickLabel(d, "d") + Tile.direction(u).name());
        }
        catch (TiledUnsignedIntDirectionException e) {
          e.printStackTrace();
        }
      }
    }
    
    return tiles;
  }
  
  public static TileMap parseTMX(Context context, String xml) {
    
    return new TileMap((TileMap) parse(PARSE_TMX, context, xml));
  }

  public static Tileset parseTSX(Context context, String xml) {
    
    return new Tileset((Tileset) parse(PARSE_TSX, context, xml));
  }
  
  private static Object parse(int parseType, Context context, String xml) {
    
    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      XmlPullParser xpp = factory.newPullParser();
      xpp.setInput(new InputStreamReader(
            // TODO switch to xml
            context.getResources().getAssets().open(xml)));
      xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
      
      int eventType = xpp.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        xmlParseLoop(context, parseType, eventType, xpp);
        eventType = xpp.next();
      }
      System.out.println("End document");
      
      // more than one tilemap possible?
      if (parseType == PARSE_TMX) {
        return mTileMap;
      }
      if (parseType == PARSE_TSX) {
        return mTileset;
      }
    }
    catch (XmlPullParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  private static void xmlParseLoop(Context context, int parseType, int eventType, XmlPullParser xpp) {
    
    System.out.println("--------------------" + XmlPullParser.TYPES[eventType]);
    switch (eventType) {
    case XmlPullParser.START_DOCUMENT:
      System.out.println("Start document");
      break;
    case XmlPullParser.START_TAG:
      System.out.println("Start tag " + xpp.getName());
      if (parseType == PARSE_TSX) {
        startTagTSX(context, xpp);
      }
      if (parseType == PARSE_TMX) {
        startTagTMX(context, xpp);
      }
      break;
    case XmlPullParser.END_TAG:
      System.out.println("End tag " + xpp.getName());
      break;
    case XmlPullParser.TEXT:
      System.out.println("Text " + xpp.getText());
      break;
    }
  }
  
  private static void startTagTSX(Context context, XmlPullParser xpp) {
    
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      System.out.println(xpp.getAttributeName(i) + ": " + xpp.getAttributeValue(i));
      if (Tileset.usesTag(xpp.getName())) {
        if (mTileset == null) {
          mTileset = new Tileset();
        }
        System.out.println("Set Attribute");
        mTileset.setAttribute(xpp.getName(), xpp.getAttributeName(i), xpp.getAttributeValue(i));
      }
    }
  }
  
  private static void startTagTMX(Context context, XmlPullParser xpp) {
    
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      System.out.println(xpp.getAttributeName(i) + ": " + xpp.getAttributeValue(i));
      if (TileMap.usesTag(xpp.getName())) {
        if (xpp.getName().equalsIgnoreCase("map")) {
          if (mTileMap == null) {
            mTileMap = new TileMap();
          }
          mTileMap.setAttribute(xpp.getName(), xpp.getAttributeName(i), xpp.getAttributeValue(i));
        }
        if (xpp.getName().equalsIgnoreCase("tileset")) {
          Tileset tileset = null;
          if (xpp.getAttributeName(i).equalsIgnoreCase("source")) {
            tileset = TiledXMLParser.parseTSX(context, xpp.getAttributeValue(i));
          }
          mTileMap.setTilesetAttribute(xpp.getName(), xpp.getAttributeName(i), xpp.getAttributeValue(i), tileset);
          tileset = null;
        }
        if (xpp.getName().equalsIgnoreCase("layer")) {
          mTileMap.setLayerAttribute(xpp.getName(), xpp.getAttributeName(i), xpp.getAttributeValue(i));
        }
        if (xpp.getName().equalsIgnoreCase("tile")) {
          mTileMap.setTileAttribute(xpp.getName(), xpp.getAttributeName(i), xpp.getAttributeValue(i));
        }
      }
    }
  }
  
  
  
  public static class Helper {
    // *** HELPER METHODS FOR TESTS *********************
    
    private static String quickLabel(boolean a, String r) {
      if (a)
        return " " + r + " ";
      else return "   ";
    }
    
    private static String quickLabel(long l) {
      return String.format("%10d", l);
    }
  }
}
