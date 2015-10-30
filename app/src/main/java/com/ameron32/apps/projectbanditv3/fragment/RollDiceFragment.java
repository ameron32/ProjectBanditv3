package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.Game;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import me.himanshusoni.quantityview.QuantityView;


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

		setDefaultsFromGame();
	}

	private void setDefaultsFromGame() {
		final Game currentGame = GameManager.get().getCurrentGame();
		String defaultDice = currentGame.getDefaultDice().trim();
		Log.d("RollDice", defaultDice.toString());
		String[] diceSizes = defaultDice.split(",");
		Log.d("RollDice", Arrays.toString(diceSizes));
		for (String dieSize : diceSizes) {
			String[] pieces = dieSize.split(":");
			Log.d("RollDice", Arrays.toString(diceSizes));
			Dice die = Dice.valueOf(pieces[0]);
			int quantityOfDiceAtSize = Integer.valueOf(pieces[1]);

			setDieCount(die.ordinal(), quantityOfDiceAtSize);
		}
	}

	private void setDieCount(int position, int qty) {
		quantityViewDiceList.get(position).setQuantity(qty);
	}

	@InjectViews({R.id.qv_d4, R.id.qv_d6, R.id.qv_d8, R.id.qv_d10, R.id.qv_d12, R.id.qv_d20})
	List<QuantityView> quantityViewDiceList;
	@InjectView(R.id.qv_mod)
	QuantityView quantityViewMod;

	@OnClick(R.id.button_roll) void onRoll() {
		int[] dicePerSize = new int[6];
		int mod;
		for (int i = 0; i < quantityViewDiceList.size(); i++) {
			QuantityView qv = quantityViewDiceList.get(i);
			dicePerSize[i] = qv.getQuantity();
		}
		mod = quantityViewMod.getQuantity();

		int[][] keyValuePairs = new int[6][2];
		for (int i = 0; i < dicePerSize.length; i++) {
			keyValuePairs[i] = new int[] { getSizeFromPosition(i), dicePerSize[i] };
		}

	  Util.rollDice(keyValuePairs,mod);
	}

	private int getSizeFromPosition(int position) {
		switch(position) {
			case 0:
				return 4;
			case 1:
				return 6;
			case 2:
				return 8;
			case 3:
				return 10;
			case 4:
				return 12;
			case 5:
				return 20;
			default:
				return 6;
		}
	}

	@Override public void onDestroyView() {
	  super.onDestroyView();
	  ButterKnife.reset(this);
	}

	enum Dice {
		d4, d6, d8, d10, d12, d20;


	}
}
