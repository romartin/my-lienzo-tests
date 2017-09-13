package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class DockingTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private Layer layer;

    public void test(Layer layer) {
        this.layer = layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {

            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children) {
                GWT.log("ALLOW CONT = true");
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children) {
                GWT.log("ACCEPT CONT = true");
                return true;
            }
        });

        wires_manager.setDockingAcceptor(new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed(WiresContainer parent, WiresShape child) {
                final boolean b = checkDocking( parent, child );
                GWT.log("ALLOW DOCKING = " + b);
                return b;
            }

            @Override
            public boolean acceptDocking(WiresContainer parent, WiresShape child) {
                final boolean b = checkDocking( parent, child );
                GWT.log( "ACCEPT DOCKING = " + b + " [x=" + child.getGroup().getX() + ", y=" + child.getGroup().getY() + "]" );
                return b;
            }

            @Override
            public int getHotspotSize() {
                return IDockingAcceptor.HOTSPOT_SIZE;
            }

            private boolean checkDocking(WiresContainer parent, WiresShape child) {
                final String pd = getUserData(parent);
                final String cd = getUserData(child);
                return "dock-source".equals(pd) && "dock-target".equals(cd);
            }

            private String getUserData(WiresContainer shape) {
                return (null != shape && null != shape.getContainer() &&
                        null != shape.getContainer().getUserData()) ?
                        shape.getContainer().getUserData().toString() : null;
            }

        });


        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        parentShape.getContainer().setUserData( "dock-source" );
        wires_manager.register( parentShape );
        parentShape.setDraggable(true).setLocation(new Point2D(200, 200));
        wires_manager.getMagnetManager().createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#CC0000");
        final WiresShape childShape = new WiresShape(childMultiPath);
        childShape.getContainer().setUserData( "dock-source" );
        wires_manager.register( childShape );
        childShape.setDraggable(true).setLocation(new Point2D(600, 200));
        wires_manager.getMagnetManager().createMagnets(childShape);

        MultiPath dockMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#0000FF");
        final WiresShape dockShape = new WiresShape(dockMultiPath);
        dockShape.getContainer().setUserData( "dock-target" );
        wires_manager.register( dockShape );
        dockShape.setDraggable(true).setLocation(new Point2D(600, 200));
        wires_manager.getMagnetManager().createMagnets(dockShape);

        doDock( parentShape, dockShape );
        
    }
    
    private void doDock( WiresShape parentShape , WiresShape child ) {

        child.removeFromParent();

        child.getGroup().setX( -50 ).setY( 50 );
        
        parentShape.add(child);

        child.setDockedTo(parentShape);
        
    }

}
