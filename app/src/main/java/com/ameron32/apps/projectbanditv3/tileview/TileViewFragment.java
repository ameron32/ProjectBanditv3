package com.ameron32.apps.projectbanditv3.tileview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.fragment.AbsContentFragment;
import com.qozix.tileview.TileView;

public class TileViewFragment extends AbsContentFragment {
  
  private TileView mTileView;
  
  public TileViewFragment() {}
  
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;
        mTileView = new TileView(getActivity());
        mTileView.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        viewGroup.addView(mTileView, 0);
    }

    @Override
    protected int getCustomLayoutResource() {
        return R.layout.fragment_tileview;
    }

    @Override
  public void onPause() {
    super.onPause();
    mTileView.clear();
  }

  @Override
  public void onResume() {
    super.onResume();
    mTileView.resume();
  }

  @Override
  public void onDestroyView() {
    super.onDestroy();
    mTileView.destroy();
    mTileView = null;
  }

  public TileView getTileView(){
    return mTileView;
  }

  /**
   * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
   * see https://github.com/moagrius/TileView/wiki/FAQ
   */
  public void frameTo( final double x, final double y ) {
    getTileView().post( new Runnable() {
      @Override
      public void run() {
        getTileView().moveToAndCenter( x, y );
      }     
    });   
  }
}