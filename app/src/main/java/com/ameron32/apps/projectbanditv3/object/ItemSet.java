package com.ameron32.apps.projectbanditv3.object;

import android.view.View;
import android.widget.Spinner;

import com.ameron32.apps.projectbanditv3.R;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import com.parse.starter.object.Item.Set.Builder;
//import com.parse.starter.object.Item.Set.Template;
//import com.parse.starter.object.Item.Set.Template.Piece;


public class ItemSet {
  
  public static class Template {
    final String templateName;
    List<Template.Piece> setPieces;
    
    public String getName() {
      return templateName;
    }
    
    public Template(
        String templateName) {
      this.templateName = templateName;
      setPieces = new ArrayList<Template.Piece>();
    }
    
    public ItemSet.Template add(String name,
        Item.Type type,

        float costVariant,
        float valueVariant,
        String... positions) {
      Template.Piece piece = new Piece(name, type, costVariant, valueVariant, positions);
      setPieces.add(piece);
      return this;
    }
    
    public ItemSet.Template addWeapon(String name,
        Item.Type type,
        Item.WeaponType weaponType,
        float costVariant,
        float valueVariant,
        String... positions) {
      Template.Piece piece = new Piece(name, type, costVariant, valueVariant, positions);
      piece.setWeaponType(weaponType);
      setPieces.add(piece);
      return this;
    }
    
    public static class Piece {
      String name;
      Item.Type type;
      String[] positions;
      float costVariant;
      float valueVariant;
      
      Item.WeaponType weaponType;
      
      public Piece(String name,
          Item.Type type,

          float costVariant,
          float valueVariant,
          String... positions) {
        super();
        this.name = name;
        this.type = type;
        this.positions = positions;
        this.costVariant = costVariant;
        this.valueVariant = valueVariant;
      }
      
      public Piece setWeaponType(Item.WeaponType weaponType) {
        this.weaponType = weaponType;
        return this;
      }
    }
  }
  
  private static final List<ItemSet.Template> templates = new ArrayList<ItemSet.Template>();
  static {
    templates.clear();
    
    ItemSet.Template lightFullSet = new ItemSet.Template("Standard_Light_Armor_FullSet");
    lightFullSet.add("Cap", Item.Type.Armor, 1.0f, 1.0f, "Head");
    lightFullSet.add("Vest", Item.Type.Armor, 1.7f, 2.0f, "Chest");
    lightFullSet.add("Pads", Item.Type.Armor, 1.5f, 1.5f, "Shoulder");
    lightFullSet.add("Cuffs", Item.Type.Armor, 0.5f, 1.0f, "Arm");
    lightFullSet.add("Bracers", Item.Type.Armor, 0.5f, 1.0f, "Wrist");
    lightFullSet.add("Gloves", Item.Type.Armor, 1.5f, 1.0f, "Hand");
    lightFullSet.add("Greaves", Item.Type.Armor, 1.0f, 1.0f, "Lower Torso");
    lightFullSet.add("Shinguards", Item.Type.Armor, 1.2f, 0.5f, "Shin");
    lightFullSet.add("Boots", Item.Type.Armor, 1.0f, 1.0f, "Foot");
    templates.add(lightFullSet);
    
    ItemSet.Template heavyFullSet = new ItemSet.Template("Standard_Heavy_Armor_FullSet");
    heavyFullSet.add("Helm", Item.Type.Armor, 1.0f, 1.0f, "Head");
    heavyFullSet.add("Breastplate", Item.Type.Armor, 1.7f, 2.0f, "Chest");
    heavyFullSet.add("Pauldrons", Item.Type.Armor, 1.5f, 1.5f, "Shoulder");
    heavyFullSet.add("Cuffs", Item.Type.Armor, 0.5f, 1.0f, "Arm");
    heavyFullSet.add("Bracers", Item.Type.Armor, 0.5f, 1.0f, "Wrist");
    heavyFullSet.add("Gauntlets", Item.Type.Armor, 1.5f, 1.0f, "Hand");
    heavyFullSet.add("Greaves", Item.Type.Armor, 1.0f, 1.0f, "Lower Torso");
    heavyFullSet.add("Shinguards", Item.Type.Armor, 1.2f, 0.5f, "Shin");
    heavyFullSet.add("Boots", Item.Type.Armor, 1.0f, 1.0f, "Foot");
    templates.add(heavyFullSet);
    
    // TODO: add weapon types
    ItemSet.Template weaponsFullSet = new ItemSet.Template("Standard_Weapon_FullSet");
    weaponsFullSet.addWeapon("Longsword", Item.Type.Weapon, Item.WeaponType.Sword, 1.0f, 1.0f, "Off-Hand");
    weaponsFullSet.addWeapon("Mace", Item.Type.Weapon, Item.WeaponType.Mace, 0.75f, 0.75f, "Off-Hand");
    weaponsFullSet.addWeapon("Dagger", Item.Type.Weapon, Item.WeaponType.Dagger, 0.50f, 0.25f, "Off-Hand");
    weaponsFullSet.addWeapon("Axe", Item.Type.Weapon, Item.WeaponType.Axe, 1.25f, 1.0f, "Off-Hand");
    weaponsFullSet.addWeapon("Hammer", Item.Type.Weapon, Item.WeaponType.Hammer, 1.25f, 1.0f, "Two-Handed");
    templates.add(weaponsFullSet);
  }
  
