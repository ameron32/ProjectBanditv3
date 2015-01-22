package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class PartialSlidingPaneLayout extends SlidingPaneLayout {

  public PartialSlidingPaneLayout(Context context) {
    super(context);
  }

  public PartialSlidingPaneLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PartialSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  private View fullView;
  private View partialView;

  void init() {
    setSliderFadeColor(Color.TRANSPARENT);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    if (getChildCount() < 1) {
      return;
    }

    final View panel = getChildAt(0);
    if (!(panel instanceof ViewGroup)) {
      return;
    }

    final ViewGroup viewGroup = (ViewGroup) panel;
    if (viewGroup.getChildCount() != 2) {
      return;
    }
    fullView = viewGroup.getChildAt(0);
    partialView = viewGroup.getChildAt(1);

    super.setPanelSlideListener(crossFadeListener);
  }

  private final SimplePanelSlideListener crossFadeListener = new SimplePanelSlideListener() {
    @Override
    public void onPanelSlide(View panel, float slideOffset) {
      super.onPanelSlide(panel, slideOffset);
      if (partialView == null || fullView == null) {
        return;
      }

      partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);
      partialView.setAlpha(1 - slideOffset);
      fullView.setAlpha(slideOffset);
    }
  };

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    if (partialView != null) {
      partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);
    }
  }
}
