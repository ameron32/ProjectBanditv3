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

public class ExpandableTextView2
//    extends LinearLayout
//    implements View.OnClickListener
{
  
//  protected class ExpandCollapseAnimation
//      extends Animation {
//    private final int mEndHeight;
//    private final int mStartHeight;
//    private final View mTargetView;
//
//    public ExpandCollapseAnimation(
//        View view, int startHeight,
//        int endHeight) {
//      mTargetView = view;
//      mStartHeight = startHeight;
//      mEndHeight = endHeight;
//      setDuration(mAnimationDuration);
//    }
//
//    @Override protected void applyTransformation(
//        float interpolatedTime,
//        Transformation t) {
//      final int newHeight = (int) ((mEndHeight - mStartHeight)
//          * interpolatedTime + mStartHeight);
//      mTv.setMaxHeight(newHeight
//          - mMarginBetweenTxtAndBottom);
//      if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
//        applyAlphaAnimation(mTv, mAnimAlphaStart
//            + interpolatedTime
//            * (1.0f - mAnimAlphaStart));
//      }
//      mTargetView.getLayoutParams().height = newHeight;
//      mTargetView.requestLayout();
//    }
//
//    @Override public void initialize(
//        int width, int height,
//        int parentWidth,
//        int parentHeight) {
//      super.initialize(width, height, parentWidth, parentHeight);
//    }
//
//    @Override public boolean willChangeBounds() {
//      return true;
//    }
//  }
//
//  /* The default alpha value when the animation starts */
//  private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;
//
//  /* The default animation duration */
//  private static final int DEFAULT_ANIM_DURATION = 300;
//
//  /* The default number of lines */
//  private static final int MAX_COLLAPSED_LINES = 8;
//
//  private static final String TAG = ExpandableTextView2.class.getSimpleName();
//
//  @TargetApi(Build.VERSION_CODES.HONEYCOMB) private static void applyAlphaAnimation(
//      View view, float alpha) {
//    if (isPostHoneycomb()) {
//      view.setAlpha(alpha);
//    } else {
//      AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
//      // make it instant
//      alphaAnimation.setDuration(0);
//      alphaAnimation.setFillAfter(true);
//      view.startAnimation(alphaAnimation);
//    }
//  }
//
//  private static int getRealTextViewHeight(
//      TextView textView) {
//    int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
//    int padding = textView.getCompoundPaddingTop()
//        + textView.getCompoundPaddingBottom();
//    return textHeight + padding;
//  }
//
//  private static boolean isPostHoneycomb() {
//    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
//  }
//
//  private float mAnimAlphaStart;
//
//  private int mAnimationDuration;
//
//  protected ImageButton mButton; // Button to expand/collapse
//
//  private boolean mCollapsed = true; // Show short version as default.
//
//  private int mCollapsedHeight;
//
//  private Drawable mCollapseDrawable;
//
//  /* For saving collapsed status when used in ListView */
//  private SparseBooleanArray mCollapsedStatus;
//
//  private Drawable mExpandDrawable;
//
//  private int mMarginBetweenTxtAndBottom;
//  private int mMaxCollapsedLines;
//
//  private int mPosition;
//
//  private boolean mRelayout;
//
//  private int mTextHeightWithMaxLines;
//
//  protected TextView mTv;
//
//  public ExpandableTextView2(
//      Context context) {
//    super(context);
//  }
//
//  public ExpandableTextView2(
//      Context context,
//      AttributeSet attrs) {
//    super(context, attrs);
//    init(attrs);
//  }
//
//  @TargetApi(Build.VERSION_CODES.HONEYCOMB) public ExpandableTextView2(
//      Context context,
//      AttributeSet attrs, int defStyle) {
//    super(context, attrs, defStyle);
//    init(attrs);
//  }
//
//  private void findViews() {
//    mTv = (TextView) findViewById(R.id.expandable_text);
//    mTv.setOnClickListener(this);
//    mButton = (ImageButton) findViewById(R.id.expand_collapse);
//    mButton.setImageDrawable(mCollapsed ? mExpandDrawable
//        : mCollapseDrawable);
//    mButton.setOnClickListener(this);
//  }
//
//  public CharSequence getText() {
//    if (mTv == null) { return ""; }
//    return mTv.getText();
//  }
//
//  private void init(AttributeSet attrs) {
//    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
//    mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
//    mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
//    mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
//    mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
//    mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
//
//    if (mExpandDrawable == null) {
//      mExpandDrawable = getResources().getDrawable(R.drawable.ic_expand_small_holo_light);
//    }
//    if (mCollapseDrawable == null) {
//      mCollapseDrawable = getResources().getDrawable(R.drawable.ic_collapse_small_holo_light);
//    }
//
//    typedArray.recycle();
//  }
//
//  @Override public void onClick(
//      View view) {
//    if (mButton.getVisibility() != View.VISIBLE) { return; }
//
//    mCollapsed = !mCollapsed;
//    mButton.setImageDrawable(mCollapsed ? mExpandDrawable
//        : mCollapseDrawable);
//
//    if (mCollapsedStatus != null) {
//      mCollapsedStatus.put(mPosition, mCollapsed);
//    }
//
//    Animation animation;
//    if (mCollapsed) {
//      animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
//    } else {
//      animation = new ExpandCollapseAnimation(this, getHeight(), getHeight()
//          + mTextHeightWithMaxLines
//          - mTv.getHeight());
//    }
//
//    animation.setFillAfter(true);
//    animation.setAnimationListener(new Animation.AnimationListener() {
//      @Override public void onAnimationEnd(
//          Animation animation) {
//        // clear animation here to avoid repeated applyTransformation() calls
//        clearAnimation();
//      }
//
//      @Override public void onAnimationRepeat(
//          Animation animation) {}
//
//      @Override public void onAnimationStart(
//          Animation animation) {
//        applyAlphaAnimation(mTv, mAnimAlphaStart);
//      }
//    });
//
//    clearAnimation();
//    startAnimation(animation);
//  }
//
//  @Override protected void onFinishInflate() {
//    findViews();
//  }
//
//  @Override protected void onMeasure(
//      int widthMeasureSpec,
//      int heightMeasureSpec) {
//    // If no change, measure and return
//    if (!mRelayout
//        || getVisibility() == View.GONE) {
//      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//      return;
//    }
//    mRelayout = false;
//
//    // Setup with optimistic case
//    // i.e. Everything fits. No button needed
//    mButton.setVisibility(View.GONE);
//    mTv.setMaxLines(Integer.MAX_VALUE);
//
//    // Measure
//    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    // If the text fits in collapsed mode, we are done.
//    if (mTv.getLineCount() <= mMaxCollapsedLines) { return; }
//
//    // Saves the text height w/ max lines
//    mTextHeightWithMaxLines = getRealTextViewHeight(mTv);
//
//    // Doesn't fit in collapsed mode. Collapse text view as needed. Show
//    // button.
//    if (mCollapsed) {
//      mTv.setMaxLines(mMaxCollapsedLines);
//    }
//    mButton.setVisibility(View.VISIBLE);
//
//    // Re-measure with new setup
//    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    if (mCollapsed) {
//      // Gets the margin between the TextView's bottom and the ViewGroup's
//      // bottom
//      mTv.post(new Runnable() {
//        @Override public void run() {
//          mMarginBetweenTxtAndBottom = getHeight()
//              - mTv.getHeight();
//        }
//      });
//      // Saves the collapsed height of this ViewGroup
//      mCollapsedHeight = getMeasuredHeight();
//    }
//  }
//
//  public void setText(CharSequence text) {
//    mRelayout = true;
//    mTv.setText(text);
//    setVisibility(TextUtils.isEmpty(text) ? View.GONE
//        : View.VISIBLE);
//  }
//
//  public void setText(
//      CharSequence text,
//      SparseBooleanArray collapsedStatus,
//      int position) {
//    mCollapsedStatus = collapsedStatus;
//    mPosition = position;
//    boolean isCollapsed = collapsedStatus.get(position, true);
//    clearAnimation();
//    mCollapsed = isCollapsed;
//    mButton.setImageDrawable(mCollapsed ? mExpandDrawable
//        : mCollapseDrawable);
//    setText(text);
//    getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//    requestLayout();
//  };
}
