package org.roger600.lienzo.client.casemodeller.factory;

import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.WiresUtils;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresDockingAndContainmentControlImpl;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;
import org.roger600.lienzo.client.casemodeller.AbstractCaseModellerShape;

public class CaseModellerDockingAndContainmentControlImpl extends WiresDockingAndContainmentControlImpl {

    private Integer m_origin_shape_index;
    private WiresContainer m_origin_container;
    private AbstractCaseModellerShape m_ghost;

    private enum DragEndMode {
        VETO,
        REMOVE_GHOST,
        ADD
    }

    public CaseModellerDockingAndContainmentControlImpl( final WiresShape shape,
                                                         final WiresManager wiresManager ) {
        super( shape,
               wiresManager );
    }

    @Override
    public void dragStart( final Context context ) {
        super.dragStart( context );

        m_origin_shape_index = null;
        m_origin_container = null;

        if ( !( m_shape instanceof AbstractCaseModellerShape ) ) {
            return;
        }

        m_origin_container = m_parent;
        m_origin_shape_index = getShapeIndex();
        m_ghost = ( (AbstractCaseModellerShape) m_shape ).getGhost();

        if ( ( m_parent instanceof AbstractCaseModellerShape ) ) {
            ( (AbstractCaseModellerShape) m_parent ).logicallyReplace( m_shape,
                                                                       m_ghost );
        }
    }

    private Integer getShapeIndex() {
        if ( m_parent == null || m_shape == null ) {
            return null;
        }
        return m_parent.getChildShapes().toList().indexOf( m_shape );
    }

    @Override
    public boolean dragAdjust( final Point2D dxy ) {
        super.dragAdjust( dxy );

        final double mouseX = m_mouseStartX + dxy.getX();
        final double mouseY = m_mouseStartY + dxy.getY();

        //Handle moving ghost from one container to another
        if ( m_ghost != null && m_parent != null ) {
            if ( m_parent.getContainmentAcceptor().acceptContainment( m_parent,
                                                                      m_shape ) ) {
                final Point2D parentAbsLoc = WiresUtils.getLocation( m_parent.getGroup() );
                final Point2D mouseRelativeLoc = new Point2D( mouseX - parentAbsLoc.getX(),
                                                              mouseY - parentAbsLoc.getY() );

                //Children contains m_ghost and others excluding m_shape. This therefore moves m_ghost within children.
                m_parent.getLayoutHandler().add( m_ghost,
                                                 m_parent,
                                                 mouseRelativeLoc );
                m_layer.getLayer().batch();

                final NFastArrayList<WiresShape> shapesToSkip = new NFastArrayList<>();
                shapesToSkip.add( m_ghost );
                shapesToSkip.add( m_shape );
                m_picker = new ColorMapBackedPicker( m_layer.getChildShapes(),
                                                     m_layer.getLayer().getScratchPad(),
                                                     shapesToSkip,
                                                     false,
                                                     IDockingAcceptor.HOTSPOT_SIZE );
            }
        }

        return true;
    }

    @Override
    protected void addShapeToParent() {
        //Children contains m_ghost and others excluding m_shape. This replaces m_ghost with m_shape.
        final DragEndMode mode = getDragEndMode();
        switch ( mode ) {
            case VETO:
                restoreDraggedShape();
                break;
            case REMOVE_GHOST:
                super.addShapeToParent();
                m_ghost.removeFromParent();
                break;
            case ADD:
                super.addShapeToParent();
        }

        m_ghost = null;
        m_origin_shape_index = null;
        m_origin_container = null;
    }

    private DragEndMode getDragEndMode() {
        if ( m_parent == null ) {
            return DragEndMode.VETO;
        } else if ( !m_parent.getContainmentAcceptor().acceptContainment( m_parent,
                                                                          m_shape ) ) {
            return DragEndMode.VETO;
        } else if ( m_ghost != null ) {
            return DragEndMode.REMOVE_GHOST;
        }
        return DragEndMode.ADD;
    }

    private void restoreDraggedShape() {
        if ( m_ghost == null ) {
            return;
        }
        if ( m_origin_container != null ) {
            if ( m_origin_container instanceof AbstractCaseModellerShape ) {
                if ( m_origin_container.getChildShapes().contains( m_ghost ) ) {
                    ( (AbstractCaseModellerShape) m_origin_container ).logicallyReplace( m_ghost,
                                                                                         m_shape );
                } else {
                    m_ghost.removeFromParent();
                    ( (AbstractCaseModellerShape) m_origin_container ).addShape( m_shape,
                                                                                 m_origin_shape_index );
                }

            } else if ( m_ghost.getParent() != null ) {
                final WiresContainer ghostContainer = m_ghost.getParent();
                if ( ghostContainer instanceof AbstractCaseModellerShape ) {
                    final int targetIndex = ghostContainer.getChildShapes().toList().indexOf( m_ghost );
                    ( (AbstractCaseModellerShape) ghostContainer ).addShape( m_shape, targetIndex );
                    m_ghost.removeFromParent();
                }
            }
        }
        m_layer.getLayer().batch();
        m_ghost = null;
    }

}
