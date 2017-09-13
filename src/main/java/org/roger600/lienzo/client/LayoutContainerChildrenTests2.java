package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class LayoutContainerChildrenTests2 extends FlowPanel implements MyLienzoTest, HasButtons, HasMediators {

    private Layer layer;
    private WiresShape parentShape;
    private Rectangle rectangle;

    public void test(Layer _layer) {
        this.layer = _layer;

        final WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor() {
            
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children) {
                return true;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children) {
                return true;
            }
        });
        
        final MultiPath parentMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#000000");
        parentShape = new WiresShape(parentMultiPath).setDraggable(true);
        wires_manager.register( parentShape );
        TestsUtils.addResizeHandlers( parentShape );
        wires_manager.getMagnetManager().createMagnets(parentShape);

        rectangle = new Rectangle( 100, 100).setFillColor("#FF0000").setDraggable(false);
        parentShape.addChild(rectangle, WiresLayoutContainer.Layout.CENTER);

        //  Where to add click handler?

        parentShape.getPath().addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                GWT.log("CLICK ON PATH");
            }
        } );

        parentShape.getGroup().addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                GWT.log("CLICK ON GROUP");
            }
        } );

    }

    @Override
    public void setButtonsPanel( Panel panel ) {

    }


}
