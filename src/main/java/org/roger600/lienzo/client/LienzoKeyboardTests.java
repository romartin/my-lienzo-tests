package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import org.roger600.lienzo.client.panel.FocusableLienzoPanelView;

public class LienzoKeyboardTests /*implements EntryPoint*/ {

    private FocusableLienzoPanelView panel = new FocusableLienzoPanelView( 1200,900 );
    //private LienzoPanel panel = new LienzoPanel(1200, 900);
    private Layer layer = new Layer();

    private boolean listening = false;

    public void onModuleLoad()
    {
        if ( false ) {

            testGWTFocusPanel();

        } else if ( false ) {

            testWrapping();

        } else {

            testListenOnRootPanel();

        }

    }

    private void testListenOnRootPanel() {

        RootPanel.get().add( panel );

        panel.addMouseOverHandler( new MouseOverHandler() {
            @Override
            public void onMouseOver( MouseOverEvent mouseOverEvent ) {
                // GWT.log( "OVERRRR" );
                LienzoKeyboardTests.this.listening = true;
            }
        } );

        panel.addMouseOutHandler( new MouseOutHandler() {
            @Override
            public void onMouseOut( MouseOutEvent mouseOutEvent ) {
                // GWT.log( "OUTT" );
                LienzoKeyboardTests.this.listening = false;
            }
        } );

        RootPanel.get().addDomHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress( KeyPressEvent keyPressEvent ) {
                if ( LienzoKeyboardTests.this.listening ) {
                    GWT.log("PRESS="+keyPressEvent.getUnicodeCharCode());
                }
            }
        }, KeyPressEvent.getType() );

        RootPanel.get().addDomHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( KeyDownEvent keyDownEvent ) {
                if ( LienzoKeyboardTests.this.listening ) {
                    GWT.log( "DOWN=" + keyDownEvent.getNativeKeyCode() );
                }
            }
        }, KeyDownEvent.getType() );

        RootPanel.get().addDomHandler( new KeyUpHandler() {
            @Override
            public void onKeyUp( KeyUpEvent keyUpEvent ) {
                if ( LienzoKeyboardTests.this.listening ) {
                    GWT.log( "UP=" + keyUpEvent.getNativeKeyCode() );
                }
            }
        }, KeyUpEvent.getType() );

        layer.setTransformable(true);
        panel.add(layer);

        layer.add( new Rectangle( 50, 50 ) );
        layer.draw();

    }

    private void testWrapping() {
        FocusPanel focusPanel = new FocusPanel();
        focusPanel.getElement().getStyle().setWidth( 100, Style.Unit.PC );
        RootPanel.get().add(focusPanel);
        focusPanel.add( panel );

        focusPanel.addKeyPressHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress( KeyPressEvent keyPressEvent ) {
                GWT.log("PRESS="+keyPressEvent.getUnicodeCharCode());
            }
        } );

        focusPanel.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( KeyDownEvent keyDownEvent ) {
                GWT.log( "DOWN=" + keyDownEvent.getNativeKeyCode() );
            }
        } );

        focusPanel.addKeyUpHandler( new KeyUpHandler() {
            @Override
            public void onKeyUp( KeyUpEvent keyUpEvent ) {
                GWT.log( "UP=" + keyUpEvent.getNativeKeyCode() );
            }
        } );


        layer.setTransformable(true);
        panel.add(layer);

        layer.add( new Rectangle( 50, 50 ) );
        layer.draw();
    }

    private void testGWTFocusPanel() {

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
