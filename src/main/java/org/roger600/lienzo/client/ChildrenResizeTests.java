package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class ChildrenResizeTests extends FlowPanel implements MyLienzoTest {

    private IControlHandleList m_ctrls;

    public void test(Layer layer) {

        WiresManager wires_manager = WiresManager.get( layer );

        WiresShape endEventShape = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 200 ).setY( 200 );
        wires_manager.createMagnets( endEventShape );

        Circle startEventCircle = new Circle(50).setFillColor("#0000CC").setDraggable(false);
        endEventShape.addChild(startEventCircle, WiresLayoutContainer.Layout.CENTER);

        endEventShape.setDraggable( true ).setResizable( true ).addWiresResizeHandler( new ResizeHandler() {

            @Override
            public void onResizeStart( ResizeEvent resizeEvent ) {
                log( "onResizeStart" );
            }

            @Override
            public void onResizeStep( ResizeEvent resizeEvent ) {
                log( "onResizeStep" );
            }

            @Override
            public void onResizeEnd( ResizeEvent resizeEvent ) {
                log( "onResizeEnd" );
            }
        } );


    }

    private void log(String s) {
        // GWT.log( s );
    }

}
