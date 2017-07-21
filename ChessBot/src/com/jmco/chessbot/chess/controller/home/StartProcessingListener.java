package com.jmco.chessbot.chess.controller.home;

import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class StartProcessingListener extends GlobalKeyAdapter {

    private final KeyboardActionListener listener;

    public StartProcessingListener(KeyboardActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void keyPressed(GlobalKeyEvent event) {
        char one = '1';
        if (isStartKeyPressed(event) && event.getKeyChar() == one) {
            listener.action();
        }
    }

    @Override
    public void keyReleased(GlobalKeyEvent event) {
    }

    private boolean isStartKeyPressed(GlobalKeyEvent event) {
        return event.isShiftPressed() && event.isControlPressed();
    }

    public static interface KeyboardActionListener {
        void action();
    }
}
