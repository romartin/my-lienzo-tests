package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;

public class CardinalIntersectSimpleTest implements MyLienzoTest {

    @Override
    public void test(final Layer layer) {
        layer.setListening(false);
        layer.add(makeAllCardinalIntersectionPoints());
    }

    private final Group makeAllCardinalIntersectionPoints()
    {
        final Group container = new Group()
                .setX(0).setY(0);
                //.setX(-60).setY(450);

        // circle
        MultiPath circle = new MultiPath();
        Group gc = new Group();
        gc.setX(0);
        gc.setY(0);
        container.add(gc);
        circle.M(100, 0);
        circle.A(150, 0, 150, 50, 50);
        circle.A(150, 100, 100, 100, 50);
        circle.A(50, 100, 50, 50, 50);
        circle.A(50, 0, 100, 0, 50);
        final Point2DArray circlePoints = drawPath(circle, gc);
        GWT.log("*** Circle ****");
        log(circlePoints);

        // rectangle
        MultiPath rectangle = new MultiPath();
        Group gr = new Group();
        gr.setX(200);
        gr.setY(200);
        container.add(gr);
        rectangle(rectangle, 100, 100, 25);
        final Point2DArray rectanglePoints = drawPath(rectangle, gr);
        GWT.log("*** Rectangle ****");
        log(rectanglePoints);

        return container;
    }

    private final Point2DArray drawPath(final MultiPath path, final Group g)
    {
        g.add(path);

        final PathPartList list = path.getPathPartListArray().get(0);

        final Point2DArray array = Geometry.getCardinalIntersects(list,
                                                                  MagnetManager.EIGHT_CARDINALS);

        for (Point2D p : array)
        {
            if (null != p)
            {
                drawCircle(g, p);
            }
        }

        return array;
    }

    private final void drawCircle(final Group container, final Point2D p)
    {
        container.add(new Circle(3).setLocation(p).setFillColor(ColorName.RED).setStrokeColor(ColorName.RED));
    }

    public static MultiPath rectangle(final MultiPath path,
                                      final double w,
                                      final double h,
                                      final double r) {
        if ((w > 0) && (h > 0)) {
            if ((r > 0) && (r < (w / 2)) && (r < (h / 2))) {
                path.M(r,
                       0);
                path.L(w - r,
                       0);
                path.A(w,
                       0,
                       w,
                       r,
                       r);
                path.L(w,
                       h - r);
                path.A(w,
                       h,
                       w - r,
                       h,
                       r);
                path.L(r,
                       h);
                path.A(0,
                       h,
                       0,
                       h - r,
                       r);
                path.L(0,
                       r);
                path.A(0,
                       0,
                       r,
                       0,
                       r);
            } else {
                path.rect(0,
                          0,
                          w,
                          h);
            }
            path.Z();
        }
        return path;
    }

    private void log(final Point2DArray array) {
        if (null != array) {
            int x = 0;
            for (Point2D point2D : array) {
                GWT.log("[" + x++ + "] " + "{" + point2D.getX() + ", " + point2D.getY() + "}" );
            }
        }
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
