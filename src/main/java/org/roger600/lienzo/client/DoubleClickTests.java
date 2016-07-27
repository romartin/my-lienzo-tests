package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseDoubleClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseDoubleClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class DoubleClickTests extends FlowPanel {

    private static final String BLACK = ColorName.BLACK.getColorString();

    private Layer layer;

    public DoubleClickTests( Layer layer) {
        this.layer = layer;
    }

    public void test() {

        layer.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                GWT.log("CLICK");
            }
        } );


        layer.addNodeMouseDoubleClickHandler( new NodeMouseDoubleClickHandler() {
            @Override
            public void onNodeMouseDoubleClick( NodeMouseDoubleClickEvent event ) {
                GWT.log("DOUBLE CLICK");
            }
        } );


    }

}