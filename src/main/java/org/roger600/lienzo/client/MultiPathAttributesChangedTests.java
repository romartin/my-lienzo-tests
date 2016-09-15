package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.event.*;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.common.api.flow.Flows;
import com.google.gwt.core.client.GWT;

import java.util.Map;

import static com.ait.lienzo.client.core.AttributeOp.any;

public class MultiPathAttributesChangedTests implements MyLienzoTest {

    private static final Flows.BooleanOp XY_OP  = any( Attribute.X, Attribute.Y );

    private final IAttributesChangedBatcher attributesChangedBatcher = new AnimationFrameAttributesChangedBatcher();
    private IControlHandleList m_ctrls;
    private MultiPath    m_multi;
    private Layer layer;

    public void test( final Layer layer ) {
        this.layer = layer;

        m_multi = new MultiPath()
                .rect( 100, 100, 100, 100 )
                .setFillColor( ColorName.RED )
                .setDraggable( true );

        applyPathResize();

        layer.add( m_multi );

        addButtons( 0, 0 );

        initAttChangedHandler();
   }

    private void initAttChangedHandler()
    {
        m_multi.setAttributesChangedBatcher(attributesChangedBatcher);

        final AttributesChangedHandler handler = new AttributesChangedHandler()
        {
            @Override
            public void onAttributesChanged(AttributesChangedEvent event)
            {
                if (event.evaluate(XY_OP))
                {
                    GWT.log( "SHAPE MOVED TO [" + m_multi.getX() + ", "
                    + m_multi.getY() + "]!!");
                }
            }
        };

        // Attribute change handlers.
        m_multi.addAttributesChangedHandler(Attribute.X, handler);
        m_multi.addAttributesChangedHandler(Attribute.Y, handler);
    }

    private void addButtons( final double x, final double y ) {
        Rectangle moveButton = new Rectangle( 100, 50 )
                .setX( x )
                .setY( y )
                .setFillColor( ColorName.BLUE );

        moveButton.addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {

                GWT.log("Moving path...");
                m_multi.setX( 500 ).setY( 500 );
                layer.batch();
            }
        } );

        layer.add( moveButton );
    }

    private void applyPathResize() {
        m_multi.addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                if (event.isShiftKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles( IControlHandle.ControlHandleStandardType.RESIZE);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get( IControlHandle.ControlHandleStandardType.RESIZE);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show(layer);
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
                            m_ctrls.show(layer);
                        }
                    }
                }
            }
        });
    }
    
}
