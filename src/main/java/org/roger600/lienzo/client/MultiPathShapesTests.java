package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.PathPartList;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class MultiPathShapesTests extends FlowPanel implements MyLienzoTest {

    private static final String BLACK = ColorName.BLACK.getColorString();

    public void test(Layer _layer) {
        final Layer layer = _layer;

        MultiPath path = createRectangle( 100, 100, 10 );
        path.setX( 100 ).setY( 100 );
        layer.add( path );

    }

    // w = width
    // h = height
    // r = corner radius
    private MultiPath createRectangle( final double w, final double h, final double r ) {

        MultiPath path = new MultiPath();

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

    private void createRectangleWithUserIcon(Layer layer) {
        final WiresManager wires_manager = WiresManager.get(layer);

        //Group timerIcon = timer();
        Group timerIcon = user();
        final BoundingBox bb = timerIcon.getBoundingBox();
        final double w = bb.getWidth();
        final double h = bb.getHeight();

        MultiPath path =  new MultiPath().rect(0, 0, w, h).setStrokeColor(ColorName.BLACK);
        final WiresShape parentShape = new WiresShape(path);
        parentShape.setDraggable(true).setX(200).setY(200);
        parentShape.addChild( timerIcon, LayoutContainer.Layout.CENTER );

        wires_manager.register( parentShape );
        wires_manager.getMagnetManager().createMagnets(parentShape);
        TestsUtils.addResizeHandlers( parentShape );

    }

    private void regularShapes(Layer layer) {
        final WiresManager wires_manager = WiresManager.get(layer);
        MultiPath path = createRectangle( 100, 100 );
        final WiresShape parentShape = new WiresShape(path);
        parentShape.setDraggable(true).setX(200).setY(200);
        wires_manager.register( parentShape );
        wires_manager.getMagnetManager().createMagnets(parentShape);
        TestsUtils.addResizeHandlers( parentShape );

        Circle circle = new Circle( 100 ).setX( 500 ).setY( 200 ).setFillColor( ColorName.RED );
        layer.add( circle );
    }

    private static Group user() {

        final Group group = new Group();

        final SVGPath path1 = createSVGPath( "M0.585,24.167h24.083v-7.833c0,0-2.333-3.917-7.083-5.167h-9.25\n" +
                "\t\t\tc-4.417,1.333-7.833,5.75-7.833,5.75L0.585,24.167z", "#F4F6F7", 1, BLACK );
        group.add( path1 );

        final SVGPath path2 = createSVGPath( "M 6 20 L 6 24", null, 1, BLACK );
        group.add(path2);

        final SVGPath path3 = createSVGPath( "M 20 20 L 20 24", null, 1, BLACK );
        group.add( path3 );

        final Circle circle =
                new Circle(5.417)
                        .setX(13.002)
                        .setY(5.916)
                        .setFillColor( ColorName.BLACK )
                        .setStrokeColor(ColorName.BLACK);
        group.add( circle );

        final SVGPath path4 = createSVGPath("M8.043,7.083c0,0,2.814-2.426,5.376-1.807s4.624-0.693,4.624-0.693\n" +
                "\t\t\tc0.25,1.688,0.042,3.75-1.458,5.584c0,0,1.083,0.75,1.083,1.5s0.125,1.875-1,3s-5.5,1.25-6.75,0S8.668,12.834,8.668,12\n" +
                "\t\t\ts0.583-1.25,1.25-1.917C8.835,9.5,7.419,7.708,8.043,7.083z", "#F0EFF0", 1, BLACK);
        group.add( path4 );

        return group;
    }

    private static Group timer() {

        final Group group = new Group();

        final SVGPath path1 = createSVGPath( "M 16 6 L 16 9" +
                "   M 21 7 L 19.5 10" +
                "   M 25 11 L 22 12.5" +
                "   M 26 16 L 23 16" +
                "   M 25 21 L 22 19.5" +
                "   M 21 25 L 19.5 22" +
                "   M 16 26 L 16 23" +
                "   M 11 25 L 12.5 22" +
                "   M 7 21 L 10 19.5" +
                "   M 6 16 L 9 16" +
                "   M 7 11 L 10 12.5" +
                "   M 11 7 L 12.5 10" +
                "   M 18 9 L 16 16 L 20 16", null, 1, BLACK );

        group.add( path1 );

        return group;
    }

    private static SVGPath createSVGPath( final String path,
                                          final String fillColor,
                                          final double fillAlpha,
                                          final String strokeColor ) {
        SVGPath result =  createSVGPath(path)
                .setFillAlpha(fillAlpha)
                .setStrokeColor( strokeColor );

        if ( null != fillColor ) {

            result.setFillColor(fillColor);

        }

        return result;
    }


    private static SVGPath createSVGPath( final String path ) {
        return new SVGPath(path)
                .setDraggable(false);
    }

}
