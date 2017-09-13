package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.ILocationAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.HasMediators;
import org.roger600.lienzo.client.MyLienzoTest;
import org.roger600.lienzo.client.TestsUtils;

public class WiresDockingTests extends FlowPanel implements MyLienzoTest,
                                                            HasMediators {

    public void test(Layer layer) {

        final WiresManager wires_manager = WiresManager.get(layer);

        final IContainmentAcceptor containmentAcceptor = new IContainmentAcceptor() {
            @Override
            public boolean containmentAllowed(WiresContainer parent,
                                              WiresShape[] children) {
                // GWT.log("ALLOW CONT = true");
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent,
                                             WiresShape[] children) {
                // GWT.log("ACCEPT CONT = true");
                return true;
            }
        };

        final IDockingAcceptor dockingAcceptor = new IDockingAcceptor() {
            @Override
            public boolean dockingAllowed(WiresContainer parent,
                                          WiresShape child) {
                final boolean b = checkDocking(parent,
                                               child);
                // GWT.log("ALLOW DOCK = " + b);
                return b;
            }

            @Override
            public boolean acceptDocking(WiresContainer parent,
                                         WiresShape child) {
                final boolean b = checkDocking(parent,
                                               child);
                // GWT.log("ACCEPT DOCK = " + b);
                return b;
            }

            @Override
            public int getHotspotSize() {
                return IDockingAcceptor.HOTSPOT_SIZE;
            }

            private boolean checkDocking(WiresContainer parent,
                                         WiresShape child) {
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

        // wires_manager.setLocationAcceptor(locationAcceptor);
        wires_manager.setContainmentAcceptor(containmentAcceptor);
        wires_manager.setDockingAcceptor(dockingAcceptor);

        /*
        wires_manager.setContainmentAcceptor(IContainmentAcceptor.NONE);
        wires_manager.getLayer().setContainmentAcceptor(IContainmentAcceptor.NONE);
        wires_manager.setDockingAcceptor(IDockingAcceptor.NONE);
        wires_manager.getLayer().setDockingAcceptor(IDockingAcceptor.NONE);
         */

        MultiPath parentMultiPath = new MultiPath().rect(0,
                                                         0,
                                                         400,
                                                         400)
                .setStrokeColor("#000000")
                .setFillColor(ColorName.WHITE);
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        parentShape.getContainer().setUserData("parent");
        wires_manager.register(parentShape);
        parentShape.setLocation(new Point2D(500d, 200d));
        parentShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(parentShape);
        TestsUtils.addResizeHandlers(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0,
                                                        0,
                                                        100,
                                                        100)
                .setStrokeColor("#CC0000")
                .setFillColor("#CC0000");
        final WiresShape childShape = new WiresShape(childMultiPath);
        childShape.getContainer().setUserData("child");
        wires_manager.register(childShape);
        childShape.setLocation(new Point2D(50d, 200d));
        childShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(childShape);
        TestsUtils.addResizeHandlers(childShape);

        MultiPath dockMultiPath = new MultiPath().rect(0,
                                                       0,
                                                       100,
                                                       100)
                .setStrokeColor("#CC00CC")
                .setFillColor("#CC00CC");
        final WiresShape dockShape = new WiresShape(dockMultiPath);
        dockShape.getContainer().setUserData("dock");
        wires_manager.register(dockShape);
        dockShape.setLocation(new Point2D(50d, 400d));
        dockShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(dockShape);
        TestsUtils.addResizeHandlers(dockShape);
    }
}
