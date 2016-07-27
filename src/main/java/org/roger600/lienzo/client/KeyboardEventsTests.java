package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;

public class KeyboardEventsTests {

    private Layer layer;

    public KeyboardEventsTests( Layer layer) {
        this.layer = layer;
    }
    
    public void test( final LienzoPanel lienzoPanel ) {
       /*
        lienzoPanel.addKeyPressHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress( KeyPressEvent keyPressEvent ) {
                GWT.log("PRESS="+keyPressEvent.getUnicodeCharCode());
            }
        } );

        lienzoPanel.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( KeyDownEvent keyDownEvent ) {
                GWT.log( "DOWN=" + keyDownEvent.getNativeKeyCode() );
            }
        } );

        lienzoPanel.addKeyUpHandler( new KeyUpHandler() {
            @Override
            public void onKeyUp( KeyUpEvent keyUpEvent ) {
                GWT.log( "UP=" + keyUpEvent.getNativeKeyCode() );
            }
        } );*/

        Rectangle rectangle = new Rectangle( 400, 400 ).setFillColor( ColorName.BLACK );

        layer.add( rectangle );

    }
    
}
