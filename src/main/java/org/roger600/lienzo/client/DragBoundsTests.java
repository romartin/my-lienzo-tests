package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.DragBounds;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.widget.DragConstraintEnforcer;
import com.ait.lienzo.client.widget.DragContext;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragConstraint;
import com.google.gwt.core.client.GWT;
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
        double x = 50;
        double y = 50;
        double w = 50;
        double h = 50;
        WiresManager wires_manager = WiresManager.get(layer);
        taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor(ColorName.RED) );
        wires_manager.register(taskNodeShape);
        taskNodeShape.setX(x).setY(y).getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets( taskNodeShape );
        taskNodeShape.setDraggable(true).setResizable(true);

        // final DragBounds dragBounds = new DragBounds(25, 25, LienzoTests.WIDE - w, LienzoTests.HIGH - h);
        // taskNodeShape.getGroup().setDragBounds(dragBounds);

        final DragBounds dragBounds = new DragBounds(0,
                                                     0,
                                                     LienzoTests.WIDE,
                                                     LienzoTests.HIGH);
        //final WiresDragConstraintEnforcer enforcer = new WiresDragConstraintEnforcer(taskNodeShape, dragBounds);
        taskNodeShape.getGroup().setDragConstraints(enforcer);

        taskNodeShape.getGroup().addNodeDragMoveHandler(new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove(NodeDragMoveEvent event) {
                double x1 = taskNodeShape.getGroup().getX();
                double y1 = taskNodeShape.getGroup().getY();
                // log(" GROUP [" + x1 + ", " + y1 + "]");
            }
        });
    }

    private DragConstraintEnforcer enforcer = new DragConstraintEnforcer() {

        double dragStartX = 0;
        double dragStartY= 0;
        double minX = 0;
        double minY = 0;
        double maxX = 0;
        double maxY = 0;

        @Override
        public void startDrag(DragContext dragContext) {
            //dragStartX = dragContext.getDragStartX();
            dragStartX = taskNodeShape.getGroup().getX();
            //dragStartY = dragContext.getDragStartY();
            dragStartY = taskNodeShape.getGroup().getY();
            log(" START [" + dragStartX + ", " + dragStartY + "]");
            minX = 0 - dragStartX;
            minY = 0 - dragStartY;
            maxX = LienzoTests.WIDE - 50 - dragStartX;
            maxY = LienzoTests.HIGH - 50 - dragStartY;

            double scaleX = layer.getViewport().getAbsoluteTransform().getScaleX();
            double scaleY = layer.getViewport().getAbsoluteTransform().getScaleY();
        }

        @Override
        public boolean adjust(Point2D dxy) {

            double x1 = taskNodeShape.getGroup().getX();
            double y1 = taskNodeShape.getGroup().getY();
            log(" GROUP [" + x1 + ", " + y1 + "]");
            double xx = dragStartX + dxy.getX();
            double yy = dragStartY + dxy.getY();
            log(" DRAG POS [" + xx + ", " + yy + "]");
            double scaleX = layer.getViewport().getAbsoluteTransform().getScaleX();
            double scaleY = layer.getViewport().getAbsoluteTransform().getScaleY();
            double dxxx = dxy.getX();
            double dxxy = dxy.getY();
            boolean adjust = false;
            if (dxxx <= minX ) {
                dxy.setX(minX);
                adjust = true;
            } else if (dxxy <= minY) {
                dxy.setY(minY);
                adjust = true;
            } else if (dxxx >= maxX) {
                dxy.setX(maxX);
                adjust = true;
            } else if (dxxy >= maxY) {
                dxy.setY(maxY);
                adjust = true;
            }
            log(" ADJUST [" + adjust + "]");
            return adjust;
        }

    };

    private void log( String s ) {
        GWT.log( s );
    }

}
