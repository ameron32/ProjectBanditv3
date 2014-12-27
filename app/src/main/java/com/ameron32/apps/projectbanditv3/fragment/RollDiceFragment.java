package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;

import butterknife.ButterKnife;
import butterknife.OnClick;



public class RollDiceFragment extends AbsContentFragment {

	@Override
	protected int getCustomLayoutResource() {
		return R.layout.fragment_dice_roll;
	}
	
	@Override public void onViewCreated(
	    View view,
	    Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  ButterKnife.inject(this, view);
	}
	
	@OnClick(R.id.button_roll) void onRoll() {
	  Util.rollDice();
	}
	
	@Override public void onDestroyView() {
	  super.onDestroyView();
	  ButterKnife.reset(this);
	}

}
