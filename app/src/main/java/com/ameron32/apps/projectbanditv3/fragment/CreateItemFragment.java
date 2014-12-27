package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.view.MultiSelectSpinner;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.Item;
import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class CreateItemFragment 
    extends AbsResettingContentFragment
    implements AbsResettingContentFragment.OnPerformTaskListener, AbsResettingContentFragment.TaskWorker 
{
  
  private static final String TAG = CreateItemFragment.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = true;
  
  // private static final String[] ITEM_TYPES = { "Item", "Weapon", "Armor",
  // "Ingredient", "Ammo", "Consumable" };
  private static final String[] WEAPON_SLOTS = {
      "None", "Off-Hand", "MainHand",
      "Back Weapon", "Ammo" };
  private static final String[] AMMO_WEAPONS = {
      "None", "Bow", "Crossbow" };
  private static final String[] WEAPON_TYPES = Item.WeaponType.nameValues();
  // private static final String[] ARMOR_SLOTS = { "None", "Head", "Neck",
  // "Chest", "Back", "Shoulder", "Arm",
  // "Wrist", "Hand", "Left Finger", "Right Finger", "Belt", "Lower Torso",
  // "Shin", "Feet" };
  
  private View mRootView;
  
  // private OnFragmentFinishListener callback;
  
  public CreateItemFragment() {}
  
//  @Override public View onCreateView(
//      LayoutInflater inflater,
//      ViewGroup container,
//      Bundle savedInstanceState) {
//    mRootView = inflater.inflate(R.layout.fragment_create_item, container, false);
//  }
  
  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment_create_item;
  }
  
  @InjectView(R.id.cb_verify) CheckBox addToGame;
  
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    mRootView = view;
    
    Spinner spinnerItemTypes = (Spinner) mRootView.findViewById(R.id.s_item_type);
    String[] itemTypes = getResources().getStringArray(R.array.rules_item_types);
    spinnerItemTypes.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemTypes));
    spinnerItemTypes.setOnItemSelectedListener(new OnItemSelectedListener() {
      
      @Override public void onItemSelected(
          AdapterView<?> parent,
          View view, int position,
          long id) {
        hideElements();
        String[] itemTypes = getResources().getStringArray(R.array.rules_item_types);
        String type = itemTypes[position];
        if (type.equalsIgnoreCase("Weapon")) {
          unhideElement(R.id.ll_weapon_elements, R.id.ll_durability_elements);
        }
        if (type.equalsIgnoreCase("Armor")) {
          unhideElement(R.id.ll_armor_elements, R.id.ll_durability_elements);
        }
        if (type.equalsIgnoreCase("Ingredient")) {
          unhideElement(R.id.ll_ingredient_elements);
        }
        if (type.equalsIgnoreCase("Ammo")) {
          unhideElement(R.id.ll_ammo_elements);
        }
        if (type.equalsIgnoreCase("Item")) {
          hideElements();
        }
      }
      
      @Override public void onNothingSelected(
          AdapterView<?> parent) {
        hideElements();
      }
    });
    
    MultiSelectSpinner spinnerWeaponSlots = (MultiSelectSpinner) mRootView.findViewById(R.id.mss_weapon_slots);
    spinnerWeaponSlots.setItems(WEAPON_SLOTS);
    spinnerWeaponSlots.setSelectedIndex(0);
    
    MultiSelectSpinner spinnerArmorSlots = (MultiSelectSpinner) mRootView.findViewById(R.id.mss_armor_slots);
    spinnerArmorSlots.setItems(getResources().getStringArray(R.array.rules_armor_slots));
    spinnerArmorSlots.setSelectedIndex(0);
    
    MultiSelectSpinner spinnerAmmoTypes = (MultiSelectSpinner) mRootView.findViewById(R.id.mss_ammo_weapons);
    spinnerAmmoTypes.setItems(AMMO_WEAPONS);
    spinnerAmmoTypes.setSelectedIndex(0);
    
    MultiSelectSpinner weaponTypes = (MultiSelectSpinner) mRootView.findViewById(R.id.mss_weapon_type);
    weaponTypes.setItems(WEAPON_TYPES);
    weaponTypes.setSelectedIndex(0);
    
    // mRootView.findViewById(R.id.b_submit).setOnClickListener(new
    // View.OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    //
    // }
    // });
    
    hideElements();
  }
  
  private void hideElements() {
    mRootView.findViewById(R.id.ll_weapon_elements).setVisibility(View.GONE);
    mRootView.findViewById(R.id.ll_ammo_elements).setVisibility(View.GONE);
    mRootView.findViewById(R.id.ll_armor_elements).setVisibility(View.GONE);
    mRootView.findViewById(R.id.ll_ingredient_elements).setVisibility(View.GONE);
    mRootView.findViewById(R.id.ll_durability_elements).setVisibility(View.GONE);
  }
  
  private void unhideElement(
      int... resources) {
    for (int res : resources) {
      mRootView.findViewById(res).setVisibility(View.VISIBLE);
    }
  }
  
  
  @Override public int provideClickViewId() {
    return R.id.b_submit;
  }
  
  @Override public OnPerformTaskListener provideOnPerformTaskListener() {
    return this;
  }
  
  @Override public TaskWorker provideTaskWorker() {
    return this;
  };
  
  @Override public void doTaskInBackground() {
    try {
      Spinner spinner = (Spinner) mRootView.findViewById(R.id.s_item_type);
      String string = (String) spinner.getSelectedItem();
      Item.Type type = Item.Type.valueOfIgnoreCase(string);
      Item.Builder builder = Item.Builder.getNewItem(type).from(mRootView).loadView();
      Item createdItem = builder.create();
      if (addToGame.isChecked()) {
        Util.addItemToGame(createdItem, GameManager.get().getCurrentGame());
      }
      createdItem.save();
      if (LOG)
        Log.i(TAG, "item created & saved.");
    } catch (ParseException e) {
      e.printStackTrace();
      if (LOG)
        Log.i(TAG, "item save failed. ParseException");
    } catch (NumberFormatException e) {
      String eMessage = "Invalid number format (somewhere)";
      if (LOG)
        Log.i(TAG, "item save failed. "
            + eMessage);
      if (TOAST)
        Toast.makeText(getActivity(), eMessage, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override public void onPrePerformTask() {
    ((Button) getView().findViewById(provideClickViewId())).setText("...Processing...");
  }
  
  @Override public void onPostPerformTask() {
    // TODO Auto-generated method stub
    
  }
}
