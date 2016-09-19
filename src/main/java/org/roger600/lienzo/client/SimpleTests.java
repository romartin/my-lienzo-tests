package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.ShapeMovedEvent;
import com.ait.lienzo.client.core.shape.wires.event.ShapeMovedHandler;
import com.ait.lienzo.client.core.shape.wires.event.ShapeResizedEvent;
import com.ait.lienzo.client.core.shape.wires.event.ShapeResizedHandler;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class SimpleTests extends FlowPanel implements MyLienzoTest {


    public void test(Layer layer) {

        WiresManager wires_manager = WiresManager.get( layer );

        WiresShape shape1 = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );

        wires_manager.createMagnets( shape1 );

        Rectangle circle4 = new Rectangle( 50, 50).setFillColor("#CC00CC").setDraggable(false);
        shape1.addChild(circle4, WiresLayoutContainer.Layout.CENTER);

        shape1
                .setResizable( true )
                .setDraggable( true );

        shape1.addShapeMovedHandler( new ShapeMovedHandler() {
            @Override
            public void onShapeMoved( ShapeMovedEvent event ) {
                log( "onShapeMoved #1 [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape1.addShapeResizedHandler( new ShapeResizedHandler() {
            @Override
            public void onShapeResized( ShapeResizedEvent resizeEvent ) {
                log( "onShapeResized #1 [x=" + resizeEvent.getX() + ", y=" + resizeEvent.getY()
                        + ", width=" + resizeEvent.getWidth()
                        + ", height=" + resizeEvent.getHeight() + "]" );
            }
        } );

    }


    private void log( String s ) {
        GWT.log( s );
    }

}
