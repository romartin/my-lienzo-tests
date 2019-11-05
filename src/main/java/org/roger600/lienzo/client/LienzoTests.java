package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.mediator.EventFilter;
import com.ait.lienzo.client.core.mediator.IEventFilter;
import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.mediator.MousePanMediator;
import com.ait.lienzo.client.core.mediator.MouseWheelZoomMediator;
import com.ait.lienzo.client.core.shape.GridLayer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Line;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.widget.panel.LienzoPanel;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelBoundsChangedEvent;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelBoundsChangedEventHandler;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelResizeEvent;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelResizeEventHandler;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelScaleChangedEvent;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelScaleChangedEventHandler;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelScrollEvent;
import com.ait.lienzo.client.widget.panel.event.LienzoPanelScrollEventHandler;
import com.ait.lienzo.client.widget.panel.impl.BoundsProviderFactory;
import com.ait.lienzo.client.widget.panel.scrollbars.ScrollablePanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.common.api.java.util.function.Consumer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.roger600.lienzo.client.ks.CardinalIntersectKSTests;
import org.roger600.lienzo.client.ks.MultiPathResizeTests;
import org.roger600.lienzo.client.ks.WiresAlignDistroTests;
import org.roger600.lienzo.client.ks.WiresArrowsTests;
import org.roger600.lienzo.client.ks.WiresDockingTests;
import org.roger600.lienzo.client.ks.WiresResizesTests;
import org.roger600.lienzo.client.ks.WiresSquaresTests;

import static org.roger600.lienzo.client.ListensToKeyboardEvents.Key.getKey;

public class LienzoTests implements EntryPoint {

    public static final int WIDE = 800;
    public static final int HIGH = 700;

    private final IEventFilter[] zommFilters = new IEventFilter[]{EventFilter.CONTROL};
    private final IEventFilter[] panFilters = new IEventFilter[]{EventFilter.SHIFT};

    private final static MyLienzoTest[] TESTS = new MyLienzoTest[]{
            new ToolboxTests(),
            new PerformanceTests(),
            new SelectionManagerTests(),
            new TextWrapTests(),
            new AutoMagnetsConnectorsTests(),
            new CardinalIntersectSimpleTest(),
            new WiresDragHandlersTests(),
            new DragHandlersTests(),
            new SVGPicturesTests(),
            new ContainerTests(),
            new SVGTests(),
            new UXSVGTests(),
            new DragConstraintsTests(),
            new FontTests(),
            new ImagesTests(),
            new MultiPathShapesTests(),
            new WiresRingTests(),
            new BasicWiresShapesTests(),
            new GlyphPositionsAndScaleTests(),
            new TransformTests(),
            new MagnetsAndCPsTests(),
            new BoundingBoxTests(),
            new WiresDragAndMoveTests(),
            new ShapeResizeTests(),
            new DragBoundsTests(),
            new LayoutContainerChildrenTests(),
            new LayoutContainerChildrenTests2(),
            new ChildRectangleResizeTests(),
            new ChildCircleResizeTests(),
            new StandaloneConnectorsTests(),
            new ConnectionAndMagnetsTests(),
            new ConnectionAcceptorsTests(),
            new ConnectorsSelectionTests(),
            new ConnectorsAndParentsTests(),
            new ConnectorsAndParentsTests2(),
            new DeleteChildTests(),
            new DockingTests(),
            new MarkConnectorTests(),
            new MediatorsTests(),
            new WiresTests(),
            new MultiPathAttributesChangedTests(),
            // From Lienzo KS
            new WiresAlignDistroTests(),
            new CardinalIntersectKSTests(),
            new MultiPathResizeTests(),
            new WiresArrowsTests(),
            new WiresSquaresTests(),
            new WiresResizesTests(),
            new WiresDockingTests(),
    };

    private static final int MAX_BUTTONS_ROW = 7;
    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel buttonsPanel = new VerticalPanel();
    private HorizontalPanel screenButtonsPanel = new HorizontalPanel();
    private HorizontalPanel buttonsRowPanel;
    private int buttonsPanelSize = 0;
    private FlowPanel testsPanel = new FlowPanel();
    private Consumer<KeyDownEvent> keyDownEventConsumer;
    private Consumer<KeyUpEvent> keyUpEventConsumer;
    private Consumer<KeyPressEvent> keyPressEventConsumer;

    public void onModuleLoad() {
        final boolean isSingleTest = TESTS.length == 1;

        buttonsPanel.getElement().getStyle().setMargin(10, Style.Unit.PX);

        RootPanel.get().add(mainPanel);

        if (!isSingleTest) {
            for (final MyLienzoTest test : TESTS) {

                final Button button = new Button(test.getClass().getSimpleName());
                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        createPanelForTest(test);
                    }
                });

