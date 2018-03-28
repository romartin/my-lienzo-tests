package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Ring;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.user.client.ui.FlowPanel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;

public class WiresRingTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private WiresShape wiresShape0;
    private Ring ring;
    private Layer layer;

    public void test( final Layer layer ) {

        this.layer = layer;

        WiresManager wires_manager = WiresManager.get( layer );

        final double size = 100;

        final double r = size / 2;

        ring = new Ring( getInner( r ), getOuter( r ) );

        wiresShape0 =
                new WiresShape( new MultiPath().rect( 0, 0, size, size ) )
                        .setDraggable( true );
        wiresShape0.setLocation(new Point2D(200, 200));

        wiresShape0.addChild( ring, CENTER );

        wires_manager.register( wiresShape0 );
        wires_manager.getMagnetManager().createMagnets( wiresShape0 );
        addResizeHandlers( wiresShape0 );

        wiresShape0.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( WiresResizeStartEvent event ) {
                resize( event.getWidth(), event.getHeight() );
            }
        } );

        wiresShape0.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                resize( event.getWidth(), event.getHeight() );
            }
        } );

        wiresShape0.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                resize( event.getWidth(), event.getHeight() );
            }
        } );

    }

    private void resize( final double width, final double height ) {

        final double s = width >= height ? height : width;
        final double r = s / 2;
        final double o = getOuter( r );
        final double i = getInner( r );
        ring.setOuterRadius( o );
        ring.setInnerRadius( i );
        layer.batch();

    }

    private double getOuter( final double r ) {
        return r;
    }

    private double getInner( final double r ) {
        return r / 2;
    }

    private static void addResizeHandlers( final WiresShape shape ) {
        shape
                .setResizable( true )
                .getPath()
                .addNodeMouseClickHandler( new NodeMouseClickHandler() {
                    @Override
                    public void onNodeMouseClick( NodeMouseClickEvent event ) {
                        final IControlHandleList controlHandles = shape.loadControls( IControlHandle.ControlHandleStandardType.RESIZE );
                        if ( null != controlHandles ) {
                            if ( event.isShiftKeyDown() ) {
                                controlHandles.show();
                            } else {
                                controlHandles.hide();
                            }

                        }
                    }
                } );
    }

    @Override
    public int compareTo(MyLienzoTest other) {
        return this.getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
    }

}
