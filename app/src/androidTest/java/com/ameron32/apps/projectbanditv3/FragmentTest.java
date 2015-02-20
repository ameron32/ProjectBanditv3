package com.ameron32.apps.projectbanditv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.test.ActivityUnitTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.manager.CommunicationPipeline;
import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 1/27/2015.
 */
public class FragmentTest extends ActivityUnitTestCase<FragmentTest.MyActivity> {

  public static class MyActivity extends FragmentActivity {
  }

  public static class FragmentUnderTest extends CommunicationPipeline {
    public boolean created;
    public boolean started;
    public boolean viewCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      created = true;
    }

    @Override
    public void onStart() {
      super.onStart();
      started = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      TextView text = new TextView(getActivity());
      text.setId(android.R.id.text1);
      text.setText("hoge");
      return text;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      viewCreated = true;
    }
  }

  FragmentManager mFragmentManager;

  public FragmentTest() {
    super(MyActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startActivity(new Intent(Intent.ACTION_MAIN), null, null);
    mFragmentManager = getActivity().getSupportFragmentManager();

    Parse.initialize(getActivity(),
        getActivity().getResources().getString(R.string.parse_application_id),
        getActivity().getResources().getString(R.string.parse_client_key));
  }

  public void testFragment() throws Exception {
    mFragmentManager.beginTransaction()
        .replace(android.R.id.content, new FragmentUnderTest())
        .commit();
    mFragmentManager.executePendingTransactions();

    FragmentUnderTest underTest = (FragmentUnderTest) mFragmentManager.findFragmentById(android.R.id.content);

    assertNotNull(underTest);

    getInstrumentation().callActivityOnStart(getActivity());
    getInstrumentation().callActivityOnResume(getActivity());
    mFragmentManager.executePendingTransactions();

    assertTrue(underTest.created);
    assertTrue(underTest.started);
    assertTrue(underTest.viewCreated);
    assertEquals("hoge", TextView.class.cast(underTest.getView()
        .findViewById(android.R.id.text1))
        .getText());

//    underTest.example();
  }

  public void testMessages() throws Exception {
    try {
      final List<ParseObject> objects = ParseQuery.getQuery("Message").find();
      assertTrue(objects.size() == 100);
    } catch (com.parse.ParseException e) {
      e.printStackTrace();
    }
  }
}
