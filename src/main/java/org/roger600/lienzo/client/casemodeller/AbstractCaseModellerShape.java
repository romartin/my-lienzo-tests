package org.roger600.lienzo.client.casemodeller;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.WiresShape;

public abstract class AbstractCaseModellerShape extends WiresShape {

    private static int UUID = 0;

    private int uuid = UUID++;

    private double currentWidth;
    private double currentHeight;
    private final double minWidth;
    private final double minHeight;

    protected final MultiPath decoration = new MultiPath();

    public AbstractCaseModellerShape( final double minWidth,
                                      final double minHeight ) {
        super( new MultiPath() );
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.currentWidth = minWidth;
        this.currentHeight = minHeight;
        this.getGroup().add( decoration );

        setPath( minWidth, minHeight );
        updateDecoration();
    }

    @Override
    public String toString() {
        return "ID: " + uuid;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public double getWidth() {
        return currentWidth;
    }

    public void setWidth( final double width ) {
        this.currentWidth = width;
        setPath( width, this.currentHeight );
        updateDecoration();
    }

    public double getMinHeight() {
        return minHeight;
    }

    public double getHeight() {
        return currentHeight;
    }

    public void setHeight( final double height ) {
        this.currentHeight = height;
        setPath( this.currentWidth, height );
        updateDecoration();
    }

    public void logicallyReplace( final WiresShape original,
                                  final WiresShape replacement ) {
        if ( original == null ) {
            return;
        }
        if ( replacement == null ) {
            return;
        }
        if ( replacement.getParent() == this ) {
            return;
        }

        getChildShapes().set( getIndex( original ), replacement );
        getContainer().getChildNodes().set( getNodeIndex( original.getGroup() ), replacement.getGroup() );

        original.setParent( null );
        replacement.setParent( this );

        if ( original.getMagnets() != null ) {
            original.getMagnets().shapeMoved();
        }

        if ( replacement.getMagnets() != null ) {
            replacement.getMagnets().shapeMoved();
        }

        getLayoutHandler().requestLayout( this );
    }

    public void addShape( final WiresShape shape,
                          final int targetIndex ) {
        if ( shape == null ) {
            return;
        }
        if ( targetIndex < 0 || targetIndex > getChildShapes().size() ) {
            return;
        }
        final List<WiresShape> existingParentShapes = new ArrayList<>();
        existingParentShapes.addAll( getChildShapes().toList() );
        for ( WiresShape ws : existingParentShapes ) {
            ws.removeFromParent();
        }

        existingParentShapes.remove( shape );
        existingParentShapes.add( targetIndex,
                                  shape );

        for ( WiresShape ews : existingParentShapes ) {
            add( ews );
        }

        if ( shape.getMagnets() != null ) {
            shape.getMagnets().shapeMoved();
        }

        getLayoutHandler().requestLayout( this );
    }

    private int getIndex( final WiresShape shape ) {
        return getChildShapes().toList().indexOf( shape );
    }

    private int getNodeIndex( final Group group ) {
        return getContainer().getChildNodes().toList().indexOf( group );
    }

    public void updateDecoration() {
        //No decoration by default
    }

    public abstract void setPath( final double width,
                                  final double height );

    public abstract AbstractCaseModellerShape getGhost();

}
