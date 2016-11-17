package org.roger600.lienzo.client.casemodeller;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;

public class CaseModellerDelegatingContainmentAcceptor implements IContainmentAcceptor {

    private static final List<IContainmentAcceptor> delegates = new ArrayList<IContainmentAcceptor>() {{
        add( CaseModellerShape.CASE_MODEL );
        add( CaseModellerStageShape.CASE_STAGE );
        add( CaseModellerActivityShape.CASE_ACTIVITY );
    }};

    @Override
    public boolean containmentAllowed( final WiresContainer parent,
                                       final WiresShape child ) {
        for ( IContainmentAcceptor delegate : delegates ) {
            if ( delegate.containmentAllowed( parent,
                                              child ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean acceptContainment( final WiresContainer parent,
                                      final WiresShape child ) {
        for ( IContainmentAcceptor delegate : delegates ) {
            if ( delegate.acceptContainment( parent,
                                             child ) ) {
                return true;
            }
        }
        return false;
    }

}
