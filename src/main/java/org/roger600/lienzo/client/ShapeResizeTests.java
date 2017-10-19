package org.roger600.lienzo.client;

import java.util.Map;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresMoveHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class ShapeResizeTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private WiresManager wiresManager;
    private WiresShape rectangle;
    private WiresShape rectangle2;
    private WiresShape rectangle3;
    private WiresShape circle;

    @Override
    public void setButtonsPanel(Panel panel) {
        final Button button1 = new Button("Show magnets #R");
        button1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rectangle.getMagnets().show();
            }
        });
        panel.add(button1);
        final Button button2 = new Button("Show magnets #C");
        button2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                circle.getMagnets().show();
            }
        });
        panel.add(button2);
    }

    public void test(Layer layer) {

        wiresManager = WiresManager.get( layer );

        // Rectangle - no cornering
        rectangle = new WiresShape( new MultiPath().rect( 0, 0, 100, 100 )
                                            .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 100 ).setY( 100 );

        registerNewShape("R", rectangle);

        // Rectangle - cornering via ARC
        rectangle2 = new WiresShape( TestsUtils.rect(new MultiPath(), 100, 100, 10)
                                             .setStrokeColor( "#FFFFFF" ).setFillColor( "#CC0000" ) )
                .setX( 300 ).setY( 100 );

        registerNewShape("R2", rectangle2);

        // Rectangle - cornering via corner-radius attribute
        rectangle3 = new WiresShape( new MultiPath().rect(0, 0, 400, 400)
                                             .setCornerRadius(50)
                                             .setStrokeColor( "#FFFFFF" ).setFillColor( "#CCBB00" ) )
                .setX( 500 ).setY( 100 );

        registerNewShape("R3", rectangle3);

        circle = new WiresShape( new MultiPath().circle(50)
                                         .setStrokeColor( "#FFFFFF" ).setFillColor( "#0000FF" ) )
                .setX( 700 ).setY( 100 );

        registerNewShape("C", circle);

        // Primitive resizing.

        MultiPath p1 = createMultiPathShape();
        layer.add(p1.setX(100).setY(300));

        MultiPath c1 = createMultiPathCircle(0, 0, 50);
        layer.add(c1.setX(300).setY(300));

    }

    private MultiPath createMultiPathCircle(final double x,
                                            final double y,
                                            final double r ) {
        MultiPath m_multi = new MultiPath();

        final double c = r * 2;
        m_multi.A(x + r, y, x + r, y + r, r);
        m_multi.A(x + r, y + c, x, y + c, r);


        m_multi.A(x - r, y + c, x - r, y + r, r);
        m_multi.A(x - r, y, x, y, r);
        m_multi.Z();
        m_multi.setStrokeWidth(5).setStrokeColor("#0000CC").setDraggable(true);

        return addResize(m_multi);
    }

    private MultiPath createMultiPathShape() {
        MultiPath m_multi = new MultiPath();

        m_multi.M(100, 100);

        m_multi.L(200, 150);

        m_multi.L(300, 100);

        m_multi.L(250, 200);

        m_multi.L(300, 300);

        m_multi.L(200, 250);

        m_multi.L(100, 300);

        m_multi.L(150, 200);

        m_multi.Z();

        m_multi.setStrokeWidth(5).setStrokeColor("#0000CC").setDraggable(true);

        return addResize(m_multi);
    }

    private MultiPath addResize(final MultiPath m_multi) {

        m_multi.addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                IControlHandleList m_ctrls = null;

                if (event.isShiftKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles(IControlHandle.ControlHandleStandardType.RESIZE);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get( IControlHandle.ControlHandleStandardType.RESIZE);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show();
                        }
                    }
                }
                else if (event.isAltKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles( IControlHandle.ControlHandleStandardType.POINT);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get( IControlHandle.ControlHandleStandardType.POINT);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show();
                        }
                    }
                }
            }
        });

        return m_multi;
    }

    private void registerNewShape(final String name,
                                  final WiresShape shape) {
        wiresManager.register( shape );
        wiresManager.getMagnetManager().createMagnets( shape );

        shape
                .setDraggable( true );

        TestsUtils.addResizeHandlers( shape );

        shape.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #" + name + " [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        shape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart #" + name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #" + name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

        shape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #"+ name + " [x=" + event.getX() + ", y=" + event.getY()
                             + ", width=" + event.getWidth()
                             + ", height=" + event.getHeight() + "]" );
            }
        } );

    }

    private void log( String s ) {
        GWT.log( s );
    }

}