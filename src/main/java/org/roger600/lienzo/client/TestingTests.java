package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class TestingTests extends FlowPanel {

    private Layer layer;
    private Rectangle rectangle;

    public TestingTests(Layer layer) {
        this.layer = layer;
    }

    public void test() {

        buildButtons();
        testStates();
        testMediators();
        
    }

    public void testStates() {

        this.rectangle = new Rectangle(50, 50);
        rectangle.setX(200).setY(200).setFillColor(ColorName.BLACK);
        layer.add(rectangle);

        
        
    }

    public void testMediators() {

        
        layer.getViewport().pushMediator(new MouseWheelZoomMediator(EventFilter.SHIFT, EventFilter.CONTROL));



    }
    
    private void buildButtons() {
        Rectangle button1 = new Rectangle(100, 50);
        button1.setX(0).setY(0).setFillColor(ColorName.BLACK);
        button1.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("Saving state");
                layer.getContext().save();
                TestingTests.this.rectangle.setFillColor(ColorName.RED);
                layer.batch();
                
            }
        });
        layer.add(button1);
        Rectangle button2 = new Rectangle(100, 50);
        button2.setX(120).setY(0).setFillColor(ColorName.GREY);
        button2.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("Restoring state");
                layer.getContext().restore();
                layer.batch();
            }
        });
        layer.add(button2);
    }

}
