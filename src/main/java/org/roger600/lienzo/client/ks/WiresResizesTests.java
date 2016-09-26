package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.tooling.nativetools.client.util.Console;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.MyLienzoTest;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.*;

public class WiresResizesTests extends FlowPanel implements MyLienzoTest {

    public void test( final Layer layer ) {

        WiresManager wires_manager = WiresManager.get(layer);

        double w = 100;

        double h = 100;

        double radius = 25;

        WiresShape wiresShape0 =
                new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CC0000") )
                        .setX(400)
                        .setY(400)
                        .setDraggable(true)
                        .addChild(new Circle(radius).setFillColor( "#CC0000" ), CENTER);

        wires_manager.register( wiresShape0 );
        wires_manager.getMagnetManager().createMagnets(wiresShape0);
        addResizeHandlers( wiresShape0 );

        WiresShape wiresShape1 =
                new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#00CC00"))
                        .setX(400)
                        .setY(50)
                        .setDraggable(true)
                        .addChild(new Circle(radius).setFillColor( "#00CC00" ), TOP);

        wires_manager.register( wiresShape1 );
        wires_manager.getMagnetManager().createMagnets(wiresShape1);
        addResizeHandlers( wiresShape1 );

        WiresShape wiresShape2 =
                new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#0000CC"))
                        .setX(750)
                        .setY(400)
                        .setDraggable(true)
                        .addChild(new Circle(radius).setFillColor( "#0000CC" ), RIGHT);

        wires_manager.register( wiresShape2 );
        wires_manager.getMagnetManager().createMagnets(wiresShape2);
        addResizeHandlers( wiresShape2 );

        WiresShape wiresShape3 =
                new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CCCC00"))
                        .setX(400)
                        .setY(700)
                        .setDraggable(true)
                        .addChild(new Circle(radius).setFillColor( "#CCCC00" ), BOTTOM);

        wires_manager.register( wiresShape3 );
        wires_manager.getMagnetManager().createMagnets(wiresShape3);
        addResizeHandlers( wiresShape3 );

        WiresShape wiresShape4 =
                new WiresShape( new MultiPath().rect(0, 0, w, h).setStrokeWidth(5).setStrokeColor("#CC00CC"))
                        .setX(50)
                        .setY(400)
                        .setDraggable(true)
                        .addChild(new Circle(radius).setFillColor( "#CC00CC" ), LEFT);

        wires_manager.register( wiresShape4 );
        wires_manager.getMagnetManager().createMagnets(wiresShape4);
        addResizeHandlers( wiresShape4 );

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

}
