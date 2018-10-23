package org.roger600.lienzo.client.widget;

import com.ait.lienzo.client.core.shape.wires.WiresLayer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Supplier;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

public class InfiniteWiresLayer extends InfiniteLayer
{
    private final Supplier<WiresLayer> wiresLayer;

    public InfiniteWiresLayer()
    {
        this.wiresLayer = new Supplier<WiresLayer>()
        {
            @Override public WiresLayer get()
            {
                return WiresManager.get(getLayer()).getLayer();
            }
        };
    }

    public InfiniteWiresLayer(final Supplier<WiresLayer> wiresLayer)
    {
        this.wiresLayer = wiresLayer;
    }

    @Override
    public NFastArrayList<Bounds> getShapesBounds()
    {
        final NFastArrayList<Bounds>     result      = new NFastArrayList<>();
        final NFastArrayList<WiresShape> childShapes = wiresLayer.get().getChildShapes();
        if (null != childShapes)
        {
            for (WiresShape shape : childShapes)
            {
                final Point2D     location    = shape.getLocation();
                final BoundingBox boundingBox = shape.getGroup().getBoundingBox();
                result.add(new Bounds(location.getX(),
                                      location.getY(),
                                      boundingBox.getWidth(),
                                      boundingBox.getHeight()));
            }
        }
        return result;
    }
}
