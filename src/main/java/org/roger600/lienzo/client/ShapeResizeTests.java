package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class ShapeResizeTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private Layer layer;
    private IControlHandleList m_ctrls;

    public void test(Layer layer) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get( layer );

        final WiresShape shape1 = new WiresShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );
        wires_manager.register( shape1 );
        wires_manager.getMagnetManager().createMagnets( shape1 );

        shape1
                .setDraggable( true );

        TestsUtils.addResizeHandlers( shape1 );

        shape1.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #1 [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape1.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape1.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape1.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        addButton1( shape1 );

        WiresShape shape2 = addAnotherShape( wires_manager );
        addButton2( shape2 );
    }

    private void addButton1( final WiresShape shape ) {

        Rectangle button = new Rectangle( 25, 25 )
                .setX( 0 )
                .setY( 0 )
                .setFillColor( ColorName.RED );

        button.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                shape.getMagnets().show();
            }
        } );

        layer.add( button );

    }

    private void addButton2( final WiresShape shape ) {

        Rectangle button = new Rectangle( 25, 25 )
                .setX( 0 )
                .setY( 50 )
                .setFillColor( ColorName.BLUE );

        button.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                shape.getMagnets().show();
            }
        } );

        layer.add( button );

    }

    private WiresShape addAnotherShape( final WiresManager wires_manager ) {
        final WiresShape shape2 = new WiresShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 300 ).setY( 300 );

        wires_manager.register( shape2 );
        wires_manager.getMagnetManager().createMagnets( shape2 );

        shape2
            .setDraggable( true );
        TestsUtils.addResizeHandlers( shape2 );

        shape2.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #2 [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape2.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart #2 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape2.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #2 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape2.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #2 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        return shape2;
    }

    private void log( String s ) {
        GWT.log( s );
    }

}
