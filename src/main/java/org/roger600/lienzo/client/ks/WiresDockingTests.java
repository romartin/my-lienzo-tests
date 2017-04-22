package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.HasMediators;
import org.roger600.lienzo.client.MyLienzoTest;

public class WiresDockingTests extends FlowPanel implements MyLienzoTest, HasMediators {

    public void test(Layer layer) {

        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        
        wires_manager.setDockingAcceptor(new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed(WiresContainer parent, WiresShape child) {
                return acceptDocking( parent, child );
            }

            @Override
            public boolean acceptDocking(WiresContainer parent, WiresShape child) {
                final String pd = getUserData(parent);
                final String cd = getUserData(child);
                return "parent".equals(pd) && "dock".equals(cd);
            }

            @Override
            public int getHotspotSize() {
                return IDockingAcceptor.HOTSPOT_SIZE;
            }

            private String getUserData(WiresContainer shape) {
                return (null != shape && null != shape.getContainer() &&
                        null != shape.getContainer().getUserData()) ?
                        shape.getContainer().getUserData().toString() : null;
            }

        });

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
