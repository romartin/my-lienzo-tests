package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;


public class ResizeTests extends FlowPanel {
    
    private Layer layer;
    private WiresShape circleShape;
    private Circle circle;
    private WiresShape rectangleShape;
    private Rectangle rectangle;

    public ResizeTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        WiresManager wires_manager = WiresManager.get(layer);

        final double startX = 300;
        final double startY = 300;
        final double w = 100;
        final double h = 100;

        // Circle.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        circleShape = wires_manager.createShape(startEventMultiPath);
        circleShape.getGroup().setX(startX).setY(startY).setUserData("event");
        circle = new Circle(25).setX(25).setY(25).setFillColor("#0000CC").setDraggable(false);
        circleShape.setResizable(true).addChild(circle, WiresPrimitivesContainer.Layout.CENTER);

        // Rectangle.
        rectangleShape = wires_manager.createShape(new MultiPath().rect(0, 0, w, h));
        rectangleShape.getGroup().setX(startX + 200).setY(startY).setUserData("task");
        rectangle = new Rectangle(50, 50).setFillColor("#0000CC").setDraggable(false);
        rectangleShape.setResizable(true).addChild(rectangle, WiresPrimitivesContainer.Layout.RIGHT);
        
        // Create shapes' magnets.
        wires_manager.createMagnets(circleShape);
        wires_manager.createMagnets(rectangleShape);

        // Connector from blue start event to green task node.
        connect(layer, circleShape.getMagnets(), 3, rectangleShape.getMagnets(), 7, wires_manager, true, false);

        /*circleShape.setDraggable(true).addWiresHandler(AbstractWiresEvent.DRAG, new DragHandler() {
            @Override
            public void onDragStart(DragEvent dragEvent) {
                final WiresShape shape = dragEvent.getShape();
                final double dx = dragEvent.getX();
                final double dy = dragEvent.getY();
                log("onDragStart#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy + "]");
            }

            @Override
            public void onDragMove(DragEvent dragEvent) {
                final WiresShape shape = dragEvent.getShape();
                final double dx = dragEvent.getX();
                final double dy = dragEvent.getY();
                log("onDragMove#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy + "]");
            }

            @Override
            public void onDragEnd(DragEvent dragEvent) {
                final WiresShape shape = dragEvent.getShape();
                final double dx = dragEvent.getX();
                final double dy = dragEvent.getY();
                log("onDragEnd#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy + "]");
            }
        });

        circleShape.setResizable(true).addWiresHandler(AbstractWiresEvent.RESIZE, new ResizeHandler() {

            private int sx;
            private double sr;
            
            @Override
            public void onResizeStart(final ResizeEvent resizeEvent) {
                final WiresShape shape = resizeEvent.getShape();
                final int index = resizeEvent.getControlIndex();
                final IPrimitive<?> control = shape.getControls().getHandle(index).getControl();
                final int rx = resizeEvent.getX();
                final int ry = resizeEvent.getY();
                this.sx = rx;
                this.sr = circle.getRadius();
                log("onResizeStart#RESIZE - [shape=" + shape  + ", x=" + rx + ", y=" + ry + ", control=" + control + "]");
            }

            @Override
            public void onResizeStep(final ResizeEvent resizeEvent) {
                final WiresShape shape = resizeEvent.getShape();
                final int index = resizeEvent.getControlIndex();
                final IPrimitive<?> control = shape.getControls().getHandle(index).getControl();
                final int rx = resizeEvent.getX();
                final int ry = resizeEvent.getY();
                *//*final double radius = ( sr * rx ) / sx;
                log("Circle RADIUS="+radius);
                circle.setRadius(radius);*//*
                log("onResizeStep#RESIZE - [shape=" + shape  + ", x=" + rx + ", y=" + ry + ", control=" + control + "]");
            }

            @Override
            public void onResizeEnd(final ResizeEvent resizeEvent) {
                final WiresShape shape = resizeEvent.getShape();
                final int index = resizeEvent.getControlIndex();
                final IPrimitive<?> control = shape.getControls().getHandle(index).getControl();
                final double rx = resizeEvent.getX();
                final double ry = resizeEvent.getY();
                log("onResizeEnd#RESIZE - [shape=" + shape  + ", x=" + rx + ", y=" + ry + ", control=" + control + "]");
            }
        });*/

        addButton(layer);
        
    }

    private Rectangle button;

    private void addButton(final Layer layer) {
        button = new Rectangle(50, 50).setFillColor(ColorName.BLACK);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                circle.setRadius(40);
                layer.batch();
            }
        });
        layer.add(button);
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

        OrthogonalPolyLine line = createLine(x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);

        WiresConnector connector = wires_manager.createConnector(m0_1, m1_1, line,
                headArrow ? new SimpleArrow(20, 0.75) : null,
                tailArrow ? new SimpleArrow(20, 0.75) : null);

        connector.getDecoratableLine().setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
    
    private void log(String message) {
        // GWT.log(message);
    }

}
