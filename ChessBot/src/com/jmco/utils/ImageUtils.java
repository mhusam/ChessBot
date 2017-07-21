package com.jmco.utils;

import static com.jmco.chessbot.chess.position.ChessBoardUtils.IMAGE_MIME_TYPE;
import com.jmco.recorder.DesktopScreenRecorder;
import static com.jmco.recorder.DesktopScreenRecorder.CAPTURE_1_X;
import static com.jmco.recorder.DesktopScreenRecorder.CAPTURE_1_Y;
import static com.jmco.recorder.DesktopScreenRecorder.CAPTURE_2_X;
import static com.jmco.recorder.DesktopScreenRecorder.CAPTURE_2_Y;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ImageUtils {
    
    public static int COUNTER = 1;
    
    public static void writeImage(String path, String moveNumber, String fileName, BufferedImage image){
        try{
            ImageIO.write(image, IMAGE_MIME_TYPE, new FileOutputStream(path + "\\" + fileName + moveNumber + "." + IMAGE_MIME_TYPE));
        }catch(Exception e) {
            UIUtils.popupError(e, "ImageUtils::writeImage");
        }
    }
    
    public static BufferedImage captureImage(String iii) {
        try {
            Rectangle rec = new Rectangle(CAPTURE_1_X, CAPTURE_1_Y, CAPTURE_2_X, CAPTURE_2_Y);
            Robot robot = new Robot();
            
            BufferedImage img = robot.createScreenCapture(rec);
            
            int[] rawData = new int[DesktopScreenRecorder.FRAME_SIZE];

            img.getRGB(0, 0,
                    DesktopScreenRecorder.CAPTURE_2_X,
                    DesktopScreenRecorder.CAPTURE_2_Y,
                    rawData,
                    0,
                    DesktopScreenRecorder.CAPTURE_2_X);
            
            return img;
        } catch (Exception e) {
            UIUtils.popupError(e, "ImageUtils::writeImage");
        }
        return null;
    }
    
    public static BufferedImage captureImage() {
        try {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Robot rt = new Robot();
            BufferedImage img = rt.createScreenCapture(
                    new Rectangle(DesktopScreenRecorder.CAPTURE_1_X, 
                            DesktopScreenRecorder.CAPTURE_1_Y, 
                            DesktopScreenRecorder.CAPTURE_2_X, 
                            DesktopScreenRecorder.CAPTURE_2_Y));
            
            return img;
        } catch (Exception e) {
            UIUtils.popupError(e, "ImageUtils::writeImage");
        }
        return null;
    }
    
    public static File capture() {
        try {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Robot rt = new Robot();
            BufferedImage img = rt.createScreenCapture(
                    new Rectangle(DesktopScreenRecorder.CAPTURE_1_X, 
                            DesktopScreenRecorder.CAPTURE_1_Y, 
                            DesktopScreenRecorder.CAPTURE_2_X, 
                            DesktopScreenRecorder.CAPTURE_2_Y));
            
            File imageFile = new File("find-move-"+String.format("%2d", COUNTER)+"-cap.jpeg");
            COUNTER++;
            if (ImageIO.write(img, "jpeg", imageFile)) {
                return imageFile;
            }
        } catch (Exception e) {
            UIUtils.popupError(e, "ImageUtils::capture");
        }
        return null;
    }
    
    public static void writeStatus(String move) {
        if(move == null || move.trim().length() == 0){
            return;
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\ChessBot\\status.txt", true));){
            bw.write(move);
            bw.write("\r\n");
            
        } catch (Exception e) {
            UIUtils.popupError(e, "ImageUtils::writeStatus");
        }
    }
    
    public static Image getImage(String path) {
        File file = new File(path);
        return new Image(file.toURI().toString());
    }
}
