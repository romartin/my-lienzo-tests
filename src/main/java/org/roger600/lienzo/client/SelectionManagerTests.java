package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.*;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import java.util.Set;

public class SelectionManagerTests extends FlowPanel implements MyLienzoTest, HasMediators, HasButtons
{
    private WiresManager wires_manager;

    private       boolean isDockingAccept       = true;

    private       boolean isContainmentAccept   = true;

    private       boolean isLocationAccept      = true;

    private       int     containmentAllowCount = 0;

    private final Timer   timer                 = new Timer()
    {
        @Override
        public void run()
        {
            GWT.log("Running timer!");
            wires_manager.resetContext();
        }
    };

    public void test(Layer layer)
    {
        wires_manager = WiresManager.get(layer);

        //wires_manager.setContainmentAcceptor( IContainmentAcceptor.ALL );
        wires_manager.setContainmentAcceptor(new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(WiresContainer parent,
                                              WiresShape[] children)
            {
                logOperation("CONTAINMENT ALLOW [" + containmentAllowCount++ + "] ",
                             parent, children, isContainmentAccept);
                if (parent instanceof WiresLayer)
                {
                    GWT.log("PARENT IS LAYER");
                }
                return isContainmentAccept;
            }

            @Override
            public boolean acceptContainment(WiresContainer parent,
                                             WiresShape[] children)
            {
                logOperation("CONTAINMENT ACCEPT", parent, children, isContainmentAccept);
                containmentAllowCount = 0;
                return isContainmentAccept;
            }
        });

        wires_manager.setDockingAcceptor(new IDockingAcceptor()
        {
            @Override
            public boolean dockingAllowed(WiresContainer parent,
                                          WiresShape child)
            {
                logOperation("DOCKING ALLOW", parent, new WiresShape[]{child}, isDockingAccept);
                return isDockingAccept;
            }

            @Override
            public boolean acceptDocking(WiresContainer parent,
                                         WiresShape child)
            {
                logOperation("DOCKING ACCEPT", parent, new WiresShape[]{child}, isDockingAccept);
                return isDockingAccept;
            }

            @Override
            public int getHotspotSize()
            {
                return 10;
            }
        });

        // wires_manager.setLocationAcceptor(ILocationAcceptor.ALL);
        wires_manager.setLocationAcceptor(new ILocationAcceptor()
        {
            @Override
            public boolean allow(WiresContainer[] shapes,
                                 Point2D[] locations)
            {
                return true;
            }

            @Override
            public boolean accept(WiresContainer[] shapes,
                                  Point2D[] locations)
            {
                final boolean accepts = isLocationAccept;
                log("ACCEPT LOCATION = " + accepts);
                int i = 0;
                for (WiresContainer shape : shapes)
                {
                    if (accepts)
                    {
                        log("ACCEPT LOCATION [" + locations[i].getX() + ", " + locations[i].getY() + "]");
                    }
                    else
                    {
                        log("DO NOT ACCEPT LOCATION [" + locations[i].getX() + ", " + locations[i].getY() + "]"
                            + " LOCATION = [" + shape.getX() + ", " + shape.getY() + "]");
                    }
                    i++;
                }
                return accepts;
            }
        });

        wires_manager.setConnectionAcceptor(IConnectionAcceptor.ALL);

        wires_manager.enableSelectionManager();

        wires_manager.getSelectionManager().setSelectionListener(new DelegateSelectionListener()
        {
            @Override
            protected void select(WiresShape shape)
            {
                log("SELECTED [" + shape.getGroup().getUserData().toString() + "]");
            }

            @Override
            protected void select(WiresConnector connector)
            {
                log("SELECTED CONNECTOR");
            }

            @Override
            protected void unselect(WiresShape shape)
            {
                log("UN-SELECTED [" + shape.getGroup().getUserData().toString() + "]");
            }

            @Override
            protected void unselect(WiresConnector connector)
            {
                log("UN-SELECTED CONNECTOR");
            }
        });

        final double startX = 100;
        final double startY = 100;
        final double w      = 100;
        final double h      = 100;

        MultiPath  redPath  = new MultiPath().rect(0, 0, w, h).setFillColor("#FF0000");
        WiresShape redShape = new WiresShape(redPath);
        wires_manager.register(redShape);
        redShape.setLocation(new Point2D(startX, startY));
        redShape.setDraggable(true).getContainer().setUserData("red");

        WiresShape greenShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#00CC00"));
        wires_manager.register(greenShape);
        greenShape.setLocation(new Point2D(startX + 200, startY));
        greenShape.setDraggable(true).getContainer().setUserData("green");

        WiresShape blueShape = new WiresShape(new MultiPath().rect(0, 0, w, h).setFillColor("#0000FF"));
        wires_manager.register(blueShape);
        blueShape.setLocation(new Point2D(startX + 400, startY));
        blueShape.setDraggable(true).getContainer().setUserData("blue");

        WiresShape parentShape = new WiresShape(new MultiPath().rect(0, 0, 300, 300)
                                                               .setFillColor("#FFFFFF")
                                                               .setStrokeColor("#000000"));
        wires_manager.register(parentShape);
        parentShape.setLocation(new Point2D(startX + 200, startY + 200));
        parentShape.setDraggable(true).getContainer().setUserData("parent");

