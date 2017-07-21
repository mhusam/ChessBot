package com.jmco.chessbot.chess.position;

import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessMoveStopListener extends GlobalKeyAdapter {

    private Boolean startWait = Boolean.FALSE;
    private final ChessBoardProcessor chessProcessor;

    public ChessMoveStopListener(ChessBoardProcessor chessProcessor) {
        this.chessProcessor = chessProcessor;
    }

    @Override
    public void keyPressed(GlobalKeyEvent event) {
        if (isCtrlClicked(event) && !startWait) {
            startWait = Boolean.TRUE;
        }
    }

    @Override
    public void keyReleased(GlobalKeyEvent event) {
        if (isCtrlClicked(event) && startWait) {
            synchronized(chessProcessor){
                chessProcessor.notify();
            }
            startWait = Boolean.FALSE;
        }
    }

    public Boolean isStartWait() {
        return startWait;
    }

    private boolean isCtrlClicked(GlobalKeyEvent event) {
        return event.getVirtualKeyCode() == 162; // CTRL
    }
}
