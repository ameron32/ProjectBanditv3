package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Arrays;

/**
 * Created by Micah on 11/6/2015.
 */
public class RevealView extends View implements View.OnClickListener {

  private boolean halfTileOffset = false;
  private int tileRows = 1; // default
  private int tileCols = 1; // default

  private Grid grid;

  private int viewWidth = 0;
  private int viewHeight = 0;
  private int cellWidth = 0;
  private int cellHeight = 0;
  private Paint blackPaint;
  private Paint transparentPaint;

  private int halfWidth = 0;
  private int halfHeight= 0;

  public RevealView(Context context) {
    super(context);
    initialize();
  }

  public RevealView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public RevealView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  private void initialize() {
//    grid = new Grid(getRowCount(), getColCount());
//    resetReveal();

    blackPaint = new Paint();
    blackPaint.setColor(Color.BLACK);
    transparentPaint = new Paint();
    transparentPaint.setColor(Color.TRANSPARENT);

    //Set an observer to call and update the size of the view correctly, and then calculate the sizes of the "cells" based
    //on the row and column counts.

    ViewTreeObserver viewTreeObserver = getViewTreeObserver();
    if (viewTreeObserver.isAlive()) {
      viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          getViewTreeObserver().removeOnGlobalLayoutListener(this);
          readjustCellSizes();
        }
      });


      //Set a touch listener to save the tileCol and tileRow coords so the onClick method can use them.
