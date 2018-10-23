package org.roger600.lienzo.client;

import java.util.Set;

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
import com.ait.lienzo.client.core.shape.wires.DefaultSelectionListener;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IControlPointsAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.ILocationAcceptor;
import com.ait.lienzo.client.core.shape.wires.OptionalBounds;
import com.ait.lienzo.client.core.shape.wires.SelectionManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresShapeControlImpl;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.panel.Bounds;
import com.ait.lienzo.client.widget.panel.LienzoPanel;
import com.ait.lienzo.client.widget.panel.impl.BoundsProviderFactory;
import com.ait.lienzo.client.widget.panel.impl.PreviewPanel;
import com.ait.lienzo.client.widget.panel.scrollbars.ScrollablePanel;
import com.ait.tooling.common.api.java.util.function.Function;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.roger600.lienzo.client.panel.ResizeFlowPanel;

public class InfiniteCanvasTests implements EntryPoint {

    private final IEventFilter[] zommFilters = new IEventFilter[]{EventFilter.CONTROL};

    private final IEventFilter[] panFilters = new IEventFilter[]{EventFilter.SHIFT};

    private static final int PANEL_WIDTH = 900; // 600 -> 900

    private static final int PANEL_HEIGHT = 600;

    private static final double ASPECT_RATIO = 300 / 150;

    private static final int PREVIEWL_WIDTH = 300;

    private static final boolean IS_WIRES = true;

    private ScrollablePanel panel;

    private HorizontalPanel previewContainer;

    private ScrollabelPanelPresenter panelPresenter;

    private Layer layer;

    private PreviewPanel previewPanel;

    private Layer previewLayer;

    private WiresManager wiresManager;

    private WiresManager previewWiresManager;

    private WiresShape redShape;

    private WiresShape blueShape;

    private WiresConnector connector;

    private WiresShape previewRedShape;

    private WiresShape previewBlueShape;

    private class ScrollabelPanelPresenter implements IsWidget {

        @Override
        public Widget asWidget() {
            return panel;
        }
    }

    public void onModuleLoad() {
        build();
        draw();
    }

    private void build() {
        BoundsProviderFactory.FunctionalBoundsProvider boundsProvider = null;
        if (IS_WIRES) {
            boundsProvider = new BoundsProviderFactory.WiresBoundsProvider();
        } else {
            boundsProvider = new BoundsProviderFactory.PrimitivesBoundsProvider();
        }
        boundsProvider.setBoundsBuilder(new Function<BoundingBox, Bounds>() {
            @Override
            public Bounds apply(BoundingBox boundingBox) {
                return BoundsProviderFactory.computeBoundsAspectRatio(ASPECT_RATIO, boundingBox);
            }
        });
        panel = new ScrollablePanel(boundsProvider,
                                    PANEL_WIDTH,
                                    PANEL_HEIGHT);
        panelPresenter = new ScrollabelPanelPresenter();

        panel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        panel.getElement().getStyle().setBorderColor("#000000");
        panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);

        final VerticalPanel v = new VerticalPanel();
        final HorizontalPanel b = new HorizontalPanel();
        previewContainer = new HorizontalPanel();

        addButtons(b);

        v.add(b);
        v.add(previewContainer);

        final ResizeFlowPanel resizeFlowPanel = new ResizeFlowPanel();
        resizeFlowPanel.addDomHandler(new ContextMenuHandler() {
            @Override
            public void onContextMenu(ContextMenuEvent event) {
                GWT.log("PREVENING CONTEXT MENU EVENT!!");
                event.preventDefault();
                event.stopPropagation();
            }
        }, ContextMenuEvent.getType());

