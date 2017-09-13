package org.roger600.lienzo.client.casemodeller;

import java.util.List;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.ILayoutHandler;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.Color;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

public class CaseModellerStageShape extends AbstractCaseModellerShape {

    private static final double MIN_WIDTH = 100.0;
    private static final double MIN_HEIGHT = 500.0;
    private static final double PADDING_X = 0.0;
    private static final double PADDING_Y = 10.0;

    private static final double DECORATION_HEIGHT = 50.0;
    private static final double DECORATION_OFFSET = 20.0;

    public static final IContainmentAcceptor CASE_STAGE = new IContainmentAcceptor() {

        @Override
        public boolean containmentAllowed( final WiresContainer parent,
                                           final WiresShape[] children ) {
            return ( parent instanceof CaseModellerStageShape && children[0] instanceof CaseModellerActivityShape );
        }

        @Override
        public boolean acceptContainment( final WiresContainer parent,
                                          final WiresShape[] children ) {
            return ( parent instanceof CaseModellerStageShape && children[0] instanceof CaseModellerActivityShape );
        }
    };

    private static final ILayoutHandler LAYOUT_VERTICAL_STACK = new AbstractNestedLayoutHandler() {

        @Override
        protected void orderChildren( final WiresShape shape,
                                      final WiresContainer container,
                                      final Point2D mouseRelativeLoc ) {
            if ( container == null ) {
                return;
            }
            final double my = mouseRelativeLoc.getY();

            final NFastArrayList<WiresShape> nChildren = container.getChildShapes().copy();
            final List<WiresShape> children = nChildren.remove( shape ).toList();

            int targetIndex = children.size();

            for ( int idx = 0; idx < children.size(); idx++ ) {
                final WiresShape child = children.get( idx );
                if ( my < child.getY() ) {
                    targetIndex = idx;
                    break;
                }
            }

            final int currentIndex = container.getChildShapes().toList().indexOf( shape );
            if ( currentIndex != targetIndex ) {
                ( (AbstractCaseModellerShape) container ).addShape( shape,
                                                                    targetIndex );
            }
        }

        @Override
        public void layout( final WiresContainer container ) {
            double y = DECORATION_HEIGHT + PADDING_Y;
            for ( WiresShape ws : container.getChildShapes() ) {
                ws.setLocation( new Point2D(PADDING_X, y ) );
                y = y + ws.getPath().getBoundingBox().getHeight() + PADDING_Y;
            }
            resizeContainer( container );
        }

        @Override
        protected void resizeContainer( final WiresContainer container ) {
            if ( container instanceof AbstractCaseModellerShape ) {
                final AbstractCaseModellerShape shape = (AbstractCaseModellerShape) container;
                final BoundingBox pbb = getParentContentBoundingBox( container );
                final double requiredWidth = Math.max( shape.getMinWidth(),
                                                       pbb.getWidth() + PADDING_X * 2 );

                shape.setWidth( requiredWidth );
            }
        }

    };

    public CaseModellerStageShape() {
        super( MIN_WIDTH,
               MIN_HEIGHT );
        decoration.setStrokeColor( "#880000" ).setFillColor( "#ff0000" );
        setLayoutHandler( LAYOUT_VERTICAL_STACK );
    }

    @Override
    public void setPath( final double width,
                         final double height ) {
        final MultiPath path = getPath();
        path.clear();
        path.rect( 0, 0, width, height ).setStrokeColor( ColorName.WHITE ).setStrokeWidth( 0.01 );
    }

    @Override
    public void updateDecoration() {
        decoration.clear();
        decoration.M( 0, 0 )
                .L( DECORATION_OFFSET, DECORATION_HEIGHT / 2 )
                .L( 0, DECORATION_HEIGHT )
                .L( getWidth(), DECORATION_HEIGHT )
                .L( getWidth() + DECORATION_OFFSET, DECORATION_HEIGHT / 2 )
                .L( getWidth(), 0 ).L( 0, 0 ).Z()
                .setStrokeWidth( 2.0 )
                .setDraggable( false )
                .setListening( false )
                .close();
    }

    @Override
    public AbstractCaseModellerShape getGhost() {
        final CaseModellerStageShape ghost = new CaseModellerStageShape();
        ghost.decoration.setStrokeColor( "#c87977" ).setFillColor( "#e0a1a1" );

        for ( WiresShape ws : getChildShapes() ) {
            final AbstractCaseModellerShape wsg = ( (AbstractCaseModellerShape) ws ).getGhost();
            ghost.add( wsg );
        }

        return ghost;
    }
}
