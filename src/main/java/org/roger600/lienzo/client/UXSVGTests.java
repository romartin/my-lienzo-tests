package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class UXSVGTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private MultiPath picturePath;
    private WiresShape pictureShape;
    private Picture picture;

    @Override
    public void test( Layer layer ) {

        // final String uri = LienzoTestsResources.INSTANCE.envelopeIconSVG().getSafeUri().asString();
        final String uri = LienzoTestsResources.INSTANCE.eventEndIconSVG().getSafeUri().asString();
        picture = new Picture( uri );

        WiresManager wires_manager = WiresManager.get(layer);

        final double w = 150d;
        final double h = 150d;
        picturePath = new MultiPath().rect(0, 0, w, h)
                .setStrokeColor( ColorName.BLACK )
                .setStrokeWidth( 1 )
                .setStrokeAlpha( 1 );
        pictureShape = new WiresShape( picturePath );
        resize( w, h );
        pictureShape.addChild( picture , WiresLayoutContainer.Layout.CENTER );
        wires_manager.register( pictureShape );
        wires_manager.getMagnetManager().createMagnets( pictureShape );

        pictureShape
                .setDraggable( true );

        TestsUtils.addResizeHandlers( pictureShape );

        pictureShape.addWiresMoveHandler( new WiresMoveHandler() {
            @Override
            public void onShapeMoved( WiresMoveEvent event ) {
                log( "onShapeMoved #1 [x=" + event.getX() + ", y=" + event.getY() + "]" );
            }
        } );

        pictureShape.addWiresResizeStartHandler( new WiresResizeStartHandler() {
            @Override
            public void onShapeResizeStart( final WiresResizeStartEvent event ) {
                log( "onShapeResizeStart #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
                resize( event.getWidth(), event.getHeight() );
            }
        } );

        pictureShape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
                resize( event.getWidth(), event.getHeight() );
            }
        } );

        pictureShape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
                resize( event.getWidth(), event.getHeight() );
            }
        } );

    }

    public void resize( final double width,
                                final double height ) {
        scalePicture( width, height );
    }

    private void scalePicture( final double width,
                               final double height ) {
        final BoundingBox bb = picture.getBoundingBox();
        final double[] scale = getScaleFactor( bb.getWidth(), bb.getHeight(), width, height );
        log("** Applying scale factor [" + scale[0] + ", " + scale[1] + "]" );
        picture.setScale( scale[ 0 ], scale[ 1 ] );
    }

    private static double[] getScaleFactor( final double width,
                                           final double height,
                                           final double targetWidth,
                                           final double targetHeight ) {
        return new double[]{
                width > 0 ? targetWidth / width : 1,
                height > 0 ? targetHeight / height : 1 };

    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        // panel.add(  new Image( LienzoTestsResources.INSTANCE.envelopeIconSVG().getSafeUri().asString() ) );
    }

    private void log( String s ) {
        GWT.log( s );
    }
}
