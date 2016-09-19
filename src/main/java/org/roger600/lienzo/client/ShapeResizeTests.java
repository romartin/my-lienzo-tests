package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.ShapeMovedEvent;
import com.ait.lienzo.client.core.shape.wires.event.ShapeMovedHandler;
import com.ait.lienzo.client.core.shape.wires.event.ShapeResizedEvent;
import com.ait.lienzo.client.core.shape.wires.event.ShapeResizedHandler;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class ShapeResizeTests extends FlowPanel implements MyLienzoTest {

    private Layer layer;
    private IControlHandleList m_ctrls;
    private WiresShape shape1;

    public void test(Layer layer) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get( layer );

        shape1 = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );

        wires_manager.createMagnets( shape1 );

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


        addAnotherShape( wires_manager );

        addButton();

    }

    private void addButton() {

        Rectangle button = new Rectangle( 25, 25 )
                .setX( 0 )
                .setY( 0 )
                .setFillColor( ColorName.RED );

        button.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                Point2DArray points = shape1.getPath().getAttributes().getPoints();
                GWT.log( points.toJSONString() );
            }
        } );

        layer.add( button );

    }

    private void addAnotherShape( final WiresManager wires_manager ) {
        final WiresShape shape2 = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 300 ).setY( 300 );

        wires_manager.createMagnets( shape2 );

        shape2
            .setResizable( true )
            .setDraggable( true );

        shape2.addShapeMovedHandler( new ShapeMovedHandler() {
            @Override
            public void onShapeMoved( ShapeMovedEvent event ) {
                log( "onShapeMoved #2 [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape2.addShapeResizedHandler( new ShapeResizedHandler() {
            @Override
            public void onShapeResized( ShapeResizedEvent resizeEvent ) {
                log( "onShapeResized #2 [x=" + resizeEvent.getX() + ", y=" + resizeEvent.getY()
                        + ", width=" + resizeEvent.getWidth()
                        + ", height=" + resizeEvent.getHeight() + "]" );
            }
        } );
    }

    private void log( String s ) {
        GWT.log( s );
    }

}
