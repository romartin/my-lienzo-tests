package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class DeleteChildTests extends FlowPanel {

    private Layer layer;

    public DeleteChildTests(Layer layer) {
        this.layer = layer;
    }
    
    public void test() {

        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {
            
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child) {
                return true;
            }
        });
        
        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        final WiresShape parentShape = wires_manager.createShape(parentMultiPath);
        parentShape.setDraggable(true).setX(0).setY(0);
        wires_manager.createMagnets(parentShape);

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#CC0000");
        final WiresShape childShape = wires_manager.createShape(childMultiPath);
        childShape.setDraggable(true).setX(500).setY(0);
        wires_manager.createMagnets(childShape);


        Rectangle button = new Rectangle(50,50).setFillColor(ColorName.BLUE);
        button.setX(500).setY(500);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(final NodeMouseClickEvent event) {
                GWT.log("Deregistering shape " + childShape);
                wires_manager.deregisterShape(childShape);
                layer.batch();
            }
        });
        layer.add(button);
    }
    
}
