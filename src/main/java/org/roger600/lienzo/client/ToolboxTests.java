/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.roger600.lienzo.client;

import java.util.Collection;

import com.ait.lienzo.client.core.event.AbstractNodeDragEvent;
import com.ait.lienzo.client.core.event.AbstractNodeMouseEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseEnterEvent;
import com.ait.lienzo.client.core.event.NodeMouseEnterHandler;
import com.ait.lienzo.client.core.event.NodeMouseExitEvent;
import com.ait.lienzo.client.core.event.NodeMouseExitHandler;
import com.ait.lienzo.client.core.shape.AbstractDirectionalMultiPointShape;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.PolyLine;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.toolbox.ToolboxVisibilityExecutors;
import com.ait.lienzo.client.core.shape.toolbox.grid.AutoGrid;
import com.ait.lienzo.client.core.shape.toolbox.items.ButtonItem;
import com.ait.lienzo.client.core.shape.toolbox.items.impl.ToolboxFactory;
import com.ait.lienzo.client.core.shape.toolbox.items.impl.WiresShapeToolbox;
import com.ait.lienzo.client.core.shape.toolbox.items.tooltip.ToolboxTextTooltip;
import com.ait.lienzo.client.core.shape.toolbox.items.impl.ItemsToolboxHighlight;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IControlPointsAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.ILocationAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.proxy.AbstractWiresProxy;
import com.ait.lienzo.client.core.shape.wires.proxy.WiresConnectorProxy;
import com.ait.lienzo.client.core.shape.wires.proxy.WiresDragProxy;
import com.ait.lienzo.client.core.shape.wires.proxy.WiresShapeProxy;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.Direction;
import com.ait.tooling.common.api.java.util.function.Consumer;
import com.ait.tooling.common.api.java.util.function.Supplier;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;
import com.ait.tooling.nativetools.client.collection.NFastStringMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;

// TODO: Add also examples of dropdown buttons, etc