  public static List<ItemSet.Template> getTemplates() {
    return templates;
  }

  public static class Builder {
    
    List<Item.Builder> builders = new ArrayList<Item.Builder>();
    private Item.Builder setReferenceBuilder;
    private ItemSet.Template template;
    private View rootView;
    
    // private String setName;
    
    public static ItemSet.Builder from(
        View rootView) {
      return new ItemSet.Builder().loadFrom(rootView);
    }
    
    private Builder() {}
    
    private ItemSet.Builder loadFrom(
        View rootView) {
      this.rootView = rootView;
      return this;
    }
    
    public ItemSet.Builder ofTemplate(
        ItemSet.Template template) {
      this.template = template;
      return this;
    }
    
    //
    // public Set.Builder named(
    // String name) {
    // this.setName = name;
    // return this;
    // }

    private void process() {
       Spinner spinner = (Spinner) rootView.findViewById(R.id.s_item_type);
       String subType = (String) spinner.getSelectedItem();
      // subType = subType.toLowerCase(Locale.US);
      // Item.Type type = Item.Type.valueOfIgnoreCase("Setreference_"
      // + subType);
      
      Item.Type type = Item.Type.valueOfIgnoreCase(subType);
      
      setReferenceBuilder = Item.Builder.getNewSetReference(type);
      // setReferenceBuilder.setType(type);
      setReferenceBuilder.from(rootView).loadView();
      
      // create items from SetReference
      for (Template.Piece piece : template.setPieces) {
        Item.Type typeOfPiece = piece.type;
        Item.Builder builder = Item.Builder.getNewItem(typeOfPiece);
        
        builder.from(rootView).loadView();
        builder.setName(setReferenceBuilder.name
            + " " + piece.name);
        
        if (typeOfPiece == Item.Type.Weapon) {
          builder.setWeaponSlots(Arrays.asList(piece.positions));
          builder.setWeaponType(piece.weaponType.name());
        }
        if (typeOfPiece == Item.Type.Armor) {
          builder.setArmorSlots(Arrays.asList(piece.positions));
        }
        
        builder.multiplyCost(piece.costVariant);
        builder.multiplyValue(piece.valueVariant);
        
        builders.add(builder);
      }
    }

    public List<Item> create() {
      return create(null);
    }
    
    public List<Item> create(
        SaveCallback saveCallback) {
      List<Item> createdItems = new ArrayList<Item>();
      
      process();

      // saves items in create
      createdItems.add(setReferenceBuilder.create(saveCallback));
      for (Item.Builder itemBuilder : builders) {
        createdItems.add(itemBuilder.create(saveCallback));
      }

      rootView = null;
      return createdItems;
    }

  }
}