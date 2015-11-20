package com.ameron32.apps.projectbanditv3.object;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;

import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.adapter.TableAdapter.Columnable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
      case Float:
        final float f = this.getNumber(c.key).floatValue();
        return String.valueOf(f);
      case String:
        final String s = this.getString(c.key);
        return s;
      case Boolean:
        final boolean b = this.getBoolean(c.key);
        return String.valueOf(b);
      case Array:
        return "<<Array>>";
      case Date:
        Date d;
        if (c.key.equalsIgnoreCase("createdAt")) {
          d = this.getCreatedAt();
        } else if (c.key.equalsIgnoreCase("updatedAt")) {
          d = this.getUpdatedAt();
        } else {
          d = this.getDate(c.key);
        }
        if (d == null) {
          return "null";
        }
        return new DateTime(d).toString(DateTimeFormat.shortDateTime());
      case ListOfStrings:
        List<String> list = this.getList(c.key);
        if (list != null && !list.isEmpty()) {
          return Util.listToString(',', list);
        }
        return "<<a List of Strings>>";
      case Pointer:
        ParseObject parseObject = this.getParseObject(c.key);
        if (parseObject == null) {
          return "null";
        }
        if (parseObject instanceof AbsBanditObject) {
          return ((AbsBanditObject) parseObject).getName();
        } else if (parseObject instanceof User) {
          return ((User) parseObject).getName();
        } else {
          return parseObject.getObjectId();
        }
      case JSONObject:
        final JSONObject jso = this.getJSONObject(c.key);
        return jso.toString();
      case Relation:
        try {
          final List<ParseObject> parseObjects = this.getRelation(c.key).getQuery().fromLocalDatastore().find();
          this.getRelation(c.key).getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
            if (e == null && objects != null && !objects.isEmpty()) {
              ParseObject.pinAllInBackground("relationData", objects);
            }
            }
          });
          if (parseObjects == null || parseObjects.isEmpty()) {
            return "empty";
          }
          ParseObject example = parseObjects.get(0);
          if (example instanceof AbsBanditObject) {
            return Util.displayAsList(',', parseObjects,"name");
          } else if (example instanceof User) {
            return Util.displayAsList(',', parseObjects,"username");
          } else {
            return Util.displayAsList(',', parseObjects,"objectId");
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
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
