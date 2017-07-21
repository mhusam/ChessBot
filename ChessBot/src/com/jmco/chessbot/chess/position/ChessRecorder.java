package com.jmco.chessbot.chess.position;

import com.jmco.utils.ImageUtils;
import com.jmco.utils.UIUtils;
import java.awt.image.BufferedImage;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessRecorder implements Runnable {

    private volatile boolean shutdown;
    private final ChessBoardProcessor chessProcessor;

    public ChessRecorder(CaptureProcessorListener chessProcessor) {
        this.chessProcessor = new ChessBoardProcessor(chessProcessor);
    }

    public void shutdown() {
        shutdown = Boolean.TRUE;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                Thread.sleep(100);

                BufferedImage chessBoardImage = ImageUtils.captureImage();
                if (chessBoardImage != null) {
//                    chessProcessor.doChessStuff(chessBoardImage);
                }
            } catch (InterruptedException e) {
                UIUtils.popupError(e, "ChessRecorder::run");
            }
        }
    }
}
