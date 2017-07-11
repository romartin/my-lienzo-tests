package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.*;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.google.gwt.user.client.ui.FlowPanel;


public class MediatorsTests2 extends FlowPanel implements MyLienzoTest {

    private Layer layer;
    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    public void test(Layer layer) {
        this.layer = layer;
        WiresManager wires_manager = WiresManager.get(layer);

        final double startX = 0;
        final double startY = 0;
        final double w = 100;
        final double h = 100;

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, w + 900 , h + 900).setStrokeColor("#000000");
        WiresShape parentShape = new WiresShape(parentMultiPath);
        wires_manager.register( parentShape );
        parentShape.setX( 300 ).setY( 300 ).getContainer().setUserData("parent");

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape startEventShape = new WiresShape(startEventMultiPath);
        wires_manager.register( startEventShape );
        startEventShape.setX(startX).setY(startY).getContainer().setUserData("event");
        wires_manager.getMagnetManager().createMagnets( startEventShape );

        parentShape.add( startEventShape );

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register( taskNodeShape );
        taskNodeShape.setX(startX + 200).setY(startY).getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets(taskNodeShape);

        parentShape.add( taskNodeShape );

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#FFEB52"));
        wires_manager.register( task2NodeShape );
        task2NodeShape.setX(startX + 200).setY(startY + 300).getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets(task2NodeShape);

        parentShape.add( task2NodeShape );

        // Red end event.
        WiresShape endEventShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#FFFFFF"));
        wires_manager.register( endEventShape );
        endEventShape.setX(startX + 400).setY(startY);
        endEventShape.getContainer().setUserData("event");
        wires_manager.getMagnetManager().createMagnets(endEventShape);

        parentShape.add( endEventShape );

        // Connector from blue start event to green task node.
        connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager);
        // Connector from green task node to red end event
        connect(layer, taskNodeShape.getMagnets(), 3, endEventShape.getMagnets(), 7, wires_manager);
        // Connector from blue start event to yellow task node.
        connect(layer, startEventShape.getMagnets(), 3, task2NodeShape.getMagnets(), 7, wires_manager);

        parentShape.setDraggable(true);
        startEventShape.setDraggable(true);
        taskNodeShape.setDraggable(true);
        task2NodeShape.setDraggable(true);
        endEventShape.setDraggable(true);

        addMediators();

    }

    private void addMediators() {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
    }

    private void connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager)
    {

        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;

        MultiPath head = new MultiPath();
        head.M(15, 20);
        head.L(0, 20);
        head.L(15/2, 0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15, 20);
        tail.L(0, 20);
        tail.L(15/2, 0);
        tail.Z();

// head.getAbsoluteLocation();

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1-x0)/2)), (y0 + ((y1-y0)/2)), x1, y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        WiresConnector connector = new WiresConnector(m0_1, m1_1, line,
                new MultiPathDecorator(head),
                new MultiPathDecorator(tail));

        wiresManager.register( connector );

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine( final Layer layer,
                                                 final double... points )
    {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add( line );
        return line;
    }

}