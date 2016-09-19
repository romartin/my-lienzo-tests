package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class BoundingBoxTests extends FlowPanel implements MyLienzoTest {


    /*
        BB RECTANGLE = [0, 0, 50, 50]
        BB CIRCLE = [-50, -50, 100, 100]
     */
    public void test(Layer layer) {

       // testRectangle( layer );

        testCircle( layer );

    }

    private void testRectangle( Layer layer ) {

        Rectangle rectangle = new Rectangle( 50, 50 )
                .setX( 100 )
                .setY( 100 )
                .setFillColor( ColorName.RED );

        layer.add( rectangle );

        BoundingBox bb = rectangle.getBoundingBox();
        final double bbx = bb.getX();
        final double bby = bb.getY();
        final double bbw = bb.getWidth();
        final double bbh = bb.getHeight();

        GWT.log( " BB RECTANGLE = [" + bbx + ", " + bby + ", " + bbw + ", " + bbh + "]" );

    }

    private void testCircle( Layer layer ) {

        Circle circle = new Circle( 50 )
                .setX( 0  )
                .setY( 0  )
                .setFillColor( ColorName.RED );

        layer.add( circle );

        BoundingBox bb = circle.getBoundingBox();
        final double bbx = bb.getX();
        final double bby = bb.getY();
        final double bbw = bb.getWidth();
        final double bbh = bb.getHeight();

        GWT.log( " BB CIRCLE = [" + bbx + ", " + bby + ", " + bbw + ", " + bbh + "]" );

    }

    private void log( String s ) {
        // GWT.log( s );
    }

}
