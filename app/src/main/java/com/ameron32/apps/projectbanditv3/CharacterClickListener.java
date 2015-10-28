package com.ameron32.apps.projectbanditv3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;


public class CharacterClickListener
    implements
    RecyclerView.OnItemTouchListener {

  private CharacterClickListener.OnCharacterClickListener mListener;

  public interface OnCharacterClickListener {
    public void onCharacterClick(
        View view, int position);
  }

  private final GestureDetector mGestureDetector;

  public CharacterClickListener(
      Context context,
      CharacterClickListener.OnCharacterClickListener listener) {
    mListener = listener;
    SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
      @Override public boolean onSingleTapUp(
          MotionEvent e) {
        return true;
      }
    };
    mGestureDetector = new GestureDetector(context, gestureListener);
  }

  @Override public boolean onInterceptTouchEvent(
      RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null
        && mListener != null
        && mGestureDetector.onTouchEvent(e)) {
//      onItem
      int position = view.getChildPosition(childView);
      mListener.onCharacterClick(view, position);
    }
    return false;
  }

  @Override public void onTouchEvent(
      RecyclerView view,
      MotionEvent motionEvent) {}

  /**
   * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
   * intercept touch events with
   * {@link ViewGroup#onInterceptTouchEvent(MotionEvent)}.
   *
   * @param disallowIntercept True if the child does not want the parent to
   *                          intercept touch events.
   * @see ViewParent#requestDisallowInterceptTouchEvent(boolean)
   */
  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    // TODO confirm empty
  }

}
