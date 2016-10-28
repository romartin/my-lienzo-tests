package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Picture;
import com.ait.lienzo.client.core.shape.SVGPath;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.resources.LienzoTestsResources;

public class UXSVGTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons {

    @Override
    public void test( Layer layer ) {
        final SVGPath path = new SVGPath( getEnvelopeSVGPath() );
        path.setX( 100 ).setY( 100 );
        // layer.add( path );

        final String uri = LienzoTestsResources.INSTANCE.envelopeIconSVG().getSafeUri().asString();
        final Picture picture = new Picture( uri );
        picture.setX( 100 ).setY( 100 );
        layer.add( picture );
    }

    private String getEnvelopeSVGPath() {
        return "M448 177.5v198.5q0 16.5-11.75 28.25t-28.25 11.75h-368q-16.5 0-28.25-11.75t-11.75-28.25v-198.5q11 12.25 25.25 21.75 90.5 61.5 124.25 86.25 14.25 10.5 23.125 16.375t23.625 12 27.5 6.125h0.5q12.75 0 27.5-6.125t23.625-12 23.125-16.375q42.5-30.75 124.5-86.25 14.25-9.75 25-21.75zM448 104q0 19.75-12.25 37.75t-30.5 30.75q-94 65.25-117 81.25-2.5 1.75-10.625 7.625t-13.5 9.5-13 8.125-14.375 6.75-12.5 2.25h-0.5q-5.75 0-12.5-2.25t-14.375-6.75-13-8.125-13.5-9.5-10.625-7.625q-22.75-16-65.5-45.625t-51.25-35.625q-15.5-10.5-29.25-28.875t-13.75-34.125q0-19.5 10.375-32.5t29.625-13h368q16.25 0 28.125 11.75t11.875 28.25z";
    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        // panel.add(  new Image( LienzoTestsResources.INSTANCE.envelopeIconSVG().getSafeUri().asString() ) );
    }
}
