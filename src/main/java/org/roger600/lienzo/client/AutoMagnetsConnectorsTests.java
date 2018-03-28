package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class AutoMagnetsConnectorsTests extends FlowPanel implements MyLienzoTest,
                                                                     HasMediators,
                                                                     HasButtons {

    private WiresConnector c1;
    private WiresConnectorControl control1;

    @Override
    public void setButtonsPanel(Panel panel) {
    }

    public void test(Layer layer) {

        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.getMagnetManager().setHotspotSize(10);

        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wires_manager.setConnectionAcceptor(IConnectionAcceptor.ALL);
        wires_manager.setDockingAcceptor(IDockingAcceptor.ALL);

        // Blue rectangle.
        WiresShape blueRectShape = new WiresShape(new MultiPath().rect(100,
                                                                       100,
                                                                       100,
                                                                       100).setStrokeColor(ColorName.BLUE));
        wires_manager.register(blueRectShape);
        wires_manager.getMagnetManager().createMagnets(blueRectShape);
        blueRectShape.setDraggable(true);
        TestsUtils.addResizeHandlers(blueRectShape);

        // Green rectangle.
        WiresShape greenRectShape = new WiresShape(new MultiPath().rect(300,
                                                                        300,
                                                                        100,
                                                                        100).setFillColor(ColorName.GREEN));
        wires_manager.register(greenRectShape);
        wires_manager.getMagnetManager().createMagnets(greenRectShape);
        greenRectShape.setDraggable(true);
        TestsUtils.addResizeHandlers(greenRectShape);

        // Black rectangle.
        WiresShape blackRectShape = new WiresShape(new MultiPath().rect(500,
                                                                        100,
                                                                        300,
                                                                        300).setStrokeColor(ColorName.BLACK));
        wires_manager.register(blackRectShape);
        wires_manager.getMagnetManager().createMagnets(blackRectShape);
        blackRectShape.setDraggable(true);
        TestsUtils.addResizeHandlers(blackRectShape);

        c1 = connect(layer,
                     blueRectShape.getMagnets(),
                     3,
                     greenRectShape.getMagnets(),
                     7,
                     wires_manager);
        control1 = wires_manager.register(c1);
    }

    private WiresConnector createConnector(Layer layer,
                                           WiresManager wiresManager,
                                           double... points) {
        return createConnector(layer,
                               wiresManager,
                               null,
                               null,
                               points);
    }

    private WiresConnector createConnector(Layer layer,
                                           WiresManager wiresManager,
                                           WiresMagnet headMagnet,
                                           WiresMagnet tailMagnet,
                                           double... points) {

        MultiPath head = new MultiPath();
        head.M(15,
               20);
        head.L(0,
               20);
        head.L(15 / 2,
               0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15,
               20);
        tail.L(0,
               20);
        tail.L(15 / 2,
               0);
        tail.Z();

        OrthogonalPolyLine line = createLine(layer,
                                             points);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        WiresConnector connector = new WiresConnector(headMagnet,
                                                      tailMagnet,
                                                      line,
                                                      new MultiPathDecorator(head),
                                                      new MultiPathDecorator(tail));

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");

        return connector;
    }

    private WiresConnector connect(Layer layer,
                                   MagnetManager.Magnets magnets0,
                                   int i0_1,
                                   MagnetManager.Magnets magnets1,
                                   int i1_1,
                                   WiresManager wiresManager) {

        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();

        return createConnector(layer,
                               wiresManager,
                               m0_1,
                               m1_1,
                               0,
                               0,
                               x0,
                               y0,
                               (x0 + ((x1 - x0) / 2)),
                               (y0 + ((y1 - y0) / 2)),
                               x1,
                               y1);
    }

    private final OrthogonalPolyLine createLine(final Layer layer,
                                                final double... points) {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add(line);
        return line;
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}