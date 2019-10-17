package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.animation.AnimationCallback;
import com.ait.lienzo.client.core.animation.AnimationProperties;
import com.ait.lienzo.client.core.animation.AnimationProperty;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Shape;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.Color;
import com.ait.tooling.common.api.java.util.function.Supplier;
import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import org.roger600.lienzo.client.util.FPSCounter;

public class PerformanceTests implements MyLienzoTest,
                                         HasButtons {

    private static final int TOTAL_CIRCLES = 100;

    private static Supplier<Shape> shapeBuilder = new Supplier<Shape>() {
        @Override
        public Shape get() {
            return new Circle(10).setStrokeColor(Color.getRandomHexColor()).setStrokeWidth(2).setFillColor(Color.getRandomHexColor()).setDraggable(true);
        }
    };

    private Layer layer;
    private int count = TOTAL_CIRCLES;
    private Shape[] instances;
    private IAnimationHandle[] handles;
    private FPSCounter fpsCounter;
    private HTML countText;
    private Button startAnimationButton;
    private Button stopAnimationButton;
    private HTML timeText;

    private boolean isAnimationEnabled = false;
    private int width;
    private int height;
    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;

    @Override
    public void setButtonsPanel(Panel panel) {

        // FPS.
        final HTML fpsCounterPanel = new HTML(" ");
        fpsCounter = FPSCounter.toHTML(fpsCounterPanel);
        fpsCounterPanel.getElement().getStyle().setColor("blue");
        fpsCounterPanel.getElement().getStyle().setFloat(Style.Float.RIGHT);
        panel.add(fpsCounterPanel);

        // Buttons.
        final Button decreaseCountButton = new Button("[-]");
        decreaseCountButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                decreaseInstanceCount();
            }
        });
        panel.add(decreaseCountButton);

        countText = new HTML("" + count);
        panel.add(countText);

        final Button increaseCountButton = new Button("[+]");
        increaseCountButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                increaseInstanceCount();
            }
        });
        panel.add(increaseCountButton);

        final Button runButton = new Button("Run");
        runButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                run();
            }
        });
        panel.add(runButton);

        timeText = new HTML("" + count);
        panel.add(timeText);

        startAnimationButton = new Button("Start");
        startAnimationButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                startAnimation();
            }
        });
        panel.add(startAnimationButton);

        stopAnimationButton = new Button("Stop");
        stopAnimationButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                stopAnimation();
            }
        });
        panel.add(stopAnimationButton);
    }

    @Override
    public void test(Layer layer) {
        this.layer = layer;
        width = LienzoTests.WIDE;
        height = LienzoTests.HIGH;
        leftPadding = 0;
        topPadding = 0;
        rightPadding = 0;
        bottomPadding = 0;
        updateAnimationButtonsState();
        run();
        fpsCounter.start();
    }

    private void decreaseInstanceCount() {
        setCountIncrement(-1 * getCountInc());
    }

    private void increaseInstanceCount() {
        setCountIncrement(getCountInc());
    }

    private void updateAnimationButtonsState() {
        startAnimationButton.setEnabled(!isAnimationEnabled);
        stopAnimationButton.setEnabled(isAnimationEnabled);
    }

    private void stopAnimation() {
        isAnimationEnabled = false;
        if (null != handles) {
            for (int i = 0; i < handles.length; i++) {
                IAnimationHandle handle = handles[i];
                handle.stop();
                handles[i] = null;
            }
            handles = null;
        }
        updateAnimationButtonsState();
    }

    private void startAnimation() {
        isAnimationEnabled = true;
        handles = new IAnimationHandle[instances.length];
        int i = 0;
        for (Shape instance : instances) {
            animateInstance(i, instance);
            i++;
        }
        for (IAnimationHandle handle : handles) {
            handle.run();
        }
        updateAnimationButtonsState();
    }

    private IAnimationHandle animateInstance(final int i,
                                             final Shape shape) {
        final double[] location = getRandomLocation(shape, width, height, leftPadding, topPadding, rightPadding, bottomPadding);
        // console.log("Shape animated to [" + location[0] + ", " + location[1] + "]");
        IAnimationHandle handle = shape.animate(AnimationTweener.ELASTIC,
                                                AnimationProperties.toPropertyList(AnimationProperty.Properties.X(location[0]),
                                                                                   AnimationProperty.Properties.Y(location[1])),
                                                500,
                                                new AnimationCallback() {
                                                    @Override
                                                    public void onClose(IAnimation animation, IAnimationHandle handle) {
                                                        super.onClose(animation, handle);
                                                        if (isAnimationEnabled) {
                                                            animateInstance(i, shape);
                                                        }
                                                    }
                                                });
        handles[i] = handle;
        return handle;
    }

    private void setCountIncrement(int value) {
        if (count + value > 0) {
            count = count + value;
            countText.setHTML("" + count);
            run();
        }
    }

    private void run() {
        boolean wasAnimationEnabled = isAnimationEnabled;

        clear();

        Duration duration = new Duration();

        instances = new Shape[count];
        for (int i = 0; i < count; i++) {
            final Shape shape = shapeBuilder.get();
            instances[i] = shape;
            setRandomLocation(shape);
            layer.add(shape);
        }

        layer.draw();

        int ellapsed = duration.elapsedMillis();
        GWT.log("Rendering Test completed - #[" + count + "] took [" + ellapsed + "ms]");
        timeText.setHTML("[ " + ellapsed + "ms ]");

        if (wasAnimationEnabled) {
            startAnimation();
        }
    }

    private void clear() {
        stopAnimation();
        if (null != instances) {
            for (Shape instance : instances) {
                instance.removeFromParent();
            }
            instances = null;
        }
        layer.clear();
        layer.draw();
    }

    private int getCountInc() {
        return count / 4;
    }

    private static void setLocation(Shape shape,
                                    int viewWidth, int viewHeight,
                                    int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        double[] location = getRandomLocation(shape, viewWidth, viewHeight, leftPadding, topPadding, rightPadding, bottomPadding);
        shape.setX(location[0]).setY(location[1]);
    }

    private void setRandomLocation(Shape shape) {
        setLocation(shape, width, height, leftPadding, topPadding, rightPadding, bottomPadding);
    }

    // TODO
    /*public void destroy() {
        clear();
        fpsCounter.destroy();
        fpsCounter = null;
        decreaseCountButton.remove();
        countText.remove();
        increaseCountButton.remove();
        runTestButton.remove();
        timeText.remove();
        startAnimationButton.remove();
        stopAnimationButton.remove();
    }*/

    private static double[] getRandomLocation(Shape shape,
                                              int viewWidth, int viewHeight,
                                              int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        // this is needed as some shapes, like circles and multipaths, won't have same x,y result as rectangle
        Point2DArray points = shape.getBoundingPoints().getArray();
        double xOffset = points.get(0).getX() - shape.getX();
        double yOffset = points.get(0).getY() - shape.getY();

        double shapeWidth = points.get(1).getX() - points.get(0).getX();
        double shapeHeight = points.get(3).getY() - points.get(0).getY();

        double wRange = viewWidth - leftPadding - rightPadding - shape.getStrokeWidth() - shapeWidth;
        double hRange = viewHeight - topPadding - bottomPadding - shape.getStrokeWidth() - shapeHeight;

        double x = Math.random() * wRange;
        double y = Math.random() * hRange;

        x = x + leftPadding + shape.getStrokeWidth() - xOffset;
        y = y + topPadding + shape.getStrokeWidth() - yOffset;

        return new double[]{x, y};
    }
}
