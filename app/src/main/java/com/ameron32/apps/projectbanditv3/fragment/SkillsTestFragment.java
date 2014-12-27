package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.adapter.ParseRecyclerQueryAdapter;
import com.ameron32.apps.projectbanditv3.R;
import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SkillsTestFragment
    extends
    SectionContainerTestFragment {
  
  @InjectView(R.id.rlist_skills) RecyclerView mRecyclerView;
  
  private LinearLayoutManager mLayoutManager;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
//    mLayoutManager.setStackFromEnd(true);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(new SkillAdapter("Skill"));
  }
  
  public static class SkillAdapter
      extends
      ParseRecyclerQueryAdapter<ParseObject, SkillAdapter.ViewHolder> {
    
    public SkillAdapter(String className) {
      super(className);
    }
    
    public static class ViewHolder
        extends RecyclerView.ViewHolder {
      @InjectView(R.id.textview_name) TextView name;
      @InjectView(R.id.textview_description) TextView description;
      @InjectView(R.id.textview_object_id) TextView objectId;
      @InjectView(R.id.textview_created_time) TextView createdTime;
      @InjectView(R.id.imageview_skill) ImageView skillImage;
      
      public ViewHolder(View v) {
        super(v);
        ButterKnife.inject(this, v);
      }
    }
    
    @Override public SkillAdapter.ViewHolder onCreateViewHolder(
        ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_skill_standard, parent, false);
      ViewHolder vh = new ViewHolder(v);
      return vh;
    }
    
    @Override public void onBindViewHolder(
        SkillAdapter.ViewHolder vh,
        int position) {
      ParseObject skill = getItem(position);
      String nameStr = skill.getString("name");
      String descriptionStr = skill.getString("description");
      String objectIdStr = skill.getObjectId();
      String createdAtStr = skill.getCreatedAt().toString();
      
      vh.name.setText(nameStr);
      vh.description.setText(descriptionStr);
      vh.objectId.setText(objectIdStr);
      vh.createdTime.setText(createdAtStr);
    }
  }
}
