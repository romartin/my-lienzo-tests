package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;


public class ContainerTests extends FlowPanel {

    private Layer layer;

    public ContainerTests(Layer layer) {
        this.layer = layer;
    }

    public void test() {
        WiresManager wires_manager = WiresManager.get(layer);

        IContainmentAcceptor acceptor = new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child)
            {
                return acceptContainment(parent, child);
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child)
            {
                if (parent == null)
                {
                    GWT.log("acceptContainment - Parent is NULL");
                    return true;
                }
                GWT.log("acceptContainment - Parent is " + parent.toString());
                return true;
                //return !parent.getContainer().getUserData().equals(child.getContainer().getUserData());
            }
        };

        wires_manager.setContainmentAcceptor( acceptor );
        wires_manager.getLayer().setContainmentAcceptor( acceptor );

        final double startX = 300;
        final double startY = 300;
        final double w = 100;
        final double h = 100;

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape startEventShape = wires_manager.createShape(startEventMultiPath);
        startEventShape.setX(startX).setY(startY).setDraggable(true).getContainer().setUserData("event");

        // Green task node.
        WiresShape taskNodeShape = wires_manager.createShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        taskNodeShape.setX(startX + 200).setY(startY).setDraggable(true).getContainer().setUserData("task");

    }

    private void log(String message) {
        // GWT.log(message);
    }

}