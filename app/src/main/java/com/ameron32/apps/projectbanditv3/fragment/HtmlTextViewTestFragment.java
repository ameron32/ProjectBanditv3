package com.ameron32.apps.projectbanditv3.fragment;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.View;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by klemeilleur on 1/23/2015.
 */
public class HtmlTextViewTestFragment extends SectionContainerTestFragment {

  private static final int LAYOUT = R.layout.view_demo_textview_with_scroll;
  private static final int ID = android.R.id.text1;

  private String content;
  private TextView textView;

  @Override
  protected int onReplaceFragmentLayout(int storedLayoutResource) {
    return LAYOUT;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    textView = (TextView) view.findViewById(ID);
    textView.setText("Loading");

    task = new LoadTask();
    task.execute();
  }

  private LoadTask task;

  class LoadTask extends AsyncTask<Void, Void, Spanned> {

    @Override
    protected Spanned doInBackground(Void... params) {
      try {
        final String assetPath = "demo_content.txt";
        AssetManager m = getResources().getAssets();
        InputStream is = m.open(assetPath);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        // byte buffer into a string
        content = new String(buffer);
        content = "<html><body>" + content + "</body></html>";
      } catch (IOException e) {
        e.printStackTrace();
      }

      Spanned s = Html.fromHtml(content);
      return s;
    }

    @Override
    protected void onPostExecute(Spanned aVoid) {
      super.onPostExecute(aVoid);
      textView.setText(aVoid);
    }
  }
}
