package com.jmco.recorder;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import com.jmco.utils.UIUtils;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class DesktopScreenRecorder extends ScreenRecorder {
    
    public static int CAPTURE_1_X = 100;
    public static int CAPTURE_1_Y = 100;
    public static int CAPTURE_2_X = 400;
    public static int CAPTURE_2_Y = 400;
    
    public static int FRAME_SIZE;
    
    static{
        changeAxises(CAPTURE_1_X, CAPTURE_1_Y, CAPTURE_2_X, CAPTURE_2_Y);
    }
    
    public static boolean useWhiteCursor = true;
    private Robot robot;
    
    public DesktopScreenRecorder(ScreenRecorderListener listener, CaptureProcessorListener captureChessListener) {
        super(listener, captureChessListener);
    }

    @Override
    public Rectangle initialiseScreenCapture() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            UIUtils.popupError(e, "DesktopScreenRecorder::initialiseScreenCapture");
            return null;
        }

        return new Rectangle(CAPTURE_1_X, CAPTURE_1_Y, CAPTURE_2_X, CAPTURE_2_Y);
    }

    public Robot getRobot() {
        return robot;
    }

    @Override
    public BufferedImage captureScreen(Rectangle recordArea) {
        BufferedImage image = robot.createScreenCapture(recordArea);
        return image;
    }
    
    public static void changeAxises(int xAxis1, int yAxis1,int xAxis2, int yAxis2) {
        CAPTURE_1_X = xAxis1;
        CAPTURE_1_Y = yAxis1;
        CAPTURE_2_X = Math.abs(CAPTURE_1_X - xAxis2);
        CAPTURE_2_Y = Math.abs(CAPTURE_1_Y - yAxis2);
        
        FRAME_SIZE = Math.abs(CAPTURE_2_X * CAPTURE_2_Y);
    }
}
