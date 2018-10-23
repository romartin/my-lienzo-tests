/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
package org.roger600.lienzo.client.widget.scrollbars;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.roger600.lienzo.client.widget.Bounds;
import org.roger600.lienzo.client.widget.InfiniteLayer;
import org.roger600.lienzo.client.widget.InfiniteLienzoLayer;
import org.roger600.lienzo.client.widget.InfiniteLienzoPanel;
import org.roger600.lienzo.client.widget.mediators.RestrictedMousePanMediator;

/*
 * Responsible for the setup and control of every scroll related event.
 */

public class LienzoScrollHandler
{
    private final InfiniteLienzoPanel panel;

    private final LienzoScrollBounds  scrollBounds;

    static final int DEFAULT_INTERNAL_SCROLL_HEIGHT = 1;

    static final int DEFAULT_INTERNAL_SCROLL_WIDTH  = 1;

    private RestrictedMousePanMediator mousePanMediator;

    public LienzoScrollHandler(final InfiniteLienzoPanel panel)
    {
        this.panel = panel;
        this.scrollBounds = new LienzoScrollBounds(this);
    }

    public void init()
    {
        setupGridLienzoScrollStyle();
        setupScrollBarSynchronization();
        setupMouseDragSynchronization();
        setupContextSwitcher();
    }

    void setupContextSwitcher()
    {
        getDomElementContainer().addDomHandler(disablePointerEvents(), MouseWheelEvent.getType());
        getPanel().addMouseMoveHandler(enablePointerEvents());
    }

    MouseWheelHandler disablePointerEvents()
    {
        return new MouseWheelHandler()
        {
            @Override
            public void onMouseWheel(MouseWheelEvent event)
            {
                LienzoScrollHandler.this.gridLienzoScrollUI().disablePointerEvents(LienzoScrollHandler.this.getDomElementContainer());
            }
        };
    }

    MouseMoveHandler enablePointerEvents()
    {
        return new MouseMoveHandler()
        {
            @Override
            public void onMouseMove(MouseMoveEvent event)
            {
                LienzoScrollHandler.this.gridLienzoScrollUI().enablePointerEvents(LienzoScrollHandler.this.getDomElementContainer());
            }
        };
    }

    public Integer scrollbarWidth()
    {
        return getScrollPanel().getElement().getOffsetWidth() - getScrollPanel().getElement().getClientWidth();
    }

    public Integer scrollbarHeight()
    {
        return getScrollPanel().getElement().getOffsetHeight() - getScrollPanel().getElement().getClientHeight();
    }

    void setupGridLienzoScrollStyle()
    {
        gridLienzoScrollUI().setup();
    }

    LienzoScrollUI gridLienzoScrollUI()
    {
        return new LienzoScrollUI(this);
    }

    void setupScrollBarSynchronization()
    {
        getScrollPanel().addDomHandler(onScroll(),
                                       ScrollEvent.getType());
        synchronizeScrollSize();
    }

    void setupMouseDragSynchronization()
    {

        mousePanMediator = makeRestrictedMousePanMediator();

        getLienzoPanel().getViewport().getMediators().push(mousePanMediator);
    }

    RestrictedMousePanMediator makeRestrictedMousePanMediator()
    {
        return new RestrictedMousePanMediator()
        {
            @Override
            protected void onMouseMove(final NodeMouseMoveEvent event)
            {
                refreshScrollPosition();
            }

            @Override
            protected Viewport getLayerViewport()
            {
                return getViewport();
            }
        };
    }

    ScrollHandler onScroll()
    {
        return new ScrollHandler()
        {
            @Override
            public void onScroll(ScrollEvent event)
            {
                final Boolean mouseIsNotDragging = !LienzoScrollHandler.this.getMousePanMediator().isDragging();

                if (mouseIsNotDragging)
                {
                    LienzoScrollHandler.this.updateGridLienzoPosition();
                }
            }
        };
    }

