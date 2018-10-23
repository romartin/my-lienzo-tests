package org.roger600.lienzo.client.casemodeller.control;

import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.AlignAndDistributeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.MouseEvent;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresContainmentControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresDockingControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresMagnetsControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresParentPickerControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresMagnetsControlImpl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresParentPickerCachedControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresShapeControlImpl;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.Point2D;

public class CaseModellerShapeControl implements WiresShapeControl {

    private final WiresShapeControlImpl shapeControl;

    public CaseModellerShapeControl(WiresShape shape) {
        final ColorMapBackedPicker.PickerOptions pickerOptions =
                new ColorMapBackedPicker.PickerOptions(false,
                                                       0);
        final WiresParentPickerCachedControl parentPicker =
                new WiresParentPickerCachedControl(new CaseModellerShapeLocationControl(shape),
                                                   pickerOptions);
        shapeControl = new WiresShapeControlImpl(parentPicker,
                                                 new WiresMagnetsControlImpl(shape),
                                                 null,
                                                 new CaseModellerContainmentControl(parentPicker));
    }

    @Override
    public boolean isOutOfBounds(final double dx,
                                 final double dy) {
        return false;
    }

    @Override
    public void setAlignAndDistributeControl(AlignAndDistributeControl control) {
        shapeControl.setAlignAndDistributeControl(control);
    }

    @Override
    public WiresMagnetsControl getMagnetsControl() {
        return shapeControl.getMagnetsControl();
    }

    @Override
    public AlignAndDistributeControl getAlignAndDistributeControl() {
        return shapeControl.getAlignAndDistributeControl();
    }

    @Override
    public WiresDockingControl getDockingControl() {
        return shapeControl.getDockingControl();
    }

    @Override
    public WiresContainmentControl getContainmentControl() {
        return shapeControl.getContainmentControl();
    }

    @Override
    public WiresParentPickerControl getParentPickerControl() {
        return shapeControl.getParentPickerControl();
    }

    @Override
    public boolean accept() {
        return shapeControl.accept();
    }

    @Override
    public void execute() {
        shapeControl.execute();
    }

    @Override
    public void clear() {
        shapeControl.clear();
    }

    @Override
    public void reset() {
        shapeControl.reset();
    }

    @Override
    public void destroy()
    {
        shapeControl.destroy();
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        shapeControl.onMouseClick(event);
    }

    @Override
    public void onMouseDown(MouseEvent event) {
        shapeControl.onMouseDown(event);
    }

    @Override
    public void onMouseUp(MouseEvent event) {
        shapeControl.onMouseUp(event);
    }

    @Override
    public void onMoveStart(double x,
                            double y) {
        shapeControl.onMoveStart(x,
                                 y);
    }

    @Override
    public boolean onMove(double dx,
                          double dy) {
        return shapeControl.onMove(dx,
                                   dy);
    }

    @Override
    public boolean onMoveComplete() {
        return shapeControl.onMoveComplete();
    }

    @Override
    public Point2D getAdjust() {
        return shapeControl.getAdjust();
    }
}
