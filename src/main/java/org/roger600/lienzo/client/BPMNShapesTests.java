package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.LinearGradient;
import com.ait.lienzo.client.core.types.RadialGradient;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DragMode;
import com.google.gwt.user.client.ui.FlowPanel;


public class BPMNShapesTests extends FlowPanel {
    
    private Layer layer;

    public BPMNShapesTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        
        WiresManager wiresManager = WiresManager.get(layer);
        
        WiresShape taskShape = task(wiresManager);
        
        
    }
    
    private WiresShape task(WiresManager wiresManager) {

        double width = 100;
        double height = 100;
        
        String startColor = "#dfeff8";
        String endColor = "#FFFFFF";
        final LinearGradient linearGradient = new LinearGradient(0, ( width / 2), 0, - ( height / 2) );
        linearGradient.addColorStop(1, endColor );
        linearGradient.addColorStop(0, startColor );
        
        MultiPath path = new MultiPath().rect(0, 0, width, height);
        
        path.setFillGradient(linearGradient);
        path.setStrokeWidth(1);
        
        WiresShape taskShape = wiresManager.createShape(path);
        
        Group userTypeIcon = taskTypeUser();
        taskShape.addChild(userTypeIcon, WiresLayoutContainer.Layout.TOP);

        addText(taskShape, "My task");
        
        return taskShape;
        
    }
    
    private Group taskTypeUser() {
        
        final String p1 = "M0.585,24.167h24.083v-7.833c0,0-2.333-3.917-7.083-5.167h-9.25\n" +
                "\t\t\tc-4.417,1.333-7.833,5.75-7.833,5.75L0.585,24.167z";

        final String p2 = "M 6 20 L 6 24";

        final String p3 = "M 20 20 L 20 24";

        // TODO: Circle
        
        final String p4 = "M8.043,7.083c0,0,2.814-2.426,5.376-1.807s4.624-0.693,4.624-0.693\n" +
                "\t\t\tc0.25,1.688,0.042,3.75-1.458,5.584c0,0,1.083,0.75,1.083,1.5s0.125,1.875-1,3s-5.5,1.25-6.75,0S8.668,12.834,8.668,12\n" +
                "\t\t\ts0.583-1.25,1.25-1.917C8.835,9.5,7.419,7.708,8.043,7.083z";

        final Group userTypeGroup = new Group()
                .setX(50).setY(50);
        
        final SVGPath svgP1 = createSVGPath(p1);
        userTypeGroup.add(svgP1);
        final SVGPath svgP2 = createSVGPath(p2);
        userTypeGroup.add(svgP2);
        final SVGPath svgP3 = createSVGPath(p3);
        userTypeGroup.add(svgP3);
        final SVGPath svgP4 = createSVGPath(p4);
        userTypeGroup.add(svgP4);
        
        return userTypeGroup;
    }
    
    private SVGPath createSVGPath(String path) {
        return new SVGPath(path)
                .setStrokeColor(ColorName.BLACK)
                .setDraggable(false);
    }

    private WiresShape startNoneEvent(WiresManager wiresManager) {
        
        double radius = 50;

        String startColor = "#dfeff8";
        String endColor = "#FFFFFF";
        final RadialGradient radialGradient = new RadialGradient(0, 0, 0, 0, 0, 40);
        radialGradient.addColorStop(1.0, endColor);
        radialGradient.addColorStop(0.0, startColor);
        
        return null;
    }
    
    private void addText(WiresShape shape, String _text) {
        Text text = new Text(_text)
                .setFontSize(8)
                .setStrokeWidth(0.5)
                .setFontFamily("Verdana");
        
        shape.addChild(text, WiresLayoutContainer.Layout.CENTER);
        text.moveToTop();
    }
        
    private void log(final String message) {
        // GWT.log(message);
    }

}
