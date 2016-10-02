package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;


public class StandaloneConnectorsTests extends FlowPanel implements MyLienzoTest, HasMediators {

    public void test(Layer layer) {

        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.getMagnetManager().setHotspotSize( 10 );

        wires_manager.setConnectionAcceptor( IConnectionAcceptor.ALL );
        wires_manager.setDockingAcceptor( IDockingAcceptor.ALL );

        final double startX = 500;
        final double startY = 300;
        final double radius = 50;
        final double w = 100;
        final double h = 100;

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor( ColorName.BLUE );
        WiresShape startEventShape = new WiresShape(startEventMultiPath);
        wires_manager.register( startEventShape );
        Circle startEventCircle = new Circle(radius).setFillColor(ColorName.BLUE).setDraggable(false);
        startEventShape.setX(startX).setY(startY).getContainer().setUserData(ColorName.BLUE.getColorString());
        startEventShape.addChild(startEventCircle, WiresLayoutContainer.Layout.CENTER);
        wires_manager.getMagnetManager().createMagnets( startEventShape );
        startEventShape.setDraggable( true );
        TestsUtils.addResizeHandlers( startEventShape );

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.GREEN ));
        wires_manager.register( taskNodeShape );
        taskNodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( taskNodeShape );
        taskNodeShape.setX(startX + 200).setY(startY).getContainer().setUserData(ColorName.GREEN.getColorString());
        wires_manager.getMagnetManager().createMagnets(taskNodeShape);

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.YELLOW ));
        wires_manager.register( task2NodeShape );
        task2NodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( task2NodeShape );
        task2NodeShape.setX(startX + 200).setY(startY + 300).getContainer().setUserData(ColorName.YELLOW.getColorString());
        wires_manager.getMagnetManager().createMagnets(task2NodeShape);

        createConnector( layer, wires_manager, 0, 0, 100, 100 );

        createConnector( layer, wires_manager, 200, 200, 300, 300 );

    }

    private WiresConnector createConnector( Layer layer, WiresManager wiresManager, double... points ) {

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

        OrthogonalPolyLine line = createLine( layer, points );

        WiresConnector connector = new WiresConnector( null, null, line,
                new MultiPathDecorator(head),
                new MultiPathDecorator(tail));

        wiresManager.register( connector );

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");

        return connector;
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

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1-x0)/2)), (y0 + ((y1-y0)/2)), x1, y1);
        final double hh = head.getBoundingBox().getHeight();
        line.setHeadOffset( hh );
        final double th = tail.getBoundingBox().getHeight();
        line.setTailOffset( th );

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