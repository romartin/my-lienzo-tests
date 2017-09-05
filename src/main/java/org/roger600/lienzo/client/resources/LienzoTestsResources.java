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

    @ClientBundle.Source( "images/envelope.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource envelopeIconSVG();

    @ClientBundle.Source( "images/envelope_nogrid.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource envelopeNoGridIconSVG();

    @ClientBundle.Source( "images/event-end.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventEndIconSVG();

    @ClientBundle.Source( "images/event-end-nogrid.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventEndNoGridIconSVG();

    @ClientBundle.Source( "images/rectangle.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource rectangleSVG();

    @ClientBundle.Source( "images/task-user.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskUserSVG();

    @ClientBundle.Source( "images/task-script.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskScript();

    @ClientBundle.Source( "images/task-business-rule.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskBusinessRule();

    @ClientBundle.Source( "images/cancel.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource cancel();

    @ClientBundle.Source( "images/circle.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource circle();

    @ClientBundle.Source( "images/clock-o.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource clockO();

    @ClientBundle.Source( "images/event-end.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventEnd();

    @ClientBundle.Source( "images/event-intermediate.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventIntermediate();

    @ClientBundle.Source( "images/event-start.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource eventStart();

    @ClientBundle.Source( "images/lane.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource lane();

    @ClientBundle.Source( "images/parallel-event.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource parallelEvent();

    @ClientBundle.Source( "images/parallel_multiple.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource parallelMultiple();

    @ClientBundle.Source( "images/plus-square.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource plusQuare();

    @ClientBundle.Source( "images/sub-process.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource subProcess();

    @ClientBundle.Source( "images/p.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource p();

    /** Composite SVGs **/

    @ClientBundle.Source( "images/svgcomp/taskComposite.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskComposite();

    @ClientBundle.Source( "images/svgcomp/taskScriptComposite.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskScriptComposite();

    @ClientBundle.Source( "images/svgcomp/taskUserComposite.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource taskUserComposite();



    @ClientBundle.Source( "images/edit.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource edit();

    @ClientBundle.Source( "images/gears.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource gears();

    @ClientBundle.Source( "images/delete.svg" )
    @DataResource.MimeType("image/svg+xml")
    DataResource delete();

}

