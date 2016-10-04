package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class ImagesTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    private Layer layer;
    private Picture pictureJPG;
    private Picture picturePNG;

    public void setButtonsPanel( Panel panel ) {

        final Button scaleJPG = new Button( "Scale JPG/2" );
        scaleJPG.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                pictureJPG.setScale( 0.5 );
                layer.batch();
            }
        } );
        panel.add( scaleJPG );

        final Button scaleJPG2 = new Button( "Scale JPG*2" );
        scaleJPG2.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                pictureJPG.setScale( 2 );
                layer.batch();
            }
        } );
        panel.add( scaleJPG2 );

        final Button scalePNG = new Button( "Scale PNG/2" );
        scalePNG.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                picturePNG.setScale( 0.5 );
                layer.batch();
            }
        } );
        panel.add( scalePNG );

        final Button scalePNG2 = new Button( "Scale PNG*2" );
        scalePNG2.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                picturePNG.setScale( 2 );
                layer.batch();
            }
        } );
        panel.add( scalePNG2 );

    }

    public void test(Layer layer) {
        this.layer = layer;

        pictureJPG = new Picture( LienzoTestsResources.INSTANCE.yamahaLogoJPG().getSafeUri().asString() );
        pictureJPG.setX( 50 ).setY( 50 ).setDraggable( true );
        layer.add( pictureJPG );

        picturePNG = new Picture( LienzoTestsResources.INSTANCE.yamahaLogoPNG().getSafeUri().asString() );
        picturePNG.setX( 800 ).setY( 50 ).setDraggable( true );
        layer.add( picturePNG );

    }

}
