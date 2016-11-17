package org.roger600.lienzo.client.casemodeller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.ILayoutHandler;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;

public abstract class AbstractNestedLayoutHandler implements ILayoutHandler {

    private ReverseBreadthFirstTreeWalker walker = new ReverseBreadthFirstTreeWalker();

    @Override
    public void add( final WiresShape shape,
                     final WiresContainer container,
                     final Point2D mouseRelativeLoc ) {
        orderChildren( shape,
                       container,
                       mouseRelativeLoc );
    }

    protected abstract void orderChildren( final WiresShape shape,
                                           final WiresContainer container,
                                           final Point2D mouseRelativeLoc );

    @Override
    public void remove( final WiresShape shape,
                        final WiresContainer container ) {
        container.remove( shape );
    }

    @Override
    public void requestLayout( final WiresContainer container ) {
        final WiresContainer root = findRoot( container );
        final List<WiresContainer> children = walker.getChildren( root );
        for ( WiresContainer child : children ) {
            child.getLayoutHandler().layout( child );
        }
    }

    private WiresContainer findRoot( final WiresContainer parent ) {
        WiresContainer _parent = parent;
        while ( _parent.getParent() != null ) {
            _parent = _parent.getParent();
        }
        return _parent;
    }

    protected abstract void resizeContainer( final WiresContainer container );

    protected BoundingBox getParentContentBoundingBox( final WiresContainer parent ) {
        if ( parent.getChildShapes().isEmpty() ) {
            return new BoundingBox( 0,
                                    0,
                                    0,
                                    0 );
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for ( WiresShape ws : parent.getChildShapes() ) {
            final MultiPath wsp = ws.getPath();
            final BoundingBox wspbb = wsp.getBoundingBox();
            minX = Math.min( minX, ws.getX() + wspbb.getX() );
            maxX = Math.max( maxX, ws.getX() + wspbb.getX() + wspbb.getWidth() );
            minY = Math.min( minY, ws.getY() + wspbb.getY() );
            maxY = Math.max( maxY, ws.getY() + wspbb.getY() + wspbb.getHeight() );
        }

        final BoundingBox bb = new BoundingBox( 0,
                                                0,
                                                maxX - minX,
                                                maxY - minY );
        return bb;
    }

    static class ReverseBreadthFirstTreeWalker {

        List<WiresContainer> getChildren( final WiresContainer root ) {
            final Queue<WiresContainer> q = new LinkedList<>();
            final Stack<WiresContainer> s = new Stack<>();
            q.add( root );

            while ( !q.isEmpty() ) {
                final WiresContainer container = q.remove();
                for ( WiresShape child : container.getChildShapes() ) {
                    q.add( child );
                }
                s.add( container );
            }

            final List<WiresContainer> ws = new ArrayList<>();
            while ( !s.isEmpty() ) {
                ws.add( s.pop() );
            }
            return ws;
        }

    }

}
