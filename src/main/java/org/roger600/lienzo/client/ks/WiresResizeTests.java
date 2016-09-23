package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.HasMediators;
import org.roger600.lienzo.client.MyLienzoTest;

import java.util.Map;

public class WiresResizeTests extends FlowPanel implements MyLienzoTest, HasMediators {

    private IControlHandleList m_ctrls;
    private MultiPath    m_multi;

    public void test( final Layer layer ) {

        m_multi = new MultiPath();

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

        layer.add(m_multi);

    }
    
}
