package org.roger600.lienzo.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

public interface LienzoTestsResources extends ClientBundleWithLookup {

    public static final LienzoTestsResources INSTANCE = GWT.create( LienzoTestsResources.class );

    @ClientBundle.Source( "images/yamaha_logo_red.jpg" )
    DataResource yamahaLogoJPG();

    @ClientBundle.Source( "images/yamaha_logo_trans.png" )
    DataResource yamahaLogoPNG();

    @ClientBundle.Source( "images/envelope.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource envelopeIconSVG();

    @ClientBundle.Source( "images/event-end.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventEndIconSVG();

    @ClientBundle.Source( "images/event-end-nogrid.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventEndNoGridIconSVG();

    @ClientBundle.Source( "images/rectangle.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource rectangleSVG();

}

