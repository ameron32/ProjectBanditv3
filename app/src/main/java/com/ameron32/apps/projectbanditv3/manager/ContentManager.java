package com.ameron32.apps.projectbanditv3.manager;

import com.ameron32.apps.projectbanditv3.fragment.AbsContentFragment;
import com.ameron32.apps.projectbanditv3.fragment.AddCharactersFragment;
import com.ameron32.apps.projectbanditv3.fragment.AddPlayersFragment;
import com.ameron32.apps.projectbanditv3.fragment.AdvantageCheckerFragment;
import com.ameron32.apps.projectbanditv3.fragment.ChatManagerFragment;
import com.ameron32.apps.projectbanditv3.adapter.ContentAdapter;
import com.ameron32.apps.projectbanditv3.fragment.CreateItemFragment;
import com.ameron32.apps.projectbanditv3.fragment.CreateSetItemsFragment;
import com.ameron32.apps.projectbanditv3.fragment.DEMORxJavaFragment;
import com.ameron32.apps.projectbanditv3.fragment.EquipmentHeadersTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.EquipmentRecyclerTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.EquipmentTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.GameFragment;
import com.ameron32.apps.projectbanditv3.fragment.HtmlTextViewTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.InventoryHeadersTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.InventoryRecyclerTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.InventoryTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.IssueItemFragment;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.fragment.PartialPaneFragment;
import com.ameron32.apps.projectbanditv3.fragment.RelationAttacherFragment;
import com.ameron32.apps.projectbanditv3.fragment.RollDiceFragment;
import com.ameron32.apps.projectbanditv3.fragment.SectionContainerTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.SkillCheckerFragment;
import com.ameron32.apps.projectbanditv3.fragment.SkillsTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.StatsTestFragment;
import com.ameron32.apps.projectbanditv3.fragment.TableTestFragment;
//import com.ameron32.apps.projectbanditv3.tileview.MyTileViewFragment;

import java.util.ArrayList;
import java.util.List;



public class ContentManager extends AbsManager {

  private static ContentManager contentManager;

  public static ContentManager get() {
    if (contentManager == null) {
      contentManager = new ContentManager();
      contentManager.initialize();
    }
    return contentManager;
  }


  public static void destroy() {
    contentManager = null;
  }

  private List<ContentItem> contentItems;

  private ContentManager() {
    listeners = new ArrayList<OnContentChangeListener>();
    contentItems = createContentItems();
  }

  public void initialize() {
    setInitialized(true);
  }

