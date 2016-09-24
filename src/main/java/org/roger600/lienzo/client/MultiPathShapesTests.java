package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class MultiPathShapesTests extends FlowPanel implements MyLienzoTest {

    public void test(Layer _layer) {
        final Layer layer = _layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        //MultiPath path = createRectangle( 300, 300 );
        // MultiPath path = createPolygon( 5, 50, 0 );
        // MultiPath path = createRing( 50, 25 );
        //MultiPath path = createCircle( new MultiPath().M( 0, -100 ), 0, -100, 100 );
        MultiPath path = new MultiPath().M( 0, -100 ).circle( 100 );
        final WiresShape parentShape = new WiresShape(path);
        parentShape.setDraggable(true).setX(200).setY(200);
        wires_manager.register( parentShape );
        wires_manager.getMagnetManager().createMagnets(parentShape);
        TestsUtils.addResizeHandlers( parentShape );

        Circle circle = new Circle( 100 ).setX( 500 ).setY( 200 ).setFillColor( ColorName.RED );
        layer.add( circle );

    }

    private MultiPath createRectangle( final double width, final double height ) {
        return  new MultiPath().rect(0, 0, width, height).setStrokeColor("#000000").setFillColor( ColorName.BLACK );
    }

    private MultiPath createPolygon(final int sides,
                                    final double radius,
                                    final double cornerRadius )
    {
        final MultiPath result = new MultiPath();

        if ((sides > 2) && (radius > 0))
        {
            result.M(0, 0 - radius);

            if (cornerRadius <= 0)
            {
                for (int n = 1; n < sides; n++)
                {
                    final double theta = (n * 2 * Math.PI / sides);

                    result.L(radius * Math.sin(theta), -1 * radius * Math.cos(theta));
                }
                result.Z();
            }
            else
            {
                final Point2DArray list = new Point2DArray(0, 0 - radius);

                for (int n = 1; n < sides; n++)
                {
                    final double theta = (n * 2 * Math.PI / sides);

                    list.push(radius * Math.sin(theta), -1 * radius * Math.cos(theta));
                }
                Geometry.drawArcJoinedLines(result.getPathPartList(), list.push(0, 0 - radius), cornerRadius);
            }
        }

        return result.setStrokeColor("#000000").setFillColor( ColorName.BLACK );
    }

    private MultiPath createRing( final double outer, final double inner ) {


        MultiPath path = new MultiPath().setStrokeColor( ColorName.BLACK );
        createCircle( path, 0, 0, outer );
        path.setFillColor( ColorName.BLACK );
        /*circle( path, 0, 0, 10 );
        path.setFillColor( ColorName.WHITE );
        path.Z();*/


        return  path;
    }

    private MultiPath createCircle( final MultiPath path,
                         final double x,
                         final double y,
                         final double r )
    {
        final double c = r * 2;

        path.A(x + r, y, x + r, y + r, r);

        path.A(x + r, y + c, x, y + c, r);

        path.A(x - r, y + c, x - r, y + r, r);

        path.A(x - r, y, x, y, r);

        path.Z();

        return path;
    }

}
