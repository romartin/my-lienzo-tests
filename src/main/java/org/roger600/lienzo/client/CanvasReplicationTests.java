package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.GridLayer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CanvasReplicationTests implements EntryPoint {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel buttonsPanel = new VerticalPanel();
    private HorizontalPanel canvasPanel = new HorizontalPanel();

    private Layer layer1;
    private Layer layer2;
    private WiresManager wiresManager1;
    private WiresManager wiresManager2;
    private Rectangle rectangle;
    private WiresShape wiresShape1;
    private WiresShape wiresShape2;

    public void onModuleLoad()
    {
        RootPanel.get().add( mainPanel );

        mainPanel.add( buttonsPanel );
        mainPanel.add( canvasPanel );

        final Object[] o1 = createPanel();
        final Object[] o2 = createPanel();
        final FlowPanel container1 = (FlowPanel) o1[0];
        final LienzoPanel panel1 = (LienzoPanel) o1[1];
        layer1 = (Layer) o1[2];
        final FlowPanel container2 = (FlowPanel) o2[0];
        final LienzoPanel panel2 = (LienzoPanel) o2[1];
        layer2 = (Layer) o2[2];
        wiresManager1 = WiresManager.get(layer1);
        wiresManager2 = WiresManager.get(layer2);

        canvasPanel.add(container1);
        canvasPanel.add(container2);

        drawPrimitives();

        drawWires();

        addButtons();
    }

    private void addButtons() {
        final Button b1 = new Button( "Move" );
        b1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                rectangle.setX(300).setY(300);
                wiresShape1.setX(400).setY(400);
                wiresShape2.setX(400).setY(400);
                draw();
            }
        });
        buttonsPanel.add(b1);
    }

    private void drawWires( ) {
        wiresShape1 = new WiresShape( new MultiPath().rect(0, 0, 100, 100).setFillColor("#00CC00") );
        wiresShape1.setX(200).setY(200).getContainer().setUserData("task");
        register(wiresManager1, wiresShape1);
        wiresShape2 = new WiresShape( new MultiPath().rect(0, 0, 100, 100).setFillColor("#00CC00") );
        wiresShape2.setX(200).setY(200).getContainer().setUserData("task");
        register(wiresManager2, wiresShape2);
    }

    private void register( WiresManager manager, WiresShape shape ) {
        manager.register(shape);
        manager.getMagnetManager().createMagnets( shape );
    }

    private void drawPrimitives( ) {

        final Rectangle r1 = new Rectangle( 50, 50 ).setFillColor(ColorName.RED);
        layer1.add( r1 );

        final Rectangle r2 = new Rectangle( 50, 50 ).setFillColor(ColorName.BLUE);
        layer2.add( r2 );

        rectangle = new Rectangle( 50, 50 )
                .setX(100)
                .setY(100)
                .setFillColor(ColorName.GREEN);
        layer1.add( rectangle );
        layer2.add( rectangle );

        draw();
    }

    private void draw() {
        layer1.draw();
        layer2.draw();
    }

    private Object[] createPanel() {

        final FlowPanel container = new FlowPanel();
        container.getElement().getStyle().setMargin( 10, Style.Unit.PX );
        container.getElement().getStyle().setBorderWidth( 1, Style.Unit.PX );
        container.getElement().getStyle().setBorderStyle( Style.BorderStyle.SOLID );
        container.getElement().getStyle().setBorderColor( "#000000" );

        final LienzoPanel panel = new LienzoPanel(600, 600);
        applyGrid( panel );
        final Layer layer = new Layer();

        container.add( panel );
        layer.setTransformable(true);
        panel.add(layer);

        addMediators( layer );

        return new Object[]{ container, panel, layer };
    }

    private void addMediators( Layer layer ) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push( new MouseWheelZoomMediator( zommFilters ) );
        mediators.push( new MousePanMediator( panFilters ) );
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
