package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class LienzoTests implements EntryPoint {

    private final static MyLienzoTest[] TESTS = new MyLienzoTest[] {
        new BPMNIconsTests(),
        new CircleResizeTests(),
            new ConnectionAcceptorsTests(),
            new DeleteChildTests(),
            new DockingTests(),
            new MarkConnectorTests(),
            new MediatorsTests(),
            new MediatorsTests2(),
            new WiresTests()
    };

    private VerticalPanel mainPanel = new VerticalPanel();
    private HorizontalPanel buttonsPanel = new HorizontalPanel();
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

            buttonsPanel.add( button );

        }

        mainPanel.add( buttonsPanel );
        mainPanel.add( testsPanel );

    }

    private void createPanelForTest( MyLienzoTest test ) {

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

        test.test( layer );
        layer.draw();

    }

}
