package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class WiresDragAndMoveTests extends FlowPanel implements MyLienzoTest, HasMediators {

    public void test(Layer _layer) {
        final Layer layer = _layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        MultiPath parentMultiPath1 = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#FF0000");
        final WiresShape parentRedShape = new WiresShape(parentMultiPath1);
        wires_manager.register( parentRedShape );
        parentRedShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(parentRedShape);

        addHandlers( "ParentRed", parentRedShape );

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        final WiresShape parentShape = new WiresShape(parentMultiPath);
        wires_manager.register( parentShape );
        parentShape.setDraggable(true).setLocation(new Point2D(200, 200));
        wires_manager.getMagnetManager().createMagnets(parentShape);

        addHandlers( "ParentBlack", parentShape );

        MultiPath childMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#CC00CC");
        final WiresShape childShape = new WiresShape(childMultiPath);
        wires_manager.register( childShape );
        childShape.setDraggable(true);
        wires_manager.getMagnetManager().createMagnets(childShape);
        addHandlers( "Child", childShape );

        parentShape.add( childShape );

    }

    private void addHandlers( final String name,
                              final WiresShape shape ) {
        shape.addWiresDragStartHandler( new WiresDragStartHandler() {
            @Override
            public void onShapeDragStart( WiresDragStartEvent event ) {
                GWT.log( "[" + name + "] Drag START - [" + shape.getGroup().getX() + ", " + shape.getGroup().getY() + "] ");
            }
        } );

        shape.addWiresDragMoveHandler( new WiresDragMoveHandler() {
            @Override
            public void onShapeDragMove( WiresDragMoveEvent event ) {
                GWT.log( "[" + name + "] Drag MOVE - [" + shape.getGroup().getX() + ", " + shape.getGroup().getY() + "] ");
            }
        } );

        shape.addWiresDragEndHandler( new WiresDragEndHandler() {
            @Override
            public void onShapeDragEnd( WiresDragEndEvent event ) {
                GWT.log( "[" + name + "] Drag END - [" + shape.getGroup().getX() + ", " + shape.getGroup().getY() + "] ");
            }
        } );

        shape.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                GWT.log( "[" + name + "] MOVE - [" + shape.getGroup().getX() + ", " + shape.getGroup().getY() + "] ");
            }
        } );
    }

    private void addButton( Layer layer ) {
        Rectangle button = new Rectangle(50,50).setFillColor(ColorName.BLUE);
        button.setX(500).setY(500);
        button.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(final NodeMouseClickEvent event) {
                GWT.log("Click");

            }
        });
        layer.add(button);
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}
