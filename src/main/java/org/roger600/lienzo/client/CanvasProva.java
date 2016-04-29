package org.roger600.lienzo.client;

import com.google.gwt.canvas.client.Canvas;

public class CanvasProva {
    
    Canvas canvas;

    public CanvasProva() {
        canvas = Canvas.createIfSupported();
    }

    public void prova() {
        canvas.getContext2d().rect(0, 0, 50, 50);
    }
    
}
