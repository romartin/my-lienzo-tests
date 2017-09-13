package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragEndEvent;
import com.ait.lienzo.client.core.event.NodeDragEndHandler;
import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.event.NodeDragStartEvent;
import com.ait.lienzo.client.core.event.NodeDragStartHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresDragMoveHandler;
import com.ait.lienzo.client.core.types.DragBounds;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class DragConstraintsTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    public void setButtonsPanel( Panel panel ) {

    }

    public void test(Layer layer) {
        testWithPrimitives( layer );
        testWithWires( layer );
    }

    private void testWithWires( final Layer layer) {
        final WiresManager wiresManager = WiresManager.get( layer );
        final WiresShape rectangle = createRectangle( 50, 50, wiresManager );
        rectangle.setLocation(new Point2D(500 , 300));

        rectangle.addWiresDragMoveHandler( new WiresDragMoveHandler() {
            @Override
            public void onShapeDragMove( WiresDragMoveEvent wiresDragMoveEvent ) {
                double x = rectangle.getGroup().getX();
                double y = rectangle.getGroup().getY();
                GWT.log( "Move at [" + x + ", " + y + "]" );
                double tx = x < 200 ? 200 : x;
                double tx1 = tx > 700 ? 700 : tx;
                double ty = y < 200 ? 200 : y;
                double ty1 = ty > 700 ? 700 : ty;
                GWT.log( "Adjust at [" + tx1 + ", " + ty1 + "]" );
                rectangle.setLocation( new Point2D(tx1, ty1));
            }
        } );
    }

    private WiresShape createRectangle( final double w, final  double h, final WiresManager wiresManager ) {
        MultiPath path = new MultiPath().rect( 0, 0, w, h ).setFillColor( ColorName.YELLOW );
        final WiresShape shape = new WiresShape( path );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

    private void testWithPrimitives(Layer layer) {
        Rectangle parent = new Rectangle( 500, 500 )
                .setX( 200 )
                .setY( 200 )
                .setStrokeAlpha( 1 )
                .setStrokeColor( ColorName.BLACK )
                .setStrokeWidth( 5 )
                .setDraggable( false );

        Rectangle child = new Rectangle( 50, 50 )
                .setX( 300 )
                .setY( 300 )
                .setFillAlpha( 1 )
                .setFillColor( ColorName.BLACK )
                .setDraggable( true )
                .setDragBounds( new DragBounds( 200, 200, 700, 700 ) );

        child.addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                GWT.log("** Drag start **");
            }
        } );

        child.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                GWT.log("** Drag move **");
            }
        } );

        child.addNodeDragEndHandler( new NodeDragEndHandler() {
            @Override
            public void onNodeDragEnd( NodeDragEndEvent nodeDragEndEvent ) {
                GWT.log("** Drag end **");
            }
        } );

        layer.add( parent );
        layer.add( child );

    }

}
