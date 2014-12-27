package com.ameron32.apps.projectbanditv3.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;


/**
 * 
 * RETYPE FROM THE FOLLOWING LINK:
 * https://github.com/Manabu-GT/ExpandableTextView/blob/master/lib/src/main/java/com/ms/square/android/expandabletextview/ExpandableTextView.java
 * 
 */
public class ExpandableTextView 
    extends LinearLayout 
    implements View.OnClickListener
{
  
  private static final String TAG = ExpandableTextView.class.getSimpleName();

  /* The default number of lines */
  private static final int MAX_COLLAPSED_LINES = 8;

  /* The default animation duration */
  private static final int DEFAULT_ANIM_DURATION = 300;

  /* The default alpha value when the animation starts */
  private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;
  
  
  
  
  
  
  private static boolean isPostHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }
  
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private static void applyAlphaAnimation(View view, float alpha) {
    if (isPostHoneycomb()) {
      view.setAlpha(alpha);
    } else {
      AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
      alphaAnimation.setDuration(0);
      alphaAnimation.setFillAfter(true);
      view.startAnimation(alphaAnimation);
    }
  }
  
  private static int getRealTextViewHeight(TextView textView) {
    final int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
    final int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
    return textHeight + padding;
  }
  
  
  
  
  
  
  
  
  // CHILD VIEWS
  protected TextView mTv;
  // button to expand/collapse
  protected ImageButton mButton;

  // ATTRIBUTES
  private int mMaxCollapsedLines;
  private int mAnimationDuration;
  private float mAnimAlphaStart;
  private Drawable mExpandDrawable;
  private Drawable mCollapseDrawable;

  // default to collapsed
  private boolean mCollapsed = true;
  
  
  // MEASURE VARIABLES
  private boolean mRelayout;
  private int mTextHeightWithMaxLines;
  private int mCollapsedHeight;
  private int mMarginBetweenTxtAndBottom;
  
  
  
  /* For saving collapsed status when used in ListView */
  private SparseBooleanArray mCollapsedStatus;
  private int mPosition;
  
  
  
  
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public ExpandableTextView(
      Context context,
      AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  public ExpandableTextView(
      Context context,
      AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
    mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
    mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
    mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
    
    mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
    mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
    
    if (mExpandDrawable == null) {
      mExpandDrawable = getResources().getDrawable(R.drawable.ic_expand_small_holo_light);
    }
    if (mCollapseDrawable == null) {
      mCollapseDrawable = getResources().getDrawable(R.drawable.ic_collapse_small_holo_light);
    }
    
    typedArray.recycle();
  }

  public ExpandableTextView(
      Context context) {
    super(context);
  }
  
  @Override protected void onMeasure(
      int widthMeasureSpec,
      int heightMeasureSpec) {
    // if no change, measure and return
    if (!mRelayout || getVisibility() == View.GONE) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;
    }
    
    mRelayout = false;
    
    // setup: everything fits, no button needed
    
    mButton.setVisibility(View.GONE);
    mTv.setMaxLines(Integer.MAX_VALUE);
    
    // Measure
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    // if the text fits, return
    if (mTv.getLineCount() <= mMaxCollapsedLines) {
       return;
    }
    
    // saves the text height w/ max lines
    mTextHeightWithMaxLines = getRealTextViewHeight(mTv);
    
    // doesn't fit in collapsed mode, show button
    if (mCollapsed) {
      mTv.setMaxLines(mMaxCollapsedLines);
    }
    mButton.setVisibility(View.VISIBLE);
    
    // remeasure with new setup
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    
    if (mCollapsed) {
      // get the margin between the TextView's bottom and the ViewGroup's bottom
      mTv.post(new Runnable() {

        @Override public void run() {
          mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
        }
      });
      // saves the collapsed height of this viewgroup
      mCollapsedHeight = getMeasuredHeight();
    }
  }
  
  @Override protected void onFinishInflate() {
    // super.onFinishInflate();
    findViews();
  }

  private void findViews() {
    mTv = (TextView) findViewById(R.id.expandable_text);
    mTv.setOnClickListener(this);
    mButton = (ImageButton) findViewById(R.id.expand_collapse);
    mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
    mButton.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    if (mButton.getVisibility() != View.VISIBLE) { 
      return; 
    }
    
    mCollapsed = !mCollapsed;
    mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
    
    if (mCollapsedStatus != null) {
      mCollapsedStatus.put(mPosition, mCollapsed);
    }
    
    Animation animation;
    if (mCollapsed) {
      animation = new ExpandCollapseAnimation(this, getHeight(), 
          mCollapsedHeight);
    } else {
      animation = new ExpandCollapseAnimation(this, getHeight(), 
          getHeight() + mTextHeightWithMaxLines - mTv.getHeight());
    }
    
    animation.setFillAfter(true);
    animation.setAnimationListener(new Animation.AnimationListener() {
      
      @Override public void onAnimationStart(
          Animation animation) {
        applyAlphaAnimation(mTv, mAnimAlphaStart);
      }
      
      @Override public void onAnimationRepeat(
          Animation animation) {}
      
      @Override public void onAnimationEnd(
          Animation animation) {
        // clear to avoid repeated applyTransformation() calls
        clearAnimation();
      }
    });
    
    clearAnimation();
    startAnimation(animation);
  }
  
  public void setText(CharSequence text, SparseBooleanArray collapsedStatus, int position) {
    mCollapsedStatus = collapsedStatus;
    mPosition = position;
    boolean isCollapsed = collapsedStatus.get(position, true);
    clearAnimation();
    mCollapsed = isCollapsed;
    mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
    setText(text);
    getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    requestLayout();
  }
  
  public void setText(CharSequence text) {
    mRelayout = true;
    mTv.setText(text);
    setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
  }
  
  public CharSequence getText() {
    if (mTv == null) {
      return "";
    }
    return mTv.getText();
  }

  
  protected class ExpandCollapseAnimation extends Animation {
    private View mTargetView;
    private int mStartHeight;
    private int mEndHeight;

    public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
      mTargetView = view;
      mStartHeight = startHeight;
      mEndHeight = endHeight;
      setDuration(mAnimationDuration);
    }
    
    @Override protected void applyTransformation(
        float interpolatedTime,
        Transformation t) {
      // super.applyTransformation(interpolatedTime, t);
      final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
      mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
      if (Float.compare(mAnimAlphaStart,  1.0f) != 0) {
        applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
      }
      mTargetView.getLayoutParams().height = newHeight;
      mTargetView.requestLayout();
    }
    
    @Override public void initialize(
        int width, int height,
        int parentWidth,
        int parentHeight) {
      super.initialize(width, height, parentWidth, parentHeight);
    }
    
    @Override public boolean willChangeBounds() {
      return true;
    }
  }
    
}
