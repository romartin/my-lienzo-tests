package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.SelectionListener;
import com.ait.lienzo.client.core.shape.wires.SelectionManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveHandler;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.google.gwt.core.client.GWT;
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

        wires_manager.getSelectionManager().setSelectionListener(new DelegateSelectionListener() {

            @Override
            protected void select(WiresShape shape) {
                GWT.log("SELECTED [" + shape.getGroup().getUserData().toString() + "]");
            }

            @Override
            protected void select(WiresConnector connector) {
                GWT.log("SELECTED CONNECTOR");
            }

            @Override
            protected void unselect(WiresShape shape) {
                GWT.log("UN-SELECTED [" + shape.getGroup().getUserData().toString() + "]");
            }

            @Override
            protected void unselect(WiresConnector connector) {
                GWT.log("UN-SELECTED CONNECTOR");
            }
        });

        final double startX = 300;
        final double startY = 300;
        final double w = 100;
        final double h = 100;

        MultiPath redPath = new MultiPath().rect(0, 0, w, h).setFillColor("#FF0000");
        WiresShape redShape = new WiresShape(redPath);
        wires_manager.register( redShape );
        redShape.setX(startX).setY(startY).setDraggable(true).getContainer().setUserData("red");

        WiresShape greenShape  = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register( greenShape );
        greenShape.setX(startX + 200).setY(startY).setDraggable(true).getContainer().setUserData("green");

        WiresShape parentShape  = new WiresShape(new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000"));
        wires_manager.register( parentShape );
        parentShape.setX(startX + 200).setY(startY + 200).setDraggable(true).getContainer().setUserData("parent");

        wires_manager.getMagnetManager().createMagnets(redShape);
        wires_manager.getMagnetManager().createMagnets(greenShape);
        wires_manager.getMagnetManager().createMagnets(parentShape);

        TestsUtils.addResizeHandlers( redShape );
        TestsUtils.addResizeHandlers( greenShape );
        TestsUtils.addResizeHandlers( parentShape );

        redShape.addWiresMoveHandler(new WiresMoveHandler() {
            @Override
            public void onShapeMoved(WiresMoveEvent event) {
                GWT.log("MOVE [RED] TO [" + event.getX() + ", " + event.getY() + "]");
            }
        });
        greenShape.addWiresMoveHandler(new WiresMoveHandler() {
            @Override
            public void onShapeMoved(WiresMoveEvent event) {
                GWT.log("MOVE [GREEN] TO [" + event.getX() + ", " + event.getY() + "]");
            }
        });
        parentShape.addWiresMoveHandler(new WiresMoveHandler() {
            @Override
            public void onShapeMoved(WiresMoveEvent event) {
                GWT.log("MOVE [PARENT] TO [" + event.getX() + ", " + event.getY() + "]");
            }
        });

        connect(layer,
                redShape.getMagnets(),
                3,
                greenShape.getMagnets(),
                7,
                wires_manager);
    }

    private static abstract class DelegateSelectionListener implements SelectionListener {

        protected abstract void select(WiresShape shape);
        protected abstract void select(WiresConnector connector);
        protected abstract void unselect(WiresShape shape);
        protected abstract void unselect(WiresConnector connector);

        @Override
        public void onChanged(SelectionManager.SelectedItems selectedItems) {
            SelectionManager.ChangedItems changed = selectedItems.getChanged();
            for (WiresShape shape : changed.getRemovedShapes())
            {
                unselect(shape);
            }

            for (WiresConnector connector : changed.getRemovedConnectors())
            {
                unselect(connector);
            }
            if (!selectedItems.isSelectionGroup() && selectedItems.size() == 1)
            {
                // it's one or the other, so attempt both, it'll short circuit if the first selects.
                if (selectedItems.getShapes().size() == 1)
                {
                    for (WiresShape shape : selectedItems.getShapes())
                    {
                        select(shape);
                        break;
                    }
                }
                else
                {
                    for (WiresConnector connector : selectedItems.getConnectors())
                    {
                        select(connector);
                        break;
                    }
                }
            }
            else if (selectedItems.isSelectionGroup())
            {
                // we don't which have selectors shown, if any. Just iterate and unselect all
                // null check will do nothing, if it's already unselected.
                for (WiresShape shape : selectedItems.getShapes())
                {
                    unselect(shape);
                }


                for (WiresConnector connector : selectedItems.getConnectors())
                {
                    unselect(connector);
                }
            }
        }
    }

    private void connect(Layer layer,
                         MagnetManager.Magnets magnets0,
                         int i0_1,
                         MagnetManager.Magnets magnets1,
                         int i1_1,
                         WiresManager wiresManager) {
        WiresMagnet m0_1 = (WiresMagnet) magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = (WiresMagnet) magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;

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

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer,
                          0,
                          0,
                          x0,
                          y0,
                          (x0 + ((x1 - x0) / 2)),
                          (y0 + ((y1 - y0) / 2)),
                          x1,
                          y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());
        line.setSelectionStrokeOffset(25);

        WiresConnector connector = new WiresConnector(m0_1,
                                                      m1_1,
                                                      line,
                                                      new MultiPathDecorator(head),
                                                      new MultiPathDecorator(tail));
        wiresManager.register(connector);

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine(Layer layer,
                                                double x,
                                                double y,
                                                final double... points) {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

}