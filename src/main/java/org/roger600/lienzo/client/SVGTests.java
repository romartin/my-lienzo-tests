package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.SVGPath;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.user.client.ui.FlowPanel;

public class SVGTests  extends FlowPanel {

    private Layer layer;

    public SVGTests(Layer layer) {
        this.layer = layer;
    }
    
    public void testSVG() {
        
        final String spec = "M 800.00,0.00 C 800.00,0.00 800.00,400.00 800.00,400.00  800.00,400.00 0.00,400.00 0.00,400.00 0.00,400.00 0.00,0.00 0.00,0.00 0.00,0.00 800.00,0.00 800.00,0.00 Z";

        final SVGPath path = new SVGPath(spec)
                .setStrokeColor(ColorName.BLUE)
                .setStrokeWidth(2)
                .setFillColor(ColorName.RED)
                .setX(50).setY(50)
                .setDraggable(true)
                .setDragMode(DragMode.SAME_LAYER)
                .setScale(1);

        layer.add(path);

    }
    
}