                addButton(button);
            }
        }

        mainPanel.add(buttonsPanel);
        mainPanel.add(screenButtonsPanel);
        mainPanel.add(testsPanel);

        addKeyboardStuff();

        if (isSingleTest) {
            createPanelForTest(TESTS[0]);
        }
    }

    private void addKeyboardStuff() {

        setEmptyKeyboardEventConsumers();

        final boolean[] isMouseOverPanel = new boolean[]{false};

        testsPanel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent mouseOverEvent) {
                isMouseOverPanel[0] = true;
            }
        }, MouseOverEvent.getType());

        testsPanel.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                isMouseOverPanel[0] = false;
            }
        }, MouseOutEvent.getType());

        RootPanel.get().addDomHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent keyPressEvent) {
                if (isMouseOverPanel[0]) {
                    keyPressEventConsumer.accept(keyPressEvent);
                }
            }
        }, KeyPressEvent.getType());

        RootPanel.get().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                if (isMouseOverPanel[0]) {
                    keyDownEventConsumer.accept(keyDownEvent);
                }
            }
        }, KeyDownEvent.getType());

        RootPanel.get().addDomHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                if (isMouseOverPanel[0]) {
                    keyUpEventConsumer.accept(keyUpEvent);
                }
            }
        }, KeyUpEvent.getType());
    }

    private void setEmptyKeyboardEventConsumers() {
        keyDownEventConsumer = new Consumer<KeyDownEvent>() {
            @Override
            public void accept(KeyDownEvent keyDownEvent) {
            }
        };
        keyUpEventConsumer = new Consumer<KeyUpEvent>() {
            @Override
            public void accept(KeyUpEvent keyUpEvent) {
            }
        };
        keyPressEventConsumer = new Consumer<KeyPressEvent>() {
            @Override
            public void accept(KeyPressEvent keyPressEvent) {
            }
        };
    }

    private void createPanelForTest(MyLienzoTest test) {

        screenButtonsPanel.clear();
        testsPanel.clear();
        testsPanel.getElement().getStyle().setMargin(10, Style.Unit.PX);
        testsPanel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        testsPanel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        testsPanel.getElement().getStyle().setBorderColor("#000000");

        final ScrollablePanel panel = new ScrollablePanel(new BoundsProviderFactory.WiresBoundsProvider(), WIDE, HIGH);
        panel.addLienzoPanelBoundsChangedEventHandler(new LienzoPanelBoundsChangedEventHandler() {
            @Override
            public void onBoundsChanged(LienzoPanelBoundsChangedEvent event) {
                // GWT.log("[ScrollablePanel] ON BOUNDS CHANGED");
            }
        });
        panel.addLienzoPanelResizeEventHandler(new LienzoPanelResizeEventHandler() {
            @Override
            public void onResize(LienzoPanelResizeEvent event) {
                GWT.log("[ScrollablePanel] ON RESIZE");
            }
        });
        panel.addLienzoPanelScaleChangedEventHandler(new LienzoPanelScaleChangedEventHandler() {
            @Override
            public void onScale(LienzoPanelScaleChangedEvent event) {
                // GWT.log("[ScrollablePanel] ON SCALE");
            }
        });
        panel.addLienzoPanelScrollEventHandler(new LienzoPanelScrollEventHandler() {
            @Override
            public void onScroll(LienzoPanelScrollEvent event) {
                GWT.log("[ScrollablePanel] ON SCROLL");
            }
        });
        final LienzoPanel lienzoPanel = panel.getLienzoPanel();
        applyGrid(lienzoPanel);




        lienzoPanel.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                GWT.log("**** REQUEST STARTED");
            }
        }, MouseDownEvent.getType());

        lienzoPanel.addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                GWT.log("**** REQUEST COMPLETED");
            }
        }, MouseUpEvent.getType());

        final Layer layer = new Layer() {
            @Override
            protected void drawWithoutTransforms(Context2D context, double alpha, BoundingBox bounds) {

                final double vw = 500;
                final double vh = 500;
                //context.save();
                context.setGlobalAlpha(1);
                context.setStrokeColor(ColorName.RED);
                context.setStrokeWidth(5);
                context.moveTo(0, 0);
                context.lineTo(vw, 0);
                context.moveTo(0, 0);
                context.lineTo(0, vh);
                //context.restore();

                super.drawWithoutTransforms(context, alpha, bounds);
            }
        };

        if (test instanceof ListensToKeyboardEvents) {
            final ListensToKeyboardEvents events = (ListensToKeyboardEvents) test;
            keyDownEventConsumer = new Consumer<KeyDownEvent>() {
                @Override
                public void accept(KeyDownEvent keyDownEvent) {
                    events.onKeyDown(getKey(keyDownEvent.getNativeKeyCode()));
                }
            };
            keyUpEventConsumer = new Consumer<KeyUpEvent>() {
                @Override
                public void accept(KeyUpEvent keyUpEvent) {
                    events.onKeyUp(getKey(keyUpEvent.getNativeKeyCode()));
                }
            };
            keyPressEventConsumer = new Consumer<KeyPressEvent>() {
                @Override
                public void accept(KeyPressEvent keyPressEvent) {
                    events.onKeyPress(getKey(keyPressEvent.getUnicodeCharCode()));
                }
            };
        } else {
            setEmptyKeyboardEventConsumers();
        }

        testsPanel.add(panel);
        layer.setTransformable(true);
        panel.add(layer);

        if (test instanceof HasButtons) {
            ((HasButtons) test).setButtonsPanel(screenButtonsPanel);
        }

        if (test instanceof HasMediators) {
            addMediators(layer);
        }

        if (test instanceof NeedsThePanel) {
            ((NeedsThePanel) test).setLienzoPanel(panel);
        }

        test.test(layer);

        layer.draw();
    }

    private void addMediators(Layer layer) {
        final Mediators mediators = layer.getViewport().getMediators();
        mediators.push(new MouseWheelZoomMediator(zommFilters));
        mediators.push(new MousePanMediator(panFilters));
    }

    private void addButton(final Button button) {

        if (buttonsPanelSize >= MAX_BUTTONS_ROW) {

            buttonsPanelSize = 0;
            buttonsRowPanel = null;
        }

        if (null == buttonsRowPanel) {
            buttonsRowPanel = new HorizontalPanel();
            buttonsPanel.add(buttonsRowPanel);
        }

        buttonsRowPanel.add(button);
        buttonsPanelSize++;
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
}
