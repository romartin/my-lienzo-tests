package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.AbstractWiresEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.ResizeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

public class ShapeResizeTests extends FlowPanel implements MyLienzoTest {

    private Layer layer;
    private IControlHandleList m_ctrls;

    public void test(Layer layer) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get( layer );

        final WiresShape shape1 = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );

        wires_manager.createMagnets( shape1 );

        shape1.setDraggable( true );
        shape1.setResizable( true )
            .addWiresResizeHandler( new ResizeHandler() {
                @Override
                public void onResizeStart( ResizeEvent resizeEvent ) {
                    log( "[Shape1] onResizeStart" );
                }

                @Override
                public void onResizeStep( ResizeEvent resizeEvent ) {
                    log( "[Shape1] onResizeStep" );
                }

                @Override
                public void onResizeEnd( ResizeEvent resizeEvent ) {
                    log( "[Shape1] onResizeEnd" );
                }
            } );


        addAnotherShape( wires_manager );

    }

    private void addAnotherShape( final WiresManager wires_manager ) {
        final WiresShape shape2 = wires_manager.createShape( new MultiPath().rect( 0, 0, 100, 100 )
                .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 300 ).setY( 300 );

        wires_manager.createMagnets( shape2 );

        shape2
                .setDraggable( true )
                .setResizable( true )
                .addWiresResizeHandler( new ResizeHandler() {
                    @Override
                    public void onResizeStart( ResizeEvent resizeEvent ) {
                        log( "[Shape2] onResizeStart" );
                    }

                    @Override
                    public void onResizeStep( ResizeEvent resizeEvent ) {
                        log( "[Shape2] onResizeStep" );
                    }

                    @Override
                    public void onResizeEnd( ResizeEvent resizeEvent ) {
                        log( "[Shape2] onResizeEnd" );
                    }
                } );
    }

    private void log( String s ) {
        // GWT.log( s );
    }

}
