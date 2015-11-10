package com.ameron32.apps.projectbanditv3.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Random;

import butterknife.InjectView;

/**
 * Created by Micah on 11/6/2015.
 */
public class RevealView extends View implements View.OnClickListener {

  private boolean[][] visiblity;
  private static final int rowCount = 36;
  private static final int colCount = 36;
  private int viewWidth;
  private int viewHeight;
  private int cellWidth;
  private int cellHeight;
  private Paint blackPaint;
  private Paint transparentPaint;
//  private float touchX;
//  private float touchY;
//  private Point touchedCell;

  public RevealView(Context context) {
    super(context);
    init();

  }

  public RevealView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();

  }

  public RevealView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();

  }

  private void init() {
    visiblity = new boolean[rowCount][colCount];

//    touchedCell = new Point(-1, -1);

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


      //Set a touch listener to save the x and y coords so the onClick method can use them.
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

    //testing...
//    visiblity[0][0] = true;
//    visiblity[0][1] = false;
//    visiblity[0][2] = true;
//
//    visiblity[1][0] = false;
//    visiblity[1][1] = true;
//    visiblity[1][2] = false;
//
//    visiblity[2][0] = true;
//    visiblity[2][1] = false;
//    visiblity[2][2] = true;

    //testing.

  }

  public void setColor(int color, int transparency) {
    blackPaint.setColor(color);
    if (transparency >= 0) {
      blackPaint.setAlpha(transparency);
    }
  }

  @Override
  public void onDraw(Canvas c) {
    Rect r = new Rect();
    for (int x = 0; x < rowCount; x++) {
      for (int y = 0; y < colCount; y++) {
        r.left = x * cellWidth;
        r.right = (x + 1) * cellWidth;
        r.top = y * cellHeight;
        r.bottom = (y + 1) * cellHeight;
        if (visiblity[x][y]) {
          c.drawRect(r, transparentPaint);
        } else {
          c.drawRect(r, blackPaint);
        }
      }
    }
  }

  private void readjustCellSizes() {
    viewWidth = getWidth();
    viewHeight = getHeight();
    cellWidth = viewWidth / colCount;
    cellHeight = viewHeight / rowCount;
  }

  @Override
  public void onClick(View v) {

  }

  public void setVisiblityData(boolean[][] newData) {
    for (int i = 0; i < newData.length; i++) {
      for (int j = 0; j < newData[i].length; j++) {
        visiblity[i][j] = newData[i][j];
      }
    }
//    visiblity = newData;
  }

  public void resetReveal() {
    visiblity = new boolean[rowCount][colCount];
  }

  /**
   * feed me a motionevent from the ScalingLayout onTouch and
   * @param e
   */
  public void reveal(MotionEvent e, float scale, int additionalRadius) {
    float touchX = (e.getX() / scale);
    float touchY = (e.getY() / scale);
    int row = (int) touchX / cellWidth;
    int col = (int) touchY / cellHeight;
////    touchedCell.set(row, col);

    reveal(row, col, additionalRadius);
    invalidate();
  }

  public void reveal(int row, int col, int additionalRadius) {
    final int radius = additionalRadius;
    visiblity[row][col] = true;
    for (int r = row - radius; r <= row + radius; r++) {
      for (int c = col - radius; c <= col + radius; c++) {
        try {
          visiblity[r][c] = true;
        } catch (IndexOutOfBoundsException e) {
        }
      }
    }

//    final int yc = col;
//    final int xc = row;
//    int x = 0;
//    int y = 0;
//    int R = radius;
//    for (x = -R; x <= R; x++) {
//      for (y = -R; y <= R; y++) {
//        double r = Math.sqrt(x * x + y * y);
//        double inv_rad = r <= R ? 1 / r : 0; // truncate outside radius R
//        final double v = 1 - inv_rad;
//        map[yc + y][xc + x] = 1 - inv_rad;
//      }
//    }
  }



  public void _randomizeVisiblity(boolean andColor, boolean withTransparency) {
    Random r = new Random();
    for (int i = 0; i < colCount; i++) {
      for (int j = 0; j < rowCount; j++) {
        visiblity[i][j] = r.nextBoolean();
      }
    }

    if (andColor) {
      switch(r.nextInt(4)) {
        case 0:
        case 1:
        case 2:
        case 3:
        default:
          blackPaint.setColor(Color.DKGRAY);
          if (withTransparency) {
            blackPaint.setAlpha(192);
          }
      }
    }
  }
}
