package com.ameron32.apps.projectbanditv3.object;

import com.ameron32.apps.projectbanditv3.adapter.TableAdapter;
import com.parse.ParseObject;

/**
 * Created by klemeilleur on 10/26/2015.
 */
public interface ParseColumnable<T extends ParseObject> extends TableAdapter.Columnable<T> {


}
