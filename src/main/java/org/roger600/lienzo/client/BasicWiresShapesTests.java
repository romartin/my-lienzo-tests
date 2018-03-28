package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Ring;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;

public class BasicWiresShapesTests extends FlowPanel implements MyLienzoTest {

    private static final double SIZE = 100;
    private static final double RADIUS = SIZE / 2;
    private static final double GLYPH_SIZE = 50;

    private WiresManager wiresManager;
    private final Group box1 = new Group();
    private final Group box2 = new Group();
    private final Group box3 = new Group();
    private final Group box4 = new Group();
    private final Group box5 = new Group();

    public void test(Layer layer) {

        this.wiresManager = WiresManager.get( layer );

        box1
                .setX( 100 )
                .setY( 400 );
        final Rectangle boxr1 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box1.add( boxr1 );
        layer.add( box1 );

        box2
                .setX( 300 )
                .setY( 400 );
        final Rectangle boxr2 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box2.add( boxr2 );
        layer.add( box2 );

        box3
                .setX( 500 )
                .setY( 400 );
        final Rectangle boxr3 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box3.add( boxr3 );
        layer.add( box3 );

        box4
                .setX( 700 )
                .setY( 400 );
        final Rectangle boxr4 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box4.add( boxr4 );
        layer.add( box4 );

        box5
                .setX( 900 )
                .setY( 400 );
        final Rectangle boxr5 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box5.add( boxr5 );
        layer.add( box5 );

        WiresShape rectangle = createRectangle();
        Group rectangleGlyph = createGlyph( rectangle );
        box1.add( rectangleGlyph );
        rectangle.setLocation(new Point2D(100, 100));
        setResizable( rectangle );

        WiresShape circle = createCircle();
        Group circleGlyph = createGlyph( circle );
        box2.add( circleGlyph );
        circle.setLocation(new Point2D(300, 100));

        WiresShape polygon =  createPolygon();
        Group polygonGlyph = createGlyph( polygon );
        box3.add( polygonGlyph );
        polygon.setLocation(new Point2D(500, 100));
        setResizable( polygon );

        WiresShape polygonWithIcon =  createPolygon();
        polygonWithIcon.addChild( new Circle( RADIUS / 3 ), LayoutContainer.Layout.CENTER );
        Group polygonIconGlyph = createGlyph( polygonWithIcon );
        box4.add( polygonIconGlyph );
        polygonWithIcon.setLocation(new Point2D(700, 100));
        setResizable( polygonWithIcon );

        WiresShape ringShape =  createRing();
        Ring ring = new Ring( RADIUS / 2, RADIUS );
        ringShape.addChild( ring, LayoutContainer.Layout.CENTER );
        Group ringGlyph = createGlyph( ringShape );
        box5.add( ringGlyph );
        ringShape.setLocation(new Point2D(900, 100));
        setResizable( ringShape );

    }

    private Group createGlyph( final WiresShape shape ) {
        final Group group = shape.getGroup().copy();

        // Scale, if necessary, to the given glyph size.
        final BoundingBox pbb = shape.getPath().getBoundingBox();
        final double[] scale = getScaleFactor( pbb.getWidth(), pbb.getHeight(), GLYPH_SIZE, GLYPH_SIZE );
        group.setScale( scale[0], scale[1] );

        return group;
    }

    private WiresShape createRing() {
        MultiPath path = createRing( new MultiPath(), RADIUS );
        final WiresShape shape = new WiresShape( path );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }


    private static MultiPath createRing( final MultiPath path, final double radius ) {
        return path.rect( 0, 0, radius * 2, radius * 2 )
                .setStrokeWidth( 0 )
                .setStrokeAlpha( 0 );
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
        MultiPath path = createRectangle( new MultiPath().setFillColor( ColorName.LIGHTGREY ), SIZE, SIZE, 0 );
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

    private static double[] getScaleFactor( final double width,
                                            final double height,
                                            final double targetWidth,
                                            final double targetHeight) {

        return new double[] {
                width > 0 ? targetWidth / width : 1,
                height > 0 ? targetHeight / height : 1 };

    }

    private void setResizable( WiresShape shape ) {
        TestsUtils.addResizeHandlers( shape );
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}