package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;


public class ContainerTests extends FlowPanel {
    
    private Layer layer;
    private WiresShape circleShape;
    private Circle circle;
    private WiresShape rectangleShape;
    private Rectangle rectangle;

    public ContainerTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child) {
                GWT.log("Doing acceptContaintment!");
                return true;
            }
        });
        
        final double startX = 300;
        final double startY = 300;

        // Circle.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, 50, 50).setStrokeColor("#000000");
        circleShape = wires_manager.createShape(startEventMultiPath);
        circleShape.getGroup().setX(startX).setY(startY).setUserData("circle");
        circle = new Circle(25).setFillColor("#0000CC").setDraggable(false);
        circleShape.setResizable(true).addChild(circle, WiresLayoutContainer.Layout.CENTER, 25, 25);

        // Rectangle.
        rectangleShape = wires_manager.createShape(new MultiPath().rect(0, 0, 100, 100));
        rectangleShape.getGroup().setX(startX + 200).setY(startY).setUserData("rectangle");
        rectangle = new Rectangle(100, 100).setFillColor("#FFFFFF").setDraggable(false);
        rectangleShape.setResizable(true).addChild(rectangle, WiresLayoutContainer.Layout.RIGHT);
        
        // Create shapes' magnets.
        wires_manager.createMagnets(circleShape);
        wires_manager.createMagnets(rectangleShape);

    }

    
    private void log(String message) {
        // GWT.log(message);
    }

}
