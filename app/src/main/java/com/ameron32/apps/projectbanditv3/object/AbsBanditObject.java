package com.ameron32.apps.projectbanditv3.object;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.ameron32.apps.projectbanditv3.adapter.TableAdapter.Columnable;
import com.parse.ParseObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.Date;
import java.util.Locale;

public abstract class AbsBanditObject<T extends AbsBanditObject.Column>
  extends ParseObject
  implements Columnable<T>
{

  public AbsBanditObject() {
    // REQUIRED
  }

  protected String asString(final Column c) {
    switch(c.dataType) {
      case Integer:
        final int i = this.getInt(c.key);
        return String.valueOf(i);
      case String:
        final String s = this.getString(c.key);
        return s;
      case Boolean:
        final boolean b = this.getBoolean(c.key);
        return String.valueOf(b);
      case Array:
        return "<<Array>>";
      case Date:
        final Date d = this.getDate(c.key);
        if (d == null) {
          return "null";
        }
        return new DateTime(d).toString(DateTimeFormat.fullDateTime());
      case ListOfStrings:
        return "<<a List of Strings>>";
      case Pointer:
        ParseObject parseObject = this.getParseObject(c.key);
        if (parseObject == null) {
          return "null";
        }
        if (parseObject instanceof AbsBanditObject) {
          return ((AbsBanditObject)parseObject).getName();
        }
        return "<<ParseObject [name]>>";
      case Relation:
        return "<<relation>>";
      default:
        return "<<unknown>>";
    }
  }

  protected static class Column {
    public String key;
    public DataType dataType;

    public Column(String key,
                  DataType dataType) {
      this.key = key;
      this.dataType = dataType;
    }
  }

  private boolean isHeader = false;

  @Override public String getColumnHeader(
      int columnPosition) {
    if (isHeader) {
      return get(columnPosition).key;
    } else {
      return asString(get(columnPosition));
    }
  }

  @Override public void useAsHeaderView(
      boolean b) {
    isHeader = b;
  }

  @Override public boolean isHeaderView() {
    return isHeader;
  }

  public String getName() {
    return this.getString("name");
  }
}
