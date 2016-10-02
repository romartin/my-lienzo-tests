package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;
import com.google.gwt.user.client.ui.FlowPanel;

public class BasicWiresShapesTests extends FlowPanel implements MyLienzoTest {

    private static final double SIZE = 100;
    private static final double RADIUS = SIZE / 2;

    private WiresManager wiresManager;

    public void test(Layer layer) {

        this.wiresManager = WiresManager.get( layer );

        WiresShape rectangle = createRectangle();
        rectangle.setX( 100 ).setY( 100 );
        setResizable( rectangle );

        WiresShape circle = createCircle();
        circle.setX( 300 ).setY( 100 );

        WiresShape polygon =  createPolygon();
        polygon.setX( 500 ).setY( 100 );
        setResizable( polygon );

        WiresShape polygonWithIcon =  createPolygon();
        polygonWithIcon.setX( 700 ).setY( 100 );
        polygonWithIcon.addChild( new Circle( RADIUS / 3 ), LayoutContainer.Layout.CENTER );
        setResizable( polygonWithIcon );

    }

    private void setResizable( WiresShape shape ) {
        TestsUtils.addResizeHandlers( shape );
    }

    private WiresShape createPolygon() {
        MultiPath path = createPolygon( new MultiPath(), 4, RADIUS, 0 );
        final WiresShape shape = new WiresShape( path );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

    private static MultiPath createPolygon( final MultiPath result,
                                     final int sides,
                                     final double radius,
                                     final double cornerRadius ) {

        if ((sides > 2) && (radius > 0))
        {
            final double ix = radius;
            final double iy = radius;

            result.M( ix, iy - radius);

            if (cornerRadius <= 0)
            {
                for (int n = 1; n < sides; n++)
                {
                    final double theta = (n * 2 * Math.PI / sides);

                    result.L( ix + ( radius * Math.sin(theta) ) , iy + ( -1 * radius * Math.cos(theta) ) );
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

        return result;
    }

    private WiresShape createRectangle() {
        MultiPath path = createRectangle( new MultiPath(), SIZE, SIZE, 0 );
        final WiresShape shape = new WiresShape( path );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

    private WiresShape createCircle() {
        MultiPath path =  createCircle( new MultiPath(), RADIUS );
        final WiresShape shape = new WiresShape( path );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

    private static MultiPath createCircle( final MultiPath path,
                                     final double radius ) {
        return path.M( radius, 0 ).circle( radius );
    }


    private static MultiPath createRectangle( final MultiPath path,
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


}