  private List<ContentItem> createContentItems() {
    final List<ContentItem> items = new ArrayList<ContentItem>();

    items.add(new ContentItem("Game", R.drawable.ic_construction,
        GameFragment.newInstance(R.layout.section_game)));


//  TODO: instantiate GridPagerFragment correctly
//    items.add(new ContentItem("GridTest", R.drawable.ic_construction,
//        GridPagerFragment.newInstance(3)));

    items.add(new ContentItem("Stats", R.drawable.ic_construction,
        SectionContainerTestFragment.newInstance(StatsTestFragment.class, R.layout.section_character_stats)));
//    items.add(new ContentItem("Test:Equipment2", R.drawable.ic_construction,
//        SectionContainerTestFragment.newInstance(EquipmentHeadersTestFragment.class, R.layout.section_character_equipment_headers)));
    items.add(new ContentItem("Equipment3", R.drawable.ic_construction,
        SectionContainerTestFragment.newInstance(EquipmentRecyclerTestFragment.class, R.layout.section_character_equipment_3)));
//    items.add(new ContentItem("Test:Inventory2", R.drawable.ic_construction,
//        SectionContainerTestFragment.newInstance(InventoryHeadersTestFragment.class, R.layout.section_character_inventory_headers)));
//    items.add(new ContentItem("Test:Inventory3", R.drawable.ic_construction,
//        SectionContainerTestFragment.newInstance(InventoryRecyclerTestFragment.class, R.layout.section_character_inventory_3)));
    items.add(new ContentItem("Inventory2", R.drawable.ic_construction,
        SectionContainerTestFragment.newInstance(InventoryTestFragment.class, R.layout.section_character_inventory)));
    items.add(new ContentItem("Roll Dice", R.drawable.ic_construction, new RollDiceFragment()));

//  TODO: reenable later
//    items.add(new ContentItem("Test:Characters", R.drawable.ic_construction,
//        TableTestFragment.create("Character", R.layout.fragment_default_table_layout)));

    if (GameManager.get().isCurrentUserGM()) {

      items.add(new ContentItem("RxJava DEMO", R.drawable.ic_gm, new DEMORxJavaFragment()));
      items.add(new ContentItem("SlidingPaneTest", R.drawable.ic_gm, new PartialPaneFragment()));
      items.add(new ContentItem("HtmlTextView DEMO", R.drawable.ic_gm, new HtmlTextViewTestFragment()));


//      items.add(new ContentItem("GM:Demo:blank", R.drawable.ic_gm,
//          AbsContentFragment.newInstance(/*1*/)));
      items.add(new ContentItem("GM:Chat", R.drawable.ic_gm,
              ChatManagerFragment.newInstance(0, null)));
      items.add(new ContentItem("GM:RETIRED:Equipment", R.drawable.ic_gm,
          SectionContainerTestFragment.newInstance(EquipmentTestFragment.class, R.layout.section_character_equipment)));
      items.add(new ContentItem("GM:RETIRED:Inventory", R.drawable.ic_gm,
          SectionContainerTestFragment.newInstance(InventoryTestFragment.class, R.layout.section_character_inventory)));

      items.add(new ContentItem("GM:Test:Advantages2", R.drawable.ic_gm,
          new AdvantageCheckerFragment()));
      items.add(new ContentItem("GM:Test:Skills2", R.drawable.ic_gm,
          new SkillCheckerFragment()));

      items.add(new ContentItem("GM:Test:Skills", R.drawable.ic_gm,
          SectionContainerTestFragment.newInstance(SkillsTestFragment.class, R.layout.section_skills)));
      items.add(new ContentItem("GM:Test:Table Character", R.drawable.ic_gm,
          TableTestFragment.create("Character", R.layout.section_)));
      items.add(new ContentItem("GM:Test:Table CInventory", R.drawable.ic_gm,
          TableTestFragment.create("CInventory", R.layout.section_)));
      items.add(new ContentItem("GM:Test:Table Item", R.drawable.ic_gm,
          TableTestFragment.create("Item", R.layout.section_)));

//        items.add(new ContentItem("GM:Test:TileView", R.drawable.ic_gm,
//                MyTileViewFragment.newInstance()));



      items.add(new ContentItem("GM: Add Players", R.drawable.ic_gm, new AddPlayersFragment()));
      items.add(new ContentItem("GM: Add Characters", R.drawable.ic_gm, new AddCharactersFragment()));
      items.add(new ContentItem("GM: Create Item", R.drawable.ic_gm, new CreateItemFragment()));
      items.add(new ContentItem("GM: Issue Item", R.drawable.ic_gm, new IssueItemFragment()));

      items.add(new ContentItem("Database: Create Item Set", R.drawable.ic_gm, new CreateSetItemsFragment()));
      items.add(new ContentItem("Database: Attach Relation", R.drawable.ic_gm, new RelationAttacherFragment()));
    }

    return items;
  }

  public List<OnContentChangeListener> listeners;

  public boolean addOnContentChangeListener(
      OnContentChangeListener listener) {
    return listeners.add(listener);
  }

  public boolean removeOnContentChangeListener(
      OnContentChangeListener listener) {
    return listeners.remove(listener);
  }

  private void notifyListenersOfContentChange(int position) {
    for (OnContentChangeListener listener : listeners) {
      listener.onContentChange(this, position);
    }
  }

  public interface OnContentChangeListener {
    public void onContentChange(
        ContentManager manager,
        int position);
  }

  private int mCurrentSelectedFragment = 0;

  public void setCurrentSelectedFragmentPosition(
      int position) {
    mCurrentSelectedFragment = position;
    notifyListenersOfContentChange(position);
  }

  public int getCurrentSelectedFragment() {
    return mCurrentSelectedFragment;
  }

  public String getTitleForPosition(
      int position) {
    return contentItems.get(position).title;
  }

  public AbsContentFragment getNewFragmentForPosition(
      int position) {
    return contentItems.get(position).fragment;
  }

  public String[] getTitles() {
    String[] titles = new String[contentItems.size()];
    for (int i = 0; i < titles.length; i++) {
      titles[i] = contentItems.get(i).title;
    }
    return titles;
  }

  public int[] getImageIcons() {
    int[] icons = new int[contentItems.size()];
    for (int i = 0; i < icons.length; i++) {
      icons[i] = contentItems.get(i).imageResource;
    }
    return icons;
  }

  public static class ContentItem {
    public String title;
    public int imageResource;
    AbsContentFragment fragment;

    public ContentItem(String title,
        int imageResource,
        AbsContentFragment fragment) {
      super();
      this.title = title;
      this.imageResource = imageResource;
      this.fragment = fragment;
    }
  }

  public ContentAdapter getAdapter() {
    return new ContentAdapter(contentItems);
  }
}
