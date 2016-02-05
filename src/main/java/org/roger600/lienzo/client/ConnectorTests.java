package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.SimpleArrow;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class ConnectorTests extends FlowPanel {

    private Layer layer;

    public ConnectorTests(Layer layer) {
        this.layer = layer;
    }
    
    public void test() {

        WiresManager wires_manager = WiresManager.get(layer);

        OrthogonalPolyLine line = createLine(0, 0, 100, 100);


       
        WiresConnector connector = wires_manager.createConnector(line,
                new SimpleArrow(20, 0.75),
                new SimpleArrow(20, 0.75));

        connector.getDecoratableLine().setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
}
