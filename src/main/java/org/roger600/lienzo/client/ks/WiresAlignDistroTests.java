package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.AlignAndDistribute;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.FlowPanel;
import org.roger600.lienzo.client.MyLienzoTest;

public class WiresAlignDistroTests extends FlowPanel implements MyLienzoTest {

    public void test( final Layer layer ) {

        Rectangle rect1 = new Rectangle(100, 100);
        rect1.setDraggable(true);
        rect1.setX(100);
        rect1.setY(300);
        rect1.setStrokeWidth(2);
        rect1.setFillColor("#CC0000");
        rect1.setFillAlpha(0.75);
        rect1.setStrokeColor( ColorName.BLACK);
        layer.add(rect1);

        Circle circ1 = new Circle(50);
        circ1.setDraggable(true);
        circ1.setX(320);
        circ1.setY(325);
        circ1.setStrokeWidth(2);
        circ1.setFillColor("#00CC00");
        circ1.setFillAlpha(0.75);
        circ1.setStrokeColor(ColorName.BLACK);
        layer.add(circ1);

        Rectangle rect3 = new Rectangle(100, 100);
        rect3.setDraggable(true);
        rect3.setX(500);
        rect3.setY(250);
        rect3.setStrokeWidth(2);
        rect3.setFillColor("#AACC00");
        rect3.setFillAlpha(0.75);
        rect3.setStrokeColor(ColorName.BLACK);
        layer.add(rect3);

        Rectangle rect4 = new Rectangle(300, 150);
        rect4.setCornerRadius(8);
        rect4.setDraggable(true);
        rect4.setX(50);
        rect4.setY(50);
        rect4.setStrokeWidth(2);
        rect4.setFillColor("#55CCAA");
        rect4.setFillAlpha(0.75);
        rect4.setStrokeColor(ColorName.BLACK);
        layer.add(rect4);

        Text text1 = new Text("Align");
        text1.setDraggable(true);
        text1.setX(500);
        text1.setY(500);
        text1.setFontSize(96);
        text1.setStrokeWidth(2);
        text1.setFillColor(ColorName.HOTPINK);
        text1.setFontStyle("bold");
        text1.setFillAlpha(0.75);
        text1.setStrokeColor(ColorName.BLACK);
        layer.add(text1);

        Star star1 = new Star(5, 50, 100);
        star1.setDraggable(true);
        star1.setX(250);
        star1.setY(550);
        star1.setStrokeWidth(2);
        star1.setFillColor(ColorName.DARKORCHID);
        star1.setFillAlpha(0.75);
        star1.setStrokeColor(ColorName.BLACK);
        layer.add(star1);

        AlignAndDistribute index = new AlignAndDistribute(layer);
        index.setStrokeWidth(2);
        index.setStrokeColor(ColorName.DARKBLUE.getValue());
        index.addShape(rect1);
        index.addShape(circ1);
        index.addShape(rect3);
        index.addShape(rect4);
        index.addShape(text1);
        index.addShape(star1);

    }
    
}
