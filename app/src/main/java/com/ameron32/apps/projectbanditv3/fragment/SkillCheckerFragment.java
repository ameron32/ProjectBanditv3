package com.ameron32.apps.projectbanditv3.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.Skill;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SkillCheckerFragment 
  extends
    AbsContentFragment
{

  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment_basic_recyclerview;
  }
  
  @InjectView(R.id.recyclerview) RecyclerView mRecyclerView;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
    mRecyclerView.setHasFixedSize(true);
    
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    
    ParseQuery.getQuery(Skill.class).setLimit(1000)
//    .orderByDescending("bIsForbidden")
    .addAscendingOrder("iPage")
    .addAscendingOrder("sName").findInBackground(new FindCallback<Skill>() {
      
      @Override public void done(
          final List<Skill> skills,
          ParseException e) {
        if (e == null) {
       
          mRecyclerView.setAdapter(new SkillAdapter(skills));
          mRecyclerView.addOnItemTouchListener(new ItemClickListener(getActivity(), 
              new ItemClickListener.OnItemClickListener() {
            
            @Override public void onItemClick(
                View view, int position) {
              
            }
          }));
          
        }
      }
    });
  }
  
  public static class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder> {

    private List<Skill> skills;

    public SkillAdapter(List<Skill> skills) {
      this.skills = skills;
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
      private int mOriginalHeight = 0;
      private boolean mIsViewExpanded = false;
      
      private ValueAnimator sizeAnimator;
      private ValueAnimator alphaAnimator;
      
      public ViewHolder(View v) {
        super(v);
        v.setOnClickListener(this);
      }

      @Override public void onClick(
          final View view) {
        if (mOriginalHeight == 0) {
          mOriginalHeight = view.getHeight();
        }
        if (!mIsViewExpanded) {
          mIsViewExpanded = true;
          sizeAnimator = ValueAnimator.ofInt((int) (mOriginalHeight
              * 0.1), mOriginalHeight);
          alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        } else {
          mIsViewExpanded = false;
          sizeAnimator = ValueAnimator.ofInt(mOriginalHeight, (int) (mOriginalHeight
              * 0.1));
          alphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        }
        sizeAnimator.setDuration(300);
        alphaAnimator.setDuration(300);
        sizeAnimator.setInterpolator(new LinearInterpolator());
        alphaAnimator.setInterpolator(new LinearInterpolator());
        sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(
              ValueAnimator animation) {
            Integer value = (Integer) animation.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();
            view.requestLayout();
          }
        });
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          
          @Override public void onAnimationUpdate(
              ValueAnimator animation) {
            Float value = (Float) animation.getAnimatedValue();
            view.setAlpha(value);
          }
        });
        sizeAnimator.start();
        alphaAnimator.start();
      }
    }
    
    @Override public int getItemCount() {
      return skills.size();
    }

    @Override public void onBindViewHolder(
        ViewHolder holder, int position) {
      Skill skill = skills.get(position);
      TextView tv = ((TextView) holder.itemView.findViewById(android.R.id.text1));
      tv.setText(skill.toString());
    }

    @Override public ViewHolder onCreateViewHolder(
        ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simple_textview, parent, false);
      return new SkillAdapter.ViewHolder(v);
    }
  }
  
  public static class ItemClickListener implements RecyclerView.OnItemTouchListener {
    
    private OnItemClickListener mListener;
    
    public interface OnItemClickListener {
      public void onItemClick(
          View view, int position);
    }
    
    private final GestureDetector mGestureDetector;
    
    public ItemClickListener(Context context, OnItemClickListener listener) {
      mListener = listener;
      SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override public boolean onSingleTapUp(
            MotionEvent e) {
          return true;
        }
      };
      mGestureDetector = new GestureDetector(context, gestureListener);
    }
    
    @Override public void onTouchEvent(
        RecyclerView view,
        MotionEvent e) {}
    
    @Override public boolean onInterceptTouchEvent(
        RecyclerView view,
        MotionEvent e) {
      View childViewUnder = view.findChildViewUnder(e.getX(), e.getY());
      if (childViewUnder != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
        int position = view.getChildPosition(childViewUnder);
        mListener.onItemClick(childViewUnder, position);
      }
      return false;
    }
  } 
}