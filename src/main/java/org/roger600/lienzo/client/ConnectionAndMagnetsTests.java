package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2DArray;
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

    private WiresShape redShape;
    private WiresShape greenShape;
    private WiresShape parentShape;
    private WiresConnector connector;
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

        WiresManager wires_manager = WiresManager.get(layer);
        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);
        wires_manager.setDockingAcceptor(IDockingAcceptor.ALL);
        //wires_manager.setConnectionAcceptor(IConnectionAcceptor.ALL);
        wires_manager.setConnectionAcceptor(logggerAcceptor);
        wires_manager.setSpliceEnabled(false);

        redShape = new WiresShape(new MultiPath().rect(0,
                                                       0,
                                                       100,
                                                       100).setStrokeColor("#CC0000")).setX(300).setY(100).setDraggable(true);
        wires_manager.register(redShape);
        redShape.getContainer().setUserData("A");
        redShape.addChild(new Circle(30),
                          CENTER);

        greenShape = new WiresShape(new MultiPath().rect(0,
                                                         0,
                                                         100,
                                                         100).setStrokeColor("#00CC00")).setX(50).setY(50).setDraggable(true);
        wires_manager.register(greenShape);
        greenShape.getContainer().setUserData("A");
        greenShape.addChild(new Star(5,
                                     15,
                                     40),
                            CENTER);

        parentShape = new WiresShape(new MultiPath().rect(0,
                                                          0,
                                                          500,
                                                          400).setStrokeColor("#000000")).setX(200).setY(250).setDraggable(true);
        wires_manager.register(parentShape);
        parentShape.getContainer().setUserData("A");

        wires_manager.getMagnetManager().createMagnets(parentShape);
        wires_manager.getMagnetManager().createMagnets(redShape);
        wires_manager.getMagnetManager().createMagnets(greenShape);

        connect(layer,
                greenShape.getMagnets(),
                3,
                redShape.getMagnets(),
                magnetRed,
                wires_manager);
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

    private void connect(Layer layer,
                         MagnetManager.Magnets magnets0,
                         int i0_1,
                         MagnetManager.Magnets magnets1,
                         int i1_1,
                         WiresManager wiresManager) {
        WiresMagnet m0_1 = magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = magnets1.getMagnet(i1_1);

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
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
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

        connector = new WiresConnector(m0_1,
                                       m1_1,
                                       line,
                                       new MultiPathDecorator(head),
                                       new MultiPathDecorator(tail));
        wiresManager.register(connector);

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine(Layer layer,
                                                double x,
                                                double y,
                                                final double... points) {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
}
