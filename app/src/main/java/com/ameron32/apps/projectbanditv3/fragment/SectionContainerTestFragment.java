package com.ameron32.apps.projectbanditv3.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.ameron32.apps.projectbanditv3.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionContainerTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SectionContainerTestFragment
    extends AbsContentFragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String LAYOUT_RESOURCE = "Layout Resource";
  private static final String ARG_PARAM2 = "param2";
  
  
  // TODO: Rename and change types of parameters
  private int mLayoutResource;
  private String mParam2;
  
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param layoutResource
   *          Parameter 1.
   * @param param2
   *          Parameter 2.
   * @return A new instance of fragment SectionContainerTestFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SectionContainerTestFragment newInstance(
      Class subClass, int layoutResource) {
    String subClassStr = subClass.getSimpleName();
    SectionContainerTestFragment fragment = createSubFragment(subClassStr);
    
    Bundle args = new Bundle();
    args.putInt(LAYOUT_RESOURCE, layoutResource);
    args.putString(ARG_PARAM2, subClass.getSimpleName());
    fragment.setArguments(args);
    return fragment;
  }
  
  private static SectionContainerTestFragment createSubFragment(
      String subClassStr) {
    if (subClassStr.equals(SectionContainerTestFragment.class.getSimpleName())) {
      return new SectionContainerTestFragment();
    } else if (subClassStr.equals(EquipmentTestFragment.class.getSimpleName())) {
      return new EquipmentTestFragment();
    } else if (subClassStr.equals(EquipmentHeadersTestFragment.class.getSimpleName())) {
      return new EquipmentHeadersTestFragment();
    } else if (subClassStr.equals(EquipmentRecyclerTestFragment.class.getSimpleName())) {
      return new EquipmentRecyclerTestFragment();
    } else if (subClassStr.equals(InventoryTestFragment.class.getSimpleName())) {
      return new InventoryTestFragment();
    } else if (subClassStr.equals(InventoryHeadersTestFragment.class.getSimpleName())) {
      return new InventoryHeadersTestFragment();
    } else if (subClassStr.equals(StatsTestFragment.class.getSimpleName())) {
      return new StatsTestFragment();
    } else if (subClassStr.equals(SkillsTestFragment.class.getSimpleName())) {
      return new SkillsTestFragment();
    } else if (subClassStr.equals(GameFragment.class.getSimpleName())) {
      return new GameFragment();
    } else if (subClassStr.equals(TableTestFragment.class.getSimpleName())) {
      return new TableTestFragment();
    } else {
      return new SectionContainerTestFragment();
    }
  }
  
  public SectionContainerTestFragment() {
    // Required empty public constructor
  }
  
  @Override public void onCreate(
      Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mLayoutResource = getArguments().getInt(LAYOUT_RESOURCE);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }
  
  @Override
  protected int onReplaceFragmentLayout(int storedLayoutResource) {
    return mLayoutResource;
  }
  
//  @Override public View onCreateView(
//      LayoutInflater inflater,
//      ViewGroup container,
//      Bundle savedInstanceState) {
//    // Inflate the layout for this fragment
//    mParam1 = onReplaceFragmentLayout(mParam1);
//    FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_section_container_test, container, false);
//    frame.addView(inflater.inflate(mParam1, frame, false));
//    return frame;
//  }
  
  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment_section_container_test;
  }
}
