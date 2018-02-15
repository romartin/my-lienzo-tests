package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseEnterEvent;
import com.ait.lienzo.client.core.event.NodeMouseEnterHandler;
import com.ait.lienzo.client.core.event.NodeMouseExitEvent;
import com.ait.lienzo.client.core.event.NodeMouseExitHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;

public class ConnectorsSelectionTests extends FlowPanel implements MyLienzoTest,
                                                                   HasButtons {
    private WiresConnector connector;
    private WiresConnectorControl connectorControl;
    private WiresManager wires_manager;
    private boolean connectionAllowed = true;
    private Button connectionAllowedButton;

    public void test( final Layer layer ) {
        testWires(layer);
    }

    public void testWires( final Layer layer ) {

        wires_manager = WiresManager.get(layer);

        double w = 100;

        double h = 100;

        wires_manager.setConnectionAcceptor(ACCEPTOR);
        wires_manager.setContainmentAcceptor(IContainmentAcceptor.ALL);

        WiresShape wiresShape0 = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#CC0000")).setDraggable(true);
        wiresShape0.setLocation(new Point2D(400, 400));
        wires_manager.register( wiresShape0 );
        wiresShape0.getContainer().setUserData("A");
        wiresShape0.addChild(new Circle(30), CENTER);

        WiresShape wiresShape1 = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#00CC00")).setDraggable(true);
        wiresShape1.setLocation(new Point2D(50, 50));
        wires_manager.register( wiresShape1 );
        wiresShape1.getContainer().setUserData("A");
        wiresShape1.addChild(new Star(5, 15, 40), CENTER);

        WiresShape wiresShape2 = new WiresShape(new MultiPath().rect(0, 0, 300, 200).setStrokeColor("#0000CC").setFillColor("#FFFFFF")).setDraggable(true);
        wiresShape2.setLocation(new Point2D(50, 100));
        wires_manager.register( wiresShape2 );
        wiresShape2.getContainer().setUserData("B");

        WiresShape wiresShape5 = new WiresShape(new MultiPath().rect(0, 0, 300, 200).setStrokeColor("#000000").setFillColor("#FFFFFF")).setDraggable(true);
        wiresShape5.setLocation(new Point2D(500, 100));
        wires_manager.register( wiresShape5 );
        wiresShape5.getContainer().setUserData("B");

        // bolt
        String svg = "M 0 100 L 65 115 L 65 105 L 120 125 L 120 115 L 200 180 L 140 160 L 140 170 L 85 150 L 85 160 L 0 140 Z";
        WiresShape wiresShape3 = new WiresShape(new MultiPath(svg).setStrokeColor("#0000CC")).setDraggable(true);
        wiresShape3.setLocation(new Point2D(50, 300));
        wires_manager.register( wiresShape3 );
        wiresShape3.getContainer().setUserData("B");

        wires_manager.getMagnetManager().createMagnets(wiresShape0);
        wires_manager.getMagnetManager().createMagnets(wiresShape1);
        wires_manager.getMagnetManager().createMagnets(wiresShape2);
        wires_manager.getMagnetManager().createMagnets(wiresShape3);

        connector = connect(layer, wiresShape1.getMagnets(), 3, wiresShape0.getMagnets(), 7, wires_manager);

        connector.getLine().addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("YEAH CLICKK");
                connector.getLine().asShape().setStrokeColor(ColorName.RED);
                connector.getLine().setStrokeAlpha(1);
                connector.getLine().asShape().setStrokeWidth(1);
                MultiPath head = connector.getHead();
                if (null != head) {
                    head.setStrokeColor(ColorName.RED);
                    head.setStrokeAlpha(1);
                    head.setStrokeWidth(1);
                }
                MultiPath tail = connector.getTail();
                if (null != tail) {
                    tail.setStrokeColor(ColorName.RED);
                    tail.setStrokeAlpha(1);
                    tail.setStrokeWidth(1);
                }
                layer.draw();
            }
        });

        connector.getLine().addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            @Override
            public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                GWT.log("ENTERRRRRRRRRRR");

            }
        });

        connector.getLine().addNodeMouseExitHandler(new NodeMouseExitHandler() {
            @Override
            public void onNodeMouseExit(NodeMouseExitEvent event) {
                GWT.log("EXITTTTTTTTTT");

            }
        });

    }

    private WiresConnector connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager)
    {
        WiresMagnet m0_1 = (WiresMagnet) magnets0.getMagnet(i0_1);
        WiresMagnet m1_1 = (WiresMagnet) magnets1.getMagnet(i1_1);

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
        line.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("YEAH CLICKK");
            }
        });
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        WiresConnector connector = new WiresConnector(m0_1, m1_1, line, new MultiPathDecorator(head), new MultiPathDecorator(tail));
        connectorControl = wiresManager.register(connector);

        head.setStrokeWidth(0.5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(0.5).setStrokeColor("#0000CC");
        line.setStrokeWidth(0.5).setStrokeColor("#0000CC");
        line.setSelectionStrokeOffset(15);
        return connector;
    }

    private final OrthogonalPolyLine createLine(Layer layer, double x, double y, final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points))
                .setCornerRadius(5)
                .setDraggable(true);
    }

    private IConnectionAcceptor ACCEPTOR = new IConnectionAcceptor() {

        @Override
        public boolean headConnectionAllowed(WiresConnection head,
                                             WiresShape shape) {
            GWT.log("ALLOW HEAD = " + connectionAllowed);
            return connectionAllowed;
        }

        @Override
        public boolean tailConnectionAllowed(WiresConnection tail,
                                             WiresShape shape) {
            GWT.log("ALLOW TAIL= " + connectionAllowed);
            return connectionAllowed;
        }

        @Override
        public boolean acceptHead(WiresConnection head,
                                  WiresMagnet magnet) {
            GWT.log("ACCEPT HEAD = " + connectionAllowed);
            return connectionAllowed;
        }

        @Override
        public boolean acceptTail(WiresConnection tail,
                                  WiresMagnet magnet) {
            GWT.log("ACCEPT TAIL = " + connectionAllowed);
            return connectionAllowed;
        }
    };

    @Override
    public void setButtonsPanel(Panel panel) {
        connectionAllowedButton = new Button("Connection allowed [" + connectionAllowed + "]");
        connectionAllowedButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                switchConnectionAllowed();
            }
        });
        panel.add(connectionAllowedButton);
        Button delete = new Button("Delete connector");
        delete.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Deleting connector");
                wires_manager.deregister(connector);
                wires_manager.getLayer().getLayer().batch();
            }
        });
        panel.add(delete);

        Button showPoints = new Button("Show connector points");
        showPoints.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Showing connector points");
                connectorControl.showControlPoints();
            }
        });
        panel.add(showPoints);

        Button hidePoints = new Button("Hide connector points");
        hidePoints.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Hiding connector points");
                connectorControl.hideControlPoints();
            }
        });
        panel.add(hidePoints);
    }

    private void switchConnectionAllowed() {
        if (connectionAllowed) {
            denyConnections();
        } else {
            allowConnections();
        }
    }

    private void allowConnections() {
        connectionAllowed = true;
        setConnAllowedButtonTitle();
    }


    private void denyConnections() {
        connectionAllowed = false;
        setConnAllowedButtonTitle();
    }

    private void setConnAllowedButtonTitle() {
        final String t = "Connection allowed [" + connectionAllowed + "]";
        connectionAllowedButton.setText(t);
        connectionAllowedButton.setTitle(t);
    }


}
