package org.roger600.lienzo.client;

import java.util.Set;

import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.shape.AbstractDirectionalMultiPointShape;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.SelectionManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.DragBounds;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class DragBoundsTests extends FlowPanel implements MyLienzoTest,
                                                          HasMediators,
                                                          HasButtons {

    private WiresManager wires_manager;
    private Layer layer;
    private WiresShape taskNodeShape;

    public void test(Layer layer) {
        this.layer = layer;

        layer.add(new Rectangle(LienzoTests.WIDE, LienzoTests.HIGH)
                                         .setFillAlpha(0)
        .setStrokeColor(ColorName.BLACK)
        .setStrokeWidth(1));
        testWires(layer);
    }

    private void testWires(final Layer layer ) {
        wires_manager = WiresManager.get(layer);
        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wires_manager.setDockingAcceptor(IDockingAcceptor.ALL);

        wires_manager.enableSelectionManager();

        // Parent
        final WiresShape parent = new WiresShape(new MultiPath().rect(0, 0, 200, 200).setStrokeColor(ColorName.RED) );
        final WiresShapeControl parentShapeControl = wires_manager.register(parent);
        parent.setLocation(new Point2D(300, 100));
        parent.getContainer().setUserData("parent");
        wires_manager.getMagnetManager().createMagnets( parent );
        parent.setDraggable(true).setResizable(true);

        // Task node.
        taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, 50, 50).setFillColor(ColorName.BLACK) );
        final WiresShapeControl taskShapeControl = wires_manager.register(taskNodeShape);
        taskNodeShape.setLocation(new Point2D(50, 50));
        taskNodeShape.getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets( taskNodeShape );
        taskNodeShape.setDraggable(true).setResizable(true);

        Group taskGroup = taskNodeShape.getGroup();

        // final DragBounds dragBounds = new DragBounds(25, 25, LienzoTests.WIDE - w, LienzoTests.HIGH - h);
        // taskNodeShape.getGroup().setDragBounds(dragBounds);

        final DragBounds dragBounds = new DragBounds(0,
                                                     0,
                                                     LienzoTests.WIDE,
                                                     LienzoTests.HIGH);

        /*taskGroup.setDragConstraints(WiresDragConstraintEnforcer.enforce(taskNodeShape,
                                                                         dragBounds));*/
        /*taskGroup.setDragConstraints(new DragBoundsConstraintEnforcer(taskNodeShape.getGroup(),
                                                                      taskGroup.getDragConstraints()));*/

        final BoundingBox locBounds = new BoundingBox(0,
                                                      0,
                                                      LienzoTests.WIDE,
                                                      LienzoTests.HIGH);
        taskShapeControl.setBoundsConstraint(locBounds);
        parentShapeControl.setBoundsConstraint(locBounds);
        wires_manager.getSelectionManager().getControl().setBoundsConstraint(locBounds);

        wires_manager.getSelectionManager().setSelectionShapeProvider(new CustomShapeSelectionProvider());

        taskGroup.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove(NodeDragMoveEvent event) {
                double x1 = taskNodeShape.getGroup().getX();
                double y1 = taskNodeShape.getGroup().getY();
                // log(" GROUP [" + x1 + ", " + y1 + "]");
            }
        });


        connect(parent.getMagnets(), 0, taskNodeShape.getMagnets(), 0, wires_manager);
    }

    @Override
    public void setButtonsPanel(Panel panel) {
        final Button button1 = new Button("Log selected shapes locations");
        button1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                logSelectedShapeLocations();
            }
        });
        panel.add(button1);
    }

    private void logSelectedShapeLocations() {
        Set<WiresShape> shapes = wires_manager.getSelectionManager().getSelectedItems().getShapes();
        int i = 0;
        if (null != shapes) {
            for (WiresShape shape : shapes) {
                Object userData = shape.getGroup().getUserData();
                String id = null != userData ? userData.toString() : "shape[" + i +"]";
                Point2D location = shape.getGroup().getLocation();
                GWT.log("LOCATION - [" + id + "] [" + location + "]");
                i++;
            }
        }
    }

    private class CustomShapeSelectionProvider extends SelectionManager.RectangleSelectionProvider {

        @Override
        public SelectionManager.RectangleSelectionProvider build() {
            super.build();
            getShape().setStrokeColor(ColorName.RED);
            return this;
        }
    }

    private void connect(MagnetManager.Magnets magnets0,
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

        AbstractDirectionalMultiPointShape<?> line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        // Orthogonal.
        line = createPolyLine(
                x0,
                y0,
                x1,
                y1);
        // Directional.
        /*line = createDirectionalLine(
                x0,
                y0,
                x1,
                y1);*/
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

    private OrthogonalPolyLine createPolyLine(final double... points) {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
