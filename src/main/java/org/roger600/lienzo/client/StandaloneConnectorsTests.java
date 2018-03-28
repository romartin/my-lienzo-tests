package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;


public class StandaloneConnectorsTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private WiresConnector c1;
    private WiresConnectorControl control1;
    private WiresConnector c2;
    private WiresConnectorControl control2;
    private WiresConnector c3;
    private WiresConnectorControl control3;

    @Override
    public void setButtonsPanel( Panel panel ) {

        final Button showMagnetsButton1 = new Button( "Show ctrls #|" );
        showMagnetsButton1.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {

                control1.showControlPoints();

            }
        } );

        panel.add( showMagnetsButton1 );

        final Button showMagnetsButton2 = new Button( "Show ctrls #2" );
        showMagnetsButton2.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {

                control2.showControlPoints();

            }
        } );

        panel.add( showMagnetsButton2 );

        final Button showMagnetsButton3 = new Button( "Show ctrls #3" );
        showMagnetsButton3.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {

                control3.showControlPoints();

            }
        } );

        panel.add( showMagnetsButton3 );

    }

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
        startEventShape.setLocation(new Point2D(startX, startY));
        startEventShape.getContainer().setUserData(ColorName.BLUE.getColorString());
        startEventShape.addChild(startEventCircle, WiresLayoutContainer.Layout.CENTER);
        wires_manager.getMagnetManager().createMagnets( startEventShape );
        startEventShape.setDraggable( true );
        TestsUtils.addResizeHandlers( startEventShape );

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.GREEN ));
        wires_manager.register( taskNodeShape );
        taskNodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( taskNodeShape );
        taskNodeShape.setLocation(new Point2D(startX + 200, startY));
        taskNodeShape.getContainer().setUserData(ColorName.GREEN.getColorString());
        wires_manager.getMagnetManager().createMagnets(taskNodeShape);

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor( ColorName.YELLOW ));
        wires_manager.register( task2NodeShape );
        task2NodeShape.setDraggable( true );
        TestsUtils.addResizeHandlers( task2NodeShape );
        task2NodeShape.setLocation(new Point2D(startX + 200, startY + 300));
        task2NodeShape.getContainer().setUserData(ColorName.YELLOW.getColorString());
        wires_manager.getMagnetManager().createMagnets(task2NodeShape);

        c1 = createConnector( layer, wires_manager, 0, 0, 100, 100 );
        control1 = wires_manager.register( c1 );

        c2 = createConnector( layer, wires_manager, 200, 200, 300, 300 );
        control2 = wires_manager.register( c2 );

        // Connector from blue start event to green task node.
        c3 = connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager);
        control3 = wires_manager.register( c3 );

    }

    private WiresConnector createConnector( Layer layer, WiresManager wiresManager, double... points ) {
        return createConnector( layer, wiresManager, null, null, points );
    }

    private WiresConnector createConnector( Layer layer, WiresManager wiresManager, WiresMagnet headMagnet, WiresMagnet tailMagnet, double... points ) {

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

        WiresConnector connector = new WiresConnector( headMagnet, tailMagnet, line,
                new MultiPathDecorator(head),
                new MultiPathDecorator(tail));


        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");

        return connector;
    }

    private WiresConnector connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager) {

        WiresMagnet m0_1 = magnets0.getMagnet( i0_1 );
        WiresMagnet m1_1 = magnets1.getMagnet( i1_1 );

        double x0, x1, y0, y1;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();

        return createConnector( layer, wiresManager,  m0_1, m1_1, 0, 0, x0, y0, (x0 + ((x1-x0)/2)), (y0 + ((y1-y0)/2)), x1, y1 );

    }

    private final OrthogonalPolyLine createLine( final Layer layer,
                                                 final double... points )
    {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add( line );
        return line;
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}