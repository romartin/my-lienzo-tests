package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class CircleResizeTests extends FlowPanel implements MyLienzoTest {

    private Layer layer;
    private IControlHandleList m_ctrls;

    public void test(Layer layer) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get( layer );

        // Circle DOES NOT WORK!
        WiresShape endEventShape = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );
        wires_manager.createMagnets( endEventShape );
        endEventShape.setDraggable( true ).setResizable( true ).addWiresHandler( AbstractWiresEvent.RESIZE, new ResizeHandler() {

            @Override
            public void onResizeStart( ResizeEvent resizeEvent ) {
                GWT.log( "onResizeStart" );
            }

            @Override
            public void onResizeStep( ResizeEvent resizeEvent ) {
                GWT.log( "onResizeStep" );
            }

            @Override
            public void onResizeEnd( ResizeEvent resizeEvent ) {
                GWT.log( "onResizeEnd" );
            }
        } );


    }

}
