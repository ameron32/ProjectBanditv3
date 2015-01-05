package com.qozix.tileview.paths;

import java.util.List;

import android.graphics.Path;
import android.graphics.Point;

public class PathHelper {

	private PathHelper() {

	}

	public static Path pathFromPoints( List<Point> points ) {
		Path path = new Path();
		Point start = points.get( 0 );
		path.moveTo( (float) start.x, (float) start.y );
		int l = points.size();
		for ( int i = 1; i < l; i++ ) {
			Point point = points.get( i );
			path.lineTo( (float) point.x, (float) point.y );
		}
		return path;
	}
}
