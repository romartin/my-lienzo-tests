package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.LayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
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
        task.setX( 50 ).setY( 100 );
        WiresShape lane = lane( wiresManager );
        lane.setX( 250 ).setY( 100 );
    }

    private WiresShape task(WiresManager wiresManager) {
        MultiPath path = createRectangle( new MultiPath().setFillColor( ColorName.LIGHTGREY ), 150, 50, 5 );
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
        MultiPath path = createRectangle( new MultiPath(), 500, 200, 0 );
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

    private static MultiPath createRectangle( final MultiPath path,
                                              final double w,
                                              final double h,
                                              final double r ) {

        if ((w > 0) && (h > 0)) {
            if ((r > 0) && (r < (w / 2)) && (r < (h / 2))) {

                path.M(r, 0);

                path.L(w - r, 0);

                path.A( w , 0, w, r, r );

                path.L(w, h - r);

                path.A( w, h, w - r, h, r );

                path.L(r, h);

                path.A( 0, h, 0, h - r , r );

                path.L(0, r);

                path.A( 0, 0, r, 0, r );


            } else {

                path.rect(0, 0, w, h);

            }

            path.Z();

        }

        return path;

    }


}
