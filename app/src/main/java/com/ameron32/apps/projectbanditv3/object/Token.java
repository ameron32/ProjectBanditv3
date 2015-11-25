package com.ameron32.apps.projectbanditv3.object;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.view.RevealView;
import com.parse.ParseClassName;

import android.support.annotation.ColorRes;
import android.view.View;

import java.util.Set;

/**
 * Created by klemeilleur on 11/16/2015.
 */
@ParseClassName("Token")
public class Token extends AbsBanditObject<AbsBanditObject.Column> {

  private static final int TAG_KEY = R.id.token_key;

  public static Token fromView(View host) {
    try {
      return (Token) host.getTag(TAG_KEY);
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    return null;
  }

  @ColorRes int colorRes;
  int hostViewId;

  public static Token create(String tag, String name, @ColorRes int color, int sizeX, int sizeY, String url) {
    Token t = new Token();
    t.setTag(tag);
    t.put("name", name);
    t.setSizeX(sizeX);
    t.setSizeY(sizeY);
    t.setUrl(url);
    t.setTileCol(0);
    t.setTileRow(0);
    t.setColor(color);
    t.setRotation(0.0f);
    return t;
  }

  public Token() {
    // required empty constructor
  }

  public Token move(int tileCol, int tileRow) {
    final int oldCol = this.getTileCol();
    final int oldRow = this.getTileRow();
    double theta = Math.atan2(tileRow - oldRow, tileCol - oldCol);
    theta += Math.PI/2.0;
    double angle = Math.toDegrees(theta);
    if (angle < 0) {
      angle += 360;
    }
    angle += 180;
    if (angle < 0) {
      angle += 360;
    }
    this.setRotation((float) angle);
    this.setTileCol(tileCol);
    this.setTileRow(tileRow);
    return this;
  }

  public Token thenSave() {
    this.saveEventually();
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Token) {
      return ((Token) o).getTag() == this.getTag();
    }
    return false;
  }

  public void attachTo(View host) {
    host.setTag(TAG_KEY, this);
    this.setHostViewId(host.getId());
  }

  public void includeInTileMap(TileMap tileMap) {
    Util.addObjectRelationToObject("TileMap", tileMap.getName(), "includeIn", "Token", this.getName());
  }

  public View findMarkerIn(View host) {
    return host.findViewById(this.getHostViewId());
  }

  public String getTag() {
    return this.getString("tag");
  }

  public void setTag(String tag) {
    this.put("tag", tag);
  }

  public int getHostViewId() {
    return hostViewId;
  }

  public void setHostViewId(int hostViewId) {
    this.hostViewId = hostViewId;
  }

  public int getColor() {
    return colorRes;
  }

  public void setColor(int color) {
    colorRes = color;
  }

  public int getTileRow() {
    return this.getInt("tileRow");
  }

  public void setTileRow(int tileRow) {
    this.put("tileRow", tileRow);
  }

  public int getTileCol() {
    return this.getInt("tileCol");
  }

  public void setTileCol(int tileCol) {
    this.put("tileCol", tileCol);
  }

  public int getSizeX() {
    return this.getInt("sizeX");
  }

  public void setSizeX(int sizeX) {
    this.put("sizeX", sizeX);
  }

  public int getSizeY() {
    return this.getInt("sizeY");
  }

  public void setSizeY(int sizeY) {
    this.put("sizeY", sizeY);
  }

  public String getUrl() {
    return this.getString("url");
  }

  public void setUrl(String url) {
    this.put("url", url);
  }

  public float getRotation() {
    return this.getNumber("rotation").floatValue();
  }

  public void setRotation(float rotation) {
    this.put("rotation", rotation);
  }

  public RevealView.Tile getTile() {
    return new RevealView.Tile(getTileRow(), getTileCol());
  }












  @Override public int getColumnCount() {
    return COLUMNS.length;
  }

  private static final AbsBanditObject.Column[] COLUMNS = {
      new Column("url", DataType.String)
  };

  @Override public String toString() {
    final Set<String> keySet = this.keySet();
    final StringBuilder sb = new StringBuilder();
    for (String key : keySet) {
      sb.append("\n");
      sb.append(key);
      sb.append(": ");
      sb.append(this.get(key));
    }
    return sb.toString();
  }

  @Override public AbsBanditObject.Column get(
      int columnPosition) {
    return COLUMNS[columnPosition];
  }
}
