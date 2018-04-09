package org.roger600.lienzo.client.ks;

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.AlignAndDistribute;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.Color;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import org.roger600.lienzo.client.HasButtons;
import org.roger600.lienzo.client.MyLienzoTest;
import org.roger600.lienzo.client.casemodeller.CaseModellerActivityShape;
import org.roger600.lienzo.client.casemodeller.CaseModellerStageShape;


public class WiresAlignDistroTests extends FlowPanel implements MyLienzoTest {
    
	public void test(final Layer layer) {
		
		WiresManager wiresManager = WiresManager.get(layer);
	
		MultiPath circle = new MultiPath();				
		circle.M(100, 0);
        circle.A(150, 0, 150, 50, 50);
        circle.A(150, 100, 100, 100, 50);
        circle.A(50, 100, 50, 50, 50);
        circle.A(50, 0, 100, 0, 50);
        circle.Z();
        circle.setDraggable(true);        
        circle.setStrokeColor("#CC0000");
        circle.setFillColor(ColorName.ORANGE);
		WiresShape shape = new WiresShape(circle);
		shape.setDraggable(true);
		
		wiresManager.register(shape);
		
		WiresShape rect1 = new WiresShape(new MultiPath().rect(0, 0, 50, 100).setStrokeColor("#CC0000"));
		rect1.setDraggable(true);
		wiresManager.register(rect1);
		
		WiresShape rect2 = new WiresShape(new MultiPath().rect(120, 20, 100, 100).setStrokeColor("#CC0000"));
		rect2.setDraggable(true);
		wiresManager.register(rect2);
		
		MultiPath star = new MultiPath();
		star.M(100, 100);
		star.L(200, 150);
		star.L(300, 100);
		star.L(250, 200);
		star.L(300, 300);
		star.L(200, 250);
		star.L(100, 300);
		star.L(150, 200);
		star.Z();
		star.setStrokeColor("#0000CC");
		star.setDraggable(true);		
		WiresShape starShape = new WiresShape(star);
		starShape.setDraggable(true);
		wiresManager.register(starShape);
	}	
}