package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import org.roger600.lienzo.client.dmn.DirectionalLine;

public class TestsUtils
{
    // w = width
    // h = height
    // r = corner radius
    public static MultiPath rect(final MultiPath path,
                                 final double w,
                                 final double h,
                                 final double r)
    {
        if ((w > 0) && (h > 0))
        {
            if ((r > 0) && (r < (w / 2)) && (r < (h / 2)))
            {
                path.M(r, 0);
                path.L(w - r, 0);
                path.A(w, 0, w, r, r);
                path.L(w, h - r);
                path.A(w, h, w - r, h, r);
                path.L(r, h);
                path.A(0, h, 0, h - r, r);
                path.L(0, r);
                path.A(0, 0, r, 0, r);
            }
            else
            {
                path.rect(0, 0, w, h);
            }
            path.Z();
        }
        return path;
    }

    public static MultiPath circle(final MultiPath path,
                                   final double x,
                                   final double y,
                                   final double r)
    {
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
                                    final double cornerRadius)
    {
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

        return path.setStrokeColor("#000000").setFillColor(ColorName.BLACK);
    }

    public static void addResizeHandlers(final WiresShape shape)
    {
        shape.setResizable(true).getGroup().addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                final IControlHandleList controlHandles = shape.loadControls(IControlHandle.ControlHandleStandardType.RESIZE);

                if (null != controlHandles)
                {

                    if (event.isShiftKeyDown())
                    {
                        controlHandles.show();
                    }
                    else
                    {
                        controlHandles.hide();
                    }
                }
            }
        });
    }

    public static WiresConnector connect(MagnetManager.Magnets magnets0,
                               int i0_1,
                               MagnetManager.Magnets magnets1,
                               int i1_1,
                               WiresManager wiresManager)
    {
        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = null != magnets1 ? magnets1.getMagnet(i1_1) : null;

        double x0, x1, y0, y1;

        MultiPath head = new MultiPath();
        head.M(15,
               20);
        head.L(0,
               20);
        head.L(15 / 2,
               0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15,
               20);
        tail.L(0,
               20);
        tail.L(15 / 2,
               0);
        tail.Z();

        AbstractDirectionalMultiPointShape<?> line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = null != m1_1 ? m1_1.getControl().getX() : x0 + 50;
        y1 = null != m1_1 ? m1_1.getControl().getY() : y0 + 50;
        // Orthogonal.
        line = createPolyLine(
                x0,
                y0,
                (x0 + ((x1 - x0) / 2)),
                (y0 + ((y1 - y0) / 2)),
                x1,
                y1);
        // Directional.
        /*line = createDirectionalLine(
                x0,
                y0,
                x1,
                y1);*/
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());
        line.setSelectionStrokeOffset(25);
        line.setFillShapeForSelection(true);
        line.setFillBoundsForSelection(false);

        WiresConnector connector = new WiresConnector(m0_1,
                                                      m1_1,
                                                      line,
                                                      new MultiPathDecorator(head),
                                                      new MultiPathDecorator(tail));
        wiresManager.register(connector);

        head.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        line.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        return connector;
    }

    public static PolyLine createPolyLine(final double... points)
    {
        return new PolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

    public static DirectionalLine createDirectionalLine(final double x1,
                                                        final double y1,
                                                        final double x2,
                                                        final double y2)
    {
        return new DirectionalLine(x1, y1, x2, y2).setDraggable(true);
    }
}
