package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragEndEvent;
import com.ait.lienzo.client.core.event.NodeDragEndHandler;
import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class DragHandlersTests extends FlowPanel implements MyLienzoTest, HasButtons {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private Layer layer;
    private WiresShape startShape;
    private WiresShape parentShape;
    private WiresShape childShape;

    public void test(Layer layer) {
        this.layer = layer;

        Rectangle rectangle = new Rectangle( 100, 100 ).setFillColor( ColorName.BLACK ).setDraggable( true );

        rectangle.addNodeDragEndHandler( h1 );
        rectangle.addNodeDragEndHandler( h2 );

        layer.add( rectangle );
    }

    NodeDragEndHandler h1 = new NodeDragEndHandler() {
        @Override
        public void onNodeDragEnd( NodeDragEndEvent nodeDragEndEvent ) {
            GWT.log( "H1" );

        }
    };

    NodeDragEndHandler h2 = new NodeDragEndHandler() {
        @Override
        public void onNodeDragEnd( NodeDragEndEvent nodeDragEndEvent ) {
            GWT.log( "H2" );
        }
    };

    @Override
    public void setButtonsPanel( Panel panel ) {

    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }
}