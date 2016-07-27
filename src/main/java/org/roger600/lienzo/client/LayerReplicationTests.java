package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LayerReplicationTests /* implements EntryPoint */ {

    private VerticalPanel mainPanel = new VerticalPanel();
    private LienzoPanel panel = new LienzoPanel(600, 600);
    private Layer layer = new Layer();

    private VerticalPanel separator = new VerticalPanel();

    private LienzoPanel panel1 = new LienzoPanel(600, 600);
    private Layer layer1 = new Layer();

    public void onModuleLoad()
    {

        RootPanel.get().add(mainPanel);
        mainPanel.add(panel);

        separator.getElement().getStyle().setHeight( 100, Style.Unit.PX );
        separator.getElement().getStyle().setBackgroundColor(ColorName.LIGHTGREY.getColorString());
        mainPanel.add(separator);

        mainPanel.add(panel1);

        layer.setTransformable(true);
        layer1.setTransformable(true);

        panel1.add(layer1);
        layer.setReplicatedLayer( layer1 );
        panel.add(layer);

        doIt();

        layer.draw();
        layer1.draw();
    }

    private void doIt() {

        Circle c1 = new Circle( 50 ).setFillColor( ColorName.YELLOW );

        layer.add( c1.setX( 50).setY( 50 ) );

        buildButtons();

    }

    private void buildButtons() {
        Rectangle button1 = new Rectangle(100, 50);
        button1.setX(0).setY(0).setFillColor(ColorName.BLACK);
        button1.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("Adding circle2");
                Circle c2 = new Circle( 25 ).setFillColor( ColorName.MAROON );
                layer.add( c2.setX( 150 ).setY( 150 ) );
                layer.batch();

            }
        });
        layer.add(button1);
        Rectangle button2 = new Rectangle(100, 50);
        button2.setX(120).setY(0).setFillColor(ColorName.GREY);
        button2.addNodeMouseClickHandler(new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("Adding circle2");
                Circle c3 = new Circle( 25 ).setFillColor( ColorName.CHOCOLATE );
                layer.add( c3.setX( 150 ).setY( 300 ) );
                layer.batch();
            }
        });
        layer.add(button2);
    }

}
