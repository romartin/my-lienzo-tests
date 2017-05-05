package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.HasMediators;
import org.roger600.lienzo.client.MyLienzoTest;

public class WiresDockingTests extends FlowPanel implements MyLienzoTest, HasMediators {

    public void test(Layer layer) {

        final WiresManager wires_manager = WiresManager.get(layer);

        final IContainmentAcceptor containmentAcceptor = new IContainmentAcceptor() {
            @Override
            public boolean containmentAllowed(WiresContainer parent,
                                              WiresShape child) {
                GWT.log("ALLOW CONT = true");
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent,
                                             WiresShape child) {
                GWT.log("ACCEPT CONT = true");
                return true;
            }
        };

        final IDockingAcceptor dockingAcceptor = new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed(WiresContainer parent, WiresShape child) {
                final boolean b = checkDocking( parent, child );
                GWT.log("ALLOW DOCK = " + b );
                return b;
            }

            @Override
            public boolean acceptDocking(WiresContainer parent, WiresShape child) {
                final  boolean b = checkDocking(parent, child);
                GWT.log("ACCEPT DOCK = " + b );
                return b;
            }

            @Override
            public int getHotspotSize() {
                return IDockingAcceptor.HOTSPOT_SIZE;
            }

            private boolean checkDocking(WiresContainer parent, WiresShape child) {
                final String pd = getUserData(parent);
                final String cd = getUserData(child);
                return "parent".equals(pd) && "dock".equals(cd);
            }

            private String getUserData(WiresContainer shape) {
                return (null != shape && null != shape.getContainer() &&
                        null != shape.getContainer().getUserData()) ?
                        shape.getContainer().getUserData().toString() : null;
            }

        };

        wires_manager.setContainmentAcceptor(containmentAcceptor);
        wires_manager.getLayer().setContainmentAcceptor(containmentAcceptor);
        wires_manager.setDockingAcceptor(dockingAcceptor);
        wires_manager.getLayer().setDockingAcceptor(dockingAcceptor);

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 400, 400)
                .setStrokeColor("#000000")
                .setFillColor( ColorName.WHITESMOKE);
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        parentShape.getContainer().setUserData( "parent" );
        wires_manager.register( parentShape );
        parentShape.setDraggable(true).setX(500).setY(200);
        wires_manager.getMagnetManager().createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100)
                .setStrokeColor("#CC0000")
                .setFillColor( "#CC0000" );
        final WiresShape childShape = new WiresShape(childMultiPath);
        childShape.getContainer().setUserData( "child" );
        wires_manager.register( childShape );
        childShape.setDraggable(true).setX(50).setY(200);
        wires_manager.getMagnetManager().createMagnets(childShape);

        MultiPath dockMultiPath = new MultiPath().rect(0, 0, 100, 100)
                .setStrokeColor("#CC00CC")
                .setFillColor( "#CC00CC" );
        final WiresShape dockShape = new WiresShape(dockMultiPath);
        dockShape.getContainer().setUserData( "dock" );
        wires_manager.register( dockShape );
        dockShape.setDraggable(true).setX(50).setY(400);
        wires_manager.getMagnetManager().createMagnets(dockShape);

    }
    

}
