package org.roger600.lienzo.client.casemodeller;

import java.util.List;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.ILayoutHandler;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

public class CaseModellerShape extends AbstractCaseModellerShape {

    private static final double MIN_WIDTH = 500.0;
    private static final double MIN_HEIGHT = 500.0;
    private static final double PADDING_X = 25.0;
    private static final double PADDING_Y = 25.0;

    public static final IContainmentAcceptor CASE_MODEL = new IContainmentAcceptor() {

        @Override
        public boolean acceptContainment( final WiresContainer parent,
                                          final WiresShape child ) {
            return ( parent instanceof CaseModellerShape && child instanceof CaseModellerStageShape );
        }

        @Override
        public boolean containmentAllowed( final WiresContainer parent,
                                           final WiresShape child ) {
            return ( parent instanceof CaseModellerShape && child instanceof CaseModellerStageShape );
        }
    };

    private static final ILayoutHandler LAYOUT_HORIZONTAL_STACK = new ILayoutHandler() {

        @Override
        public void add( final WiresShape shape,
                         final WiresContainer container,
                         final Point2D mouseRelativeLoc ) {
            orderChildren( shape,
                           container,
                           mouseRelativeLoc );
        }

        private void orderChildren( final WiresShape shape,
                                    final WiresContainer container,
                                    final Point2D mouseRelativeLoc ) {
            if ( container == null ) {
                return;
            }
            final double shapeX = mouseRelativeLoc.getX();

            final NFastArrayList<WiresShape> nChildren = container.getChildShapes().copy();
            final List<WiresShape> children = nChildren.remove( shape ).toList();

            int targetIndex = children.size();

            for ( int idx = 0; idx < children.size(); idx++ ) {
                final WiresShape child = children.get( idx );
                if ( shapeX < child.getX() ) {
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
        public void remove( final WiresShape shape,
                            final WiresContainer container ) {
            container.remove( shape );
        }

        @Override
        public void requestLayout( final WiresContainer container ) {
            layout( container );
        }

        @Override
        public void layout( final WiresContainer container ) {
            double x = PADDING_X;
            for ( WiresShape ws : container.getChildShapes() ) {
                ws.setX( x ).setY( PADDING_Y );
                x = x + ws.getPath().getBoundingBox().getWidth() + PADDING_X;
            }
        }
    };

    public CaseModellerShape() {
        super( MIN_WIDTH,
               MIN_HEIGHT );
        setLayoutHandler( LAYOUT_HORIZONTAL_STACK );
    }

    @Override
    public void setPath( final double width,
                         final double height ) {
        final MultiPath path = getPath();
        path.clear();
        path.rect( 0, 0, width, height ).setStrokeColor( "#00ff00" ).setStrokeWidth( 1.0 );
    }

    @Override
    public AbstractCaseModellerShape getGhost() {
        throw new IllegalStateException( "Defensive Programming: Should not happen" );
    }

}
