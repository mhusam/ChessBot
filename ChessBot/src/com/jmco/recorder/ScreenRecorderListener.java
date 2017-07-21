package com.jmco.recorder;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import java.io.IOException;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public interface ScreenRecorderListener {

    public void startRecording(CaptureProcessorListener captureChessListener);

    public void stopRecording();

    public void frameRecorded(boolean fullFrame) throws IOException;
}
