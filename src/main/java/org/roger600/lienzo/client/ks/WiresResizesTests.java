package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.tooling.nativetools.client.util.Console;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.HasButtons;
import org.roger600.lienzo.client.MyLienzoTest;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.*;

public class WiresResizesTests extends FlowPanel implements MyLienzoTest, HasButtons {

    private WiresShape wires_shape;
    private WiresManager wires_manager;

    public void test( final Layer layer ) {

       wires_manager = WiresManager.get(layer);

       wires_shape = center();

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

    private WiresShape create( String color, double size, LayoutContainer.Layout layout ) {
        WiresShape wiresShape0 =
                new WiresShape( new MultiPath().rect(0, 0, size, size).setStrokeWidth(5).setStrokeColor( color ) )
                        .setX(400)
                        .setY(200)
                        .setDraggable(true)
                        .addChild( new Circle( size / 4 ).setFillColor( color ), layout );

        wires_manager.register( wiresShape0 );
        wires_manager.getMagnetManager().createMagnets(wiresShape0);
        addResizeHandlers( wiresShape0 );
        return wiresShape0;
    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        final Button left = new Button( "Left" );
        left.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                if ( null != wires_shape ) {
                    wires_manager.deregister( wires_shape );
                    wires_shape = left();
                }
            }
        } );

        panel.add( left );

        final Button right = new Button( "Right" );
        right.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                if ( null != wires_shape ) {
                    wires_manager.deregister( wires_shape );
                    wires_shape = right();
                }
            }
        } );

        panel.add( right );

        final Button center = new Button( "center" );
        center.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                if ( null != wires_shape ) {
                    wires_manager.deregister( wires_shape );
                    wires_shape = center();
                }
            }
        } );

        panel.add( center );

        final Button bottom = new Button( "Bottom" );
        bottom.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                if ( null != wires_shape ) {
                    wires_manager.deregister( wires_shape );
                    wires_shape = bottom();
                }
            }
        } );

        panel.add( bottom );

        final Button top = new Button( "top" );
        top.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                if ( null != wires_shape ) {
                    wires_manager.deregister( wires_shape );
                    wires_shape = top();
                }
            }
        } );

        panel.add( top );
    }

    private WiresShape left() {
        return create( "#CC00CC", 100, LEFT );
    }

    private WiresShape right() {
        return create( "#0000CC", 100, RIGHT );
    }

    private WiresShape center() {
        return create( "#CC0000", 100, CENTER );
    }

    private WiresShape top() {
        return create( "#00CC00", 100, TOP );
    }

    private WiresShape bottom() {
        return create( "#CCCC00", 100, BOTTOM );
    }
}
