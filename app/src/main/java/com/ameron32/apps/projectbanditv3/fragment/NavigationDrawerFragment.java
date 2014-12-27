package com.ameron32.apps.projectbanditv3.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.adapter.ContentAdapter;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.ContentManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NavigationDrawerFragment
    extends Fragment
    implements
    ContentManager.OnContentChangeListener,
    CharacterManager.OnCharacterChangeListener {
  private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
  private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
  private static final String PREFERENCES_FILE = "my_app_settings";
  // TODO: change this to your file
  
  private NavigationDrawerCallbacks mCallbacks;
  private RecyclerView mDrawerList;
  private Toolbar mToolbar;
  private View mFragmentContainerView;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mActionBarDrawerToggle;
  private boolean mUserLearnedDrawer;
  private boolean mFromSavedInstanceState;
  private int mCurrentSelectedPosition;
  
  @InjectView(R.id.imageview_character_image_full_size) ImageView characterImage;
  @InjectView(R.id.textview_character_navtitle) TextView characterTitle;
  @InjectView(R.id.textview_game_navtitle) TextView gameTitle;
  
  
  @Override public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(getFragmentLayoutResourceId(), container, false);
    ButterKnife.inject(this, view);
    mDrawerList = findRecyclerViewWithin(view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mDrawerList.setLayoutManager(layoutManager);
    mDrawerList.setHasFixedSize(true);
    
    ContentAdapter adapter = createAdapter();
    mDrawerList.setAdapter(adapter);
    
    com.ameron32.apps.projectbanditv3.object.Character currentCharacter = CharacterManager.get().getCurrentCharacter();
    setCharacterImageAndText(currentCharacter);
    setGameText(GameManager.get().getCurrentGame());
    
    selectItem(mCurrentSelectedPosition);
    return view;
  }

  @Override public void onCreate(
      Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
    if (savedInstanceState != null) {
      mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
      mFromSavedInstanceState = true;
    }
  }
  
  @Override public void onAttach(
      Activity activity) {
    super.onAttach(activity);
    try {
      mCallbacks = (NavigationDrawerCallbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
    }
  }
  
  @Override public void onPause() {
    super.onPause();
    ContentManager.get().removeOnContentChangeListener(this);
    CharacterManager.get().removeOnCharacterChangeListener(this);
  }
  
  @Override public void onResume() {
    super.onResume();
    ContentManager.get().addOnContentChangeListener(this);
    CharacterManager.get().addOnCharacterChangeListener(this);
  }
  
  public ActionBarDrawerToggle getActionBarDrawerToggle() {
    return mActionBarDrawerToggle;
  }
  
  public void setActionBarDrawerToggle(
      ActionBarDrawerToggle actionBarDrawerToggle) {
    mActionBarDrawerToggle = actionBarDrawerToggle;
  }
  
  public void setup(int fragmentId,
      DrawerLayout drawerLayout,
      Toolbar toolbar) {
    mToolbar = toolbar;
    mFragmentContainerView = getActivity().findViewById(fragmentId);
    mDrawerLayout = drawerLayout;
    mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
      @Override public void onDrawerClosed(
          View drawerView) {
        super.onDrawerClosed(drawerView);
        if (!isAdded()) return;
        // getActivity().invalidateOptionsMenu();
        
        getActivity().supportInvalidateOptionsMenu();
        // calls onPrepareOptionsMenu()
      }
      
      @Override public void onDrawerOpened(
          View drawerView) {
        super.onDrawerOpened(drawerView);
        if (!isAdded()) return;
        if (!mUserLearnedDrawer) {
          mUserLearnedDrawer = true;
          saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
        }
        
        // getActivity().invalidateOptionsMenu();
        
        getActivity().supportInvalidateOptionsMenu();
        // calls onPrepareOptionsMenu()
      }
    };
    
    if (!mUserLearnedDrawer
        && !mFromSavedInstanceState)
      mDrawerLayout.openDrawer(mFragmentContainerView);
    
    mDrawerLayout.post(new Runnable() {
      @Override public void run() {
        mActionBarDrawerToggle.syncState();
      }
    });
    
    mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    
    tintDrawerArrow();
    setDrawerButtonListeners();
  }
  
  private void tintDrawerArrow() {
    ImageButton upButton = (ImageButton) getActivity().findViewById(R.id.imagebutton_navigation_drawer_up_arrow);
    final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    upArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
    upButton.setImageDrawable(upArrow);
  }
  
  private void setDrawerButtonListeners() {
    ImageButton upButton = (ImageButton) getActivity().findViewById(R.id.imagebutton_navigation_drawer_up_arrow);
    upButton.setOnClickListener(new OnClickListener() {
      
      @Override public void onClick(
          View v) {
        closeDrawer();
      }
    });
    
    ImageButton logoutButton = (ImageButton) getActivity().findViewById(R.id.imagebutton_navigation_drawer_logout);
    logoutButton.setOnClickListener(new OnClickListener() {
      
      @Override public void onClick(
          View v) {
        mCallbacks.onLogoutClick();
      }
    });
  }
  
  public void openDrawer() {
    mDrawerLayout.openDrawer(mFragmentContainerView);
  }
  
  public void closeDrawer() {
    mDrawerLayout.closeDrawer(mFragmentContainerView);
  }
  
  @Override public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }
  
  private void selectItem(int position) {
    mCurrentSelectedPosition = position;
    if (mDrawerLayout != null) {
      mDrawerLayout.closeDrawer(mFragmentContainerView);
    }
    ((ContentAdapter) mDrawerList.getAdapter()).selectPosition(position);
  }
  
  public boolean isDrawerOpen() {
    return mDrawerLayout != null
        && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
  }
  
  @Override public void onConfigurationChanged(
      Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mActionBarDrawerToggle.onConfigurationChanged(newConfig);
  }
  
  @Override public void onSaveInstanceState(
      Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
  }
  
  //
  // @Override
  // public void onNavigationDrawerItemSelected(int position) {
  // mCallbacks.onNavigationDrawerItemSelected(position);
  // selectItem(position);
  // }
  
  public DrawerLayout getDrawerLayout() {
    return mDrawerLayout;
  }
  
  public void setDrawerLayout(
      DrawerLayout drawerLayout) {
    mDrawerLayout = drawerLayout;
  }
  
  public static void saveSharedSetting(
      Context ctx, String settingName,
      String settingValue) {
    SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(settingName, settingValue);
    editor.apply();
  }
  
  public static String readSharedSetting(
      Context ctx, String settingName,
      String defaultValue) {
    SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    return sharedPref.getString(settingName, defaultValue);
  }
  
  
  @Override public void onCreateOptionsMenu(
      Menu menu, MenuInflater inflater) {
    // If the drawer is open, show the global app actions in the action bar. See
    // also showGlobalContextActionBar, which controls the top-left area of the
    // action bar.
    if (mDrawerLayout != null
        && isDrawerOpen()) {
//      inflateGlobalMenu(menu, inflater);
    }
    // showGlobalContextActionBar();
    super.onCreateOptionsMenu(menu, inflater);
  }

  private void inflateGlobalMenu(Menu menu,
      MenuInflater inflater) {
    inflater.inflate(R.menu.global, menu);
  }
  
  @Override public void onActivityCreated(
      Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Indicate that this fragment would like to influence the set of actions in
    // the action bar.
    setHasOptionsMenu(true);
  }
  
  /**
   * Per the navigation drawer design guidelines, updates the action bar to show
   * the global app
   * 'context', rather than just what's in the current screen.
   */
  private void showGlobalContextActionBar() {
    Toolbar toolbar = mToolbar;
    // actionBar.setDisplayShowTitleEnabled(true);
    // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    // toolbar.setTitle(R.string.app_name);
    
    // restoreCharacterIcons(toolbar);
  }
  
  
  @Override public boolean onOptionsItemSelected(
      MenuItem item) {
    if (mActionBarDrawerToggle.onOptionsItemSelected(item)) { return true; }
    
    if (item.getItemId() == R.id.action_example) {
      Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
      return true;
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  protected int getFragmentLayoutResourceId() {
    return R.layout.fragment_navigation_drawer_recycler;
  }
  
  protected RecyclerView findRecyclerViewWithin(
      View fragmentView) {
    return (RecyclerView) fragmentView.findViewById(getRecyclerViewResourceId());
  }
  
  protected int getRecyclerViewResourceId() {
    return R.id.lv_navigation_list;
  }
  
  protected ContentAdapter createAdapter() {
    // return new ArrayAdapter<String>(getActivity(),
    // android.R.layout.simple_list_item_activated_1, android.R.id.text1, new
    // String[] {
    // getString(R.string.title_section1),
    // getString(R.string.title_section2),
    // getString(R.string.title_section3), });
    return ContentManager.get().getAdapter();
  }
  
  @Override public void onContentChange(
      ContentManager manager,
      int position) {
    selectItem(position);
  }
  
  @Override public void onCharacterChange(
      CharacterManager manager,
      Character newCharacter) {
    setCharacterImageAndText(newCharacter);
  }
  
  @Override public void onChatCharacterChange(
      CharacterManager manager,
      Character newCharacter) {
    // ignore
  }
  
  private void setCharacterImageAndText(
      Character newCharacter) {
    final String name = newCharacter.getString("name");
    final String urlFullSize = newCharacter.getUrlFullSize();

    characterTitle.setText(name);
    Picasso.with(getActivity()).load(urlFullSize).placeholder(R.drawable.ic_bandit_clear).into(characterImage);
  }
  
  private void setGameText(
      Game currentGame) {
    final String name = currentGame.getName();
    gameTitle.setText(name.toUpperCase(Locale.ENGLISH));
  }
  
  public interface NavigationDrawerCallbacks {
    public void onLogoutClick();
  }
}
