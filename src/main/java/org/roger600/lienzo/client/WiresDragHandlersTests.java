package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragStartHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class WiresDragHandlersTests extends FlowPanel implements MyLienzoTest, HasMediators {

    
    /*

        WIRES CONTAINER DRAG MOVE
        DRAG MOVE
        WIRES CONTAINER DRAG END
        DRAG END
        WIRES SHAPE DRAG END
        ALLOW
        AFTER CONTAINMENT

     */
    
    
    public void test(Layer layer) {

        final WiresManager wires_manager = WiresManager.get(layer);


        wires_manager.setDockingAcceptor(IDockingAcceptor.NONE);

        wires_manager.setContainmentAcceptor( new IContainmentAcceptor() {
            @Override
            public boolean containmentAllowed( WiresContainer wiresContainer, WiresShape[] children ) {
                GWT.log( "ALLOW" );
                return true;
            }

            @Override
            public boolean acceptContainment( WiresContainer wiresContainer, WiresShape[] children ) {
                GWT.log( "ACCEPT" );
                return true;
            }


        } );

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 400, 400)
                .setStrokeColor("#000000")
                .setFillColor( ColorName.WHITESMOKE);
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        parentShape.getContainer().setUserData( "parent" );
        wires_manager.register( parentShape );
        parentShape.setDraggable(true).setLocation(new Point2D(500, 200));
        wires_manager.getMagnetManager().createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100)
                .setStrokeColor("#CC0000")
                .setFillColor( "#CC0000" );
        final WiresShape childShape = new WiresShape(childMultiPath);
        childShape.getContainer().setUserData( "child" );
        wires_manager.register( childShape );
        childShape.setDraggable(true).setLocation(new Point2D(50, 200));
        wires_manager.getMagnetManager().createMagnets(childShape);

       childShape.addWiresDragStartHandler( new WiresDragStartHandler() {
           @Override
           public void onShapeDragStart( WiresDragStartEvent wiresDragStartEvent ) {
               GWT.log( "DRAG START" );

           }
       } );

        childShape.addWiresDragMoveHandler( new WiresDragMoveHandler() {
            @Override
            public void onShapeDragMove( WiresDragMoveEvent wiresDragMoveEvent ) {
                GWT.log( "DRAG MOVE" );
            }
        } );

        childShape.addWiresDragEndHandler( new WiresDragEndHandler() {
            @Override
            public void onShapeDragEnd( WiresDragEndEvent wiresDragEndEvent ) {
                final double x = wiresDragEndEvent.getShape().getContainer().getAttributes().getX();
                final double y = wiresDragEndEvent.getShape().getContainer().getAttributes().getY();
                GWT.log( "DRAG END at [" + x + ", " + y + "]" );
            }
        } );

    }
    

}
