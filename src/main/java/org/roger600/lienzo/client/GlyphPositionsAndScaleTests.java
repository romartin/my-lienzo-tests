package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;

public class GlyphPositionsAndScaleTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private static final double SIZE = 100;
    private static final double GLYPH_SIZE = 50;

    private Layer layer;
    private final Group box1 = new Group();
    private final Group box2 = new Group();
    private final Group box3 = new Group();
    private final Group box4 = new Group();

    public void test(Layer layer) {
        this.layer = layer;

        box1
                .setX( 100 )
                .setY( 200 );
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
                .setX( 400 )
                .setY( 200 );
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
                .setX( 700 )
                .setY( 200 );
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
                .setX( 400 )
                .setY( 400 );
        final Rectangle boxr4 = new Rectangle( GLYPH_SIZE, GLYPH_SIZE )
                .setX( 0 )
                .setY( 0 )
                .setFillAlpha( 0 )
                .setStrokeAlpha( 1 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.BLACK );
        box4.add( boxr4 );
        // layer.add( box4 );

        // Rectangle glyph.
        doRectangle();

        // Circle glyph.
        doCircle();

        // Polygon glyph.
        doPolygon();

    }

    private void doRectangle() {
        final MultiPath rectanglePath = new MultiPath().rect(0, 0, SIZE, SIZE).setFillColor( ColorName.LIGHTGREY );
        final WiresShape rectangleShape = new WiresShape( rectanglePath );
        final Group rectangleGroup = doIt( rectangleShape );
        box1.add( rectangleGroup );
    }

    private void doCircle() {
        final double radius = SIZE / 2;
        final MultiPath circlePath = new MultiPath().M( 0, -radius ).circle( radius ).setX( 0 ).setY( 0 ).setFillColor( ColorName.LIGHTGREY );
        final WiresShape circleShape = new WiresShape( circlePath );
        final Group circleGroup = doIt( circleShape );
        box2.add( circleGroup );
    }

    private void doPolygon() {
        final MultiPath m = new MultiPath().setFillColor( ColorName.LIGHTGREY );
        final MultiPath path = createPolygon( m, 4, SIZE / 2, 0 );
        final WiresShape shape = new WiresShape( path );
        final Group group = doIt( shape );
        box3.add( group );
    }

    private Group doIt( final WiresShape shape ) {
        final Group group = shape.getGroup().copy();

        // Group's bounding box top left point must be at 0,0 for all glyphs.
        final BoundingBox gbb = group.getBoundingBox();
        final double gx = group.getX();
        final double gy = group.getY();
        final double ngx = gx - ( gbb.getX() );
        final double ngy = gy - ( gbb.getY() );
        group.setX( ngx / 2 ).setY( ngy / 2 );

        // Scale, if necessary, to the given glyph size.
        final BoundingBox pbb = shape.getPath().getBoundingBox();
        final double[] scale = getScaleFactor( pbb.getWidth(), pbb.getHeight(), GLYPH_SIZE, GLYPH_SIZE );
        group.setScale( scale[0], scale[1] );

        return group;
    }

    private static double[] getScaleFactor( final double width,
                                           final double height,
                                           final double targetWidth,
                                           final double targetHeight) {

        return new double[] {
                width > 0 ? targetWidth / width : 1,
                height > 0 ? targetHeight / height : 1 };

    }

    private static MultiPath createPolygon( final MultiPath result,
                                     final int sides,
                                     final double radius,
                                    final double cornerRadius ) {

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

        return result;
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
