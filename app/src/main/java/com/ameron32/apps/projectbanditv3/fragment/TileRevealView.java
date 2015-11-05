package com.ameron32.apps.projectbanditv3.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
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

  private void initializeView() {
    drawPath = new Path();
    drawPaint = new Paint();

    drawPaint.setColor(paintColor);

    drawPaint.setAntiAlias(true);
    drawPaint.setStrokeWidth(20);
    drawPaint.setStyle(Paint.Style.STROKE);
    drawPaint.setStrokeJoin(Paint.Join.ROUND);
    drawPaint.setStrokeCap(Paint.Cap.ROUND);

    canvasPaint = new Paint(Paint.DITHER_FLAG);

    // moved from onSizeChanged
    // TODO make size dynamic
    sizeX = sizeY = 8;
    canvasBitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_8888);
    canvasBitmap.setPixel(0,0, Color.BLUE);
    drawCanvas = new Canvas(canvasBitmap);
    drawCanvas.scale(100.0f, 100.0f);
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

    canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    canvas.drawPath(drawPath, drawPaint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float touchX = event.getX();
    float touchY = event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        drawPath.moveTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_MOVE:
        drawPath.lineTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_UP:
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
        break;
      default:
        return false;
    }

    invalidate();
    return false;
  }
}
