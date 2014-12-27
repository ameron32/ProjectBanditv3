package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.ameron32.apps.projectbanditv3.view.MultiSelectSpinner;
import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class IssueItemFragment extends
    AbsResettingContentFragment
    implements
    AbsResettingContentFragment.OnPerformTaskListener,
    AbsResettingContentFragment.TaskWorker {
  
  private MultiSelectSpinner itemSpinner;
  private MultiSelectSpinner characterSpinner;
  
  private List<ParseObject> items;
  private List<ParseObject> characters;
  
  public IssueItemFragment() {}
  
  //
  // @Override
  // public View onCreateView(LayoutInflater inflater, ViewGroup container,
  // Bundle savedInstanceState) {
  // View rootView = inflater.inflate(R.layout.fragment_issue_item, container,
  // false);
  // prepareViews(rootView);
  // return rootView;
  // }
  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment_issue_item;
  }
  
  @InjectView(R.id.cb_local_game_item) CheckBox inGameItem;
  @InjectView(R.id.cb_local_game_character) CheckBox inGameCharacter;
  @InjectView(R.id.edittext_item_type) EditText itemTypeET;
  @InjectView(R.id.edittext_character) EditText characterET;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    prepareViews(view);
  }
  
  private void prepareViews(
      View rootView) {
    // EditText itemTypeET = (EditText)
    // rootView.findViewById(R.id.edittext_item_type);
    // EditText characterET = (EditText)
    // rootView.findViewById(R.id.edittext_character);
    
    itemSpinner = (MultiSelectSpinner) rootView.findViewById(R.id.mss_item_selector);
    characterSpinner = (MultiSelectSpinner) rootView.findViewById(R.id.mss_character_selector);
    
    OnFocusChangeListener listener = new OnFocusChangeListener() {
      
      @Override public void onFocusChange(
          View v, boolean hasFocus) {
        if (!hasFocus) {
          final int vId = v.getId();
          switch (vId) {
          case R.id.edittext_item_type:
            runItemQuery();
            break;
          case R.id.edittext_character:
            runCharacterQuery();
            break;
          }
        }
      }
    };
    
    itemTypeET.setOnFocusChangeListener(listener);
    characterET.setOnFocusChangeListener(listener);
  }
  
  private <T extends ParseObject> void runItemQuery() {
    String input = "";
    if (itemTypeET != null) {
      input = itemTypeET.getText().toString().trim();
    }
    ParseQuery<T> query = null;
    
    if (inGameItem != null && inGameItem.isChecked()) {
      query = (ParseQuery<T>) Query._Item.getItemQuery();
    } else {
      query = (ParseQuery<T>) Query._Item.getFullItemQuery();
    }
    
    if (input.length() != 0) {
      query.whereEqualTo("type", input);
    }
    
    query.findInBackground(new FindCallback<T>() {
      
      @Override public void done(
          List<T> objects,
          ParseException e) {
        if (e == null
            && (objects.size() > 0)) {
          List<String> names = new ArrayList<String>();
          for (int i = 0; i < objects.size(); i++) {
            ParseObject object = objects.get(i);
            names.add(object.getString("name"));
          }
          items = (List<ParseObject>) objects;
          itemSpinner.setItems(names);
        }
      }
    });
  }
  
  private <T extends ParseObject> void runCharacterQuery() {
    String input = "";
    if (characterET != null) {
      input = characterET.getText().toString().trim();
    }
    ParseQuery<T> query = null;
    
    if (inGameCharacter != null && inGameCharacter.isChecked()) {
      query = (ParseQuery<T>) Query._Character.getPlayableCharacters();
    } else {
      query = (ParseQuery<T>) Query._Character.getAllLibraryCharacters();
    }
    
    if (input.length() != 0) {
      query.whereEqualTo("name", input);
    }
    
    query.findInBackground(new FindCallback<T>() {
      
      @Override public void done(
          List<T> objects,
          ParseException e) {
        if (e == null
            && (objects.size() > 0)) {
          List<String> names = new ArrayList<String>();
          for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            names.add(object.getString("name"));
          }
          characters = (List<ParseObject>) objects;
          characterSpinner.setItems(names);
        }
      }
    });
  }
  
  public void issueItem() {
    if (itemSpinner != null
        && characterSpinner != null
        && items != null
        && characters != null) {
      final List<ParseObject> toSave = new ArrayList<ParseObject>();
      
      final List<Integer> selectedItems = itemSpinner.getSelectedIndicies();
      final List<Integer> selectedCharacters = characterSpinner.getSelectedIndicies();
      
      for (int i = 0; i < selectedItems.size(); i++) {
        int itemNumber = selectedItems.get(i);
        ParseObject itemObject = items.get(itemNumber);
        
        for (int j = 0; j < selectedCharacters.size(); j++) {
          int characterNumber = selectedCharacters.get(j);
          ParseObject characterObject = characters.get(characterNumber);
          
          // by default, this will generate a new item per character
          ParseObject inventoryObject = createCInventoryFromItem(itemObject, characterObject);
          toSave.add(inventoryObject);
        }
      }
      
      try {
        ParseObject.saveAll(toSave);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }
  
  protected ParseObject createCInventoryFromItem(
      ParseObject item,
      ParseObject character) {
    return CInventory.assignItemToCharacter(item, character, 1);
  }
  
  //
  // @Override
  // public void onCreate(Bundle savedInstanceState) {
  // super.onCreate(savedInstanceState);
  // mCallback = new FragmentCallback() {
  //
  // @Override
  // public void onSubmit() {
  // // TODO Auto-generated method stub
  //
  // }
  // };
  // };
  //
  // @Override
  // public void onAttach(Activity activity) {
  // super.onAttach(activity);
  // if (activity instanceof FragmentCallback) {
  // mCallback = (FragmentCallback) activity;
  // }
  // else {
  // throw new
  // IllegalStateException("activity must implement FragmentCallback");
  // }
  // }
  //
  // @Override
  // public void onDetach() {
  // mCallback = new FragmentCallback() {
  //
  // @Override
  // public void onSubmit() {
  // // TODO Auto-generated method stub
  //
  // }
  // };
  // super.onDetach();
  // }
  //
  // private FragmentCallback mCallback;
  //
  // public interface FragmentCallback {
  //
  // public void onSubmit();
  // }
  
  @Override public int provideClickViewId() {
    return R.id.button_issue_item;
  }
  
  @Override public OnPerformTaskListener provideOnPerformTaskListener() {
    return this;
  }
  
  @Override public TaskWorker provideTaskWorker() {
    return this;
  }
  
  @Override public void doTaskInBackground() {
    issueItem();
  }
  
  @Override public void onPrePerformTask() {
    ((Button) getView().findViewById(provideClickViewId())).setText("...Processing...");
  }
  
  @Override public void onPostPerformTask() {
    // TODO Auto-generated method stub
  }
}