package com.ameron32.apps.projectbanditv3.manager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsManager implements IManager {
  
  private static final List<AbsManager> allManagers = new ArrayList<AbsManager>();
  public static boolean isAllInitializationComplete() {
    for (AbsManager m : allManagers) {
      if (!m.isInitialized()) return false;
    }
    return true;
  }
  
  public static boolean isInitializationComplete(
      AbsManager... managers) {
    for (AbsManager m : managers) {
      if (!m.isInitialized())
        return false;
    }
    return true;
  }
  
  public AbsManager() {
    allManagers.add(this);
  }
  
  private boolean isInitialized = false;

  public boolean isInitialized() {
    return isInitialized;
  }

  public void setInitialized(
      boolean isInitialized) {
    this.isInitialized = isInitialized;
  }

  public abstract void initialize();
}
