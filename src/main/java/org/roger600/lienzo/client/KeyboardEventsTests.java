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

        Rectangle rectangle = new Rectangle( 400, 400 ).setFillColor( ColorName.BLACK );

        layer.add( rectangle );

    }
    
}
