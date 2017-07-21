package com.jmco.recorder;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import com.jmco.chessbot.chess.position.ChessBoardProcessor;
import com.jmco.utils.UIUtils;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public abstract class ScreenRecorder implements Runnable {

    private Rectangle recordArea;

    private int[] rawData;
    private volatile boolean stopRecording = false;
    private final ScreenRecorderListener listener;
    private Thread screenRecorderThread;
    private final ChessBoardProcessor chessProcessor;

    public ScreenRecorder(OutputStream oStream, ScreenRecorderListener listener) {
        this.listener = listener;        
        this.chessProcessor = new ChessBoardProcessor();
    }

    public ScreenRecorder(ScreenRecorderListener listener, CaptureProcessorListener chessProcessor) {
        this.listener = listener;
        this.chessProcessor = new ChessBoardProcessor(chessProcessor);
    }
    
    public ScreenRecorder(OutputStream oStream, ScreenRecorderListener listener, CaptureProcessorListener chessProcessor) {
        this.listener = listener;
        //this.oStream = oStream;
        
        this.chessProcessor = new ChessBoardProcessor(chessProcessor);
    }

    @Override
    public void run() {
        long lastFrameTime = 0;
        long time = 0;

        while (!stopRecording) {
            time = System.currentTimeMillis();
            while (time - lastFrameTime < 190) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
                time = System.currentTimeMillis();
            }
            lastFrameTime = time;

            try {
                recordFrame();
            } catch (Exception e) {
                break;
            }
        }
    }

    public abstract Rectangle initialiseScreenCapture();

    public abstract BufferedImage captureScreen(Rectangle recordArea);

    public void recordFrame() throws Exception {        
        BufferedImage bImage = captureScreen(recordArea);

        rawData = new int[DesktopScreenRecorder.FRAME_SIZE];

        bImage.getRGB(0, 0,
                DesktopScreenRecorder.CAPTURE_2_X,
                DesktopScreenRecorder.CAPTURE_2_Y,
                rawData,
                0,
                DesktopScreenRecorder.CAPTURE_2_X);

        try{
            doChessStuff(bImage);
        } catch(Exception e) {
            UIUtils.popupError(e, "ScreenRecorder::recordFrame");
            listener.stopRecording();
        }
    }
    
    private void doChessStuff(BufferedImage chessBoardImage){        
        try{Thread.sleep(100);}catch(Exception e){}
        BufferedImage bImage = captureScreen(recordArea);

        rawData = new int[DesktopScreenRecorder.FRAME_SIZE];

        bImage.getRGB(0, 0,
                DesktopScreenRecorder.CAPTURE_2_X,
                DesktopScreenRecorder.CAPTURE_2_Y,
                rawData,
                0,
                DesktopScreenRecorder.CAPTURE_2_X);
        chessProcessor.doChessStuff(chessBoardImage, bImage);
    }

    public void startRecording() {
        recordArea = initialiseScreenCapture();

        if (recordArea == null) {
            return;
        }

        screenRecorderThread = new Thread(this, "Screen Recorder");
        screenRecorderThread.start();
    }

    public void interruptRecording() {
        setStopRecording(true);
        if (screenRecorderThread != null) {
            screenRecorderThread.interrupt();
        }
    }

    public void setStopRecording(boolean stopRecording) {
        this.stopRecording = stopRecording;
    }
}
