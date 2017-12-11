package org.roger600.lienzo.client.casemodeller.control;

import com.ait.lienzo.client.core.shape.wires.PickerPart;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresCompositeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectionControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresConnectorControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresControlFactory;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeHighlight;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresControlFactoryImpl;

public class CaseModellerControlFactory implements WiresControlFactory {

    private final WiresControlFactoryImpl delegate;

    public CaseModellerControlFactory() {
        this.delegate = new WiresControlFactoryImpl();
    }

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
    public WiresShapeHighlight<PickerPart.ShapePart> newShapeHighlight(WiresManager wiresManager) {
        return delegate.newShapeHighlight(wiresManager);
    }

    @Override
    public WiresConnectorControl newConnectorControl(WiresConnector connector,
                                                     WiresManager wiresManager) {
        return delegate.newConnectorControl(connector,
                                            wiresManager);
    }

    @Override
    public WiresConnectionControl newConnectionControl(WiresConnector connector,
                                                       boolean headNotTail,
                                                       WiresManager wiresManager) {
        return delegate.newConnectionControl(connector,
                                             headNotTail,
                                             wiresManager);
    }
}
