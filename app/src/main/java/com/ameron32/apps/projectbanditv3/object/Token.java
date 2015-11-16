package com.ameron32.apps.projectbanditv3.object;

import com.parse.ParseClassName;

import java.util.Set;

/**
 * Created by klemeilleur on 11/16/2015.
 */
@ParseClassName("Token")
public class Token extends AbsBanditObject<AbsBanditObject.Column> {

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
