package org.roger600.lienzo.client.casemodeller.control;

import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresContainmentControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresParentPickerControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresContainmentControlImpl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresParentPickerControlImpl;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.Point2D;
import org.roger600.lienzo.client.casemodeller.AbstractCaseModellerShape;

public class CaseModellerContainmentControl implements WiresContainmentControl {

    private enum DragEndMode {
        VETO,
        REMOVE_GHOST,
        ADD
    }

    private final WiresContainmentControlImpl containmentControl;
    private Integer m_origin_shape_index;
    private WiresContainer m_origin_container;
    private AbstractCaseModellerShape m_ghost;

    public CaseModellerContainmentControl(WiresShape shape,
                                          ColorMapBackedPicker.PickerOptions pickerOptions) {
        this.containmentControl = new WiresContainmentControlImpl(shape,
                                                                  pickerOptions);
    }

    public CaseModellerContainmentControl(WiresParentPickerControlImpl parentPickerControl) {
        this.containmentControl = new WiresContainmentControlImpl(parentPickerControl);
    }

    @Override
    public WiresContainmentControl setEnabled(boolean enabled) {
        containmentControl.setEnabled(enabled);
        return this;
    }

    @Override
    public void onMoveStart(double x,
                            double y) {
        containmentControl.onMoveStart(x,
                                       y);

        m_origin_shape_index = null;
        m_origin_container = null;

        if (!(getShape() instanceof AbstractCaseModellerShape)) {
            return;
        }

        m_origin_container = getParent();
        m_origin_shape_index = getShapeIndex();
        m_ghost = ((AbstractCaseModellerShape) getShape()).getGhost();

        final WiresParentPickerControl.Index index = containmentControl.getParentPickerControl().getIndex();
        index.clear();
        if (null != m_ghost) {
            index.addShapeToSkip(m_ghost);
        }

        if ((getParent() instanceof AbstractCaseModellerShape)) {
            ((AbstractCaseModellerShape) getParent()).logicallyReplace(getShape(),
                                                                       m_ghost);
        }
    }

    @Override
    public boolean onMove(double dx,
                          double dy) {
        containmentControl.onMove(dx,
                                  dy);

        final double mouseX = containmentControl.getParentPickerControl().getShapeLocationControl().getMouseStartX() + dx;
        final double mouseY = containmentControl.getParentPickerControl().getShapeLocationControl().getMouseStartY() + dy;

        //Handle moving ghost from one container to another
        if (m_ghost != null && getParent() != null) {
            if (getWiresManager().getContainmentAcceptor().acceptContainment(getParent(),
                                                                             new WiresShape[]{getShape()})) {
                final Point2D parentAbsLoc = getParent().getGroup().getComputedLocation();
                final Point2D mouseRelativeLoc = new Point2D(mouseX - parentAbsLoc.getX(),
                                                             mouseY - parentAbsLoc.getY());
                //Children contains m_ghost and others excluding m_shape. This therefore moves m_ghost within children.
                getParent().getLayoutHandler().add(m_ghost,
                                                   getParent(),
                                                   mouseRelativeLoc);
            }
        }

        return false;
    }

    @Override
    public boolean onMoveComplete() {
        containmentControl.onMoveComplete();
        return true;
    }

    @Override
    public boolean isAllow() {
        return containmentControl.isAllow();
    }

    @Override
    public boolean accept() {
        return containmentControl.accept();
    }

    @Override
    public Point2D getCandidateLocation() {
        return containmentControl.getCandidateLocation();
    }

    @Override
    public Point2D getAdjust() {
        return containmentControl.getAdjust();
    }

    @Override
    public void execute() {
        //Children contains m_ghost and others excluding m_shape. This replaces m_ghost with m_shape.
        final DragEndMode mode = getDragEndMode();
        switch (mode) {
            case VETO:
                restoreDraggedShape();
                break;
            case REMOVE_GHOST:
                containmentControl.execute();
                m_ghost.removeFromParent();
                break;
            case ADD:
                containmentControl.execute();
        }

        clear();
    }

    @Override
    public void clear() {
        m_ghost = null;
        m_origin_shape_index = null;
        m_origin_container = null;
    }

    @Override
    public void reset() {
        restoreDraggedShape();
    }

    private WiresContainer getParent() {
        return containmentControl.getParent();
    }

    private WiresShape getShape() {
        return containmentControl.getShape();
    }

    private WiresManager getWiresManager() {
        return getShape().getWiresManager();
    }

    private Integer getShapeIndex() {
        if (getParent() == null || getShape() == null) {
            return null;
        }
        return getParent().getChildShapes().toList().indexOf(getShape());
    }

    private DragEndMode getDragEndMode() {
        if (getParent() == null) {
            return DragEndMode.VETO;
        } else if (!getWiresManager().getContainmentAcceptor().acceptContainment(getParent(),
                                                                                 new WiresShape[]{getShape()})) {
            return DragEndMode.VETO;
        } else if (m_ghost != null) {
            return DragEndMode.REMOVE_GHOST;
        }
        return DragEndMode.ADD;
    }

    private void restoreDraggedShape() {
        if (m_ghost == null) {
            return;
        }
        if (m_origin_container != null) {
            if (m_origin_container instanceof AbstractCaseModellerShape) {
                if (m_origin_container.getChildShapes().contains(m_ghost)) {
                    ((AbstractCaseModellerShape) m_origin_container).logicallyReplace(m_ghost,
                                                                                      getShape());
                } else {
                    m_ghost.removeFromParent();
                    ((AbstractCaseModellerShape) m_origin_container).addShape(getShape(),
                                                                              m_origin_shape_index);
                }
            } else if (m_ghost.getParent() != null) {
                final WiresContainer ghostContainer = m_ghost.getParent();
                if (ghostContainer instanceof AbstractCaseModellerShape) {
                    final int targetIndex = ghostContainer.getChildShapes().toList().indexOf(m_ghost);
                    ((AbstractCaseModellerShape) ghostContainer).addShape(getShape(),
                                                                          targetIndex);
                    m_ghost.removeFromParent();
                }
            }
        }
        m_ghost = null;
    }

}