public class ToolboxTests implements MyLienzoTest,
                                     HasButtons,
                                     ListensToKeyboardEvents {

    private static final double BUTTON_SIZE = 15;
    private static final double BUTTON_PADDING = 5;
    private static final Direction TOOLBOX_AT = Direction.NORTH_EAST;
    private static final Direction GRID_TOWARDS = Direction.SOUTH_EAST;
    private static final Direction TOOLTIP_AT = Direction.EAST;
    private static final Direction TOOLTIP_TOWARDS = Direction.EAST;

    private Button showToolboxButton;
    private Button hideToolboxButton;
    private Button createToolboxButton;
    private Button destroyToolboxButton;
    private Button testToolboxButton;

    private Layer layer;
    private WiresManager wiresManager;
    private WiresShape shape;
    private WiresShape shape1;
    private WiresShape parent;
    private WiresConnector connector;
    private WiresShapeToolbox toolbox;
    private ToolboxTextTooltip tooltip;
    private HandlerRegistration layerClickHandlerRegistration;
    private HandlerRegistration shapeClickHandlerRegistration;

    private WiresDragProxy shapeProxy;
    private WiresDragProxy shapeWithConnectorProxy;
    private WiresDragProxy connectorProxy;
    private ItemsToolboxHighlight toolboxHighlight;

    @Override
    public void setButtonsPanel(final Panel panel) {
        showToolboxButton = new Button("Show");
        showToolboxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onShowToolbox();
            }
        });
        panel.add(showToolboxButton);
        hideToolboxButton = new Button("Hide");
        hideToolboxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onHideToolbox();
            }
        });
        panel.add(hideToolboxButton);
        createToolboxButton = new Button("Create");
        createToolboxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onCreateToolbox();
            }
        });
        panel.add(createToolboxButton);
        destroyToolboxButton = new Button("Destroy");
        destroyToolboxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDestroyToolbox();
            }
        });
        panel.add(destroyToolboxButton);
        testToolboxButton = new Button("Test");
        testToolboxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onTestButtonClick();
            }
        });
        panel.add(testToolboxButton);
    }

    @Override
    public void test(final Layer layer) {
        this.layer = layer;
        this.wiresManager = WiresManager.get(layer);

        wiresManager.enableSelectionManager();

        wiresManager.setContainmentAcceptor(new IContainmentAcceptor() {

            private final boolean isAllowed = true;

            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape[] children) {
                GWT.log("[Containment] Allow");
                return isAllowed;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape[] children) {
                GWT.log("[Containment] Accept");
                for (WiresShape child : children) {
                    WiresContainer childParent = child.getParent();
                    // GWT.log("[Containment] ACCEPT - parent WAS " + childParent);
                    // GWT.log("[Containment] ACCEPT - parent IS " + parent);
                }

                return isAllowed;
            }
        });

        wiresManager.setConnectionAcceptor(new IConnectionAcceptor() {

            private final boolean isAllowed = true;
            private final boolean isAccept = true;

            @Override
            public boolean headConnectionAllowed(WiresConnection head, WiresShape shape) {
                GWT.log("[Connection] HEAD - Allow");
                return isAllowed;
            }

            @Override
            public boolean tailConnectionAllowed(WiresConnection tail, WiresShape shape) {
                GWT.log("[Connection] TAIL - Allow");
                return isAllowed;
            }

            @Override
            public boolean acceptHead(WiresConnection head, WiresMagnet magnet) {
                GWT.log("[Connection] HEAD - Accept");
                return isAccept;
            }

            @Override
            public boolean acceptTail(WiresConnection tail, WiresMagnet magnet) {
                GWT.log("[Connection] TAIL - Accept");
                return isAccept;
            }
        });

        wiresManager.setDockingAcceptor(IDockingAcceptor.ALL);

        wiresManager.setLocationAcceptor(ILocationAcceptor.ALL);
        wiresManager.setControlPointsAcceptor(IControlPointsAcceptor.ALL);

        layerClickHandlerRegistration = layer.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                onHideToolbox();
            }
        });

        shape = new WiresShape(new MultiPath()
                                       .rect(0, 0, 100, 100)
                                       .setStrokeColor("#FF0000")
                                       .setFillColor("#FF0000"))
                .setDraggable(true);
        shape.setLocation(new Point2D(350, 150));
        wiresManager.register(shape);
        wiresManager.getMagnetManager().createMagnets(shape);

        shape1 = new WiresShape(new MultiPath()
                                        .rect(0, 0, 100, 100)
                                        .setStrokeColor("#00FF00")
                                        .setFillColor("#FFAADD"))
                .setDraggable(true);
        shape1.setLocation(new Point2D(50, 50));
        wiresManager.register(shape1);
        wiresManager.getMagnetManager().createMagnets(shape1);

        parent = new WiresShape(new MultiPath()
                                        .rect(0, 0, 350, 350)
                                        .setStrokeColor("#000000")
                                        .setFillColor("#FFFFFF"))
                .setDraggable(true);
        parent.setLocation(new Point2D(150, 300));
        wiresManager.register(parent);
        wiresManager.getMagnetManager().createMagnets(parent);

        connector = TestsUtils.connect(shape1.getMagnets(), 3, shape.getMagnets(), 7, wiresManager);

        // Add shape into parent.
        /*parent.add(shape);
        shape.setLocation(new Point2D(50, 50));*/

        onCreateToolbox();

        final WiresShapeProxy sp1 = createExampleShapeProxy();

        shapeProxy = new WiresDragProxy(new Supplier<AbstractWiresProxy>() {
            @Override
            public AbstractWiresProxy get() {
                return sp1;
            }
        });

        final WiresShapeProxy sp2 = createExampleShapeWithConnectorProxy(new Supplier<WiresMagnet>() {
            @Override
            public WiresMagnet get() {
                return shape.getMagnets().getMagnet(3);
            }
        });

        shapeWithConnectorProxy = new WiresDragProxy(new Supplier<AbstractWiresProxy>() {
            @Override
            public AbstractWiresProxy get() {
                return sp2;
            }
        });

        final WiresConnectorProxy cp = createExampleConnectorProxy(new Supplier<WiresMagnet>() {
            @Override
            public WiresMagnet get() {
                return shape.getMagnets().getMagnet(3);
            }
        });
        connectorProxy = new WiresDragProxy(new Supplier<AbstractWiresProxy>() {
            @Override
            public AbstractWiresProxy get() {
                return cp;
            }
        });

        /*layer.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("LAYER CLICK!");
            }
        });*/


    }

    private WiresShapeProxy createExampleShapeWithConnectorProxy(final Supplier<WiresMagnet> sourceMagnet) {
        return new WiresShapeProxy(wiresManager,
                                   new Supplier<WiresShape>() {
                                       @Override
                                       public WiresShape get() {

                                           // Shape.
                                           final WiresShape shape = createExampleShape();
                                           wiresManager.register(shape, true, true);
                                           wiresManager.getMagnetManager().createMagnets(shape);
                                           shape.getGroup().setListening(true);

                                           // Connector & source connection.
                                           final WiresConnector connector = createExampleConnector(sourceMagnet.get());
                                           wiresManager.register(connector, true);

                                           // Target connection.
                                           final WiresMagnet targetMagnet = shape.getMagnets().getMagnet(0);
                                           connector.setTailMagnet(targetMagnet);

                                           return shape;
                                       }
                                   },
                                   new Consumer<WiresShape>() {
                                       @Override
                                       public void accept(WiresShape shape) {
                                           final WiresMagnet targetMagnet = shape.getMagnets().getMagnet(0);
                                           final WiresConnector connector = targetMagnet.getConnections().get(0).getConnector();
                                           shape.getGroup().setListening(true);
                                           connector.getGroup().setListening(true);
                                           toolboxHighlight.restore();
                                       }
                                   },
                                   new Consumer<WiresShape>() {
                                       @Override
                                       public void accept(WiresShape shape) {
                                           final WiresMagnet targetMagnet = shape.getMagnets().getMagnet(0);
                                           final WiresConnector connector = targetMagnet.getConnections().get(0).getConnector();
                                           shape.getGroup().setListening(false);
                                           connector.getGroup().setListening(false);
                                           wiresManager.deregister(connector);
                                           wiresManager.deregister(shape);
                                           toolboxHighlight.restore();
                                       }
                                   });
    }

    private WiresShapeProxy createExampleShapeProxy() {
        return new WiresShapeProxy(wiresManager,
                                   new Supplier<WiresShape>() {
                                       @Override
                                       public WiresShape get() {
                                           final WiresShape shape = createExampleShape();
                                           wiresManager.register(shape, true, true);
                                           wiresManager.getMagnetManager().createMagnets(shape);
                                           return shape;
                                       }
                                   },
                                   new Consumer<WiresShape>() {
                                       @Override
                                       public void accept(WiresShape shape) {
                                           shape.getGroup().setListening(true);
                                           toolboxHighlight.restore();
                                       }
                                   },
                                   new Consumer<WiresShape>() {
                                       @Override
                                       public void accept(WiresShape shape) {
                                           wiresManager.deregister(shape);
                                           toolboxHighlight.restore();
                                       }
                                   });
    }

    private WiresConnectorProxy createExampleConnectorProxy(final Supplier<WiresMagnet> sourceMagnet) {
        return new WiresConnectorProxy(wiresManager,
                                       new Supplier<WiresConnector>() {
                                           @Override
                                           public WiresConnector get() {
                                               GWT.log("[WiresConnectorProxy] Creating");
                                               final WiresConnector instance = createExampleConnector(sourceMagnet.get());
                                               wiresManager.register(instance, true);
                                               return instance;
                                           }
                                       },
                                       new Consumer<WiresConnector>() {
                                           @Override
                                           public void accept(WiresConnector wiresConnector) {
                                               GWT.log("[WiresConnectorProxy] Accepting");
                                               toolboxHighlight.restore();
                                           }
                                       },
                                       new Consumer<WiresConnector>() {
                                           @Override
                                           public void accept(WiresConnector wiresConnector) {
                                               GWT.log("[WiresConnectorProxy] Destroying");
                                               wiresManager.deregister(wiresConnector);
                                               toolboxHighlight.restore();
                                           }
                                       });
    }

    private WiresShapeToolbox appendToolbox(final WiresShape shape) {
        final Layer topLayer = getTopLayer();
        final WiresShapeToolbox toolbox = new WiresShapeToolbox(shape)
                .attachTo(topLayer)
                .at(TOOLBOX_AT)
                .grid(new AutoGrid.Builder()
                              .forBoundingBox(shape.getGroup().getBoundingBox())
                              .withPadding(BUTTON_PADDING)
                              .withIconSize(BUTTON_SIZE)
                              .towards(GRID_TOWARDS)
                              .build())
                .useShowExecutor(ToolboxVisibilityExecutors.upScaleX())
                .useHideExecutor(ToolboxVisibilityExecutors.downScaleX());

        shapeClickHandlerRegistration = shape.getGroup().addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                if (toolbox.isVisible()) {
                    toolbox.hide();
                } else {
                    toolbox.show();
                }
            }
        });

        return toolbox;
    }


    private ButtonItem addToolboxButton(final String text,
                                        final Group shape) {
        final ButtonItem button =
                ToolboxFactory.INSTANCE.buttons()
                        .button(shape)
                        .decorate(ToolboxFactory.INSTANCE
                                          .decorators()
                                          .box()
                                          .configure(new Consumer<MultiPath>() {
                                              @Override
                                              public void accept(MultiPath path) {
                                                  path.setStrokeWidth(1)
                                                          .setStrokeColor("#0000FF")
                                                          .setShadow(new Shadow(ColorName.BLACK.getColor().setA(0.80), 10, 3, 3));
                                              }
                                          }))
                        .tooltip(tooltip.createItem(text))
                        .onMouseEnter(new NodeMouseEnterHandler() {
                            @Override
                            public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                                // GWT.log("onToolboxMouseEnter [" + color + "]");
                            }
                        })
                        .onMouseExit(new NodeMouseExitHandler() {
                            @Override
                            public void onNodeMouseExit(NodeMouseExitEvent event) {
                                // GWT.log("onToolboxMouseExit [" + color + "]");
                            }
                        });
        toolbox.add(button);
        return button;
    }

    private ButtonItem addToolboxButton(final String text,
                                        final String color) {
        Group group = new Group();
        Rectangle rectangle = new Rectangle(BUTTON_SIZE, BUTTON_SIZE)
                .setFillColor(color);
        group.add(rectangle);
        return addToolboxButton(text, group);
    }

    private void onTestButtonClick() {
        logShapes();
        // testPreviewRequestLifeCycle();
    }

    private void testPreviewRequestLifeCycle() {
        GWT.log("**** Starting request");
        //layer.setVisible(false);
        // layer.refreshDraw = false;
        layer.getAttributes().setVisible(false);

        Rectangle rectangle = new Rectangle(50, 50)
                .setX(400)
                .setY(0)
                .setFillColor(ColorName.YELLOW);

        layer.add(rectangle);
        layer.batch();

        new Timer() {

            @Override
            public void run() {
                GWT.log("**** Completing request");
                // layer.setVisible(true);
                //layer.refreshDraw = true;
                layer.getAttributes().setVisible(true);
                layer.batch();

            }
        }.schedule(3000);


    }

    private void logShapes() {
        NFastStringMap<WiresShape> shapesMap = wiresManager.getShapesMap();
        Collection<String> uuids = shapesMap.keys();
        if (null != uuids && !uuids.isEmpty()) {
            GWT.log("************ WIRES MANAGER ************");
            for (String uuid : uuids) {
                GWT.log("[" + uuid + "]");
            }
            GWT.log("***************************************");
        }
        NFastArrayList<WiresShape> childShapes = wiresManager.getLayer().getChildShapes();
        if (null != childShapes && !childShapes.isEmpty()) {
            GWT.log("************ LAYER SHAPES ************");
            for (int i = 0; i < childShapes.size(); i++) {
                WiresShape shape = childShapes.get(i);
                GWT.log("[" + shape.uuid() + "]");
            }
            GWT.log("***************************************");
        }
        WiresConnection headConnection = connector.getHeadConnection();
        WiresConnection tailConnection = connector.getTailConnection();
        WiresMagnet headMagnet = null != headConnection ? headConnection.getMagnet() : null;
        WiresMagnet tailMagnet = null != tailConnection ? tailConnection.getMagnet() : null;
        GWT.log("[HEAD CONNECTION] " + headConnection);
        GWT.log("[HEAD MAGNET] " + headMagnet);
        GWT.log("[TAIL CONNECTION] " + tailConnection);
        GWT.log("[TAIL MAGNET] " + tailMagnet);
    }

    private void onCreateToolbox() {
        if (null == toolbox) {

            toolbox = appendToolbox(shape);

            toolboxHighlight = new ItemsToolboxHighlight(toolbox);

            createTooltip();

            final String green = ColorName.GREEN.getColorString();
            final ButtonItem newShapeButton = addToolboxButton("Add a new shape", green);
            newShapeButton
                    .onClick(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            toolboxHighlight.highlight(newShapeButton);
                            shapeProxy.enable(event.getX(), event.getY());
                        }
                    })
                    .onMoveStart(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            toolboxHighlight.highlight(newShapeButton);
                            shapeProxy.enable(event.getX(), event.getY());
                        }
                    });

            final String yellow = ColorName.YELLOW.getColorString();
            final ButtonItem newConnectorButton = addToolboxButton("Add a new connector", yellow);
            newConnectorButton
                    .onClick(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            GWT.log("[ToolboxTests] Clicking on New Connector Button");
                            toolboxHighlight.highlight(newConnectorButton);
                            connectorProxy.enable(event.getX(), event.getY());
                        }
                    })
                    .onMoveStart(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            GWT.log("[ToolboxTests] Move Start on New Connector Button");
                            toolboxHighlight.highlight(newConnectorButton);
                            connectorProxy.enable(event.getX(), event.getY());
                        }
                    });

            final String blue = ColorName.BLUE.getColorString();
            final ButtonItem newConnShapeButton = addToolboxButton("Add new connector & shape", blue);
            newConnShapeButton
                    .onClick(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            toolboxHighlight.highlight(newConnShapeButton);
                            shapeWithConnectorProxy.enable(event.getX(), event.getY());
                        }
                    })
                    .onMoveStart(new Consumer<AbstractNodeMouseEvent>() {
                        @Override
                        public void accept(AbstractNodeMouseEvent event) {
                            toolboxHighlight.highlight(newConnShapeButton);
                            shapeWithConnectorProxy.enable(event.getX(), event.getY());
                        }
                    });
        }
    }

    private static void log(AbstractNodeDragEvent event) {
        GWT.log("*********** Drag Event **************");
        GWT.log("X/Y                =   [" + event.getX() + ", " + event.getY() + "]");
        GWT.log("CXT Event X/Y      =   [" + event.getDragContext().getEventX() + ", " + event.getDragContext().getEventY() + "]");
        GWT.log("CXT Start X/Y      =   [" + event.getDragContext().getDragStartX() + ", " + event.getDragContext().getDragStartY() + "]");
        GWT.log("CXT DX/DY          =   [" + event.getDragContext().getDy() + ", " + event.getDragContext().getDy() + "]");
        GWT.log("*************************************");
    }

    private void onShowToolbox() {
        if (null != toolbox) {
            toolbox.show();
            draw();
        }
    }

    private void onHideToolbox() {
        if (null != toolbox) {
            toolbox.hide();
            draw();
        }
    }

    private void onCancelProxy() {
        GWT.log("[ToolboxTests] Cancelling proxies");
        if (null != connectorProxy) {
            connectorProxy.destroy();
        }
        if (null != shapeProxy) {
            shapeProxy.destroy();
        }
        if (null != shapeWithConnectorProxy) {
            shapeWithConnectorProxy.destroy();
        }
    }

    private void onDestroyToolbox() {
        if (null != toolbox) {
            tooltip.destroy();
            tooltip = null;
            toolboxHighlight = null;
            toolbox.destroy();
            toolbox = null;
            draw();
        }
        if (null != shapeClickHandlerRegistration) {
            shapeClickHandlerRegistration.removeHandler();
        }
    }

    private void createTooltip() {
        tooltip = ToolboxFactory.INSTANCE.tooltips()
                .forToolbox(toolbox)
                .at(TOOLTIP_AT)
                .towards(TOOLTIP_TOWARDS)
                .withText(tooltipTextStyleConsumer());
    }

    private static Consumer<Text> tooltipTextStyleConsumer() {
        return new Consumer<Text>() {
            @Override
            public void accept(Text text) {
                text
                        .setFontSize(10)
                        .setFontFamily("Verdana");
            }
        };
    }

    private Layer getTopLayer() {
        return layer.getScene().getTopLayer();
    }

    private void draw() {
        layer.draw();
        getTopLayer().draw();
    }

    private static WiresShape createExampleShape() {
        return new WiresShape(new MultiPath()
                                      .rect(0, 0, 100, 100)
                                      .setStrokeColor("#000000")
                                      .setFillColor("#000000"))
                .setDraggable(true);
    }

    private static WiresConnector createExampleConnector(WiresMagnet magnet) {
        MultiPath head = new MultiPath();
        head.M(15,
               20);
        head.L(0,
               20);
        head.L(15 / 2,
               0);
        head.Z();

        MultiPath tail = new MultiPath();
        tail.M(15,
               20);
        tail.L(0,
               20);
        tail.L(15 / 2,
               0);
        tail.Z();

        AbstractDirectionalMultiPointShape<?> line;
        double x0 = magnet.getControl().getX();
        double y0 = magnet.getControl().getY();
        double x1 = x0 + 50;
        double y1 = y0 + 50;
        line = createPolyLine(
                x0,
                y0,
                (x0 + ((x1 - x0) / 2)),
                (y0 + ((y1 - y0) / 2)),
                x1,
                y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());
        line.setSelectionStrokeOffset(25);
        line.setFillShapeForSelection(true);
        line.setFillBoundsForSelection(false);

        WiresConnector connector = new WiresConnector(magnet,
                                                      null,
                                                      line,
                                                      new MultiPathDecorator(head),
                                                      new MultiPathDecorator(tail));

        head.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        line.setStrokeWidth(1.5).setStrokeColor("#0000CC");
        return connector;
    }

    private static PolyLine createPolyLine(final double... points) {
        return new PolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

    @Override
    public void onKeyPress(Key key) {

    }

    @Override
    public void onKeyUp(Key key) {

    }

    @Override
    public void onKeyDown(Key key) {
        if (Key.ESC == key) {
            onCancelProxy();
        }
    }
}
