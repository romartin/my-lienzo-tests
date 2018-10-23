package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.panel.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;


public class MediatorsTests extends FlowPanel implements MyLienzoTest, HasButtons, NeedsThePanel {

    private final IEventFilter[] zommFilters = new IEventFilter[] { EventFilter.CONTROL };
    private final IEventFilter[] panFilters = new IEventFilter[] { EventFilter.SHIFT };

    private LienzoPanel lienzoPanel;
    private Layer layer;
    private WiresShape startShape;
    private WiresShape parentShape;
    private WiresShape childShape;

    public void test(Layer layer) {
        this.layer = layer;
        WiresManager wires_manager = WiresManager.get(layer);

        // White box.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, 100, 100).setStrokeColor("#000000");
        startShape = new WiresShape(startEventMultiPath);
        wires_manager.register( startShape.setDraggable( true ) );
        startShape.setLocation(new Point2D(100, 100));
        startShape.getContainer().setUserData("event");
        // wires_manager.getMagnetManager().createMagnets( startEventShape );
        // startEventShape.getMagnets().show();

        MultiPath parentPath = new MultiPath().rect(0, 0, 300, 300).setStrokeColor("#FF0000").setStrokeWidth( 2 );
        parentShape  = new WiresShape(parentPath);
        wires_manager.register( parentShape.setDraggable( true ) );
        parentShape.setLocation(new Point2D(300, 100));
        parentShape.getContainer().setUserData("event");

        MultiPath childPath = new MultiPath().rect(0, 0, 50, 50).setFillColor("#000000");
        childShape  = new WiresShape(childPath);
        wires_manager.register( childShape.setDraggable( true ) );
        childShape.setLocation(new Point2D(50, 50));
        childShape.getContainer().setUserData("event");

        parentShape.add( childShape );

        addMediators();

    }

    private void addMediators() {
        final Mediators mediators = layer.getViewport().getMediators();
        final MouseWheelZoomMediator zoomMediator = new MouseWheelZoomMediator( zommFilters );
        zoomMediator.setMinScale( 1 );
        zoomMediator.setMaxScale( 2 );
        mediators.push( zoomMediator );
        final MousePanMediator panMediator = new MousePanMediator( panFilters );
        mediators.push( panMediator );
    }

    private void connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager)
    {

        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;

        MultiPath head = new MultiPath();
        head.M(15, 20);
        head.L(0, 20);
        head.L(15/2, 0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15, 20);
        tail.L(0, 20);
        tail.L(15/2, 0);
        tail.Z();

// head.getAbsoluteLocation();

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1-x0)/2)), (y0 + ((y1-y0)/2)), x1, y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        WiresConnector connector = new WiresConnector(m0_1, m1_1, line,
                new MultiPathDecorator(head),
                new MultiPathDecorator(tail));

        wiresManager.register( connector );

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine( final Layer layer,
                                                 final double... points )
    {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add( line );
        return line;
    }

    @Override
    public void setLienzoPanel(final LienzoPanel lienzoPanel) {
        this.lienzoPanel = lienzoPanel;
    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        Button b1 = new Button( "StartShape" );
        b1.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                Group group = startShape.getGroup();
                GWT.log( "StartShape [x=" + group.getX() + ", y=" + group.getY() );
                GWT.log( "StartShape location = " + group.getLocation().toJSONString() );
                GWT.log( "StartShape absolute location = " + group.getAbsoluteLocation().toJSONString() );
            }
        } );
        panel.add( b1 );

        Button b2 = new Button( "ParentShape" );
        b2.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                Group group = parentShape.getGroup();
                GWT.log( "ParentShape [x=" + group.getX() + ", y=" + group.getY() );
                GWT.log( "ParentShape location = " + group.getLocation().toJSONString() );
                GWT.log( "ParentShape absolute location = " + group.getAbsoluteLocation().toJSONString() );
            }
        } );
        panel.add( b2 );

        Button b3 = new Button( "ChildShape" );
        b3.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                Group group = childShape.getGroup();
                GWT.log( "ChildShape [x=" + group.getX() + ", y=" + group.getY() );
                GWT.log( "ChildShape location = " + group.getLocation().toJSONString() );
                GWT.log( "ChildShape absolute location = " + group.getAbsoluteLocation().toJSONString() );
            }
        } );
        panel.add( b3 );
        Button scaleButton = new Button( "Scale" );
        scaleButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                scaleIt();
            }
        } );
        panel.add( scaleButton );

    }

    private void scaleIt() {

        final double factor = 0.5;

        Transform transform = layer.getViewport().getTransform();

        if (transform == null)
        {
            layer.getViewport().setTransform(transform = new Transform());
        }

        transform.scale(factor, factor);

        final int w = lienzoPanel.getWidthPx();
        final int h = lienzoPanel.getHeightPx();
        final int tw = (int) (w - ( w * factor));
        final int th = (int) (h - ( h * factor));
        lienzoPanel.setPixelSize(tw, th);
    }


}