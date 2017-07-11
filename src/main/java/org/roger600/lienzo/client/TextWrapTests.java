package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.BOTTOM;
import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;
import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.LEFT;
import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.RIGHT;
import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.TOP;

public class TextWrapTests extends FlowPanel implements MyLienzoTest, HasButtons {

    private static final double SIZE = 100;

    private WiresShape wires_shape;
    private WiresManager wires_manager;
    private Text text;
    private Layer layer;

    public void test( final Layer layer ) {
        this.layer = layer;
        wires_manager = WiresManager.get( layer );

        text = new Text( "[" + SIZE +", " + SIZE + "]" )
                .setFontFamily( "Verdana" )
                .setFontSize( 12 )
                .setStrokeWidth( 1 )
                .setStrokeColor( ColorName.WHITE );

        wires_shape = center();

    }

    private void addResizeHandlers( final WiresShape shape ) {
        shape
            .setResizable( true )
            .getGroup()
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

        shape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                onShapeResize( event.getWidth(), event.getHeight() );
            }
        } );

        shape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( final WiresResizeStepEvent event ) {
                onShapeResize( event.getWidth(), event.getHeight() );
            }
        } );

        shape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( final WiresResizeEndEvent event ) {
                onShapeResize( event.getWidth(), event.getHeight() );
            }
        } );
    }

    private void onShapeResize( final double width, final double height ) {
        final String t = "[" + width + ", " + height + "]";
        // text.setText( t );
        updateTextWrapBoundaries();
    }

    private WiresShape create( String color, double size, LayoutContainer.Layout layout ) {
        text.setText( "[" + SIZE +", " + SIZE + "]" );
        final MultiPath path = new MultiPath().rect(0, 0, size, size)
                .setStrokeWidth(1)
                .setStrokeColor( color )
                .setFillColor( ColorName.LIGHTGREY );
        final WiresShape wiresShape0 =
                new WiresShape( path )
                        .setX(400)
                        .setY(200)
                        .setDraggable(true)
                        .addChild( new Circle( size / 4 ).setFillColor( color ), layout )
                        .addChild( text, CENTER );

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


        final Button textButton = new Button( "Add long text" );
        textButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                text
                        .setFillColor("#000000")
                        .setStrokeColor("#000000")
                        //.setText("S asdfasf asdfa asfd saf asfdas sfsadf asdfasfads adsfs afas fasd fsaf 34qr2354  asdfa sfd - --- af asdfas f sadfsad fas asdf F");
                        .setText("sadfffffffffffffdsaddddddddddddssssssssssssssssssffffffffffffffffffeeeeeeeeeeeeeee");
                layer.draw();

            }
        } );

        panel.add( textButton );


        final Button textWrapButton = new Button( "Wrap text" );
        textWrapButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                updateTextWrapBoundaries();

            }
        } );

        panel.add( textWrapButton );

    }

    private void updateTextWrapBoundaries() {
        BoundingBox boundingBox = wires_shape.getPath().getBoundingBox();
        GWT.log("Setting wrap boundaries for [" + boundingBox.getX() +
                        ", " + boundingBox.getX() +
                        ", " + boundingBox.getWidth() +
                        ", " + boundingBox.getHeight() + "]");
        // TODO text.setWrapBoundaries(boundingBox);
        layer.draw();
    }

    private WiresShape left() {
        return create( "#CC00CC", SIZE, LEFT );
    }

    private WiresShape right() {
        return create( "#0000CC", SIZE, RIGHT );
    }

    private WiresShape center() {
        return create( "#CC0000", SIZE, CENTER );
    }

    private WiresShape top() {
        return create( "#00CC00", SIZE, TOP );
    }

    private WiresShape bottom() {
        return create( "#CCCC00", SIZE, BOTTOM );
    }
}
