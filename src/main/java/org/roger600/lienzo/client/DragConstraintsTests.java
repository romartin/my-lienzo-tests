package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.*;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.DragBounds;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class DragConstraintsTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    public void setButtonsPanel( Panel panel ) {

    }

    public void test(Layer layer) {
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
