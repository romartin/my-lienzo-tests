package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.image.ImageShapeLoadedHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.shape.SVGPath;
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

    @Override
    public void test( Layer layer ) {
        addPictureWiresShape( layer, 0, 0, LienzoTestsResources.INSTANCE.envelopeNoGridIconSVG().getSafeUri().asString() );
        // addPictureShape( layer, 200, 200, LienzoTestsResources.INSTANCE.envelopeIconSVG().getSafeUri().asString() );
        // addSvgPath( layer, 200, 200 );
    }

    private void addPictureShape( final Layer layer,
                                  final double x,
                                  final double y,
                                  final String uri ) {
        Picture picture = new Picture( uri );
        layer.add( picture.setX( x ).setY( y ) );
    }

    private void addSvgPath( final Layer layer,
                             final double x,
                             final double y ) {
        final String p = "M224,47.9c-97.2,0-176,78.8-176,176s78.8,176,176,176s176-78.8,176-176S321.2,47.9,224,47.9z M224,447.9\n" +
                "\tc-123.7,0-224-100.3-224-224s100.3-224,224-224s224,100.3,224,224S347.7,447.9,224,447.9z";
        final SVGPath svgPath = new SVGPath( p );
        layer.add( svgPath.setX( x ).setY( y ) );
    }

    private void addPictureWiresShape( final Layer layer,
                                          final double x,
                                          final double y,
                                       final String uri ) {
        final Picture picture = new Picture( uri );

        WiresManager wires_manager = WiresManager.get(layer);

        final double w = 448d;
        final double h = 448d;
        final MultiPath picturePath = new MultiPath().rect(0, 0, w, h)
                .setStrokeColor( ColorName.BLACK )
                .setStrokeWidth( 1 )
                .setStrokeAlpha( 1 );
        final WiresShape pictureShape = new WiresShape( picturePath );
        resize( picture, w, h );
        pictureShape.addChild( picture );
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
                resize( picture, event.getWidth(), event.getHeight() );
            }
        } );

        pictureShape.addWiresResizeStepHandler( new WiresResizeStepHandler() {
            @Override
            public void onShapeResizeStep( WiresResizeStepEvent event ) {
                log( "onShapeResizeStep #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
                resize( picture, event.getWidth(), event.getHeight() );
            }
        } );

        pictureShape.addWiresResizeEndHandler( new WiresResizeEndHandler() {
            @Override
            public void onShapeResizeEnd( WiresResizeEndEvent event ) {
                log( "onShapeResizeEnd #1 [x=" + event.getX() + ", y=" + event.getY()
                        + ", width=" + event.getWidth()
                        + ", height=" + event.getHeight() + "]" );
                resize( picture, event.getWidth(), event.getHeight() );
            }
        } );
    }


    private void resize( final Picture picture,
                        final double width,
                        final double height ) {
        scalePicture( picture, width, height );
    }

    private void scalePicture( final Picture picture,
                               final double width,
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
