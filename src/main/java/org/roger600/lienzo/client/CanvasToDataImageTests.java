package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.ImageData;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.DataURLType;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class CanvasToDataImageTests implements EntryPoint {

    private LienzoPanel panel1 = new LienzoPanel(600, 600);
    private Layer layer1 = new Layer();


    private final Button button = new Button( "toImgData" );
    final PopupPanel popupPanel = new PopupPanel( true,  true );

    public void onModuleLoad()
    {

        button.addClickHandler( new ClickHandler() {

            @Override
            public void onClick( final ClickEvent clickEvent ) {
               /* final String data = layer1.toDataURL();
                showPopup( data );*/


               saveImageData();



            }

        } );

        drawLayout();

        drawThings();
        layer1.draw();

    }

    private void saveImageData() {
        final int size = 200;

        final LienzoPanel panel2 = new LienzoPanel( size, size );
        Layer layer2 = new Layer();
        layer2.setTransformable(true);
        panel2.add(layer2);

        final ImageData imageData = layer1.getContext().getImageData( 0, 0, size, size );

        layer2.getContext().putImageData( imageData, 0, 0 );

        final String data = layer2.toDataURL( DataURLType.PNG );
        showPopup( data );

    }

    private void showPopup( final String data ) {

        GWT.log("POPUP FOR [" + data + "]");

        popupPanel.clear();

        final Image i = new Image( data );
        i.getElement().getStyle().setBackgroundColor( "#0000FF" );
        popupPanel.add( i );

        popupPanel.center();

    }


    private void drawLayout() {
        final FlowPanel separator = new FlowPanel(  );
        separator.getElement().getStyle().setWidth( 50, Style.Unit.PX );;
        separator.getElement().getStyle().setHeight( 600, Style.Unit.PX );;
        separator.getElement().getStyle().setBackgroundColor( "#FF0000" );;

        final HorizontalPanel v = new HorizontalPanel();

        RootPanel.get().add( v );
        v.add( button );
        v.add( panel1 );
        v.add( separator );

        layer1.setTransformable(true);
        panel1.add(layer1);


    }

    private void drawThings() {

        final Rectangle r1 = new Rectangle( 50, 50 );
        r1.setX( 50 ).setY( 50 );

        final Rectangle r2 = new Rectangle( 100, 100);
        r2.setX( 150 ).setY( 150 );

        layer1.add( r1 );
        layer1.add( r2 );

    }

}
