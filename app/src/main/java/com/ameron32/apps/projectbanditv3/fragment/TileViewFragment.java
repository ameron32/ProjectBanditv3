package com.ameron32.apps.projectbanditv3.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ameron32.apps.projectbanditv3.R;
import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TileViewFragment extends AbsContentFragment
    implements View.OnTouchListener {

  @InjectView(R.id.tileview) TileView mTileView;
  private TileRevealView revealView;

  public TileViewFragment() {}

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

    try {
      Bitmap downsample = BitmapFactory.decodeStream(getActivity().getAssets().open("map.png"));
      ImageView downsampleView = new ImageView(getContext());
      downsampleView.setId(R.id.tileview);
      downsampleView.setImageBitmap(downsample);

      mTileView.setSize(7500 * 2, 4684 * 2);
      mTileView.addView(downsampleView, 0);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mTileView.setBitmapProvider(new BitmapProvider() {
      @Override
      public Bitmap getBitmap(Tile tile, Context context) {
        Object data = tile.getData();
        if (data instanceof String) {
          String unformattedFileName = (String) tile.getData();
          String formattedFileName = String.format(unformattedFileName, tile.getColumn(), tile.getRow());
          try {
            return Picasso.with(context).load(formattedFileName).get();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        return null;
      }
    });

    mTileView.addDetailLevel( 0.5000f , "https://dl.dropboxusercontent.com/u/949753/android/tileview/jhara/tiles/500_%d_%d.png");
    mTileView.addDetailLevel( 0.2500f , "https://dl.dropboxusercontent.com/u/949753/android/tileview/jhara/tiles/250_%d_%d.png");
    mTileView.addDetailLevel( 0.1250f , "https://dl.dropboxusercontent.com/u/949753/android/tileview/jhara/tiles/125_%d_%d.png");

    revealView = new TileRevealView(getContext());
    mTileView.addScalingViewGroup(revealView);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    mTileView.onTouchEvent(event);
    return false;
  }

  @Override
  protected int getCustomLayoutResource() {
    return R.layout.fragment_tileview;
  }

  @Override
  public void onDestroyView() {
    super.onDestroy();
    ButterKnife.reset(this);
    mTileView.destroy();
    mTileView = null;
  }

  public TileView getTileView() {
    return mTileView;
  }

  /**
   * This is a convenience method to moveToAndCenter after layout (which won't happen if called directly in onCreate
   * see https://github.com/moagrius/TileView/wiki/FAQ
   */
  public void frameTo(final double x, final double y) {
    getTileView().post(new Runnable() {
      @Override
      public void run() {
        getTileView().scrollToAndCenter(x, y);
      }
    });
  }
}
