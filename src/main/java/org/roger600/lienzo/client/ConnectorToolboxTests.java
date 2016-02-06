package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.nativetools.client.event.HandlerRegistrationManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;


public class ConnectorToolboxTests extends FlowPanel {
    
    private Layer layer;
    private Rectangle button;
    private OrthogonalPolyLine line;
    private Point2D lineStart;
    private final HandlerRegistrationManager h_manager         = new HandlerRegistrationManager();

    public ConnectorToolboxTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        WiresManager wires_manager = WiresManager.get(layer);

        button = new Rectangle(25, 25)
                .setFillColor(ColorName.BLACK);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                int x = event.getX();
                int y = event.getY();
                createDragConnector(x, y);
            }
        });
        
        // Shape 1.
        MultiPath shape1MultiPath = new MultiPath().rect(0, 0, 50, 50).setStrokeColor("#CC0000");
        WiresShape shape1 = wires_manager.createShape(shape1MultiPath);
        shape1.setX(100).setY(100);
        shape1.addChild(button, LayoutContainer.Layout.CENTER, -12.5, -12.5);

        // Shape 2.
        MultiPath shape2MultiPath = new MultiPath().rect(0, 0, 50, 50).setStrokeColor("#00CC00");
        WiresShape shape2 = wires_manager.createShape(shape2MultiPath);
        shape2.setX(400).setY(100);
        
        // Create shapes' magnets.
        wires_manager.createMagnets(shape1);
        wires_manager.createMagnets(shape2);

    }
    
    private void createDragConnector(int x, int y) {
        GWT.log("Creating connector...");


        lineStart = new Point2D(x, y);
        line = createLineEnd2End(x, y,  x + 1, y + 1);
        layer.add(line);
        
        h_manager.register(layer.addNodeMouseMoveHandler(new NodeMouseMoveHandler() {
            @Override
            public void onNodeMouseMove(NodeMouseMoveEvent event) {
                int x = event.getX();
                int y = event.getY();
                GWT.log("Moving to [" + x + ", " + y + "]");
                Point2D p = new Point2D(x, y);
                line.setPoint2DArray(new Point2DArray(lineStart, p));
                layer.batch();
            }
        }));

        h_manager.register(layer.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                int x = event.getX();
                int y = event.getY();
                GWT.log("Click at [" + x + ", " + y + "]");
                h_manager.removeHandler();
                lineStart = null;
                layer.remove(line);
                layer.batch();
            }
        }));
        
        layer.batch();
    }

    private void connect(Layer layer, MagnetManager.Magnets headMagnets, int headMagnetsIndex, MagnetManager.Magnets tailMagnets, int tailMagnetsIndex, WiresManager wires_manager,
                         final boolean tailArrow, final boolean headArrow)
    {
        WiresMagnet m0_1 = headMagnets.getMagnet(headMagnetsIndex);

        WiresMagnet m1_1 = tailMagnets.getMagnet(tailMagnetsIndex);

        double x0 = m0_1.getControl().getX();

        double y0 = m0_1.getControl().getY();

        double x1 = m1_1.getControl().getX();

        double y1 = m1_1.getControl().getY();

        OrthogonalPolyLine line = createLineEnd2End(x0, y0,  x1, y1);

        WiresConnector connector = wires_manager.createConnector(m0_1, m1_1, line,
                headArrow ? new SimpleArrow(20, 0.75) : null,
                tailArrow ? new SimpleArrow(20, 0.75) : null);

        connector.getDecoratableLine().setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLineEnd2End(final double x0, final double y0, final double x1, final double y1)
    {
        return createLine(x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);
    }

    private final OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
    
    private void log(String message) {
        // GWT.log(message);
    }

}
