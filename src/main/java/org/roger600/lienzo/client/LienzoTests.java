package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class LienzoTests implements EntryPoint {

    private LienzoPanel panel = new LienzoPanel(1200, 900);
    private Layer layer = new Layer();
    
    public void onModuleLoad()
    {
        RootPanel.get().add(panel);
        layer.setTransformable(true);
        panel.add(layer);

        DoubleClickTests tests = new DoubleClickTests(layer);
        tests.test();
        layer.draw();

    }

}