        wires_manager.getMagnetManager().createMagnets(redShape);
        wires_manager.getMagnetManager().createMagnets(greenShape);
        wires_manager.getMagnetManager().createMagnets(blueShape);
        wires_manager.getMagnetManager().createMagnets(parentShape);

        TestsUtils.addResizeHandlers(redShape);
        TestsUtils.addResizeHandlers(greenShape);
        TestsUtils.addResizeHandlers(blueShape);
        TestsUtils.addResizeHandlers(parentShape);

        TestsUtils.connect(redShape.getMagnets(),
                           3,
                           greenShape.getMagnets(),
                           7,
                           wires_manager);
    }

    @Override
    public void setButtonsPanel(Panel panel)
    {
        final Button button1 = new Button("Log selected shapes locations");
        button1.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                logSelectedShapeLocations();
            }
        });
        panel.add(button1);

        final Button acceptContainmentButton = new Button("Containment OK");
        acceptContainmentButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isContainmentAccept = true;
            }
        });
        panel.add(acceptContainmentButton);

        final Button denyContainmentButton = new Button("Containment KO");
        denyContainmentButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isContainmentAccept = false;
            }
        });
        panel.add(denyContainmentButton);

        final Button acceptDockingButton = new Button("Docking OK");
        acceptDockingButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isDockingAccept = true;
            }
        });
        panel.add(acceptDockingButton);

        final Button denyDockingButton = new Button("Docking KO");
        denyDockingButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isDockingAccept = false;
            }
        });
        panel.add(denyDockingButton);

        final Button acceptLocationButton = new Button("Location OK");
        acceptLocationButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isLocationAccept = true;
            }
        });
        panel.add(acceptLocationButton);

        final Button denyLocationButton = new Button("Location KO");
        denyLocationButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                isLocationAccept = false;
            }
        });
        panel.add(denyLocationButton);

        final Button timerButton = new Button("Schedule timer");
        timerButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                timer.schedule(2000);
            }
        });
        panel.add(timerButton);
    }

    private void logSelectedShapeLocations()
    {
        Set<WiresShape> shapes = wires_manager.getSelectionManager().getSelectedItems().getShapes();
        int             i      = 0;
        if (null != shapes)
        {
            for (WiresShape shape : shapes)
            {
                Object  userData = shape.getGroup().getUserData();
                String  id       = null != userData ? userData.toString() : "shape[" + i + "]";
                Point2D location = shape.getGroup().getLocation();
                log("LOCATION - [" + id + "] [" + location + "]");
                i++;
            }
        }
    }

    private static abstract class DelegateSelectionListener implements SelectionListener
    {
        protected abstract void select(WiresShape shape);

        protected abstract void select(WiresConnector connector);

        protected abstract void unselect(WiresShape shape);

        protected abstract void unselect(WiresConnector connector);

        @Override
        public void onChanged(SelectionManager.SelectedItems selectedItems)
        {

            SelectionManager.ChangedItems changed = selectedItems.getChanged();

            // log(changed);

            /*for (WiresShape shape : changed.getRemovedShapes())
            {
                unselect(shape);
            }

            for (WiresConnector connector : changed.getRemovedConnectors())
            {
                unselect(connector);
            }
            if (!selectedItems.isSelectionGroup() && selectedItems.size() == 1)
            {
                // it's one or the other, so attempt both, it'll short circuit if the first selects.
                if (selectedItems.getShapes().size() == 1)
                {
                    for (WiresShape shape : selectedItems.getShapes())
                    {
                        select(shape);
                        break;
                    }
                }
                else
                {
                    for (WiresConnector connector : selectedItems.getConnectors())
                    {
                        select(connector);
                        break;
                    }
                }
            }
            else if (selectedItems.isSelectionGroup())
            {
                // we don't which have selectors shown, if any. Just iterate and unselect all
                // null check will do nothing, if it's already unselected.
                for (WiresShape shape : selectedItems.getShapes())
                {
                    unselect(shape);
                }


                for (WiresConnector connector : selectedItems.getConnectors())
                {
                    unselect(connector);
                }
            }*/
        }

        private void logChangedItems(SelectionManager.ChangedItems changed)
        {
            log("************************ LOGGING CHANGED ITEMS ************************");
            for (WiresShape shape : changed.getRemovedShapes())
            {
                log("REMOVED SHAPE [" + shape.getGroup().getUserData().toString() + "]");
            }

            for (WiresConnector connector : changed.getRemovedConnectors())
            {
                log("REMOVED CONNECTOR");
            }
            for (WiresShape shape : changed.getAddedShapes())
            {
                log("ADDED SHAPE [" + shape.getGroup().getUserData().toString() + "]");
            }

            for (WiresConnector connector : changed.getAddedConnectors())
            {
                log("ADDED CONNECTOR");
            }
            log("************************************************************************");
        }
    }

    private static void logOperation(String text,
                                     WiresContainer parent,
                                     WiresShape[] children,
                                     boolean result)
    {
        final String p = parent instanceof WiresShape ?
                         parent.getGroup().getUserData().toString() :
                         "layer";
        StringBuilder c = new StringBuilder();
        for (WiresShape child : children)
        {
            c.append(child.getGroup().getUserData().toString()).append(", ");
        }
        log(text + " [parent=" + p + ", children=" + c.toString() + "] == " + Boolean.toString(result).toUpperCase());
    }

    private static void log(final String message)
    {
        GWT.log(message);
    }
}