//      setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//          if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            touchX = event.getX();
//            touchY = event.getY();
//          }
//          return false;
//        }
//      });
//
//      setOnClickListener(this);
    }
  }

  public void setTiling(int columns, int rows, boolean halfTileOffset) {
    this.tileRows = rows;
    this.tileCols = columns;
    this.halfTileOffset = halfTileOffset;
    grid = new Grid(getRowCount(), getColCount());
    resetReveal();
  }

  public void setColor(int color, int transparency) {
    blackPaint.setColor(color);
    if (transparency >= 0) {
      blackPaint.setAlpha(transparency);
    }
  }

  @Override
  public void onDraw(Canvas c) {
    if (grid == null) {
      // nothing to draw
      return;
    }

    Rect rect = new Rect();
    for (int row = 0; row < getRowCount(); row++) {
      for (int col = 0; col < getColCount(); col++) {
        rect.left = col * cellWidth - (halfTileOffset ? halfWidth : 0);
        rect.right = (col + 1) * cellWidth - (halfTileOffset ? halfWidth : 0);
        rect.top = row * cellHeight - (halfTileOffset ? halfHeight : 0);
        rect.bottom = (row + 1) * cellHeight - (halfTileOffset ? halfHeight : 0);
        if (grid.isShown(row, col)) {
//        if (visibility[row][col]) {
          c.drawRect(rect, transparentPaint);
        } else {
          c.drawRect(rect, blackPaint);
        }
      }
    }
  }

  private void readjustCellSizes() {
    viewWidth = getWidth();
    viewHeight = getHeight();
    cellWidth = viewWidth / tileCols;
    cellHeight = viewHeight / tileRows;
    halfWidth = cellWidth / 2;
    halfHeight = cellHeight / 2;
  }

  private int getRowCount() {
    return (halfTileOffset ? tileRows + 1 : tileRows);
  }

  private int getColCount() {
    return (halfTileOffset ? tileCols + 1 : tileCols);
  }

  @Override
  public void onClick(View v) {}

  public void setVisibilityData(boolean[][] newData) {
    for (int row = 0; row < newData.length; row++) {
      for (int col = 0; col < newData[row].length; col++) {
        grid.set(row, col, newData[row][col]);
//        try {
//          visibility[i][j] = newData[i][j];
//        } catch (IndexOutOfBoundsException e) {
//          // DO NOTHING
//        }
      }
    }
  }

  public void resetReveal() {
    grid.reset();
//    visibility = new boolean[getRowCount()][getColCount()];
  }

  int squareRevealStartRow = -1;
  int squareRevealStartCol = -1;
  int squareRevealEndRow = -1;
  int squareRevealEndCol = -1;
  public void squareRevealStart(MotionEvent e, float scale) {
    float touchX = (e.getX() / scale);
    float touchY = (e.getY() / scale);
    int col = (int) touchX / cellWidth;
    int row = (int) touchY / cellHeight;
    squareRevealStartRow = row;
    squareRevealStartCol = col;
  }

  public void squareRevealEnd(MotionEvent e, float scale) {
    float touchX = (e.getX() / scale);
    float touchY = (e.getY() / scale);
    int col = (int) touchX / cellWidth;
    int row = (int) touchY / cellHeight;
    squareRevealEndRow = row;
    squareRevealEndCol = col;
  }

  public void revealSquare(int additionalRadius) {
    grid.showRange(squareRevealStartRow, squareRevealStartCol, squareRevealEndRow, squareRevealEndCol, additionalRadius);
//    int actualStartRow = (squareRevealStartRow < squareRevealEndRow ? squareRevealStartRow : squareRevealEndRow) - additionalRadius;
//    int actualEndRow = (squareRevealStartRow > squareRevealEndRow ? squareRevealStartRow : squareRevealEndRow) + additionalRadius;
//    int actualStartCol = (squareRevealStartCol < squareRevealEndCol ? squareRevealStartCol : squareRevealEndCol) - additionalRadius;
//    int actualEndCol = (squareRevealStartCol > squareRevealEndCol ? squareRevealStartCol : squareRevealEndCol) + additionalRadius;
//
//    if (actualStartRow < 0) {
//      actualStartRow = 0;
//    } else if (actualStartRow > getRowCount()) {
//      actualStartRow = getRowCount();
//    }
//    if (actualEndRow < 0) {
//      actualEndRow = 0;
//    } else if (actualEndRow > getRowCount()) {
//      actualEndRow = getRowCount();
//    }
//    if (actualStartCol < 0) {
//      actualStartCol = 0;
//    } else if (actualStartCol > getColCount()) {
//      actualStartCol = getColCount();
//    }
//    if (actualEndCol < 0) {
//      actualEndCol = 0;
//    } else if (actualEndCol > getColCount()) {
//      actualEndCol = getColCount();
//    }
//
//    // should be positive if top left then bottom right
//    int distanceRow = Math.abs(actualEndRow - actualStartRow);
//    int distanceCol = Math.abs(actualEndCol - actualStartCol);
//
//    // TODO allow for non-standard (TL/BR)
//    for (int r = 0; r <= distanceRow; r++) {
//      for (int c = 0; c <= distanceCol; c++) {
//        // reveal each tile
//        reveal(r + actualStartRow, c + actualStartCol, 0);
//      }
//    }
    invalidate();

    // reset
    squareRevealStartRow = -1;
    squareRevealStartCol = -1;
    squareRevealEndRow = -1;
    squareRevealEndCol = -1;
  }

  /**
   * feed me a motionevent from the ScalingLayout onTouch and
   * @param e
   */
  public void reveal(MotionEvent e, float scale, int additionalRadius) {
    Tile tile = getTileAt(e, scale);
    reveal(tile.row, tile.col, additionalRadius);
    invalidate();
  }

  public Tile getTileAt(MotionEvent e, float scale) {
    float x = e.getX();
    float y = e.getY();
    float touchX = (x / scale);
    float touchY = (y / scale);
    int col = (int) touchX / cellWidth;
    int row = (int) touchY / cellHeight;
    return new Tile(row, col);
  }

  public void reveal(int row, int col, int additionalRadius) {
    if (halfTileOffset) {
      offsetReveal(row, col, additionalRadius);
    } else {
      standardReveal(row, col, additionalRadius);
    }
  }

  private void offsetReveal(int row, int col, int additionalRadius) {
    // reduce radius by 1
    final int radius = (additionalRadius > 0 ? additionalRadius - 1 : 0);
    grid.showRange(row, col, row + 1, col + 1, radius);

    // NE, NW, NE, SW
//    visibility[row][col] = true;
//    try {
//      visibility[row + 1][col] = true;
//      visibility[row][col + 1] = true;
//      visibility[row + 1][col + 1] = true;
//    } catch (IndexOutOfBoundsException e) {}
//    if (additionalRadius == 0) {
//      return;
//    }
//    for (int r = row - radius; r <= row +1 + radius; r++) {
//      for (int c = col - radius; c <= col +1 + radius; c++) {
//        try {
//          visibility[r][c] = true;
//        } catch (IndexOutOfBoundsException e) {}
//      }
//    }
  }

  private void standardReveal(int row, int col, int additionalRadius) {
    final int radius = additionalRadius;
    if (radius > 0) {
      grid.showRange(row, col, row, col, radius);
    } else {
      grid.show(row, col);
    }
//    final int radius = additionalRadius;
////    try {
//      visibility[row][col] = true;
////    } catch (IndexOutOfBoundsException e) {}
//    if (additionalRadius == 0) {
//      return;
//    }
//    for (int r = row - radius; r <= row + radius; r++) {
//      for (int c = col - radius; c <= col + radius; c++) {
//        try {
//          visibility[r][c] = true;
//        } catch (IndexOutOfBoundsException e) {}
//      }
//    }
  }



  public class Tile {
    int row;
    int col;
    public Tile(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }

  public class Grid {
    private final int rows;
    private final int cols;
    private boolean[][] visibility;

    public Grid(int rows, int cols) {
      this.rows = rows;
      this.cols = cols;
      reset();
    }

    public void reset() {
      visibility = new boolean[rows][cols];
    }

    public void showAll() {
      Arrays.fill(visibility, true);
    }

    public void show(int row, int col) {
      set(row, col, true);
    }

    public void hide(int row, int col) {
      set(row, col, false);
    }

    private void set(int row, int col, boolean state) {
      try {
        visibility[row][col] = state;
      } catch (IndexOutOfBoundsException e) {}
    }

    public void showRange(int startRow, int startCol, int endRow, int endCol, int additionalRadius) {
      set(startRow, startCol, endRow, endCol, true, additionalRadius);
    }

    public void hideRange(int startRow, int startCol, int endRow, int endCol, int additionalRadius) {
      set(startRow, startCol, endRow, endCol, false, additionalRadius);
    }

    private void set(int startRow, int startCol, int endRow, int endCol, boolean state, int additionalRadius) {
      final int radius = additionalRadius;
      int actualStartRow = (startRow < endRow ? startRow : endRow) - radius;
      int actualEndRow = (startRow > endRow ? startRow : endRow) + radius;
      int actualStartCol = (startCol < endCol ? startCol : endCol) - radius;
      int actualEndCol = (startCol > endCol ? startCol : endCol) + radius;

      if (actualStartRow < 0) {
        actualStartRow = 0;
      } else if (actualStartRow > getRowCount()) {
        actualStartRow = getRowCount();
      }
      if (actualEndRow < 0) {
        actualEndRow = 0;
      } else if (actualEndRow > getRowCount()) {
        actualEndRow = getRowCount();
      }
      if (actualStartCol < 0) {
        actualStartCol = 0;
      } else if (actualStartCol > getColCount()) {
        actualStartCol = getColCount();
      }
      if (actualEndCol < 0) {
        actualEndCol = 0;
      } else if (actualEndCol > getColCount()) {
        actualEndCol = getColCount();
      }

      for (int row = actualStartRow; row <= actualEndRow; row++) {
        for (int col = actualStartCol; col <= actualEndCol; col++) {
          set(row, col, state);
        }
      }
    }

    public boolean isShown(int row, int col) {
      return visibility[row][col];
    }
  }
}
