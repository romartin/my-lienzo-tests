package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class ShapeResizeTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private WiresManager wiresManager;
    private WiresShape rectangle;
    private WiresShape rectangle2;
    private WiresShape circle;

    @Override
    public void setButtonsPanel(Panel panel) {
        final Button button1 = new Button("Show magnets #R");
        button1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rectangle.getMagnets().show();
            }
        });
        panel.add(button1);
        final Button button2 = new Button("Show magnets #C");
        button2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                circle.getMagnets().show();
            }
        });
        panel.add(button2);
    }

    public void test(Layer layer) {

        wiresManager = WiresManager.get( layer );

        rectangle = new WiresShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );

        registerNewShape("R", rectangle);


        rectangle2 = new WiresShape( TestsUtils.rect(new MultiPath(), 100, 100, 10)
                                            .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 300 ).setY( 100 );

        registerNewShape("R2", rectangle2);

        circle = new WiresShape( new MultiPath().circle(50)
                                            .setStrokeColor( "#FFFFFF" ).setFillColor( "#0000FF" ) )
                .setX( 600 ).setY( 100 );

        registerNewShape("C", circle);

    }

    private void registerNewShape(final String name,
                                  final WiresShape shape) {
        wiresManager.register( shape );
        wiresManager.getMagnetManager().createMagnets( shape );

        shape
                .setDraggable( true );

        TestsUtils.addResizeHandlers( shape );

        shape.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #" + name + " [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart #" + name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #" + name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #"+ name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

    }

    private void log( String s ) {
        GWT.log( s );
    }

}
