package com.ameron32.apps.projectbanditv3;

/**
 * Created by Kris on 12/31/2014.
 *
 * Intended for use in a centrally managed reload caller.
 * All AutoReloaders should be able to load fresh data when instructed by the caller.
 */
public interface AutoReloader {

  public void loadObjects();
}
