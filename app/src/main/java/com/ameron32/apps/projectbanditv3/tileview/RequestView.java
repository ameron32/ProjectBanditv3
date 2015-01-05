package com.ameron32.apps.projectbanditv3.tileview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;


public class RequestView extends RelativeLayout {

  public void setMessage(String message) {
    this.message.setText(message);
  }
  
  public void setPositiveListener(View.OnClickListener listener) {
    add.setOnClickListener(listener);
  }
  
  public void setNegativeListener(View.OnClickListener listener) {
    delete.setOnClickListener(listener);
  }
  
  public void show() {
    changeVisibility(false, 500);
  }
  
  public void hide() {
    changeVisibility(true, 2000);
  }
  
  
  
  
  
  
  
  
  private void changeVisibility(boolean hide, int duration) {
    
    float scaleY, alpha;
    
    // hide/show toggle
    if (hide) {
      scaleY = 1; alpha = 0;
    } else {
      scaleY = 1; alpha = 1;
    }
    
    if (duration > 0) {
      // perform animation
      this.animate()
          .setDuration(duration)
          .scaleY(scaleY).alpha(alpha)
          .setInterpolator(new AccelerateInterpolator(5.0f))
          .setListener(new AnimatorListener() {
        
        @Override
        public void onAnimationStart(Animator animation) {}
        
        @Override
        public void onAnimationRepeat(Animator animation) {}
        
        @Override
        public void onAnimationEnd(Animator animation) {}
        
        @Override
        public void onAnimationCancel(Animator animation) {}
      });
    } else {
      // instant, no animation
      this.setScaleY(scaleY);
      this.setAlpha(alpha);
    }
  }
  

  
  
  Context context;
  public RequestView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  public RequestView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public RequestView(Context context) {
    super(context);
    init(context);
  }
  
  void init(Context context) {
    this.context = context;
    
    // inflate elements
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rootView = inflater.inflate(R.layout.request_view, null);
    storeViews(rootView);
    this.addView(rootView);
    
    // start hidden
    changeVisibility(true, 0);
  }
  
  ImageButton add, delete;
  TextView message;
  
  void storeViews(View rootView) {
    add = (ImageButton) rootView.findViewById(R.id.ibAdd);
    delete = (ImageButton) rootView.findViewById(R.id.ibDelete);
    message = (TextView) rootView.findViewById(R.id.tvMessage);
  }
  
  
  
}
