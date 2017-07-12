package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;

public class ConnectionAndMagnetsTests extends FlowPanel implements MyLienzoTest, HasButtons {

    private WiresShape redShape;
    private WiresShape greenShape;
    private WiresConnector connector;
    private Label labelRed;
    private Label labelGreen;
    private int magnetRed = 7;
    private int magnetGreem = 3;

    @Override
    public void setButtonsPanel(Panel panel) {

        labelRed = new Label(getRedLabelText());
        panel.add(labelRed);

        Button b1 = new Button("RED++");
        b1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetRedPlus();
                updateConnectorMagnets();
            }
        });
        panel.add(b1);

        Button b2 = new Button("RED--");
        b2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetRedLess();
                updateConnectorMagnets();
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
                updateConnectorMagnets();
            }
        });
        panel.add(b3);

        Button b4 = new Button("GREEN--");
        b4.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                mangetGreenLess();
                updateConnectorMagnets();
            }
        });
        panel.add(b4);
    }

    private void updateConnectorMagnets() {
        labelRed.setText(getRedLabelText());
        labelGreen.setText(getGreenLabelText());
        connector.getTailConnection().setMagnet(redShape.getMagnets().getMagnet(magnetRed));
        connector.getHeadConnection().setMagnet(greenShape.getMagnets().getMagnet(magnetGreem));
    }

    private String getRedLabelText() {
        return " (" + magnetRed + "): ";
    }

    private String getGreenLabelText() {
        return " (" + magnetGreem + "): ";
    }

    private void mangetRedPlus() {
        if (magnetRed >= (redShape.getMagnets().size() - 1)) {
            magnetRed = 0;
        } else {
            magnetRed++;
        }
    }

    private void mangetRedLess() {
        if (magnetRed <= 0) {
            magnetRed = redShape.getMagnets().size() - 1;
        } else {
            magnetRed--;
        }
    }

    private void mangetGreenPlus() {
        if (magnetGreem >= (greenShape.getMagnets().size() - 1)) {
            magnetGreem = 0;
        } else {
            magnetGreem++;
        }
    }

    private void mangetGreenLess() {
        if (magnetGreem <= 0) {
            magnetGreem = greenShape.getMagnets().size() - 1;
        } else {
            magnetGreem--;
        }
    }

    public void test(final Layer layer) {

        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setContainmentAcceptor(IContainmentAcceptor.NONE);
        wires_manager.setConnectionAcceptor(IConnectionAcceptor.NONE);

        wires_manager.setSpliceEnabled(false);

        double w = 100;

        double h = 100;

        redShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#CC0000")).setX(400).setY(400).setDraggable(true);
        wires_manager.register(redShape);
        redShape.getContainer().setUserData("A");
        redShape.addChild(new Circle(30), CENTER);

        greenShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#00CC00")).setX(50).setY(50).setDraggable(true);
        wires_manager.register(greenShape);
        greenShape.getContainer().setUserData("A");
        greenShape.addChild(new Star(5, 15, 40), CENTER);

        wires_manager.getMagnetManager().createMagnets(redShape);
        wires_manager.getMagnetManager().createMagnets(greenShape);

        connect(layer, greenShape.getMagnets(), 3, redShape.getMagnets(), magnetRed, wires_manager);

    }

    private void connect(Layer layer, MagnetManager.Magnets magnets0, int i0_1, MagnetManager.Magnets magnets1, int i1_1, WiresManager wiresManager) {
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
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());

        connector = new WiresConnector(m0_1, m1_1, line, new MultiPathDecorator(head), new MultiPathDecorator(tail));
        wiresManager.register(connector);

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private final OrthogonalPolyLine createLine(Layer layer, double x, double y, final double... points) {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

}
