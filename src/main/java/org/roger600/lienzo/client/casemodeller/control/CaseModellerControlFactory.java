package org.roger600.lienzo.client.casemodeller.control;

import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresCompositeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectionControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresControlFactory;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresConnectionControlImpl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresConnectorControlImpl;

public class CaseModellerControlFactory implements WiresControlFactory {

    @Override
    public WiresShapeControl newShapeControl(WiresShape shape,
                                             WiresManager wiresManager) {
        return new CaseModellerShapeControl(shape);
    }

    @Override
    public WiresCompositeControl newCompositeControl(WiresCompositeControl.Context provider,
                                                     WiresManager wiresManager) {
        throw new UnsupportedOperationException("Case Modeller does not yet support selection multiple.");
    }

    @Override
    public WiresConnectorControl newConnectorControl(WiresConnector connector,
                                                     WiresManager wiresManager) {
        return new WiresConnectorControlImpl(connector,
                                             wiresManager);
    }

    @Override
    public WiresConnectionControl newConnectionControl(WiresConnector connector,
                                                       boolean headNotTail,
                                                       WiresManager wiresManager) {
        return new WiresConnectionControlImpl(connector,
                                              headNotTail,
                                              wiresManager);
    }
}
