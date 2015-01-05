package com.ameron32.apps.projectbanditv3.tileview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ImageConverterTask extends AsyncTask<String[], Integer, String> {

  private static final int MAX_TIME_DELAY = 60; // seconds
  
  private final Context context;
  private final ProgressBar progressBar;
    
  public ImageConverterTask(Context context, ProgressBar progressBar) {
    super();
    this.context = context;
    this.progressBar = progressBar;
  }

  @Override
  protected String doInBackground(String[]... params) {
    
    for (int i = 0; i < MAX_TIME_DELAY; i++) {
      try {
        Thread.sleep(1 * 1000);
      }
      catch (InterruptedException e1) {
        e1.printStackTrace();
        return "fail";
      }
      
      publishProgress(i);
    }
    
    
//    try {
//      String[] param = params[0];
//      String inputImage = param[0];
//      String outputLocation = param[1];
//      
//      ImageInfo i = new ImageInfo(inputImage);
//      MagickImage m = new MagickImage(i);
//      
//      int newHeight = (int) ((640 / (float) m.getWidth()) * m.getHeight());
//      m = m.scaleImage(640, newHeight);
//      m = m.cropImage(new Rectangle((640 - 480) / 2, 0, 480, 480));
//      m = m.charcoalImage(0.5, 0.5);
//      
//      try {
//        byte blob[] = m.imageToBlob(i);
//        FileOutputStream fos = new FileOutputStream(new File(outputLocation));
//        fos.write(blob);
//        fos.close();
//      }
//      catch (Exception e) {
//        e.printStackTrace();
//        return "fail";
//      }
//    }
//    catch (MagickException e) {
//      e.printStackTrace();
//      return "fail";
//    }
    return "complete";
  }

  @Override
  protected void onPreExecute() {
    
    log("Start conversion...");
    if (progressBar == null) {
      log("progressBar == null");
      cancel(true);
    }
    else {
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setMax(MAX_TIME_DELAY);
      progressBar.setIndeterminate(false);
    }
    super.onPreExecute();
  }

  @Override
  protected void onPostExecute(String result) {
    
    if (result.equalsIgnoreCase("complete"))
      log("Conversion successful.");
    if (result.equalsIgnoreCase("fail"))
      log("Conversion complete with error(s).");
    
    if (progressBar == null) {
      log("progressBar == null");
      cancel(true);
    }
    else {
      progressBar.setVisibility(View.INVISIBLE);
    }
    super.onPostExecute(result);
  }

  @Override
  protected void onProgressUpdate(Integer... values) {
    
    if (progressBar == null) {
      log("progressBar == null");
      cancel(true);
    }
    else {
      progressBar.setProgress(values[0]);
    }
    super.onProgressUpdate(values);
  }

  @Override
  protected void onCancelled(String result) {
    
    if (result.equalsIgnoreCase("complete"))
      log("Conversion cancelled but successful.");
    if (result.equalsIgnoreCase("fail"))
      log("Conversion cancelled with error(s).");
    
    if (progressBar == null) {
      log("progressBar == null");
      cancel(true);
    }
    else {
      progressBar.setVisibility(View.INVISIBLE);
    }
    super.onCancelled(result);
  }
  
  private void log(String s) {
    Log.e("ImageConverterTask", s);
//    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
  }
  
}
