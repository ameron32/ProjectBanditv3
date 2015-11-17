package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TableRowLayout extends LinearLayoutCompat {

	String TAG = TableRowLayout.class.getSimpleName();

	public TableRowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TableRowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TableRowLayout(Context context) {
		super(context);
	}

	// must initialize the column layout
	// setColumns()
	public void inflateColumns(int columnCount, int cellLayoutResource, ViewGroup parent) {
//		Log.d(TAG, "inflate new row");
		this.removeAllViews();

		// determine column weightsum
		float totalWeight = columnCount;
		// TODO: replace with calculated weightsum

		this.setWeightSum(totalWeight);

		float columnWeight = 1.0f;
		for (int i = 0; i < columnCount; i++) {
			// TODO: replace with calculated column weight

			View tempCellView = LayoutInflater.from(parent.getContext())
					.inflate(cellLayoutResource, parent, false);
			LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
			    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, columnWeight);
//			tempCellView.setLayoutParams();
			this.addView(tempCellView, lp);
		}
	}

	public void populateColumnTextView(int column, String columnInfo,
			int textViewResourceId) {
//		Log.d(TAG, "populate column");
		View tempCellView = this.getChildAt(column);
		TextView textView = (TextView) tempCellView
				.findViewById(textViewResourceId);
		textView.setText(columnInfo);
	}

	public View findCellViewUnder(View parentView, float x, float y) {
		return this.getChildAt(findCellPositionUnder(parentView, x, y));
	}

	public int findCellPositionUnder(View parentView, float x, float y) {
		final int childCount = this.getChildCount();

		int iX = Math.round(x);
		int iY = Math.round(y);

		for (int i = 0; i < childCount; i++) {
			View child = this.getChildAt(i);
			if (isViewContains(child, parentView, iX, iY)) {
				return i;
			}
		}

		return -1;
	}

	private boolean isViewContains(View view, View parentView, int rx, int ry) {
	    int[] l = new int[2];
	    view.getLocationOnScreen(l);
	    int x = l[0];
	    int y = l[1];
	    int w = view.getWidth();
	    int h = view.getHeight();

	    int[] k = new int[2];
	    parentView.getLocationOnScreen(k);
	    int oX = k[0];
	    int oY = k[1];

	    int roX = oX + rx;
	    int roY = oY + ry;

//	    Log.i("isViewContains", c(rx, tileCol, w, ry, tileRow, h));
//	    Log.i("isViewContains", c("<", tileCol, roX, tileCol + w));
//	    Log.i("isViewContains", c("<", tileRow, roY, tileRow + h));

	    if (roX < x || roX > x + w || roY < y || roY > y + h) {
	        return false;
	    }
	    return true;
	}
//
//	private String c(String a, int...ints) {
//		StringBuilder sb = new StringBuilder();
//		for (int i : ints) {
//			String s = String.valueOf(i);
//			sb.append("[");
//			sb.append(s);
//			sb.append("]");
//			sb.append(" ");
//			sb.append(a);
//			sb.append(" ");
//		}
//		return sb.toString();
//	}
}
