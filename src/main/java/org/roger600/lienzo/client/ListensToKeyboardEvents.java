package org.roger600.lienzo.client;

import com.google.gwt.event.dom.client.KeyCodes;

public interface ListensToKeyboardEvents {

    public void onKeyPress(Key key);

    public void onKeyUp(Key key);

    public void onKeyDown(Key key);

    enum Key {
        ESC(27),
        CONTROL(KeyCodes.KEY_CTRL),
        SHIFT(16),
        ALT(KeyCodes.KEY_ALT),
        DELETE(46),
        SPACE(KeyCodes.KEY_SPACE),
        BACKSPACE(KeyCodes.KEY_BACKSPACE),
        ARROW_UP(38),
        ARROW_DOWN(40),
        ARROW_LEFT(37),
        ARROW_RIGHT(39),
        C(67),
        D(68),
        E(69),
        G(71),
        S(83),
        T(84),
        V(86),
        X(88),
        Z(90);

        private final int unicharCode;

        Key(final int unicharCode) {
            this.unicharCode = unicharCode;
        }

        public int getUnicharCode() {
            return unicharCode;
        }

        public static Key getKey(final int unicodeChar) {
            final Key[] keys = Key.values();
            for (final Key key : keys) {
                final int c = key.getUnicharCode();
                if (c == unicodeChar) {
                    return key;
                }
            }
            return null;
        }
    }
}
