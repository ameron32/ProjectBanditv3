package com.ameron32.apps.projectbanditv3.tileview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.ameron32.apps.projectbanditv3.tileview.Tileset.ComponentHolder;
import com.qozix.tileview.TileView;


public class MyFOW2TileBitmapDecoder extends MyTileBitmapDecoderHttp {

  private static final boolean SEGMENTED = true;

  boolean[][] shown = null;
  final int segmentsPerTile = 6;
  int segmentSize;
  private TileView tileView;

  public MyFOW2TileBitmapDecoder(TileView tileView, TileMap tileMap, Context context) {
    super(tileMap, context);
    this.tileView = tileView;

    Tile[][] tiles = tileMap.getTiles(0);

    if (!SEGMENTED) {
      shown = new boolean[tiles.length][tiles[0].length];
      shown[1][2] = true;
      shown[3][3] = true;
    } else {
      shown = new boolean[tiles.length * segmentsPerTile][tiles[0].length * segmentsPerTile];
//      shown[2 * segmentsPerTile + 2][2 * segmentsPerTile + 2] = true;
//      shown[2 * segmentsPerTile + 2][2 * segmentsPerTile + 3] = true;
//      shown[2 * segmentsPerTile + 3][2 * segmentsPerTile + 3] = true;
//      shown[2 * segmentsPerTile + 3][2 * segmentsPerTile + 2] = true;
//      shown[4 * segmentsPerTile + 1][3 * segmentsPerTile + 1] = true;
      t(2,2,2,2);
      t(2,2,2,3);
      t(2,3,2,3);
      t(2,3,2,2);
      t(4,1,3,1);
    }
  }

  private int t(int tile, int segment) {
    return tile * segmentsPerTile + segment;
  }

  private void t(int tileX, int segmentX, int tileY, int segmentY) {
    shown[t(tileX, segmentX)][t(tileY, segmentY)] = true;
  }

  @Override
  protected Bitmap decode(ComponentHolder holder, Context context) {

//    boolean breakMe = false;
//    if (shown[holder.column][holder.row]) {
//      breakMe = true;
//    }

    int modHolderSize = holder.size / segmentsPerTile;

    Bitmap blankTile = getBitmapCache().loadBlankTile(modHolderSize, modHolderSize, context);
    Bitmap clearTile = getBitmapCache().loadTransparentTile(modHolderSize, modHolderSize, context);

    // Clear the canvas
//    Canvas canvas = new Canvas(regularTile);
//
//    Paint transPainter = new Paint();
//    transPainter.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//
//    canvas.drawRect(0, 0, holder.size, holder.size, transPainter);

//  super.decode(holder, context);

//  segmentSize = holder.size / 6;

//  Log.d("A", "segmentSize: " + segmentSize);
//  Log.d("A", "bitmapSize: " + regularTile.getWidth());
//
//  if (regularTile != null && errorTile != null) {
//      for (int i = 0; i < shown.length; i++) {
//          for (int j = 0; j < shown[i].length; j++) {
//              if (hasShown(holder.column, holder.row)) {
//                  drawShown(regularTile, errorTile, holder.column, holder.row);
//              }
//          }
//      }
//  }

    if (shown[holder.column][holder.row]) {
      return clearTile;
    }
    return blankTile;
  }

  private Bitmap fuse(final Bitmap under, final Bitmap over, int sCol, int sRow) {
    sCol = sCol % segmentsPerTile;
    sRow = sRow % segmentsPerTile;
    sCol++;
    sRow++;
    int endCPx = sCol * segmentSize - 1;
    int startCPx = endCPx - segmentSize + 1;
    int endRPx = sRow * segmentSize - 1;
    int startRPx = endRPx - segmentSize + 1;
    endCPx--;
    endRPx--;

//        startCPx++;
//        startRPx++;
    endCPx = endCPx + 2;
    endRPx = endRPx + 2;

    Log.d("fuse", "start:" + startCPx + "," + startRPx + " end:" + endCPx + "," + endRPx);

    for (int i = startCPx; i < endCPx; i++) {
      for (int j = startRPx; j < endRPx; j++) {
        if (under != null && over != null) {
          under.setPixel(i, j, over.getPixel(i, j));
        }
      }
    }
    return under;
  }

  private boolean isShown(int column, int row) {
    column++;
    row++;
    int segmentColumnEnd = column * segmentsPerTile - 1;
    int segmentColumnStart = segmentColumnEnd - segmentsPerTile + 1;
    int segmentRowEnd = row * segmentsPerTile - 1;
    int segmentRowStart = segmentRowEnd - segmentsPerTile + 1;
//        segmentColumnStart--;
    segmentColumnEnd--;
//        segmentRowStart--;
    segmentRowEnd--;

    for (int i = segmentColumnStart; i < segmentColumnEnd; i++) {
      for (int j = segmentRowStart; j < segmentRowEnd; j++) {
        if (shown[i][j]) {
          return true;
        }
      }
    }
    return false;
  }

  private void drawShown(Bitmap under, Bitmap over, int column, int row) {
    column++;
    row++;
    int segmentColumnEnd = column * segmentsPerTile - 1;
    int segmentColumnStart = segmentColumnEnd - segmentsPerTile + 1;
    int segmentRowEnd = row * segmentsPerTile - 1;
    int segmentRowStart = segmentRowEnd - segmentsPerTile + 1;
//        segmentColumnStart--;
    segmentColumnEnd--;
//        segmentRowStart--;
    segmentRowEnd--;

    for (int i = segmentColumnStart; i < segmentColumnEnd; i++) {
      for (int j = segmentRowStart; j < segmentRowEnd; j++) {
        if (shown[i][j]) {
          fuse(under, over, i, j);
        }
      }
    }
  }
}
