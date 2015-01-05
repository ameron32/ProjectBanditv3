package com.ameron32.apps.projectbanditv3.tileview;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ameron32.apps.projectbanditv3.R;

public class MainActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
//    if (savedInstanceState == null) {
//      getFragmentManager().beginTransaction().add(R.id.container, MyTileViewFragment.newInstance()).commit();
//    }
  }
  

  
  @Override
  public void onResume() {
    super.onResume();
//    test();
  }
  
  private void test() {
    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    String[] params = { 
        Environment.getExternalStorageDirectory().getPath() 
            + "/Download/wallpaper-1186854.jpg",
        Environment.getExternalStorageDirectory().getPath() 
            + "/Download/convert-1186854.jpg"
    };
    new ImageConverterTask(this, progressBar).execute(params);
  }
  
  @Override
  public void onPause() {
    super.onPause();
//    finish();
  }
  
  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    
    public PlaceholderFragment() {}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_tileview, container, false);
      return rootView;
    }
  }
  
}
