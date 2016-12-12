package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.image.ImageShapeLoadedHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.shared.core.types.ImageSelectionMode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class SVGPicturesTests implements MyLienzoTest, HasButtons {


    @Override
    public void test( final Layer layer ) {
        final String ds = LienzoTestsResources.INSTANCE.taskUserSVG().getSafeUri().asString();
        // GWT.log("SVGThatWorks - " + ds );
        final Picture picture = new Picture( ds );
        layer.add( picture );

    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        // final Image i = new Image( LienzoTestsResources.INSTANCE.taskUserSVG().getSafeUri().asString() );
        // panel.add( i );
    }
}
