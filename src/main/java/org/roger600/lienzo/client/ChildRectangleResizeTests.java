package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeHandler;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class ChildRectangleResizeTests extends FlowPanel implements MyLienzoTest, HasButtons, HasMediators {

    WiresManager wires_manager;

    public void test(Layer layer) {

        this.wires_manager = WiresManager.get( layer );

        testAllLayouts( layer );

    }

    @Override
    public void setButtonsPanel( Panel panel ) {

        showLayoutButtons( panel );

    }

    private WiresShape currentShape;

    public void showLayoutButtons( Panel panel ) {

        WiresLayoutContainer.Layout[] layouts = new LayoutContainer.Layout[] {
                WiresLayoutContainer.Layout.TOP,
                WiresLayoutContainer.Layout.LEFT,
                WiresLayoutContainer.Layout.CENTER,
                WiresLayoutContainer.Layout.RIGHT,
                WiresLayoutContainer.Layout.BOTTOM
        };

        for ( final WiresLayoutContainer.Layout layout : layouts ) {

            Button button = new Button( layout.name() );

            button.addClickHandler( new ClickHandler() {
                @Override
                public void onClick( ClickEvent clickEvent ) {

                    if ( null != currentShape ) {

                        wires_manager.getLayer().remove( currentShape );

                    }

                    currentShape = create( 800, 400, 200, 200, "#CC00EE", 1, "#FFFFFF" );
                    Rectangle circle1 = new Rectangle( 50, 50).setFillColor("#00CCCC").setDraggable(false);
                    currentShape.addChild(circle1, layout);

                }
            } );

            panel.add( button );
        }

    }

    private void testAllLayouts( final Layer layer) {
        WiresShape shapeTop = create( 400, 50, 100, 100, "#CC0000", 1, "#FFFFFF" );
        Rectangle circle1 = new Rectangle( 50, 50).setFillColor("#0000CC").setDraggable(false);
        shapeTop.addChild(circle1, WiresLayoutContainer.Layout.TOP);

        WiresShape shapeLeft = create( 50, 200, 100, 100, "#00CC00", 1, "#FFFFFF" );
        Rectangle circle2 = new Rectangle( 50, 50).setFillColor("#0000CC").setDraggable(false);
        shapeLeft.addChild(circle2, WiresLayoutContainer.Layout.LEFT);

        WiresShape shapeCenter = create( 400, 200, 100, 100, "#0000CC", 1, "#FFFFFF" );
        Rectangle circle3 = new Rectangle( 50, 50).setFillColor("#CCCCCC").setDraggable(false);
        shapeCenter.addChild(circle3, WiresLayoutContainer.Layout.CENTER);

        final WiresShape shapeRight = create( 800, 200, 100, 100, "#0000FF", 1, "#FFFFFF" );
        final Rectangle circle4 = new Rectangle( 50, 50).setFillColor("#CC00CC").setDraggable(false);
        shapeRight.addChild(circle4, WiresLayoutContainer.Layout.RIGHT);

        WiresShape shapeBottom = create( 400, 400, 100, 100, "#FF00FF", 1, "#FFFFFF" );
        Rectangle circle5 = new Rectangle( 50, 50).setFillColor("#0000CC").setDraggable(false);
        shapeBottom.addChild(circle5, WiresLayoutContainer.Layout.BOTTOM);

        addResizeButton( layer, new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                circle4.setWidth( 60 ).setHeight( 30 );
                shapeRight.refresh();
                layer.batch();
            }
        } );
    }

    private void addResizeButton( Layer layer, NodeMouseClickHandler handler ) {
        Rectangle r = new Rectangle( 25, 25 ).setFillColor( ColorName.RED );
        r.addNodeMouseClickHandler( handler );
        layer.add( r );
    }

    private WiresShape create( final double x,
                               final double y,
                               final double w,
                               final double h,
                               final String fillColor,
                               final double fillAlpha,
                               final String strokeColor) {

        WiresShape endEventShape = new WiresShape( new MultiPath().rect( 0, 0, w, h )
                .setStrokeColor( strokeColor ).setFillColor( fillColor ).setFillAlpha( fillAlpha ) )
                .setX( x ).setY( y );
        wires_manager.register( endEventShape );
        wires_manager.getMagnetManager().createMagnets( endEventShape );

        TestsUtils.addResizeHandlers( endEventShape );

        endEventShape.setDraggable( true ).addWiresResizeHandler( new WiresResizeHandler() {
            @Override
            public void onShapeResized( WiresResizeEvent resizeEvent ) {
                log( "onShapeResized [x=" + resizeEvent.getX() + ", y=" + resizeEvent.getY()
                        + ", width=" + resizeEvent.getWidth()
                        + ", height=" + resizeEvent.getHeight() + "]" );
            }
        } );
        return endEventShape;
    }


    private void log(String s) {
        // GWT.log( s );
    }


}
