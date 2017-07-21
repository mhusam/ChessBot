package com.jmco.recorder;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import com.jmco.utils.UIUtils;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class Recorder implements ScreenRecorderListener {

    private ScreenRecorder recorder;
    private int frameCount = 0;

    @Override
    public void startRecording(CaptureProcessorListener captureChessListener) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
        }

        recorder = new DesktopScreenRecorder(this, captureChessListener);
        recorder.startRecording();
    }

    @Override
    public void frameRecorded(boolean fullFrame) {
        frameCount++;
    }

    @Override
    public void stopRecording() {
        try {
            recorder.interruptRecording();
            recorder = null;
            frameCount = 0;
        } catch (Exception e) {
            UIUtils.popupError(e, "Recorder::stopRecording");
        }
    }
}
