package com.ameron32.apps.projectbanditv3.object;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ameron32.apps.projectbanditv3.view.MultiSelectSpinner;
import com.ameron32.apps.projectbanditv3.R;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@ParseClassName("Item") 
public class Item
  extends AbsBanditObject<AbsBanditObject.Column>
{
  
  private static final String TAG = Item.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = true;
  
  String name;
  int baseValue;
  String type;
  
  boolean isWeapon;
  List<String> weaponSlots;
  int[] weaponDamages;
  
  boolean isAmmo;
  int[] ammoDamages;
  String weaponType;
  
  boolean isArmor;
  List<String> armorSlots;
  int[] resistances;
  
  boolean isDurable;
  int durabilityUses;
  
  boolean isConsumable;
  
  boolean isIngredient;
  
  private static Item makeItem(
      Item.Builder builder,
      SaveCallback callback) {
    if (callback == null) {
      callback = new SaveCallback() {
        
        @Override public void done(
            ParseException e) {
          if (e == null) {
            if (LOG)
              Log.i(TAG, "Item saved.");
          } else {
            e.printStackTrace();
          }
        }
      };
    }

    Item item = new Item();
    item.loadItem(builder);
    item.saveInBackground(callback);
    return item;
  }
  
  private void loadItem(
      Item.Builder builder) {
    final String typeName = builder.type.name();

    setName(builder.name);
    setBaseValue(builder.baseValue);
    setType(typeName);
    setWeapon(builder.isWeapon);
    setWeaponSlots(builder.weaponSlots);
    setWeaponDamages(builder.weaponDamages);
    setAmmo(builder.isAmmo);
    setAmmoDamages(builder.ammoDamages);
    setWeaponType(builder.weaponType);
    setArmor(builder.isArmor);
    setArmorSlots(builder.armorSlots);
    setResistances(builder.resistances);
    setDurable(builder.isDurable);
    setDurabilityUses(builder.durabilityUses);
    setConsumable(builder.isConsumable);
    setIngredient(builder.isIngredient);
  }
  
  public Item() {
    // required ParseObject constructor
  }

  public void setName(String name) {
    this.name = name;
    this.put("name", name);
  }
  
  public void setBaseValue(int baseValue) {
    this.baseValue = baseValue;
    this.put("baseValue", baseValue);
  }
  
  public void setType(String type) {
    this.type = type;
    this.put("type", type);
  }
  
  public void setWeapon(boolean isWeapon) {
    this.isWeapon = isWeapon;
    this.put("isWeapon", isWeapon);
  }
  
  public void setWeaponSlots(
      List<String> weaponSlots) {
    this.weaponSlots = weaponSlots;
    this.put("weaponSlots", weaponSlots);
  }
  
  public void setWeaponDamages(
      int[] weaponDamages) {
    this.weaponDamages = weaponDamages;
    // this.put("weaponDamages", weaponDamages);
    this.put("weaponDamages", weaponDamages);
  }
  
  public void setAmmo(boolean isAmmo) {
    this.isAmmo = isAmmo;
    this.put("isAmmo", isAmmo);
  }
  
  public void setAmmoDamages(
      int[] ammoDamages) {
    this.ammoDamages = ammoDamages;
    // this.put("ammoDamages", ammoDamages);
    this.put("ammoDamages", ammoDamages);
  }
  
  public void setWeaponType(
      String weaponType) {
    this.weaponType = weaponType;
    this.put("weaponType", weaponType);
  }
  
  public void setArmor(boolean isArmor) {
    this.isArmor = isArmor;
    this.put("isArmor", isArmor);
  }
  
  public void setArmorSlots(
      List<String> armorSlots) {
    this.armorSlots = armorSlots;
    this.put("armorSlots", armorSlots);
  }

  public void setResistances(
      int[] resistances) {
    this.resistances = resistances;
    this.put("resistances", resistances);
  }
  
  public void setDurable(
      boolean isDurable) {
    this.isDurable = isDurable;
    this.put("isDurable", isDurable);
  }
  
  public void setDurabilityUses(
      int durabilityUses) {
    this.durabilityUses = durabilityUses;
    this.put("durabilityUses", durabilityUses);
  }
  
  public void setConsumable(
      boolean isConsumable) {
    this.isConsumable = isConsumable;
    this.put("isConsumable", isConsumable);
  }
  
  public void setIngredient(
      boolean isIngredient) {
    this.isIngredient = isIngredient;
    this.put("isIngredient", isIngredient);
  }
  
  public enum Type {
    Item, Weapon, Armor,
    //
    Ingredient, Ammo, Consumable,
    //
    Setreference_item, Setreference_weapon, Setreference_armor,
    //
    Setreference_ingredient, Setreference_ammo, Setreference_consumable;
    
    public static Item.Type valueOfIgnoreCase(
        String type) {
      type = type.toLowerCase(Locale.US);
      char[] typeArray = type.toCharArray();
      typeArray[0] = java.lang.Character.toUpperCase(typeArray[0]);
      return valueOf(String.valueOf(typeArray));
    }
    
    public static Item.Type valueOfSetIgnoreCase(String type) {
      type = type.toLowerCase(Locale.US);
      char[] typeArray = type.toCharArray();
      type = String.valueOf(typeArray);
      return valueOf("Setreference_" + type);
    }
    
    public static String[] nameValues() {
      Item.Type[] values = values();
      String[] nameValues = new String[values.length];
      for (int i = 0; i < values.length; i++) {
        nameValues[i] = values[i].name();
      }
      return nameValues;
    }
  }
  
  public enum WeaponType {
    Hammer, Axe, Dagger, Mace, Sword,
    //
    Bow;
    
    public static Item.WeaponType valueOfIgnoreCase(String type) {
      type = type.toLowerCase(Locale.US);
      char[] typeArray = type.toCharArray();
      typeArray[0] = java.lang.Character.toUpperCase(typeArray[0]);
      return valueOf(String.valueOf(typeArray));
    }
    
    public static String[] nameValues() {
      WeaponType[] values = WeaponType.values();
      String[] nameValues = new String[values.length];
      for (int i = 0; i < values.length; i++) {
        nameValues[i] = values[i].name();
      }
      return nameValues;
    }
  }
  
  private void put(String key,
      int[] oldArray) {
    if (oldArray != null) {
      List<Integer> newList = new ArrayList<Integer>();
      
      // convert int[] to Integer[]
      for (int ctr = 0; ctr < oldArray.length; ctr++) {
        newList.add(Integer.valueOf(oldArray[ctr]));
      }
      super.addAll(key, newList);
    }
  }
  
  private void put(String key,
      List<String> value) {
    if (value != null) {
      this.put(key, new JSONArray(value));
    }
  }

  private void put(String key,
      String value) {
    if (value != null) {
      super.put(key, value);
    }
  }
  
  private static final AbsBanditObject.Column[] COLUMNS = { 
    new Column("name", DataType.String),
    new Column("baseValue", DataType.Integer),
    new Column("type", DataType.String),
    new Column("usableInGame", DataType.Relation) };

  @Override public AbsBanditObject.Column get(
      int columnPosition) {
    return COLUMNS[columnPosition];
  }

  @Override public int getColumnCount() {
    return COLUMNS.length;
  }

  public static class Builder {
    
    public static Builder getNewSetReference(
        Type referenceType) {
      Builder setBuilder = new Builder();
      setBuilder.setType(Item.Type.valueOfIgnoreCase("Setreference_"
          + referenceType.name()));
      setBuilder.setSubType(referenceType);
      return setBuilder;
    }
  
    public static Builder getNewItem(
        Item.Type type) {
      Builder builder = new Builder().setType(type).setSubType(type);
      return builder;
    }
  
    private static final int NUMBER_OF_DAMAGE_TYPES = 10;
    
    String name;
    int baseValue;
    Item.Type type;
    
    boolean isWeapon;
    List<String> weaponSlots;
    int[] weaponDamages;
    
    boolean isAmmo;
    int[] ammoDamages;
    String weaponType;
    
    boolean isArmor;
    List<String> armorSlots;
    int[] resistances;
    
    boolean isDurable;
    int durabilityUses;
    
    boolean isConsumable;
    
    boolean isIngredient;
    
    // public static Builder start() {
    // return new Builder();
    // }
    
    private Builder() {}
    
    private View rootView;
  
    public Builder from(View rootView) {
      this.rootView = rootView;
      return this;
    }
     
    
    public Builder loadView()
        throws NumberFormatException {
      setName(sFromET(R.id.et_name));
      setBaseValue(iFromET(R.id.et_base_value));
      // setType(type);
      setWeaponDamages(iaFromET(R.id.et_weapon_damages));
      setAmmoDamages(iaFromET(R.id.et_ammo_damages));
      setResistances(iaFromET(R.id.et_resistances));
      setDurable(isDurable);
      setDurabilityUses(iFromET(R.id.et_durability));
      
      // TODO setConsumable()
      String ammoWeaponType = sFirstFromMSS(R.id.mss_ammo_weapons);
      String meleeWeaponType = sFirstFromMSS(R.id.mss_weapon_type);
      String weaponType = (meleeWeaponType == null) ? ammoWeaponType : meleeWeaponType;
      setWeaponType(weaponType);
      setArmorSlots(lsFromMSS(R.id.mss_armor_slots));
      return this;
    }
    
    
    
    private String sFromET(int res) {
      EditText editText = (EditText) rootView.findViewById(res);
      return editText.getText().toString();
    }
    
    private int iFromET(int res) {
      EditText editText = (EditText) rootView.findViewById(res);
      String input = editText.getText().toString();
      if (input.length() < 1) { return 0; }
      return Integer.decode(input);
    }
    
    private int[] iaFromET(int res) {
      int[] results = new int[NUMBER_OF_DAMAGE_TYPES];
      
      EditText editText = (EditText) rootView.findViewById(res);
      // sanitize damage types
      String toClean = editText.getText().toString();
      // if (LOG)
      // Log.i(TAG, "toClean: "
      // + toClean);
      String[] split = toClean.split(",");
      for (int i = 0; i < split.length; i++) {
        String section = split[i];
        section = section.trim();
        if (section.length() == 0) {
          break;
        }
        // distribute
        String distribute = section.substring(0, 1).toLowerCase(Locale.US);
        int amount = Integer.decode(section.substring(1));
        int location = -1;
        // if (LOG)
        // Log.i(TAG, "distribute: "
        // + distribute);
        // if (LOG)
        // Log.i(TAG, "distribute: "
        // + distribute);
        if (distribute.equalsIgnoreCase("s")) {
          location = 0;
        }
        if (distribute.equalsIgnoreCase("f")) {
          location = 1;
        }
        if (distribute.equalsIgnoreCase("i")) {
          location = 2;
        }
        if (distribute.equalsIgnoreCase("p")) {
          location = 3;
        }
        if (distribute.equalsIgnoreCase("l")) {
          location = 4;
        }
        if (distribute.equalsIgnoreCase("m")) {
          location = 5;
        }
        if (distribute.equalsIgnoreCase("h")) {
          location = 6;
        }
        if (distribute.equalsIgnoreCase("d")) {
          location = 7;
        }
        if (distribute.equalsIgnoreCase("x")) {
          location = 8;
        }
        if (distribute.equalsIgnoreCase("y")) {
          location = 9;
        }
        
        // populate results
        results[location] = amount;
      }
      // if (LOG) {
      //
      // StringBuilder sb = new StringBuilder().append("results: ");
      // for (int r : results) {
      // sb.append(r + " ");
      // }
      // // Log.i(TAG, "distribute: "
      // // + sb.toString());
      // }
      return results;
    }
    
    private String sFirstFromMSS(int res) {
      List<String> selectedStrings = lsFromMSS(res);
      if (selectedStrings != null
          && selectedStrings.size() > 0) { return selectedStrings.get(0); }
      return null;
    }
    
    private List<String> lsFromMSS(
        int res) {
      MultiSelectSpinner mss = (MultiSelectSpinner) rootView.findViewById(res);
      if (mss != null) { return mss.getSelectedStrings(); }
      return null;
    }
     
  
    public Builder multiplyCost(
        float variant) {
      setBaseValue(modify(baseValue, variant));
      return this;
    }
  
    public Builder multiplyValue(
        float variant) {
      if (this.isWeapon) {
        setWeaponDamages(modifyArray(weaponDamages, variant));
      }
      if (this.isAmmo) {
        setAmmoDamages(modifyArray(ammoDamages, variant));
      }
      if (this.isArmor) {
        setResistances(modifyArray(resistances, variant));
      }
      return this;
    }
  
    private int[] modifyArray(
        int[] array, float variant) {
      for (int i = 0; i < array.length; i++) {
        array[i] = modify(array[i], variant);
      }
      return array;
    }
  
    private int modify(int value,
        float variant) {
      float target = value;
      target = target * variant;
      return Math.round(target);
    }
  
    public Builder setName(String name) {
      this.name = name;
      return this;
    }
    
    public Builder setBaseValue(
        int baseValue) {
      this.baseValue = baseValue;
      return this;
    }
    
    protected Builder setType(
        Item.Type type) {
      this.type = type;
      return this;
    }
    
    protected Builder setSubType(
        Item.Type type) {
      setWeapon(type == Type.Weapon);
      setArmor(type == Type.Armor);
      setAmmo(type == Type.Ammo);
      setIngredient(type == Type.Ingredient);
      return this;
    }
    
    private Builder setWeapon(
        boolean isWeapon) {
      this.isWeapon = isWeapon;
      return this;
    }
    
    public Builder setWeaponSlots(
        List<String> strings) {
      if (this.isWeapon) {
        this.weaponSlots = strings;
      }
      return this;
    }
    
    public Builder setWeaponDamages(
        int[] weaponDamages) {
      if (this.isWeapon) {
        this.weaponDamages = weaponDamages;
      }
      return this;
    }
    
    private Builder setAmmo(
        boolean isAmmo) {
      this.isAmmo = isAmmo;
      this.isDurable = true;
      this.durabilityUses = 1;
      return this;
    }
    
    public Builder setAmmoDamages(
        int[] ammoDamages) {
      if (this.isAmmo) {
        this.ammoDamages = ammoDamages;
      }
      return this;
    }
    
    public Builder setWeaponType(
        String weaponType) {
      if (this.isAmmo || this.isWeapon) {
        this.weaponType = weaponType;
      }
      return this;
    }
    
    private Builder setArmor(
        boolean isArmor) {
      this.isArmor = isArmor;
      return this;
    }
    
    public Builder setArmorSlots(
        List<String> strings) {
      if (this.isArmor) {
        this.armorSlots = strings;
      }
      return this;
    }
  
    public Builder setResistances(
        int[] resistances) {
      if (this.isArmor) {
        this.resistances = resistances;
      }
      return this;
    }
    
    private Builder setDurable(
        boolean isDurable) {
      if (this.isWeapon || this.isArmor) {
        this.isDurable = isDurable;
      }
      return this;
    }
    
    public Builder setDurabilityUses(
        int durabilityUses) {
      if (this.isWeapon || this.isArmor) {
        if (durabilityUses > 0) {
          this.durabilityUses = durabilityUses;
          setDurable(true);
        } else {
          this.durabilityUses = 0;
          setDurable(false);
        }
      }
      return this;
    }
    
    public Builder setConsumable(
        boolean isConsumable) {
      this.isConsumable = isConsumable;
      this.isDurable = true;
      this.durabilityUses = 1;
      return this;
    }
    
    private Builder setIngredient(
        boolean isIngredient) {
      this.isIngredient = isIngredient;
      return this;
    }
    
    public Item create() {
      return create(null);
    }
    
    public Item create(
        SaveCallback callback) {
      rootView = null;
      return Item.makeItem(this, callback);
    }
  }
}
