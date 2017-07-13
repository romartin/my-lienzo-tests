package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeDragEndEvent;
import com.ait.lienzo.client.core.event.NodeDragEndHandler;
import com.ait.lienzo.client.core.event.NodeDragMoveEvent;
import com.ait.lienzo.client.core.event.NodeDragMoveHandler;
import com.ait.lienzo.client.core.event.NodeDragStartEvent;
import com.ait.lienzo.client.core.event.NodeDragStartHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.widget.DragConstraintEnforcer;
import com.ait.lienzo.client.widget.DragContext;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.IColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;

public class ConnectionAndMagnetsTests extends FlowPanel implements MyLienzoTest,
                                                                    HasButtons {

    private WiresManager wiresManager;
    private WiresShape redShape;
    private WiresShape greenShape;
    private WiresShape parentShape;
    private WiresConnector connector;
    private WiresConnector connector2;
    private Label labelRed;
    private Label labelGreen;
    private int magnetRed = 7;
    private int magnetGreem = 3;
    private boolean skipCentralMagnet = false;

    @Override
    public void setButtonsPanel(Panel panel) {

        labelRed = new Label(getRedLabelText());
        panel.add(labelRed);

        Button b1 = new Button("RED++");
        b1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetRedPlus();
                doUpdate();
            }
        });
        panel.add(b1);

        Button b2 = new Button("RED--");
        b2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetRedLess();
                doUpdate();
            }
        });
        panel.add(b2);

        labelGreen = new Label(getGreenLabelText());
        panel.add(labelGreen);

        Button b3 = new Button("GREEN++");
        b3.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetGreenPlus();
                doUpdate();
            }
        });
        panel.add(b3);

        Button b4 = new Button("GREEN--");
        b4.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetGreenLess();
                doUpdate();
            }
        });
        panel.add(b4);

    }

    private void doUpdate() {
        labelRed.setText(getRedLabelText());
        labelGreen.setText(getGreenLabelText());
        updateConnectorMagnets();
    }

    private void updateConnectorMagnets() {
        // Red - TAIL
        if (!WiresConnection.isSpecialConnection(false,
                                                 magnetRed)) {
            connector.getTailConnection().setXOffset(0);
            connector.getTailConnection().setYOffset(0);
        }
        connector.setTailMagnet(redShape.getMagnets().getMagnet(magnetRed));
        // Green - HEAD
        if (!WiresConnection.isSpecialConnection(false,
                                                 magnetGreem)) {
            connector.getHeadConnection().setXOffset(0);
            connector.getHeadConnection().setYOffset(0);
        }
        connector.setHeadMagnet(greenShape.getMagnets().getMagnet(magnetGreem));
    }

    private String getRedLabelText() {
        return " (" + magnetRed + "): ";
    }

    private String getGreenLabelText() {
        return " (" + magnetGreem + "): ";
    }

    private void mangetRedPlus() {
        if (magnetRed >= (redShape.getMagnets().size() - 1)) {
            magnetRed = skipCentralMagnet ? 1 : 0;
            if (!skipCentralMagnet) {
                onCenterMagnet();
            }
        } else {
            magnetRed++;
        }
    }

    private void mangetRedLess() {
        if (magnetRed <= (skipCentralMagnet ? 1 : 0)) {
            magnetRed = redShape.getMagnets().size() - 1;
        } else {
            magnetRed--;
            if (!skipCentralMagnet && magnetRed == 0) {
                onCenterMagnet();
            }
        }
    }

    private void mangetGreenPlus() {
        if (magnetGreem >= (greenShape.getMagnets().size() - 1)) {
            magnetGreem = skipCentralMagnet ? 1 : 0;
            if (!skipCentralMagnet) {
                onCenterMagnet();
            }
        } else {
            magnetGreem++;
        }
    }

    private void mangetGreenLess() {
        if (magnetGreem <= (skipCentralMagnet ? 1 : 0)) {
            magnetGreem = greenShape.getMagnets().size() - 1;
        } else {
            magnetGreem--;
            if (!skipCentralMagnet && magnetGreem == 0) {
                onCenterMagnet();
            }
        }
    }

    private void onCenterMagnet() {
        GWT.log("ON CENTER MAGNET");
    }

    public void test(final Layer layer) {

        wiresManager = WiresManager.get(layer);
        wiresManager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wiresManager.setDockingAcceptor(IDockingAcceptor.ALL);
        //wiresManager.setConnectionAcceptor(IConnectionAcceptor.ALL);
        wiresManager.setConnectionAcceptor(logggerAcceptor);
        wiresManager.setSpliceEnabled(false);

        redShape = new WiresShape(new MultiPath().rect(0,
                                                       0,
                                                       100,
                                                       100).setStrokeColor("#CC0000")).setX(300).setY(100).setDraggable(true);
        wiresManager.register(redShape);
        redShape.getContainer().setUserData("A");
        redShape.addChild(new Circle(30),
                          CENTER);

        greenShape = new WiresShape(new MultiPath().rect(0,
                                                         0,
                                                         100,
                                                         100).setStrokeColor("#00CC00")).setX(50).setY(50).setDraggable(true);
        wiresManager.register(greenShape);
        greenShape.getContainer().setUserData("A");
        greenShape.addChild(new Star(5,
                                     15,
                                     40),
                            CENTER);

        parentShape = new WiresShape(new MultiPath().rect(0,
                                                          0,
                                                          500,
                                                          400).setStrokeColor("#000000")).setX(200).setY(250).setDraggable(true);
        wiresManager.register(parentShape);
        parentShape.getContainer().setUserData("A");

        wiresManager.getMagnetManager().createMagnets(parentShape);
        wiresManager.getMagnetManager().createMagnets(redShape);
        wiresManager.getMagnetManager().createMagnets(greenShape);

        createAndSetUpConnector1();

        createAndSetUpConnector2();

    }

    private void createAndSetUpConnector2() {

        final Layer layer = wiresManager.getLayer().getLayer();

        final WiresConnectorControl[] connectorControl =  new WiresConnectorControl[] { null };

        final double cx = 25;
        final double cy = 25;
        final Circle newButton = new Circle(15)
                .setX(cx)
                .setY(cy)
                .setFillColor(ColorName.RED)
                .setSelectionBoundsOffset(15)
                .setFillBoundsForSelection(true)
                .setDraggable(true);

        newButton.setDragConstraints(new DragConstraintEnforcer() {
            @Override
            public void startDrag(DragContext dragContext) {
                GWT.log("CREATING NEW CONNECTOR");

                if (null != connector2) {
                    wiresManager.deregister(connector2);
                    connector2 = null;
                    connectorControl[0] = null;
                }

                WiresMagnet gm3 = greenShape.getMagnets().getMagnet(3);

                Point2D loc = dragContext.getLocalAdjusted();

                connector2 = connector(layer,
                                       ColorName.RED,
                                       gm3,
                                       gm3.getControl().getLocation(),
                                       null,
                                       loc);

                connectorControl[0] = wiresManager.register(connector2);

                connectorControl[0].showControlPoints();

                connectorControl[0].getTailConnectionControl().dragStart(dragContext);

                newButton.setAlpha(0);
            }

            @Override
            public boolean adjust(Point2D dxy) {
                if (null != connectorControl[0]) {
                    boolean adjusted = connectorControl[0].getTailConnectionControl().dragAdjust(dxy);
                    GWT.log("ADJUST [" + adjusted + "] TAIL TO [" + dxy.getX() + ", " + dxy.getY() + "]");
                    return adjusted;
                }
                return false;
            }
        });

        newButton.addNodeDragStartHandler(new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart(NodeDragStartEvent event) {
            }
        });

        newButton.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove(NodeDragMoveEvent event) {
                GWT.log("MOVE TO [" + event.getX() + ", " + event.getY() + "]");
                connectorControl[0].getTailConnectionControl().dragMove(event.getDragContext());
                connector2.getTailConnection().move(event.getX(), event.getY());
            }
        });

        newButton.addNodeDragEndHandler(new NodeDragEndHandler() {
            @Override
            public void onNodeDragEnd(NodeDragEndEvent event) {
                GWT.log("DESTROYING NEW CONNECTOR");
                connector2.getTailConnection().move(event.getX(), event.getY());
                boolean accepts =
                        connectorControl[0].getTailConnectionControl().dragEnd(event.getDragContext());
                if (accepts) {
                    connectorControl[0].hideControlPoints();
                } else {
                    wiresManager.deregister(connector2);
                    connector2 = null;
                }
                connectorControl[0] = null;
                newButton
                        .setAlpha(1)
                        .setX(cx)
                        .setY(cy);
            }
        });

        layer.add(newButton);

    }

    private void createAndSetUpConnector1() {
        WiresMagnet gm3 = greenShape.getMagnets().getMagnet(3);
        WiresMagnet rm7 = redShape.getMagnets().getMagnet(7);
        connector = connector(wiresManager.getLayer().getLayer(),
                              ColorName.BLUE,
                              gm3,
                              gm3.getControl().getLocation(),
                              rm7,
                              rm7.getControl().getLocation());
        wiresManager.register(connector);
    }

    private IConnectionAcceptor logggerAcceptor = new IConnectionAcceptor() {
        private final boolean result = true;

        @Override
        public boolean headConnectionAllowed(WiresConnection head,
                                             WiresShape shape) {
            GWT.log("HEAD ALLOW");
            return result;
        }

        @Override
        public boolean tailConnectionAllowed(WiresConnection tail,
                                             WiresShape shape) {
            GWT.log("TAIL ALLOW");
            return result;
        }

        @Override
        public boolean acceptHead(WiresConnection head,
                                  WiresMagnet magnet) {
            GWT.log("HEAD ACCEPT");
            return result;
        }

        @Override
        public boolean acceptTail(WiresConnection tail,
                                  WiresMagnet magnet) {
            GWT.log("TAIL ACCEPT");
            return result;
        }
    };

    private WiresConnector connector(Layer layer,
                                     IColor color,
                                     WiresMagnet magnet0,
                                     Point2D loc0,
                                     WiresMagnet magnet1,
                                     Point2D loc1) {

        double x0, x1, y0, y1;

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

        OrthogonalPolyLine line;
        x0 = loc0.getX();
        y0 = loc0.getY();
        x1 = loc1.getX();
        y1 = loc1.getY();
        line = createLine(layer,
                          0,
                          0,
                          x0,
                          y0,
                          (x0 + ((x1 - x0) / 2)),
                          (y0 + ((y1 - y0) / 2)),
                          x1,
                          y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());
        line.setSelectionStrokeOffset(25);

        head.setStrokeWidth(2).setStrokeColor(color);
        tail.setStrokeWidth(2).setStrokeColor(color);
        line.setStrokeWidth(2).setStrokeColor(color);

        WiresConnector connector = new WiresConnector(magnet0,
                                       magnet1,
                                       line,
                                       new MultiPathDecorator(head),
                                       new MultiPathDecorator(tail));

        return connector;

    }

    private final OrthogonalPolyLine createLine(Layer layer,
                                                double x,
                                                double y,
                                                final double... points) {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
}
