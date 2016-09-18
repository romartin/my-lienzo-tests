package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.roger600.lienzo.client.ks.WiresAlignDistroTests;
import org.roger600.lienzo.client.ks.WiresArrowsTests;
import org.roger600.lienzo.client.ks.WiresResizeTests;

public class LienzoTests implements EntryPoint {

    private final static MyLienzoTest[] TESTS = new MyLienzoTest[] {
            new SimpleTests(),
            new ShapeResizeTests(),
            new ChildRectangleResizeTests(),
            new ChildCircleResizeTests(),
            new ConnectionAcceptorsTests(),
            new DeleteChildTests(),
            new DockingTests(),
            new MarkConnectorTests(),
            new MediatorsTests(),
            new MediatorsTests2(),
            new WiresTests(),
            new MultiPathAttributesChangedTests(),
            // From Lienzo KS
            new WiresAlignDistroTests(),
            new WiresResizeTests(),
            new WiresArrowsTests()
    };

    private static final int MAX_BUTTONS_ROW = 7;
    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel buttonsPanel = new VerticalPanel();
    private HorizontalPanel screenButtonsPanel = new HorizontalPanel();
    private HorizontalPanel buttonsRowPanel;
    private int buttonsPanelSize = 0;
    private FlowPanel testsPanel = new FlowPanel();

    public void onModuleLoad()
    {
        buttonsPanel.getElement().getStyle().setMargin( 10, Style.Unit.PX );

        RootPanel.get().add( mainPanel );

        for ( final MyLienzoTest test : TESTS ) {

            final Button button = new Button( test.getClass().getSimpleName() );
            button.addClickHandler( new ClickHandler() {
                @Override
                public void onClick( ClickEvent clickEvent ) {
                    createPanelForTest( test );
                }
            } );

            addButton( button );

        }

        mainPanel.add( buttonsPanel );
        mainPanel.add( screenButtonsPanel );
        mainPanel.add( testsPanel );

    }

    private void createPanelForTest( MyLienzoTest test ) {

        screenButtonsPanel.clear();
        testsPanel.clear();
        testsPanel.getElement().getStyle().setMargin( 10, Style.Unit.PX );
        testsPanel.getElement().getStyle().setBorderWidth( 1, Style.Unit.PX );
        testsPanel.getElement().getStyle().setBorderStyle( Style.BorderStyle.SOLID );
        testsPanel.getElement().getStyle().setBorderColor( "#000000" );

        final LienzoPanel panel = new LienzoPanel(1200, 900);
        final Layer layer = new Layer();

        testsPanel.add( panel );
        layer.setTransformable(true);
        panel.add(layer);

        if ( test instanceof HasButtons ) {
            ( ( HasButtons ) test ).setButtonsPanel( screenButtonsPanel );
        }
        test.test( layer );
        layer.draw();

    }

    private void addButton( final Button button ) {

        if ( buttonsPanelSize >= MAX_BUTTONS_ROW ) {

            buttonsPanelSize = 0;
            buttonsRowPanel = null;
        }


        if ( null == buttonsRowPanel ) {
            buttonsRowPanel = new HorizontalPanel();
            buttonsPanel.add( buttonsRowPanel );
        }

        buttonsRowPanel.add( button );
        buttonsPanelSize++;
    }

}
