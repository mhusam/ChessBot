package com.jmco.utils;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import com.jmco.recorder.Recorder;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public final class RecorderBuilder {

    private static RecorderBuilder INSTANCE;
    private final Recorder recorder;
    private CaptureProcessorListener captureProcessorListener;
    
    private RecorderBuilder(CaptureProcessorListener captureProcessorListener) {
        recorder = new Recorder();
        this.captureProcessorListener = captureProcessorListener;
    }

    public static RecorderBuilder getInstance(CaptureProcessorListener captureProcessorListener) {
        if (INSTANCE == null) {
            return INSTANCE = new RecorderBuilder(captureProcessorListener);
        }
        return INSTANCE;
    }

    public void start() {
        try {
            recorder.startRecording(captureProcessorListener);
        } catch (Exception e) {
            UIUtils.popupError(e, "RecorderBuilder::start");
        }
    }

    public void stop() {
        try {
            recorder.stopRecording();
        } catch (Exception e) {
            UIUtils.popupError(e, "RecorderBuilder::stop");
        }
    }
}
