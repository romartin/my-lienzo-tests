package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.Map;

public class WiresTests extends FlowPanel {
    
    private Layer layer;
    private IControlHandleList m_ctrls;

    public WiresTests(Layer layer) {
        this.layer = layer;
    }

    public void testWires() {
        WiresManager wires_manager = WiresManager.get(layer);

        wires_manager.setConnectionAcceptor(new IConnectionAcceptor() {
            @Override
            public boolean acceptHead(WiresConnection head, WiresMagnet magnet) {
                WiresConnection tail = head.getConnector().getTailConnection();

                WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }
                return accept(magnet.getMagnets().getGroup(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean acceptTail(WiresConnection tail, WiresMagnet magnet) {
                WiresConnection head = tail.getConnector().getHeadConnection();

                WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), magnet.getMagnets().getGroup());
            }

            @Override
            public boolean headConnectionAllowed(WiresConnection head, WiresShape shape) {
                WiresConnection tail = head.getConnector().getTailConnection();
                WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }

                return accept(shape.getGroup(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean tailConnectionAllowed(WiresConnection tail, WiresShape shape) {
                WiresConnection head = tail.getConnector().getHeadConnection();

                WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), shape.getGroup());
            }

            private boolean accept(final Group head, final Group tail)
            {
                GWT.log("Accept [head=" + head.getUserData() + "] [tail=" + tail.getUserData() + "]");
                final String headData = (String) head.getUserData();
                final String tailData = (String) tail.getUserData();
                if ( "event".equals(headData) && "event".equals(tailData) )
                {
                    return false;
                }
                return true;
            }
        });

        wires_manager.setContainmentAcceptor(new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(WiresContainer parent, WiresShape child)
            {
                return acceptContainment(parent, child);
            }

            @Override
            public boolean acceptContainment(WiresContainer parent, WiresShape child)
            {
                if (parent.getParent() == null)
                {
                    return true;
                }
                return !parent.getContainer().getUserData().equals(child.getGroup().getUserData());
            }
        });

        final double startX = 300;
        final double startY = 300;
        final double radius = 50;
        final double w = 100;
        final double h = 100;

        // Blue start event.
        MultiPath startEventMultiPath = new MultiPath().rect(0, 0, w, h).setStrokeColor("#000000");
        WiresShape startEventShape = wires_manager.createShape(startEventMultiPath);
        startEventShape.getGroup().setX(startX).setY(startY).add(new Circle(radius).setX(50).setY(50).setFillColor("#0000CC").setDraggable(true));
        startEventShape.getGroup().setUserData("event");
        addResizeControls(layer, startEventMultiPath);

        // Green task node.
        WiresShape taskNodeShape = wires_manager.createShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        taskNodeShape.getGroup().setX(startX + 200).setY(startY).setUserData("task");

        // Yellow task node.
        WiresShape task2NodeShape = wires_manager.createShape(new MultiPath().rect(0, 0, w, h).setFillColor("#FFEB52"));
        task2NodeShape.getGroup().setX(startX + 200).setY(startY + 300).setUserData("task");

        // Red end event.
        WiresShape endEventShape = wires_manager.createShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#FFFFFF"));
        endEventShape.getGroup().setX(startX + 400).setY(startY).add(new Circle(radius).setX(50).setY(50).setFillColor("#CC0000").setDraggable(true));
        endEventShape.getGroup().setUserData("event");

        // Create shapes' magnets.
        wires_manager.createMagnets(startEventShape);
        wires_manager.createMagnets(taskNodeShape);
        wires_manager.createMagnets(task2NodeShape);
        wires_manager.createMagnets(endEventShape);

        // Connector from blue start event to green task node.
        connect(layer, startEventShape.getMagnets(), 3, taskNodeShape.getMagnets(), 7, wires_manager, true, false);
        // Connector from green task node to red end event 
        connect(layer, taskNodeShape.getMagnets(), 3, endEventShape.getMagnets(), 7, wires_manager, true, false);
        // Connector from blue start event to yellow task node.
        connect(layer, startEventShape.getMagnets(), 3, task2NodeShape.getMagnets(), 7, wires_manager, true, false);


        startEventShape.addWiresHandler(AbstractWiresEvent.DRAG_START, new DragStartHandler() {
            @Override
            public void onDragStart(final DragStartEvent dragEvent) {
                final WiresShape shape = dragEvent.getShape();
                final double dx = dragEvent.getX();
                final double dy = dragEvent.getY();
                GWT.log("DragStartHandler#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy+ "]");
            }
        });

        startEventShape.addWiresHandler(AbstractWiresEvent.DRAG_MOVE, new DragMoveHandler() {
            @Override
            public void onDragMove(final DragMoveEvent moveEvent) {
                final WiresShape shape = moveEvent.getShape();
                final double dx = moveEvent.getX();
                final double dy = moveEvent.getY();
                GWT.log("DragMoveHandler#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy+ "]");
            }
        });
        
        startEventShape.addWiresHandler(AbstractWiresEvent.DRAG_END, new DragEndHandler() {
            @Override
            public void onDragEnd(DragEndEvent endEvent) {
                final WiresShape shape = endEvent.getShape();
                final double dx = endEvent.getX();
                final double dy = endEvent.getY();
                GWT.log("DragEndEvent#DRAG - [shape=" + shape + ", x=" + dx + ", y=" + dy+ "]");
            }
        });
        
    }

    private void connect(Layer layer, MagnetManager.Magnets headMagnets, int headMagnetsIndex, MagnetManager.Magnets tailMagnets, int tailMagnetsIndex, WiresManager wires_manager,
                         final boolean tailArrow, final boolean headArrow)
    {
        WiresMagnet m0_1 = headMagnets.getMagnet(headMagnetsIndex);

        WiresMagnet m1_1 = tailMagnets.getMagnet(tailMagnetsIndex);

        double x0 = m0_1.getControl().getX();

        double y0 = m0_1.getControl().getY();

        double x1 = m1_1.getControl().getX();

        double y1 = m1_1.getControl().getY();

        OrthogonalPolyLine line = createLine(x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);

        WiresConnector connector = wires_manager.createConnector(m0_1, m1_1, line,
                headArrow ? new SimpleArrow(20, 0.75) : null,
                tailArrow ? new SimpleArrow(20, 0.75) : null);

        connector.getDecoratableLine().setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private void addResizeControls(final Layer layer, final MultiPath m_multi)
    {
        m_multi.addNodeMouseClickHandler(new NodeMouseClickHandler()
        {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event)
            {
                if (event.isShiftKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles(IControlHandle.ControlHandleStandardType.RESIZE);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get(IControlHandle.ControlHandleStandardType.RESIZE);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show(layer);
                        }
                    }
                }
                else if (event.isAltKeyDown())
                {
                    if (null != m_ctrls)
                    {
                        m_ctrls.destroy();

                        m_ctrls = null;
                    }
                    Map<IControlHandle.ControlHandleType, IControlHandleList> hmap = m_multi.getControlHandles(IControlHandle.ControlHandleStandardType.POINT);

                    if (null != hmap)
                    {
                        m_ctrls = hmap.get(IControlHandle.ControlHandleStandardType.POINT);

                        if ((null != m_ctrls) && (m_ctrls.isActive()))
                        {
                            m_ctrls.show(layer);
                        }
                    }
                }
            }
        });
    }

    private final OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }

}
