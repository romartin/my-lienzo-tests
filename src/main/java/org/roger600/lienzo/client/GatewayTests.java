package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.RegularPolygon;
import com.ait.lienzo.client.core.shape.SVGPath;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.user.client.ui.FlowPanel;

public class GatewayTests extends FlowPanel {

    private Layer layer;

    public GatewayTests(Layer layer) {
        this.layer = layer;
    }
    
    public void test() {

        WiresManager wires_manager = WiresManager.get(layer);

        final int sides = 4;
        final int radius = 50;

        WiresShape polygonShape = wires_manager.createShape(new MultiPath().rect(0, 0, radius * 2, radius* 2));
        
        RegularPolygon polygon = new RegularPolygon(sides, radius)
                .setX(radius * 2)
                .setY(radius * 2)
                .setStrokeWidth(0)
                .setStrokeAlpha(0)
                .setFillColor("#f0e68c")
                .setFillAlpha(0.50)
                .setStrokeColor(ColorName.BLACK);

        
        polygonShape.addChild(polygon, WiresLayoutContainer.Layout.CENTER);


    }
    
}
