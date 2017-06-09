package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;

public class TestsUtils {

    // w = width
    // h = height
    // r = corner radius
    public static MultiPath rect(final MultiPath path,
                                 final double w,
                                 final double h,
                                 final double r ) {
        if ((w > 0) && (h > 0)) {
            if ((r > 0) && (r < (w / 2)) && (r < (h / 2))) {
                path.M(r, 0);
                path.L(w - r, 0);
                path.A( w , 0, w, r, r );
                path.L(w, h - r);
                path.A( w, h, w - r, h, r );
                path.L(r, h);
                path.A( 0, h, 0, h - r , r );
                path.L(0, r);
                path.A( 0, 0, r, 0, r );
            } else {
                path.rect(0, 0, w, h);
            }
            path.Z();
        }
        return path;
    }

    public static MultiPath circle( final MultiPath path,
                                    final double x,
                                    final double y,
                                    final double r ) {
        final double c = r * 2;
        path.A(x + r, y, x + r, y + r, r);
        path.A(x + r, y + c, x, y + c, r);
        path.A(x - r, y + c, x - r, y + r, r);
        path.A(x - r, y, x, y, r);
        path.Z();
        return path;
    }

    public static MultiPath polygon(final MultiPath path,
                                    final int sides,
                                    final double radius,
                                    final double cornerRadius ) {
        if ((sides > 2) && (radius > 0))
        {
            path.M(0, 0 - radius);
            if (cornerRadius <= 0)
            {
                for (int n = 1; n < sides; n++)
                {
                    final double theta = (n * 2 * Math.PI / sides);
                    path.L(radius * Math.sin(theta), -1 * radius * Math.cos(theta));
                }
                path.Z();
            }
            else
            {
                final Point2DArray list = new Point2DArray(0, 0 - radius);
                for (int n = 1; n < sides; n++)
                {
                    final double theta = (n * 2 * Math.PI / sides);
                    list.push(radius * Math.sin(theta), -1 * radius * Math.cos(theta));
                }
                Geometry.drawArcJoinedLines(path.getPathPartList(), list.push(0, 0 - radius), cornerRadius);
            }
        }

        return path.setStrokeColor("#000000").setFillColor(ColorName.BLACK );
    }

    public static void addResizeHandlers( final WiresShape shape ) {
        shape.setResizable( true ).getPath().addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                final IControlHandleList controlHandles = shape.loadControls( IControlHandle.ControlHandleStandardType.RESIZE );

                if ( null != controlHandles ) {

                    if ( event.isShiftKeyDown() ) {
                        controlHandles.show();
                    } else {
                        controlHandles.hide();
                    }

                }
            }
        });
    }
}
