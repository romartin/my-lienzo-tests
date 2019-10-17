package org.roger600.lienzo.client.util;

import com.ait.tooling.common.api.java.util.function.Consumer;
import com.ait.tooling.nativetools.client.collection.NFastDoubleArray;
import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

public class FPSCounter {

    private final NFastDoubleArray times;
    private final Consumer<Integer> fps;
    private final Runnable onDestroy;
    private boolean enabled;

    public static FPSCounter toHTML(final HTML text) {
        return new FPSCounter(new Consumer<Integer>() {
            @Override
            public void accept(Integer fps) {
                text.setHTML("FPS [ " + fps + " ]");
            }
        });
    }

    public static FPSCounter toConsole() {
        return new FPSCounter(new Consumer<Integer>() {
            @Override
            public void accept(Integer fps) {
                GWT.log("FPS [ " + fps + " ]");
            }
        });
    }

    public FPSCounter(Consumer<Integer> fps) {
        this(fps, new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public FPSCounter(Consumer<Integer> fps,
                      Runnable onDestroy) {
        this.times = new NFastDoubleArray();
        this.fps = fps;
        this.enabled = false;
        this.onDestroy = onDestroy;
    }

    public FPSCounter start() {
        this.enabled = true;
        monitorFpsRate();
        return this;

    }

    public FPSCounter stop() {
        this.enabled = false;
        return this;
    }

    public void destroy() {
        stop();
        onDestroy.run();
    }

    private void onFrame() {
        Duration duration = new Duration();
        double now = duration.getStartMillis();
        while (times.size() > 0 && times.get(0) <= (now - 1000)) {
            times.shift();
        }
        times.push(now);
        int fps_value = times.size();
        fps.accept(fps_value);
        if (enabled) {
            monitorFpsRate();
        }
    }

    private void monitorFpsRate() {
        AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
            @Override
            public void execute(double v) {
                onFrame();
            }
        });
    }
}
