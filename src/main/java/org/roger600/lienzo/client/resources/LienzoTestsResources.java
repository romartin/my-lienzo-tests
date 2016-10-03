package org.roger600.lienzo.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.DataResource;

public interface LienzoTestsResources extends ClientBundleWithLookup {

    public static final LienzoTestsResources INSTANCE = GWT.create( LienzoTestsResources.class );

    @ClientBundle.Source( "images/yamaha_logo_red.jpg" )
    DataResource yamahaLogoJPG();

    @ClientBundle.Source( "images/yamaha_logo_trans.png" )
    DataResource yamahaLogoPNG();

}

