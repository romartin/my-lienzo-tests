/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.roger600.lienzo.client.widget;

import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import org.roger600.lienzo.client.widget.scrollbars.LienzoScrollHandler;
import org.roger600.lienzo.client.widget.scrollbars.LienzoScrollable;

/**
 * Specialised LienzoPanel that is overlaid with an AbsolutePanel to support overlaying DOM elements on top of the
 * Canvas element.
 */
public class InfiniteLienzoPanel extends FocusPanel implements RequiresResize,
                                                               ProvidesResize,
                                                               LienzoScrollable
{
    protected final LienzoPanel lienzoPanel;

    protected final AbsolutePanel domElementContainer = new AbsolutePanel();

    private final   AbsolutePanel internalScrollPanel = new AbsolutePanel();

    private final   AbsolutePanel scrollPanel         = new AbsolutePanel();

    private final   AbsolutePanel rootPanel           = new AbsolutePanel();

    private final LienzoScrollHandler scrollHandler;

    private       InfiniteLayer       layer;

    public InfiniteLienzoPanel()
    {
        this(new LienzoPanel()
        {
            @Override
            public void onResize()
            {
                // Do nothing. Resize is handled by AttachHandler. LienzoPanel calls onResize() in
                // it's onAttach() method which causes the Canvas to be redrawn. However when LienzoPanel
                // is adopted by another Widget LienzoPanel's onAttach() is called before its children
                // have been attached. Should redraw require children to be attached errors arise.
            }
        });
    }

    public InfiniteLienzoPanel(final int width,
                               final int height)
    {
        this(new LienzoPanel(width,
                             height)
        {
            @Override
            public void onResize()
            {
                // Do nothing. Resize is handled by AttachHandler. LienzoPanel calls onResize() in
                // it's onAttach() method which causes the Canvas to be redrawn. However when LienzoPanel
                // is adopted by another Widget LienzoPanel's onAttach() is called before its children
                // have been attached. Should redraw require children to be attached errors arise.
            }
        });

        updatePanelSize(width,
                        height);
    }

    private InfiniteLienzoPanel(final LienzoPanel lienzoPanel)
    {
        this.lienzoPanel = lienzoPanel;
        this.scrollHandler = new LienzoScrollHandler(this);

        setupPanels();
        setupScrollHandlers();
        setupDefaultHandlers();
    }

    void setupPanels()
    {
        setupScrollPanel();
        setupDomElementContainer();
        setupRootPanel();

        add(getRootPanel());
        getElement().getStyle().setOutlineStyle(Style.OutlineStyle.NONE);
    }

    void setupScrollPanel()
    {
        getScrollPanel().add(getInternalScrollPanel());
    }

    void setupDomElementContainer()
    {
        getDomElementContainer().add(getLienzoPanel());
    }

    void setupRootPanel()
    {
        getRootPanel().add(getDomElementContainer());
        getRootPanel().add(getScrollPanel());
    }

    void setupScrollHandlers()
    {
        getScrollHandler().init();
        addMouseUpHandler();
    }

    void addMouseUpHandler()
    {
        addMouseUpHandler(new MouseUpHandler()
        {
            @Override public void onMouseUp(MouseUpEvent e)
            {
                InfiniteLienzoPanel.this.refreshScrollPosition();
            }
        });
    }

    private void setupDefaultHandlers()
    {
        //Prevent DOMElements scrolling into view when they receive the focus
        domElementContainer.addDomHandler(new ScrollHandler()
                                          {
                                              @Override
                                              public void onScroll(final ScrollEvent scrollEvent)
                                              {
                                                  domElementContainer.getElement().setScrollTop(0);
                                                  domElementContainer.getElement().setScrollLeft(0);
                                              }
                                          },
                                          ScrollEvent.getType());
        addAttachHandler(new AttachEvent.Handler()
        {
            @Override
            public void onAttachOrDetach(final AttachEvent event)
            {
                if (event.isAttached())
                {
                    onResize();
                }
            }
        });
        addMouseDownHandler(new MouseDownHandler()
        {
            @Override public void onMouseDown(MouseDownEvent e)
            {
                InfiniteLienzoPanel.this.setFocus(true);
            }
        });
    }

    @Override
    public void onResize()
    {
        scheduleDeferred(new Scheduler.ScheduledCommand()
        {
            @Override public void execute()
            {
                InfiniteLienzoPanel.this.updatePanelSize();
                InfiniteLienzoPanel.this.refreshScrollPosition();
            }
        });
    }

    void scheduleDeferred(final Scheduler.ScheduledCommand scheduledCommand)
    {
        Scheduler.get().scheduleDeferred(scheduledCommand);
    }

    @Override
    public void updatePanelSize()
    {
        final Element parentElement = getElement().getParentElement();
        final Integer width         = parentElement.getOffsetWidth();
        final Integer height        = parentElement.getOffsetHeight();

        if (width > 0 && height > 0)
        {
            updatePanelSize(width,
                            height);
        }
    }

    @Override
    public void updatePanelSize(final Integer width,
                                final Integer height)
    {
        updateScrollPanelSize(width,
                              height);
        updateInternalPanelsSizes(width,
                                  height);
    }

    private void updateInternalPanelsSizes(final int width,
                                           final int height)
    {
        final Integer scrollbarWidth  = getScrollHandler().scrollbarWidth();
        final Integer scrollbarHeight = getScrollHandler().scrollbarHeight();

        getDomElementContainer().setPixelSize(width - scrollbarWidth,
                                              height - scrollbarHeight);
        getLienzoPanel().setPixelSize(width - scrollbarWidth,
                                      height - scrollbarHeight);
    }

    private void updateScrollPanelSize(final int width,
                                       final int height)
    {
        getScrollPanel().setPixelSize(width,
                                      height);
    }

    @Override
    public void refreshScrollPosition()
    {
        getScrollHandler().refreshScrollPosition();
    }

    @Override
    public void setBounds(final Bounds bounds)
    {
        getScrollHandler().setBounds(bounds);
    }

    public LienzoPanel add(final InfiniteLayer layer)
    {
        this.layer = setupDefaultGridLayer(layer);

        // this.layer.setDomElementContainer(domElementContainer);

        lienzoPanel.add(this.layer);

        return lienzoPanel;
    }

    private InfiniteLayer setupDefaultGridLayer(final InfiniteLayer layer)
    {
        // TODO
        // layer.addOnEnterPinnedModeCommand(this::refreshScrollPosition);
        // layer.addOnExitPinnedModeCommand(this::refreshScrollPosition);

        return layer;
    }

    public final Viewport getViewport()
    {
        return lienzoPanel.getViewport();
    }

    public LienzoPanel getLienzoPanel()
    {
        return lienzoPanel;
    }

    public AbsolutePanel getScrollPanel()
    {
        return scrollPanel;
    }

    public AbsolutePanel getDomElementContainer()
    {
        return domElementContainer;
    }

    public AbsolutePanel getInternalScrollPanel()
    {
        return internalScrollPanel;
    }

    public InfiniteLayer getLayer()
    {
        return layer;
    }

    AbsolutePanel getRootPanel()
    {
        return rootPanel;
    }

    LienzoScrollHandler getScrollHandler()
    {
        return scrollHandler;
    }
}
