package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.SVGPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class CircleResizeTests extends FlowPanel {

    private Layer layer;
    private IControlHandleList m_ctrls;

    public CircleResizeTests(Layer layer) {
        this.layer = layer;
    }
    
    public void test() {

        WiresManager wires_manager = WiresManager.get(layer);

        // Circle.
        WiresShape endEventShape = wires_manager.createShape(new MultiPath().circle(100)
                .setX(100).setY(100)
                .setStrokeColor("#FFFFFF").setFillColor("#CC0000"));
        endEventShape.getGroup().setUserData("event");

        endEventShape.setResizable(true).addWiresHandler(AbstractWiresEvent.RESIZE, new ResizeHandler() {

            @Override
            public void onResizeStart(ResizeEvent resizeEvent) {
                GWT.log("onResizeStart");
            }

            @Override
            public void onResizeStep(ResizeEvent resizeEvent) {
                GWT.log("onResizeStep");
            }

            @Override
            public void onResizeEnd(ResizeEvent resizeEvent) {
                GWT.log("onResizeEnd");
            }
        });

    }
    
}
