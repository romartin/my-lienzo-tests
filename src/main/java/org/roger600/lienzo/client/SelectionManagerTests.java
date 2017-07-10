package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.google.gwt.user.client.ui.FlowPanel;

public class SelectionManagerTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private Layer layer;

    public void test(Layer layer) {
        this.layer = layer;
        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor( IContainmentAcceptor.ALL );
        wires_manager.getLayer().setContainmentAcceptor( IContainmentAcceptor.ALL );
        wires_manager.setConnectionAcceptor(IConnectionAcceptor.ALL);

        wires_manager.enableSelectionManager();

        final double startX = 300;
        final double startY = 300;
        final double w = 100;
        final double h = 100;

        MultiPath whitePath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape whiteShape = new WiresShape(whitePath);
        wires_manager.register( whiteShape );
        whiteShape.setX(startX).setY(startY).setDraggable(true).getContainer().setUserData("white");

        WiresShape greenShape  = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register( greenShape );
        greenShape.setX(startX + 200).setY(startY).setDraggable(true).getContainer().setUserData("green");

    }

}