        resizeFlowPanel.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    resizeFlowPanel.getElement().getParentElement().getStyle().setHeight(100.0, Style.Unit.PCT);
                    resizeFlowPanel.getElement().getParentElement().getStyle().setWidth(100.0, Style.Unit.PCT);
                }
            }
        });

        resizeFlowPanel.add(panelPresenter);
        previewContainer.add(resizeFlowPanel);

        layer = new Layer();

        panel.add(layer);

        wiresManager = newWiresManager(layer);

        applyGrid(panel);

        addMediators(layer);

        RootPanel.get().add(v);

        createPreview();
    }

    private void createPreview() {

        previewPanel = new PreviewPanel(PREVIEWL_WIDTH,
                                        BoundsProviderFactory.computeHeight(ASPECT_RATIO, PREVIEWL_WIDTH));
        previewPanel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        previewPanel.getElement().getStyle().setBorderColor("#000000");
        previewPanel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);

        previewLayer = new Layer();
        previewPanel.add(previewLayer);
        previewWiresManager = newWiresManager(previewLayer);

        previewPanel.observe(panel);

        previewContainer.add(previewPanel);

    }

    private void addButtons(final Panel container) {
        final Button resizePlus = new Button("Resize +");
        resizePlus.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                resizePanel(50);
            }
        });
        container.add(resizePlus);

        final Button resizeLess = new Button("Resize -");
        resizeLess.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                resizePanel(-50);
            }
        });
        container.add(resizeLess);

        Button resetViewport = new Button("Reset viewport");
        resetViewport.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                panel.getLayer().getViewport().setTransform(new Transform());
                panel.refresh();
            }
        });
        container.add(resetViewport);

        Button showPreview = new Button("Show preview");
        showPreview.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                createPreview();
            }
        });
        container.add(showPreview);

        Button logConnectorCPs = new Button("Log connector CPs");
        logConnectorCPs.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                logConnectorCPs();
            }
        });
        container.add(logConnectorCPs);

        Button logSelection = new Button("Log selection");
        logSelection.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                logSelection();
            }
        });
        container.add(logSelection);

        Button switchConnectorCPs = new Button("Switch connector CPs visibility");
        switchConnectorCPs.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (connector.getPointHandles().isVisible()) {
                    connector.getPointHandles().hide();
                } else {
                    connector.getPointHandles().show();
                }
                layer.batch();
            }
        });
        container.add(switchConnectorCPs);

        Button scheduleDestroyConnector = new Button("Destroy connector");
        scheduleDestroyConnector.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                destroyConnector();
            }
        });
        container.add(scheduleDestroyConnector);
    }

    private void destroyConnector() {
        new Timer() {
            @Override
            public void run() {
                wiresManager.deregister(connector);
                layer.batch();
            }
        }.schedule(3000);
    }

    private void logSelection() {
        SelectionManager selectionManager = wiresManager.getSelectionManager();
        if (null != selectionManager) {
            GWT.log("------------- CURRENT SELECTION ------------------");
            SelectionManager.SelectedItems selectedItems = selectionManager.getSelectedItems();
            Set<WiresConnector> connectors = selectedItems.getConnectors();
            if (null != connectors) {
                for (WiresConnector wiresConnector : connectors) {
                    GWT.log("CONNECTOR [" + wiresConnector.uuid() + "]");
                }
            }
            Set<WiresShape> shapes = selectedItems.getShapes();
            if (null != shapes) {
                for (WiresShape shape : shapes) {
                    GWT.log("SHAPE [" + shape.uuid() + "]");
                }
            }
            GWT.log("----------------------------------------------");
        }
    }

    private void logConnectorCPs() {
        Point2DArray controlPoints = connector.getControlPoints();
        GWT.log("CPS are [" + controlPoints + "]");
    }

    private void resizePanel(int factor) {
        int width = panel.getWidthPx();
        int height = panel.getHeightPx();
        panel.updateSize(width + factor,
                         height + factor);
    }

    private void applyGrid(final LienzoPanel panel) {

        // Grid.
        Line line1 = new Line(0, 0, 0, 0)
                .setStrokeColor("#0000FF")
                .setAlpha(0.2);
        Line line2 = new Line(0, 0, 0, 0)
                .setStrokeColor("#00FF00")
                .setAlpha(0.2);

        line2.setDashArray(2,
                           2);

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);

        panel.setBackgroundLayer(gridLayer);
    }

    private static WiresManager newWiresManager(Layer layer) {
        WiresManager wiresManager = WiresManager.get(layer);
        wiresManager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wiresManager.setDockingAcceptor(IDockingAcceptor.ALL);
        wiresManager.setConnectionAcceptor(IConnectionAcceptor.ALL);
        wiresManager.setLocationAcceptor(ILocationAcceptor.ALL);
        wiresManager.enableSelectionManager();
        wiresManager.setControlPointsAcceptor(new IControlPointsAcceptor() {
            IControlPointsAcceptor DECORATOR = IControlPointsAcceptor.ALL;

            @Override
            public boolean add(WiresConnector connector, int index, Point2D location) {
                GWT.log("ADDING CP AT INDEX [" + index + "] AND LOCATION [" + location + "]");
                return DECORATOR.add(connector, index, location);
            }

            @Override
            public boolean move(WiresConnector connector, Point2DArray pointsLocation) {
                GWT.log("MOVING CPS AT POINTS [" + pointsLocation + "]");
                return DECORATOR.move(connector, pointsLocation);
            }

            @Override
            public boolean delete(WiresConnector connector, int index) {
                GWT.log("DELETE CP AT INDEX [" + index + "]");
                return DECORATOR.delete(connector, index);
            }
        });

        wiresManager.getSelectionManager().setSelectionListener(new DefaultSelectionListener());

        return wiresManager;
    }

    private void addMediators(Layer layer) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push(new MouseWheelZoomMediator(zommFilters).setScaleAboutPoint(false));
        mediators.push(new MousePanMediator(panFilters));
    }

    private void draw() {
        drawBounds();
        if (IS_WIRES) {
            drawWiresThings();
            drawPreviewWiresThings();
        } else {
            drawThings();
        }
        layer.draw();
        if (null != previewLayer) {
            previewLayer.draw();
        }
    }

    private static Line newLinesDecorator(int width,
                                          int height) {
        return new Line(0, 0, width, height)
                .setDraggable(false)
                .setListening(false)
                .setFillAlpha(0)
                .setStrokeAlpha(0)
                .setStrokeAlpha(0.8)
                .setStrokeWidth(1)
                .setStrokeColor("#d3d3d3")
                .setDashArray(5);
    }

    private void drawBounds() {
        final Line h = newLinesDecorator(PANEL_WIDTH, 0);
        final Line v = newLinesDecorator(0, PANEL_HEIGHT);
        layer.getScene().getTopLayer().add(h);
        layer.getScene().getTopLayer().add(v);

        /*final Rectangle bounds = new Rectangle(PANEL_WIDTH, PANEL_HEIGHT)
                .setX(0)
                .setY(0)
                .setStrokeAlpha(1)
                .setStrokeWidth(1)
                .setStrokeColor(ColorName.GREY)
                .setDashArray(5);

        getLayer().getScene().getTopLayer().add(bounds);*/
    }

    private void drawThings() {

        final Rectangle r1 = new Rectangle(50, 50);
        r1.setX(50).setY(50);
        r1.setFillColor("#FF0000");
        r1.setDraggable(true);

        final Rectangle r2 = new Rectangle(100, 100);
        r2.setX(150).setY(150);
        r2.setFillColor("#0000FF");
        r2.setDraggable(true);

        layer.add(r1);
        layer.add(r2);
    }

    private static final double RED_SIZE = 100;

    private static final double BLUE_SIZE = 100;

    private static final double RED_X = 500;

    private static final double RED_Y = 500;

    private static final double BLUE_X = 100;

    private static final double BLUE_Y = 100;

    private void drawWiresThings() {

        MultiPath redPath = new MultiPath().rect(0, 0, RED_SIZE, RED_SIZE)
                .setFillColor("#FF0000");
        redShape = new WiresShape(redPath);
        wiresManager.register(redShape);
        redShape.setLocation(new Point2D(RED_X, RED_Y));
        redShape.setDraggable(true).getContainer().setUserData("red");
        wiresManager.getMagnetManager().createMagnets(redShape);
        TestsUtils.addResizeHandlers(redShape);

        MultiPath bluePath = new MultiPath().rect(0, 0, BLUE_SIZE, BLUE_SIZE)
                .setFillColor("#0000FF");
        blueShape = new WiresShape(bluePath);
        wiresManager.register(blueShape);
        blueShape.setLocation(new Point2D(BLUE_X, BLUE_Y));
        blueShape.setDraggable(true).getContainer().setUserData("blue");
        wiresManager.getMagnetManager().createMagnets(blueShape);
        TestsUtils.addResizeHandlers(blueShape);

        connector = TestsUtils.connect(blueShape.getMagnets(), 3, redShape.getMagnets(), 7, wiresManager);

        /*MultiPath parentPath = new MultiPath().rect(0, 0, BLUE_SIZE * 3, BLUE_SIZE * 2)
                                           .setStrokeColor("#FF0000")
                .setFillColor("#FFFFFF");
        WiresShape parentShape = new WiresShape(parentPath);
        wiresManager.register(parentShape);
        parentShape.setLocation(new Point2D(BLUE_X, BLUE_Y * 2));
        parentShape.setDraggable(true).getContainer().setUserData("parent");
        wiresManager.getMagnetManager().createMagnets( parentShape );
        TestsUtils.addResizeHandlers(parentShape);*/

        setDragConstraints();
    }

    private void setDragConstraints() {
        ((WiresShapeControlImpl) blueShape.getControl())
                .setLocationBounds(OptionalBounds.createMinBounds(0, 0));
    }

    private void drawPreviewWiresThings() {

        wiresManager.setLocationAcceptor(new ILocationAcceptor() {
            @Override
            public boolean allow(WiresContainer[] shapes, Point2D[] locations) {
                return true;
            }

            @Override
            public boolean accept(WiresContainer[] shapes, Point2D[] locations) {
                for (int i = 0; i < shapes.length; i++) {
                    WiresContainer shape = shapes[i];
                    Point2D location = locations[i];
                    if (shape.getGroup().getUserData().equals("red")) {
                        // GWT.log("Moving RED TO [" + location + "]");
                        previewRedShape.setLocation(location.copy());
                    }

                    if (shape.getGroup().getUserData().equals("blue")) {
                        // GWT.log("Moving BLUE TO [" + location + "]");
                        previewBlueShape.setLocation(location.copy());
                    }
                }

                // previewPanel.refresh();

                connector.getGroup().moveToTop();

                return true;
            }
        });

        MultiPath redPath = new MultiPath().rect(0, 0, RED_SIZE, RED_SIZE)
                .setFillColor("#FF0000");
        previewRedShape = new WiresShape(redPath);
        previewWiresManager.register(previewRedShape);
        previewRedShape.setLocation(new Point2D(RED_X, RED_Y));
        previewRedShape.setDraggable(true).getContainer().setUserData("red");
        previewWiresManager.getMagnetManager().createMagnets(previewRedShape);

        MultiPath bluePath = new MultiPath().rect(0, 0, BLUE_SIZE, BLUE_SIZE)
                .setFillColor("#0000FF");
        previewBlueShape = new WiresShape(bluePath);
        previewWiresManager.register(previewBlueShape);
        previewBlueShape.setLocation(new Point2D(BLUE_X, BLUE_Y));
        previewBlueShape.setDraggable(true).getContainer().setUserData("blue");
        previewWiresManager.getMagnetManager().createMagnets(previewBlueShape);

        WiresConnector connector = TestsUtils.connect(previewBlueShape.getMagnets(), 3, previewRedShape.getMagnets(), 7, previewWiresManager);

        previewPanel.refresh();
        previewLayer.draw();
    }

    private static double[] getScaleFactor(final double width,
                                           final double height,
                                           final double targetWidth,
                                           final double targetHeight) {
        return new double[]{
                width > 0 ? targetWidth / width : 1,
                height > 0 ? targetHeight / height : 1};
    }
}
