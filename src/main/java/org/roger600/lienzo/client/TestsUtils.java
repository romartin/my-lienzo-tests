package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.wires.IControlHandle;
import com.ait.lienzo.client.core.shape.wires.IControlHandleList;
import com.ait.lienzo.client.core.shape.wires.WiresShape;

public class TestsUtils {

    public static void addResizeHandlers( final WiresShape shape ) {
        shape.setResizable( true ).getPath().addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                final IControlHandleList controlHandles = shape.getControls( IControlHandle.ControlHandleStandardType.RESIZE );

                if ( null != controlHandles ) {

                    if ( event.isShiftKeyDown() ) {
                        controlHandles.show();
                    } else {
                        controlHandles.hide();
                    }

                }
            }
        });
    }
}
