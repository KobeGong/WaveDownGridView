package com.kobe.wavedowngridview;

import android.graphics.Path;
import android.graphics.PointF;
import android.transition.PathMotion;

/**
 * Created by kobe-mac on 15/12/6.
 */
public final class QuadPathMotion extends PathMotion {

    private PointF control;

    /**
     * @param control the relative position to centerX and centerY, where the centerX is
     *                the average of startX and endX and centerY is the average of startY
     *                and endY.
     */
    public QuadPathMotion(PointF control) {
        this.control = control;
    }

    @Override
    public Path getPath(float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(startX / 2 + startY / 2 + control.x, startY / 2 + endY / 2 + control.y, endX, endY);
        return path;
    }
}
