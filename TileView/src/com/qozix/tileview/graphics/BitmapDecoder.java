package com.qozix.tileview.graphics;

import android.content.Context;
import android.graphics.Bitmap;

public interface BitmapDecoder {

	public Bitmap decode( String fileName, Context context );

}
