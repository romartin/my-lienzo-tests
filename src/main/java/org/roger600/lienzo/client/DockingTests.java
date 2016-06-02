package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class DockingTests extends FlowPanel {

    private Layer layer;

    public DockingTests(Layer layer) {
        this.layer = layer;
    }

    public void test() {

        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {

            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child) {
                return false;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child) {
                return false;
            }
        });
        
        wires_manager.setDockingAcceptor(new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed(WiresContainer parent, WiresShape child) {
                return true;
            }

            @Override
            public boolean acceptDocking(WiresContainer parent, WiresShape child) {
                GWT.log( "acceptDocking - [x=" + child.getGroup().getX() + ", y=" + child.getGroup().getY() + "]" );
                return true;
            }
        });

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        final WiresShape parentShape = wires_manager.createShape(parentMultiPath);
        parentShape.setDraggable(true).setX(200).setY(200);
        wires_manager.createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#CC0000");
        final WiresShape childShape = wires_manager.createShape(childMultiPath);
        childShape.setDraggable(true).setX(600).setY(200);
        wires_manager.createMagnets(childShape);

        doDock( parentShape, childShape );
        
    }
    
    private void doDock( WiresShape parentShape , WiresShape child ) {

        child.removeFromParent();
        
        // Point2D absLoc = child.getGroup().getAbsoluteLocation();
        // Point2D trgAbsOffset = parentShape.getContainer().getAbsoluteLocation();
        // child.getGroup().setX(absLoc.getX() - trgAbsOffset.getX()).setY(absLoc.getY() - trgAbsOffset.getY());
        
        child.getGroup().setX( -50 ).setY( 50 );
        
        parentShape.add(child);

        child.setDockedTo(parentShape);
        
    }

}
