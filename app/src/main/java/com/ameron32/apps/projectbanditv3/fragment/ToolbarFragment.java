package com.ameron32.apps.projectbanditv3.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.projectbanditv3.CharacterClickListener;
import com.ameron32.apps.projectbanditv3.CharacterClickListener.OnCharacterClickListener;
import com.ameron32.apps.projectbanditv3.adapter.CharacterSelectorAdapter;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager.OnCharacterChangeListener;
import com.ameron32.apps.projectbanditv3.object.Character;

public class ToolbarFragment 
    extends Fragment 
    implements OnCharacterChangeListener 
{
  
  private Toolbar mToolbar;
  private CharacterSelectorAdapter mAdapter;
  private RecyclerView mCharacterRecyclerView;
  private OnToolbarFragmentCallbacks mCallbacks;
  
  public static ToolbarFragment newInstance() {
    ToolbarFragment f = new ToolbarFragment();
    return f;
  }
  
  @Override public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    final Context context = getActivity();
    View v = inflater.inflate(R.layout.toolbar_default, container, false);
    mToolbar = (Toolbar) v.findViewById(R.id.toolbar_actionbar);
    View layout = LayoutInflater.from(context).inflate(R.layout.view_toolbar_character_recyclerview, mToolbar, false);
    mCharacterRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
    mToolbar.addView(mCharacterRecyclerView);
    mCallbacks.onToolbarCreated(mToolbar);
    return v;
  }
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    init();
  }
  
  private void init() {
    addCharacterIcons_v2(mToolbar);
  }
  
  private void addCharacterIcons_v2(
      Toolbar toolbar) {
    final Context context = getActivity();
    mCharacterRecyclerView.setHasFixedSize(true);
    
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    mCharacterRecyclerView.setLayoutManager(mLayoutManager);
    // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    
    mAdapter = new CharacterSelectorAdapter(context);
    mCharacterRecyclerView.setAdapter(mAdapter);
    mCharacterRecyclerView.addOnItemTouchListener(new CharacterClickListener(context, new OnCharacterClickListener() {
      
      @Override public void onCharacterClick(
          View view, int position) {
        mAdapter.setSelection(position);
      }
    }));
  }
  
  @Override public void onAttach(
      Activity activity) {
    super.onAttach(activity);
    if (activity instanceof OnToolbarFragmentCallbacks) {
      this.mCallbacks = (OnToolbarFragmentCallbacks) activity;
    } else {
      throw new IllegalStateException("activity must implement OnToolbarFragmentCallbacks");
    }
  }
  
  @Override public void onDetach() {
    mCallbacks = null;
    super.onDetach();
  }
  
  public interface OnToolbarFragmentCallbacks {
    public void onToolbarCreated(Toolbar toolbar);
  }
  
  @Override public void onResume() {
    super.onResume();
    CharacterManager.get().addOnCharacterChangeListener(this);
  }
  
  @Override public void onPause() {
    super.onPause();
    CharacterManager.get().removeOnCharacterChangeListener(this);
  }

  @Override public void onCharacterChange(
      CharacterManager manager,
      com.ameron32.apps.projectbanditv3.object.Character newCharacter) {
    // TODO When Characters change, Toolbar doesn't know
    
  }

  @Override public void onChatCharacterChange(
      CharacterManager manager,
      Character newCharacter) {
    // TODO Auto-generated method stub
    
  }
}
