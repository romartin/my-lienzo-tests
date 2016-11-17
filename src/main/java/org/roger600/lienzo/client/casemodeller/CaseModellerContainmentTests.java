package org.roger600.lienzo.client.casemodeller;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.IDockingAcceptor;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.HasButtons;
import org.roger600.lienzo.client.MyLienzoTest;
import org.roger600.lienzo.client.TestsUtils;
import org.roger600.lienzo.client.casemodeller.factory.CaseModellerControlFactoryImpl;

public class CaseModellerContainmentTests extends FlowPanel implements MyLienzoTest,
                                                                       HasButtons {

    private Layer layer;
    private WiresManager wires_manager;

    public void test( Layer layer ) {
        this.layer = layer;
        this.wires_manager = WiresManager.get( layer );
        this.wires_manager.setWiresControlFactory( new CaseModellerControlFactoryImpl() );
        this.wires_manager.setContainmentAcceptor( new CaseModellerDelegatingContainmentAcceptor() );
        this.wires_manager.setDockingAcceptor( IDockingAcceptor.NONE );

        final CaseModellerShape caseModeller = new CaseModellerShape();
        caseModeller.setDraggable( true ).setX( 100.0 ).setY( 100.0 );
        TestsUtils.addResizeHandlers( caseModeller );

        this.wires_manager.register( caseModeller );
    }

    @Override
    public void setButtonsPanel( Panel panel ) {
        final Button addStage = new Button( "Add stage" );
        addStage.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                final CaseModellerStageShape shape = makeStageShape();
                wires_manager.register( shape, false );
                layer.batch();
            }
        } );

        final Button addActivity = new Button( "Add activity" );
        addActivity.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent clickEvent ) {
                final CaseModellerActivityShape shape = makeActivityShape();
                wires_manager.register( shape, false );
                layer.batch();
            }
        } );

        panel.add( addStage );
        panel.add( addActivity );
    }

    private CaseModellerStageShape makeStageShape() {
        final CaseModellerStageShape shape = new CaseModellerStageShape();
        shape.setX( 10 ).setY( 10 );
        shape.setDraggable( true );
        shape.getGroup().add( makeLabel( shape ) );
        return shape;
    }

    private CaseModellerActivityShape makeActivityShape() {
        final CaseModellerActivityShape shape = new CaseModellerActivityShape();
        shape.setX( 10 ).setY( 10 );
        shape.setDraggable( true );
        shape.getGroup().add( makeLabel( shape ) );
        return shape;
    }

    private Text makeLabel( final WiresShape shape ) {
        final Text label = new Text( shape.toString() );
        label.setFontSize( 8.0 );
        label.setY( 10.0 );
        return label;
    }

}
