package com.ameron32.apps.projectbanditv3.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by klemeilleur on 11/5/2015.
 */
public class TileRevealView extends ViewGroup {

  public static final String TAG = TileRevealView.class.getSimpleName();
  private int sizeX, sizeY;

  //drawing path
  private Path drawPath;
  //drawing and canvas paint
  private Paint drawPaint, canvasPaint;
  //initial color
  private int paintColor = 0xFF000000;
  //canvas
  private Canvas drawCanvas;
  //canvas bitmap
  private Bitmap canvasBitmap;

  public TileRevealView(Context context) {
    super(context);
    initializeView();
  }

  public TileRevealView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeView();
  }

  public TileRevealView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TileRevealView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initializeView();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // called on TileView scale
  }

  private Tile[][] mTiles;
  private int x0 = 0;
  private int y0 = 0;
  private int squareSize = 0;
  private boolean flipped = false;

  private void initializeView() {
    setFocusable(true);
    mTiles = new Tile[sizeX][sizeY];
    buildTiles();
  }

  private void buildTiles() {
    for (int c = 0; c < sizeX; c++) {
      for (int r = 0; r < sizeY; r++) {
        mTiles[c][r] = new Tile(c, r);
      }
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    // called on TileView scale
  }

  @Override
  protected void onDraw(Canvas canvas) {
//super.onDraw(canvas);
    final int width = getWidth();
    final int height = getHeight();

    this.squareSize = Math.min(
        getSquareSizeWidth(width),
        getSquareSizeHeight(height)
    );

    computeOrigins(width, height);

    for (int c = 0; c < sizeX; c++) {
      for (int r = 0; r < sizeY; r++) {
        final int xCoord = getXCoord(c);
        final int yCoord = getYCoord(r);

        final Rect tileRect = new Rect(
            xCoord,               // left
            yCoord,               // top
            xCoord + squareSize,  // right
            yCoord + squareSize   // bottom
        );

        mTiles[c][r].setTileRect(tileRect);
        mTiles[c][r].draw(canvas);
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    final int x = (int) event.getX();
    final int y = (int) event.getY();

    Tile tile;
    for (int c = 0; c < sizeX; c++) {
      for (int r = 0; r < sizeY; r++) {
        tile = mTiles[c][r];
        if (tile.isTouched(x, y)) {
          tile.handleTouch();
          return true;
        }
      }
    }

    return false;
  }

  private int getSquareSizeWidth(final int width) {
    return width / 8;
  }

  private int getSquareSizeHeight(final int height) {
    return height / 8;
  }

  private int getXCoord(final int x) {
    return x0 + squareSize * (flipped ? 7 - x : x);
  }

  private int getYCoord(final int y) {
    return y0 + squareSize * (flipped ? y : 7 - y);
  }

  private void computeOrigins(final int width, final int height) {
    this.x0 = (width  - squareSize * 8) / 2;
    this.y0 = (height - squareSize * 8) / 2;
  }

  public static final class Tile {
    private static final String TAG = Tile.class.getSimpleName();


    private final int col;
    private final int row;

    private boolean isDark = true;
    private final Paint squareColor;
    private Rect tileRect;


    public Tile(final int col, final int row) {
      this.col = col;
      this.row = row;


      this.squareColor = new Paint();
      squareColor.setColor(isDark() ? Color.BLACK : Color.WHITE);
    }


    public void draw(final Canvas canvas) {
      canvas.drawRect(tileRect, squareColor);
    }


    public String getColumnString() {
      switch (col) {
        case 0: return "A";
        case 1: return "B";
        case 2: return "C";
        case 3: return "D";
        case 4: return "E";
        case 5: return "F";
        case 6: return "G";
        case 7: return "H";
        default: return null;
      }
    }


    public String getRowString() {
      // To get the actual row, add 1 since 'row' is 0 indexed.
      return String.valueOf(row + 1);
    }


    public void handleTouch() {
//      Log.d(TAG, "handleTouch(): col: " + col);
//      Log.d(TAG, "handleTouch(): row: " + row);
      setDark(false);
    }


    private void setDark(boolean state) {
      this.isDark = state;
    }

    public boolean isDark() {
      return this.isDark;
    }


    public boolean isTouched(final int x, final int y) {
      return tileRect.contains(x, y);
    }


    public void setTileRect(final Rect tileRect) {
      this.tileRect = tileRect;
    }


    public String toString() {
      final String column = getColumnString();
      final String row    = getRowString();
      return "<Tile " + column + row + ">";
    }
  }
}
