package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class ImagesTests extends FlowPanel implements MyLienzoTest, HasMediators {

    public void test(Layer layer) {

        Picture pictureJPG = new Picture( LienzoTestsResources.INSTANCE.yamahaLogoJPG().getSafeUri().asString() );
        pictureJPG.setX( 50 ).setY( 50 ).setDraggable( true );
        layer.add( pictureJPG );

        Picture picturePNG = new Picture( LienzoTestsResources.INSTANCE.yamahaLogoPNG().getSafeUri().asString() );
        picturePNG.setX( 800 ).setY( 50 ).setDraggable( true );
        layer.add( picturePNG );

    }

}
