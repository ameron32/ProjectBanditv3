package com.ameron32.apps.projectbanditv3.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by klemeilleur on 11/20/2015.
 */
@ParseClassName("TileMap")
public class TileMap extends AbsBanditObject<AbsBanditObject.Column> {

    public TileMap() {
        // required empty constructor
    }

    public String getName() {
        return getString("name");
    }

    public String getDownsampleUrl() {
        return getString("downsampleUrl");
    }

    public int getDownsampleHeight() {
        return getInt("downsampleHeight");
    }

    public int getDownsampleWidth() {
        return getInt("downsampleWidth");
    }

    public int getFullSizeMultiplier() {
        return getInt("fullSizeMultiplier");
    }

    public float getMaxScale() {
        return getNumber("maxScale").floatValue();
    }

    public float getMinScale() {
        return getNumber("minScale").floatValue();
    }

    public int getSubTiles() {
        return getInt("subTiles");
    }

    public boolean isFogOffset() {
        return getBoolean("isFogOffset");
    }

    public boolean hasFog() {
        return getBoolean("hasFog");
    }

    public boolean hasBlack() {
        return getBoolean("hasBlack");
    }

    public boolean hasDownsampleBlur() {
        return getBoolean("hasDownsampleBlur");
    }

    public int getTilesHeight() {
        return getInt("tilesHeight");
    }

    public int getTilesWidth() {
        return getInt("tilesWidth");
    }

    public JSONObject getDetailLevels() {
        return getJSONObject("detailLevels");
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    private static final AbsBanditObject.Column[] COLUMNS = {
        new Column("name", DataType.String),
        new Column("downsampleUrl", DataType.String),
        new Column("downsampleHeight", DataType.Integer),
        new Column("downsampleWidth", DataType.Integer),
        new Column("fullSizeMultiplier", DataType.Integer),
        new Column("maxScale", DataType.Float),
//        new Column("detailLevels", DataType.JSONObject),
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

    public float getRevealMultiplier() {
        return getNumber("revealMultiplier").floatValue();
    }
}
