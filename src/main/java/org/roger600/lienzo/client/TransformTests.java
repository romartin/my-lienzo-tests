package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.mediator.*;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;


public class TransformTests extends FlowPanel implements MyLienzoTest, HasButtons {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private Layer layer;
    private Rectangle rectangle;
    private Rectangle rectangle2;

    public void test(Layer layer) {
        this.layer = layer;

        rectangle = new Rectangle( 100, 100 )
                .setX( 100 )
                .setY( 100 )
                .setFillColor( ColorName.BLACK )
                .setDraggable( true );

        rectangle.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( NodeDragMoveEvent event ) {
                GWT.log( "MOVING TO [Dx=" + event.getDragContext().getDx() + ", Dy=" + event.getDragContext().getDy() + "]" );
            }
        } );
        layer.add( rectangle );

        addMediators();

    }

    private void addMediators() {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
    }


    @Override
    public void setButtonsPanel( Panel panel ) {

        Button b1 = new Button( "ViewPort Log" );
        b1.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                logTransform( "ViewPort -", layer.getViewport().getTransform() );
            }
        } );
        panel.add( b1 );

        Button b2 = new Button( "ViewPort Abs Log" );
        b2.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                logTransform( "ViewPort Abs -", layer.getViewport().getAbsoluteTransform() );
            }
        } );
        panel.add( b2 );

        Button b3 = new Button( "Rect Log" );
        b3.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                GWT.log( "Rectangle [x=" + rectangle.getX() + ", y=" + rectangle.getY() + ", w=" + rectangle.getWidth() + ", h=" + rectangle.getHeight() + "]" );
                GWT.log( "Rectangle location = " + rectangle.getLocation().toJSONString() );
                GWT.log( "Rectangle absolute location = " + rectangle.getAbsoluteLocation().toJSONString() );
            }
        } );
        panel.add( b3 );

        Button b4 = new Button( "Rect Trans Log" );
        b4.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                logTransform( "Rectangle -", rectangle.getNodeTransform() );
            }
        } );
        panel.add( b4 );

        Button b5 = new Button( "Rect Abs Trans Log" );
        b5.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                logTransform( "Rectangle Abs -", rectangle.getAbsoluteTransform() );
            }
        } );
        panel.add( b5 );

        Button b6 = new Button( "Create new rect" );
        b6.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                rectangle2 = new Rectangle( 100, 100 )
                        .setX( 500 )
                        .setY( 100 )
                        .setFillColor( ColorName.RED )
                        .setDraggable( true );
                layer.add( rectangle2 );
                layer.batch();
            }
        } );
        panel.add( b6 );

        Button b7 = new Button( "Rect2 Log" );
        b7.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                GWT.log( "Rectangle2 [x=" + rectangle2.getX() + ", y=" + rectangle2.getY() + ", w=" + rectangle2.getWidth() + ", h=" + rectangle2.getHeight() + "]" );
                GWT.log( "Rectangle2 location = " + rectangle2.getLocation().toJSONString() );
                GWT.log( "Rectangle2 absolute location = " + rectangle2.getAbsoluteLocation().toJSONString() );
            }
        } );
        panel.add( b7 );

    }

    private void logTransform( String title, Transform transform ) {
        if ( null !=  transform ) {
            double sx = transform.getScaleX();
            double sy = transform.getScaleY();
            double shx = transform.getShearX();
            double shy = transform.getShearY();
            double tx = transform.getTranslateX();
            double ty = transform.getTranslateY();
            GWT.log( title + " Scale x = " + sx );
            GWT.log( title + " Scale y = " + sy );
            GWT.log( title + " Shear x = " + shx );
            GWT.log( title + " Shear y = " + shy );
            GWT.log( title + " Transform x = " + tx );
            GWT.log( title + " Transform y = " + ty );
        } else {
            GWT.log( title + " Transform is NULL." );
        }
    }
}