    public void refreshScrollPosition()
    {

        synchronizeScrollSize();

        setScrollBarsPosition(scrollPosition().currentRelativeX(),
                              scrollPosition().currentRelativeY());
    }

    void updateGridLienzoPosition()
    {

        final Double percentageX = scrollBars().getHorizontalScrollPosition();
        final Double percentageY = scrollBars().getVerticalScrollPosition();

        final Double currentXPosition = scrollPosition().currentPositionX(percentageX);
        final Double currentYPosition = scrollPosition().currentPositionY(percentageY);

        updateLayerLienzoTransform(currentXPosition,
                                   currentYPosition);
    }

    void updateLayerLienzoTransform(final Double currentXPosition,
                                    final Double currentYPosition)
    {

        final Transform oldTransform = getViewport().getTransform();
        final Double    dx           = currentXPosition - (oldTransform.getTranslateX() / oldTransform.getScaleX());
        final Double    dy           = currentYPosition - (oldTransform.getTranslateY() / oldTransform.getScaleY());

        final Transform newTransform = oldTransform.copy().translate(dx,
                                                                     dy);

        getViewport().setTransform(newTransform);
        getLayer().batch();
    }

    void synchronizeScrollSize()
    {
        getInternalScrollPanel().setPixelSize(calculateInternalScrollPanelWidth(),
                                              calculateInternalScrollPanelHeight());
    }

    Integer calculateInternalScrollPanelWidth()
    {
        final Double absWidth = scrollBounds().maxBoundX() - scrollBounds().minBoundX();

        if (getViewport() != null && scrollPosition().deltaX() != 0)
        {
            final Double scaleX = getViewport().getTransform().getScaleX();
            final Double width  = absWidth * scaleX;

            return width.intValue();
        }

        return DEFAULT_INTERNAL_SCROLL_WIDTH;
    }

    Integer calculateInternalScrollPanelHeight()
    {
        final Double absHeight = scrollBounds().maxBoundY() - scrollBounds().minBoundY();

        if (getViewport() != null && scrollPosition().deltaY() != 0)
        {
            final Double scaleY = getViewport().getTransform().getScaleY();
            final Double height = absHeight * scaleY;

            return height.intValue();
        }

        return DEFAULT_INTERNAL_SCROLL_HEIGHT;
    }

    void setScrollBarsPosition(final Double xPercentage,
                               final Double yPercentage)
    {

        scrollBars().setHorizontalScrollPosition(xPercentage);
        scrollBars().setVerticalScrollPosition(yPercentage);
    }

    RestrictedMousePanMediator getMousePanMediator()
    {
        return mousePanMediator;
    }

    AbsolutePanel getScrollPanel()
    {
        return getPanel().getScrollPanel();
    }

    AbsolutePanel getInternalScrollPanel()
    {
        return getPanel().getInternalScrollPanel();
    }

    AbsolutePanel getDomElementContainer()
    {
        return getPanel().getDomElementContainer();
    }

    LienzoPanel getLienzoPanel()
    {
        return getPanel().getLienzoPanel();
    }

    InfiniteLienzoPanel getPanel()
    {
        return panel;
    }

    InfiniteLayer getLayer()
    {
        if (null == panel.getLayer())
        {
            return emptyLayer();
        }
        return panel.getLayer();
    }

    Viewport getViewport()
    {
        return getLayer().getViewport();
    }

    InfiniteLayer emptyLayer()
    {
        return new InfiniteLienzoLayer();
    }

    LienzoScrollBars scrollBars()
    {
        return new LienzoScrollBars(this);
    }

    LienzoScrollPosition scrollPosition()
    {
        return new LienzoScrollPosition(this);
    }

    LienzoScrollBounds scrollBounds()
    {
        return scrollBounds;
    }

    public void setBounds(final Bounds bounds)
    {
        scrollBounds().setDefaultBounds(bounds);
    }
}
