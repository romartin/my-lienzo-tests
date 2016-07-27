package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class LienzoKeyboardTests /*implements EntryPoint*/ {

    private LienzoPanel panel = new LienzoPanel(1200, 900);
    private Layer layer = new Layer();
    
    public void onModuleLoad()
    {
        RootPanel.get().add(panel);
        layer.setTransformable(true);
        panel.add(layer);

        KeyboardEventsTests tests = new KeyboardEventsTests(layer);
        tests.test(panel);
        layer.draw();

    }

    private void testKeyH() {

        FocusPanel mainPanel = new FocusPanel();
        RootPanel.get().add(mainPanel);

        mainPanel.getElement().getStyle().setWidth( 600, Style.Unit.PX );
        mainPanel.getElement().getStyle().setHeight( 600, Style.Unit.PX );

        mainPanel.addKeyPressHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress( KeyPressEvent keyPressEvent ) {
                GWT.log("PRESS="+keyPressEvent.getUnicodeCharCode());
            }
        } );

        mainPanel.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( KeyDownEvent keyDownEvent ) {
                GWT.log( "DOWN=" + keyDownEvent.getNativeKeyCode() );
            }
        } );

        mainPanel.addKeyUpHandler( new KeyUpHandler() {
            @Override
            public void onKeyUp( KeyUpEvent keyUpEvent ) {
                GWT.log( "UP=" + keyUpEvent.getNativeKeyCode() );
            }
        } );

    }
    
}
