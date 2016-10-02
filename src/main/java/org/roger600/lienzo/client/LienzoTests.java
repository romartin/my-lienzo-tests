package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.*;
import com.ait.lienzo.client.core.shape.GridLayer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.roger600.lienzo.client.ks.*;

public class LienzoTests implements EntryPoint {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private final static MyLienzoTest[] TESTS = new MyLienzoTest[] {
            new MultiPathShapesTests(),
            new WiresRingTests(),
            new GlyphPositionsAndScaleTests(),
            new TransformTests(),
            new MagnetsAndCPsTests(),
            new BoundingBoxTests(),
            new WiresDragAndMoveTests(),
            new ShapeResizeTests(),
            new LayoutContainerChildrenTests(),
            new LayoutContainerChildrenTests2(),
            new ChildRectangleResizeTests(),
            new ChildCircleResizeTests(),
            new StandaloneConnectorsTests(),
            new ConnectionAcceptorsTests(),
            new ConnectorsAndParentsTests(),
            new ConnectorsAndParentsTests2(),
            new DeleteChildTests(),
            new DockingTests(),
            new MarkConnectorTests(),
            new MediatorsTests(),
            new MediatorsTests2(),
            new WiresTests(),
            new MultiPathAttributesChangedTests(),
            // From Lienzo KS
            new WiresAlignDistroTests(),
            new MultiPathResizeTests(),
            new WiresArrowsTests(),
            new WiresSquaresTests(),
            new WiresResizesTests(),
            new WiresDockingTests()
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
        applyGrid( panel );
        final Layer layer = new Layer();

        testsPanel.add( panel );
        layer.setTransformable(true);
        panel.add(layer);

        if ( test instanceof HasButtons ) {
            ( ( HasButtons ) test ).setButtonsPanel( screenButtonsPanel );
        }

        if ( test instanceof HasMediators ) {
            addMediators( layer );
        }

        test.test( layer );

        layer.draw();

    }

    private void addMediators( Layer layer ) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
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

    private void applyGrid( final LienzoPanel panel ) {

        // Grid.
        Line line1 = new Line( 0, 0, 0, 0 )
                .setStrokeColor( "#0000FF" )
                .setAlpha( 0.2 );
        Line line2 = new Line( 0, 0, 0, 0 )
                .setStrokeColor( "#00FF00"  )
                .setAlpha( 0.2 );

        line2.setDashArray( 2,
                2 );

        GridLayer gridLayer = new GridLayer( 100, line1, 25, line2 );

        panel.setBackgroundLayer( gridLayer );
    }

}
