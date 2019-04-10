package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.config.LienzoCore;
import com.ait.lienzo.client.core.event.ViewportTransformChangedEvent;
import com.ait.lienzo.client.core.event.ViewportTransformChangedHandler;
import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MouseBoxZoomMediator;
import com.ait.lienzo.client.core.shape.Circle;
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
import com.ait.lienzo.client.widget.panel.mediators.PanelMediators;
import com.ait.lienzo.client.widget.panel.scrollbars.ScrollablePanel;
import com.ait.lienzo.client.widget.panel.util.PanelTransformUtils;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;

public class MediatorsTests implements MyLienzoTest,
                                       HasButtons,
                                       NeedsThePanel,
                                       ListensToKeyboardEvents {

    private final IEventFilter[] zommFilters = new IEventFilter[]{EventFilter.CONTROL};
    private final IEventFilter[] panFilters = new IEventFilter[]{EventFilter.SHIFT};

    private ScrollablePanel panel;
    private Panel buttonsPanel;
    private Layer layer;
    private PanelMediators panelMediators;

    public void test(Layer layer) {
        this.layer = layer;

        layer.add(new Circle(5).setFillColor(ColorName.RED).setX(0).setY(0));

        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.enableSelectionManager();

        final double startX = 0;
        final double startY = 0;
        final double w = 100;
        final double h = 100;

        MultiPath parentMultiPath = new MultiPath().rect(0, 0, w + 900, h + 900).setStrokeColor("#000000");
        WiresShape parentShape = new WiresShape(parentMultiPath);
        wires_manager.register(parentShape);
        parentShape.setLocation(new Point2D(300, 300));
        parentShape.getContainer().setUserData("parent");

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape startEventShape = new WiresShape(startEventMultiPath);
        wires_manager.register(startEventShape);
        startEventShape.setLocation(new Point2D(startX, startY));
        startEventShape.getContainer().setUserData("event");
        wires_manager.getMagnetManager().createMagnets(startEventShape);

        parentShape.add(startEventShape);

        // Green task node.
        WiresShape taskNodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register(taskNodeShape);
        taskNodeShape.setLocation(new Point2D(startX + 200, startY));
        taskNodeShape.getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets(taskNodeShape);

        parentShape.add(taskNodeShape);

        // Yellow task node.
        WiresShape task2NodeShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#FFEB52"));
        wires_manager.register(task2NodeShape);
        task2NodeShape.setLocation(new Point2D(startX + 200, startY + 300));
        task2NodeShape.getContainer().setUserData("task");
        wires_manager.getMagnetManager().createMagnets(task2NodeShape);

        parentShape.add(task2NodeShape);

        // Red end event.
        WiresShape endEventShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#FFFFFF"));
        wires_manager.register(endEventShape);
        endEventShape.setLocation(new Point2D(startX + 400, startY));
        endEventShape.getContainer().setUserData("event");
        wires_manager.getMagnetManager().createMagnets(endEventShape);

        parentShape.add(endEventShape);

        // Connector from blue start event to green task node.
        connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager);
        // Connector from green task node to red end event
        connect(layer, taskNodeShape.getMagnets(), 3, endEventShape.getMagnets(), 7, wires_manager);
        // Connector from blue start event to yellow task node.
        connect(layer, startEventShape.getMagnets(), 3, task2NodeShape.getMagnets(), 7, wires_manager);

        parentShape.setDraggable(true);
        startEventShape.setDraggable(true);
        taskNodeShape.setDraggable(true);
        task2NodeShape.setDraggable(true);
        endEventShape.setDraggable(true);

        postOperations();
    }

    public static void connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager) {

        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

        double x0, x1, y0, y1;

        MultiPath head = new MultiPath();
        head.M(15, 20);
        head.L(0, 20);
        head.L(15 / 2, 0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15, 20);
        tail.L(0, 20);
        tail.L(15 / 2, 0);
        tail.Z();

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(layer, 0, 0, x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        WiresConnector connector = new WiresConnector(m0_1, m1_1, line,
                                                      new MultiPathDecorator(head),
                                                      new MultiPathDecorator(tail));

        wiresManager.register(connector);

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private static OrthogonalPolyLine createLine(final Layer layer,
                                                 final double... points) {
        final OrthogonalPolyLine line = new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
        layer.add(line);
        return line;
    }

    @Override
    public void setButtonsPanel(Panel panel) {
        this.buttonsPanel = panel;

        Button fitAndBoxButton = new Button("Fit and Box");
        fitAndBoxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Transform transform = layer.getViewport().getTransform();
                Transform t = new Transform();
                t.translate(transform.getTranslateX(), transform.getTranslateY());
                t.scale(0.53);
                layer.getViewport().setTransform(t);
                layer.batch();
                final Mediators mediators = layer.getViewport().getMediators();
                mediators.push(new MouseBoxZoomMediator());
            }
        });
        panel.add(fitAndBoxButton);
    }

    private void postOperations() {
        addMediators();
        addZoomLevelStuff();

        layer.getViewport().addViewportTransformChangedHandler(new ViewportTransformChangedHandler() {
            @Override
            public void onViewportTransformChanged(ViewportTransformChangedEvent event) {
                double level = PanelTransformUtils.computeLevel(event.getViewport());
                GWT.log("LEVEL = " + level);
            }
        });
    }

    private void addZoomLevelStuff() {
        panelMediators = PanelMediators.build(panel);
        panelMediators.getPreviewMediator().setMaxScale(2d);
    }

    private void addMediators() {
        final Mediators mediators = layer.getViewport().getMediators();
        // mediators.push( new MousePanMediator(panFilters ).setXConstrained(true).setYConstrained(true) );
        // mediators.push( new MouseWheelZoomMediator(zommFilters ) );
        // mediators.push( new MouseBoxZoomMediator(zommFilters ) );
        // mediators.push( new ZoomControlMediator(zommFilters ) );
    }

    @Override
    public void setLienzoPanel(LienzoPanel lienzoPanel) {
        this.panel = (ScrollablePanel) lienzoPanel;
    }

    @Override
    public void onKeyPress(Key key) {
        GWT.log("onKeyPress [" + getKeyName(key) + "]");
    }

    @Override
    public void onKeyUp(Key key) {
        onPreviewMediatorKeyUp(key);
    }

    @Override
    public void onKeyDown(Key key) {
        onPreviewMediatorKeyDown(key);
    }

    private boolean isCtrl = false;
    private boolean isAlt = false;

    private void onPreviewMediatorKeyDown(Key key) {
        if (Key.CONTROL.equals(key)) {
            isCtrl = true;
            panel.setCursor(Style.Cursor.ROW_RESIZE);
            if (isAlt) {
                enablePreview();
            }
        } else if (Key.ALT.equals(key)) {
            isAlt = true;
            panel.setCursor(Style.Cursor.POINTER);
            if (isCtrl) {
                enablePreview();
            }
        }
    }

    private void enablePreview() {
        panel.setCursor(Style.Cursor.CROSSHAIR);
        panelMediators.enablePreview();
    }

    private void onPreviewMediatorKeyUp(Key key) {
        if (Key.CONTROL.equals(key)) {
            isCtrl = false;
        } else if (Key.ALT.equals(key)) {
            isAlt = false;
        }
        panelMediators.disablePreview();
        panel.setCursor(LienzoCore.get().getDefaultNormalCursor());
    }

    private static String getKeyName(Key key) {
        return null != key ? key.name() : "Key not available";
    }
}