package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.DragEvent;
import com.ait.lienzo.client.core.shape.wires.event.DragHandler;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.nativetools.client.util.Console;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.*;


public class WiresSquaresTests extends FlowPanel {
    
    private Layer layer;

    public WiresSquaresTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        WiresManager wires_manager = WiresManager.get(layer);

        double w = 100;

        double h = 100;
        
        double radius = 25;

        WiresShape wiresShape0 = wires_manager
                .createShape(click(new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CC0000")))
                .setX(400)
                .setY(400)
                .setDraggable(true)
                .addChild(new Circle(radius), CENTER);

        WiresShape wiresShape1 = wires_manager
                .createShape(click(new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#00CC00")))
                .setX(400)
                .setY(50)
                .setDraggable(true)
                .addChild(new Circle(radius), TOP);

        WiresShape wiresShape2 = wires_manager
                .createShape(click(new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#0000CC")))
                .setX(750)
                .setY(400)
                .setDraggable(true)
                .addChild(new Circle(radius), RIGHT);

        WiresShape wiresShape3 = wires_manager
                .createShape(click(new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CCCC00")))
                .setX(400)
                .setY(700)
                .setDraggable(true)
                .addChild(new Circle(radius), BOTTOM);

        WiresShape wiresShape4 = wires_manager
                .createShape(click(new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CC00CC")))
                .setX(50)
                .setY(400)
                .setDraggable(true)
                .addChild(new Circle(radius), LEFT);

        wires_manager.createMagnets(wiresShape0);

        wires_manager.createMagnets(wiresShape1);

        wires_manager.createMagnets(wiresShape2);

        wires_manager.createMagnets(wiresShape3);

        wires_manager.createMagnets(wiresShape4);
        
        connect(layer, wiresShape1, 4, 5, 6, wiresShape0, 2, 1, 8, wires_manager);

        connect(layer, wiresShape2, 6, 7, 8, wiresShape0, 4, 3, 2, wires_manager);

        connect(layer, wiresShape3, 8, 1, 2, wiresShape0, 6, 5, 4, wires_manager);

        connect(layer, wiresShape4, 2, 3, 4, wiresShape0, 8, 7, 6, wires_manager);
        
    }


    private MultiPath click(final MultiPath path)
    {
        path.addNodeMouseClickHandler(new NodeMouseClickHandler()
        {

            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                Console.get().info("click");
            }
        });
        return path;
    }

    private void connect(Layer layer, WiresShape shape0, int i0_0, int i0_1, int i0_2, WiresShape shape1, int i1_0, int i1_1, int i1_2, WiresManager wires_manager)
    {
        WiresMagnet m0_0 = shape0.getMagnets().getMagnet(i0_0);

        WiresMagnet m0_1 = shape0.getMagnets().getMagnet(i0_1);

        WiresMagnet m0_2 = shape0.getMagnets().getMagnet(i0_2);

        WiresMagnet m1_0 = shape1.getMagnets().getMagnet(i1_0);

        WiresMagnet m1_1 = shape1.getMagnets().getMagnet(i1_1);

        WiresMagnet m1_2 = shape1.getMagnets().getMagnet(i1_2);

        double x0, x1, y0, y1;

        double x0multi = 1;

        double x1multi = 1;

        double y0multi = 1;

        double y1multi = 1;

        x0 = m0_0.getControl().getX();

        y0 = m0_0.getControl().getY();

        x1 = m1_0.getControl().getX();

        y1 = m1_0.getControl().getY();

        if (y0 == y1)
        {
            x0multi = 0.9;

            x1multi = 1.1;
        }
        else
        {
            y0multi = 0.9;

            y1multi = 1.1;
        }
        OrthogonalPolyLine line;

        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1 - x0) / 2)) * x0multi, (y0 + ((y1 - y0) / 2)) * y0multi, x1, y1);

        wires_manager.createConnector(m0_0, m1_0, line);

        x0 = m0_1.getControl().getX();

        y0 = m0_1.getControl().getY();

        x1 = m1_1.getControl().getX();

        y1 = m1_1.getControl().getY();

        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);

        wires_manager.createConnector(m0_1, m1_1, line);

        x0 = m0_2.getControl().getX();

        y0 = m0_2.getControl().getY();

        x1 = m1_2.getControl().getX();

        y1 = m1_2.getControl().getY();

        line = createLine(layer, 0, 0, x0, y0, x0 + ((x1 - x0) / 2) * x1multi, y0 + ((y1 - y0) / 2) * y1multi, x1, y1);

        wires_manager.createConnector(m0_2, m1_2, line);
    }

    public static OrthogonalPolyLine createLine(final Layer layer, final double x, final double y, double... points)
    {
        final Point2DArray array = Point2DArray.fromArrayOfDouble(points);

        final OrthogonalPolyLine line = new OrthogonalPolyLine(array);

        line.setCornerRadius(5);

        line.setX(x).setY(y).setStrokeWidth(5).setStrokeColor("#0000CC");

        line.setDraggable(true);

        layer.add(line);

        return line;
    }

}
