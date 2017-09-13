package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.SelectionManager;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.DragBounds;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragConstraint;
import com.google.gwt.user.client.ui.FlowPanel;

public class DragBoundsTests extends FlowPanel implements MyLienzoTest,
                                                          HasMediators {

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

    private void testRect( Layer layer ) {

        double w = 50;
        double h = 50;
        Rectangle rectangle = new Rectangle(w, h )
                .setX( 100 )
                .setY( 100 )
                .setFillColor(ColorName.RED );

        layer.add( rectangle );

        rectangle.setDraggable(true);

        DragBounds dragBounds = new DragBounds(0, 0,
                                               LienzoTests.WIDE - w, LienzoTests.HIGH - h);
        // rectangle.setDragBounds(dragBounds);

        rectangle.setDragConstraint(DragConstraint.HORIZONTAL);

    }

    private void testWires(final Layer layer ) {
        WiresManager wires_manager = WiresManager.get(layer);
        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wires_manager.setDockingAcceptor(IDockingAcceptor.ALL);

        wires_manager.enableSelectionManager();

        // Parent
        final WiresShape parent = new WiresShape(new MultiPath().rect(0, 0, 200, 200).setStrokeColor(ColorName.RED) );
        wires_manager.register(parent);
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
    }

    private class CustomShapeSelectionProvider extends SelectionManager.RectangleSelectionProvider {

        @Override
        public SelectionManager.RectangleSelectionProvider build() {
            super.build();
            getShape().setStrokeColor(ColorName.RED);
            return this;
        }
    }

}
