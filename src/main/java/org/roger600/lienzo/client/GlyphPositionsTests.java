package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;

public class GlyphPositionsTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private static final double SIZE = 100;
    private static final double GLYPH_SIZE = 50;

    private Layer layer;

    public void test(Layer layer) {
        this.layer = layer;

        final WiresManager wires_manager = WiresManager.get(layer);

        final Group box1 = new Group()
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

        final Group box2 = new Group()
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


        // Rectangle glyph.
        final MultiPath rectanglePath = new MultiPath().rect(0, 0, SIZE, SIZE).setFillColor( ColorName.LIGHTGREY );
        final WiresShape rectangleShape = new WiresShape( rectanglePath );
        final Group rectangleGroup = doIt( rectangleShape );
        box1.add( rectangleGroup );


        // Circle glyph.
        final MultiPath circlePath = new MultiPath().circle( SIZE / 2 ).setFillColor( ColorName.LIGHTGREY );
        final WiresShape circleShape = new WiresShape( circlePath );
        final Group circleGroup = doIt( circleShape );
        box2.add( circleGroup );

    }

    private Group doIt( final WiresShape shape ) {
        final Group group = shape.getGroup().copy();
        final BoundingBox pbb = shape.getPath().getBoundingBox();

        // Scale, if necessary, to the given glyph size.
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

}
