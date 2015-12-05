package com.kobe.wavedowngridview;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Property;
import android.view.View;

/**
 * Created by kobe-mac on 15/12/6.
 */
public final class XyProperty extends Property<View, PointF> {

    /**
     * A constructor that takes an identifying name and {@link #getType() type} for the property.
     *
     * @param name name of this property
     */
    public XyProperty(String name) {
        super(PointF.class, name);
    }

    @NonNull
    @Override
    public PointF get(View view) {
        return null;
    }

    @Override
    public void set(View view, @NonNull PointF value) {
        view.setX(value.x);
        view.setY(value.y);
    }
}
