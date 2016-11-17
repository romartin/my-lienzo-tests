package org.roger600.lienzo.client.casemodeller.factory;

import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresDockingAndContainmentControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresControlFactoryImpl;

public class CaseModellerControlFactoryImpl extends WiresControlFactoryImpl {

    @Override
    public WiresDockingAndContainmentControl newDockingAndContainmentControl( final WiresShape shape,
                                                                              final WiresManager wiresManager ) {
        return new CaseModellerDockingAndContainmentControlImpl( shape,
                                                                 wiresManager );
    }

}
