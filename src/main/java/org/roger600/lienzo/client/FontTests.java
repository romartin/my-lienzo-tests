package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class FontTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private static final String FAMILY = "Verdana";
    private static final String COLOR = "#000000";
    private static final int SIZE = 10;
    private static final double BORDER_SIZE = 0.5d;

    public void setButtonsPanel( Panel panel ) {

    }

    public void test(Layer layer) {
        WiresManager wiresManager = WiresManager.get( layer );
        WiresShape task = task( wiresManager );
        task.setLocation(new Point2D(50, 100));
        WiresShape lane = lane( wiresManager );
        lane.setLocation(new Point2D(250, 100));
    }

    private WiresShape task(WiresManager wiresManager) {
        MultiPath path = TestsUtils.rect( new MultiPath().setFillColor( ColorName.LIGHTGREY ), 150, 50, 5 );
        final WiresShape shape = new WiresShape( path );
        Text taskText = new Text( "Open case" )
                .setFontFamily( FAMILY )
                .setFontSize( SIZE )
                .setStrokeWidth( BORDER_SIZE )
                .setStrokeColor( COLOR );
        shape.addChild( taskText, LayoutContainer.Layout.BOTTOM );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

    private WiresShape lane(WiresManager wiresManager) {
        MultiPath path = TestsUtils.rect( new MultiPath(), 500, 200, 0 );
        path
                .setFillColor( "#FFFFFF" )
                .setFillAlpha( 0.2 )
                .setStrokeColor( "#000000" )
                .setStrokeWidth( 0.8 );
        final WiresShape shape = new WiresShape( path );
        Text taskText = new Text( "Open case" )
                .setFontFamily( "Times New Roman" )
                .setFontSize( SIZE )
                .setStrokeWidth( BORDER_SIZE )
                .setStrokeColor( COLOR )
                .setRotation( 270 );
        shape.addChild( taskText, LayoutContainer.Layout.LEFT );
        wiresManager.register( shape );
        shape.setDraggable(true);
        wiresManager.getMagnetManager().createMagnets( shape );
        return shape;
    }

}
