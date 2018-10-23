package org.roger600.lienzo.client.widget;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

public abstract class InfiniteLayer extends Layer
{
    // TODO: ??
    private static final int PADDING = 0;

    private final Bounds bounds;

    public InfiniteLayer()
    {
        this.bounds = new Bounds(0,
                                 0,
                                 0,
                                 0);
    }

    public abstract NFastArrayList<Bounds> getShapesBounds();

    public Bounds getVisibleBounds()
    {
        updateVisibleBounds();
        return bounds;
    }

    protected void updateVisibleBounds()
    {
        final Viewport viewport  = getViewport();
        Transform      transform = viewport.getTransform();
        if (transform == null)
        {
            viewport.setTransform(transform = new Transform());
        }
        final double x = (PADDING - transform.getTranslateX()) / transform.getScaleX();
        final double y = (PADDING - transform.getTranslateY()) / transform.getScaleY();
        bounds.setX(x);
        bounds.setY(y);
        bounds.setHeight(Math.max(0,
                                  (viewport.getHeight() - PADDING * 2) / transform.getScaleX()));
        bounds.setWidth(Math.max(0,
                                 (viewport.getWidth() - PADDING * 2) / transform.getScaleY()));
    }

    Bounds getBounds()
    {
        return bounds;
    }
}
