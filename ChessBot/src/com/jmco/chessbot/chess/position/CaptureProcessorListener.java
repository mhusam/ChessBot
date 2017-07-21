package com.jmco.chessbot.chess.position;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public interface CaptureProcessorListener {
    
    void doStop();
    
    void onMove(MovePosition move);
}
