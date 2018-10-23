package org.roger600.lienzo.client.widget;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

public class InfiniteLienzoLayer extends InfiniteLayer
{
    public NFastArrayList<Bounds> getShapesBounds()
    {
        NFastArrayList<Bounds>        result = new NFastArrayList<>();
        NFastArrayList<IPrimitive<?>> shapes = getChildNodes();
        if (null != shapes)
        {
            for (IPrimitive<?> shape : shapes)
            {
                BoundingBox boundingBox = shape.getBoundingBox();
                result.add(new Bounds(shape.getX(),
                                      shape.getY(),
                                      boundingBox.getWidth(),
                                      boundingBox.getHeight()));
            }
        }
        return result;
    }
}
