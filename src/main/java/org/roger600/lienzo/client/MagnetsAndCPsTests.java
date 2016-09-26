package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.mediator.*;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class MagnetsAndCPsTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    public void test(Layer layer) {

        WiresManager wires_manager = WiresManager.get( layer );

        final WiresShape parent = new WiresShape( new MultiPath().rect( 0, 0, 600, 600 )
                .setStrokeColor( "#000000" ).setFillColor( "#FFFFFF" ) )
                .setX( 100 ).setY( 100 );

        wires_manager.register( parent );
        wires_manager.getMagnetManager().createMagnets( parent );

        parent.setDraggable( true );
        TestsUtils.addResizeHandlers( parent );

        addLogging( "parent", parent );

        final WiresShape child1 = new WiresShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#000000" ).setFillColor( "#FF0000" ) )
                .setX( 100 ).setY( 100 );
        wires_manager.register( child1 );
        wires_manager.getMagnetManager().createMagnets( child1 );

        Rectangle circle1 = new Rectangle( 50, 50).setFillColor("#CC00CC").setDraggable(false);
        child1.addChild(circle1, WiresLayoutContainer.Layout.CENTER);

        child1.setDraggable( true );
        TestsUtils.addResizeHandlers( child1 );

        parent.add( child1 );

        layer.add( buildButton( 0, 0, ColorName.RED, new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                parent.getMagnets().show();
            }
        } ) );

        layer.add( buildButton( 0, 50, ColorName.BLUE, new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                child1.getMagnets().show();
            }
        } ) );

        addLogging( "child1", child1 );

        addMediators( layer );
    }

    private void addMediators( final Layer layer ) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
    }

    private void logShapeCoords( WiresShape shape ) {
        final double x = shape.getGroup().getX();
        final double y = shape.getGroup().getY();
        final double px = shape.getPath().getX();
        final double py = shape.getPath().getY();
        GWT.log( " GROUP AT [" + x + ", " + y + "]" );
        GWT.log( " PATH AT [" + px + ", " + py + "]" );
    }

    private Rectangle buildButton( final double x,
                              final double y,
                              final ColorName colorName,
                              final NodeMouseClickHandler handler ) {

        Rectangle button = new Rectangle( 25, 25 )
                .setX( x )
                .setY( y )
                .setFillColor( colorName );

        button.addNodeMouseClickHandler( handler );

        return button;
    }

    private void addLogging( final String s, final WiresShape shape ) {

        shape.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #" + s + " [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
            }
        } );

    }

    private void log( String s ) {
        GWT.log( s );
    }

}
