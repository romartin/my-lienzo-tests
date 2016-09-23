package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class LayoutContainerChildrenTests extends FlowPanel implements MyLienzoTest, HasButtons, HasMediators {

    private Layer layer;
    private WiresShape parentShape;
    private Rectangle rectangle;
    private Circle circle;

    public void test(Layer _layer) {
        this.layer = _layer;

        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {
            
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child) {
                return true;
            }
        });
        
        final MultiPath parentMultiPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#000000");
        parentShape = new WiresShape(parentMultiPath);

        TestsUtils.addResizeHandlers( parentShape );

        addRectangle();

        addRCircle();

        wires_manager.register( parentShape );
        parentShape.setDraggable(true).setX(0).setY(0);
        wires_manager.getMagnetManager().createMagnets(parentShape);

    }

    @Override
    public void setButtonsPanel( Panel panel ) {

        Button buttonRR = new Button( "Remove Rect" );
        buttonRR.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                removeRectangle();
            }
        } );

        panel.add( buttonRR );

        Button buttonAR = new Button( "Add Rect" );
        buttonAR.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                addRectangle();
            }
        } );

        panel.add( buttonAR );

        Button buttonRC = new Button( "Remove Circle" );
        buttonRC.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                removeCircle();
            }
        } );

        panel.add( buttonRC );

        Button buttonAC = new Button( "Add Circle" );
        buttonAC.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                addRCircle();
            }
        } );

        panel.add( buttonAC );
    }

    private void addRectangle() {
        rectangle = new Rectangle( 50, 50).setFillColor("#0000CC").setDraggable(false);
        parentShape.addChild(rectangle, WiresLayoutContainer.Layout.CENTER);
        batch();
    }

    private void removeRectangle() {
        parentShape.removeChild( rectangle );
        batch();
    }

    private void addRCircle() {
        circle = new Circle( 50 ).setFillColor("#CCBB00").setDraggable(false);
        parentShape.addChild(circle, WiresLayoutContainer.Layout.RIGHT);
        batch();
    }

    private void removeCircle() {
        parentShape.removeChild( circle );
        batch();
    }

    private void batch() {
        layer.batch();
    }

}
