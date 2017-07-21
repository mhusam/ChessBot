package com.jmco.chessbot.controller.home;

import com.jmco.chessbot.chess.ChessDrawerWindow;
import com.jmco.chessbot.chess.controller.home.LocateListener;
import com.jmco.chessbot.chess.controller.home.StartProcessingListener;
import com.jmco.recorder.DesktopScreenRecorder;
import com.jmco.utils.ChessProcessorListener;
import com.jmco.utils.ImageUtils;
import com.jmco.utils.OSListenerBuilder;
import com.jmco.utils.RecorderBuilder;
import com.jmco.utils.UIUtils;
import java.awt.Window;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class Home implements Initializable {

    private @FXML
    TextArea messages;
    private @FXML
    Button recorderBtn;
    private @FXML
    Button locate;
    private @FXML
    TextField xAxis1;
    private @FXML
    TextField yAxis1;
    private @FXML
    TextField xAxis2;
    private @FXML
    TextField yAxis2;

    private @FXML
    TextField status;

    private Boolean FIRST_CLICK_CAPTURE = Boolean.TRUE;
    private Boolean FIRST_CLICK_LOCATE = Boolean.TRUE;
    private Boolean FIRST_CLICK_WINDOW = Boolean.TRUE;

    private RecorderBuilder recorderBuilder;
    //private ChessRecorder chessRecorder;

    private final OSListenerBuilder oSListenerBuilder = OSListenerBuilder.getInstance();
    private ChessProcessorListener captureProcessorListener;

    private Window window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAxies();
        captureProcessorListener = new ChessProcessorListener(recorderBuilder, recorderBtn, messages, FIRST_CLICK_LOCATE);
        recorderBuilder = RecorderBuilder.getInstance(captureProcessorListener);
        captureProcessorListener.setRecorderBuilder(recorderBuilder);

        oSListenerBuilder.keyboard(new StartProcessingListener(() -> {
            Platform.runLater(() -> {
                captureAction();
            });
        }));
    }

    private void initAxies() {
        //<editor-fold defaultstate="collapsed" desc="Axies">
        xAxis1.setText(String.valueOf(DesktopScreenRecorder.CAPTURE_1_X));
        yAxis1.setText(String.valueOf(DesktopScreenRecorder.CAPTURE_1_Y));
        xAxis2.setText(String.valueOf(DesktopScreenRecorder.CAPTURE_2_X + DesktopScreenRecorder.CAPTURE_1_X));
        yAxis2.setText(String.valueOf(DesktopScreenRecorder.CAPTURE_2_Y + DesktopScreenRecorder.CAPTURE_1_Y));
        //</editor-fold>
    }

    @FXML
    public void captureAction() {
        //<editor-fold defaultstate="collapsed" desc="capture">
        DesktopScreenRecorder.changeAxises(
                Integer.valueOf(xAxis1.getText()),
                Integer.valueOf(yAxis1.getText()),
                Integer.valueOf(xAxis2.getText()),
                Integer.valueOf(yAxis2.getText()));

        if (FIRST_CLICK_CAPTURE) {
            recorderBuilder.start();
            updateCaptureMessage("Start Game Video in Computer Monitor", "Stop Game");
        } else {
            recorderBuilder.stop();
            updateCaptureMessage("Video has been stopped", "Start Game");
        }
        FIRST_CLICK_CAPTURE = !FIRST_CLICK_CAPTURE;
        //</editor-fold>
    }

    @FXML
    public void drawBoard() {
        //<editor-fold defaultstate="collapsed" desc="drawBoard">
        if (FIRST_CLICK_WINDOW) {
            int x1 = Integer.valueOf(xAxis1.getText());
            int y1 = Integer.valueOf(yAxis1.getText());
            int x2 = Integer.valueOf(xAxis2.getText());
            int y2 = Integer.valueOf(yAxis2.getText());

            window = new ChessDrawerWindow(x1, y1, x2, y2, window);
        } else {
            window.dispose();
        }

        FIRST_CLICK_WINDOW = !FIRST_CLICK_WINDOW;
        //</editor-fold>
    }

    @FXML
    public void locateAxies() {
        //<editor-fold defaultstate="collapsed" desc="locate">
        if (FIRST_CLICK_LOCATE) {
            oSListenerBuilder.mouse(new LocateListener(xAxis1, yAxis1, xAxis2, yAxis2));
            updateLocateMessage("Select The Points ... \n Keep pressing the right button in the mouse for 2 seconds.", "Stop Locate");
        } else {
            oSListenerBuilder.shutdown(false);
            updateLocateMessage("Done Getting the Points.", "Locate");

            xAxis1.setStyle("-fx-background-color: #FFFFFF;");
            yAxis1.setStyle("-fx-background-color: #FFFFFF;");
            xAxis2.setStyle("-fx-background-color: #FFFFFF;");
            yAxis2.setStyle("-fx-background-color: #FFFFFF;");
        }
        FIRST_CLICK_LOCATE = !FIRST_CLICK_LOCATE;
        //</editor-fold>
    }

    public void updateCaptureMessage(String message, String label) {
        //<editor-fold defaultstate="collapsed" desc="On capture button clicked">
        Platform.runLater(() -> {
            String oldMessage = messages.getText();
            if (oldMessage != null && oldMessage.trim().length() > 0) {
                messages.setText(oldMessage + "\n" + message);
            } else {
                messages.setText(message);
            }
            if (label != null) {
                recorderBtn.setText(label);
            }
        });
        //</editor-fold>
    }

    public void updateLocateMessage(String message, String label) {
        //<editor-fold defaultstate="collapsed" desc="On Locate button clicked">
        Platform.runLater(() -> {
            String oldMessage = messages.getText();
            if (oldMessage != null && oldMessage.trim().length() > 0) {
                messages.setText(oldMessage + "\n" + message);
            } else {
                messages.setText(message);
            }
            if (label != null) {
                locate.setText(label);
            }
        });
        //</editor-fold>
    }

    @FXML
    public void captureImage() {
        //<editor-fold defaultstate="collapsed" desc="captureImage">
        DesktopScreenRecorder.changeAxises(
                Integer.valueOf(xAxis1.getText()),
                Integer.valueOf(yAxis1.getText()),
                Integer.valueOf(xAxis2.getText()),
                Integer.valueOf(yAxis2.getText()));

        ImageUtils.capture();
        ImageUtils.writeStatus(status.getText());
        //</editor-fold>
    }

    @FXML
    public void addStatusOnly() {
        ImageUtils.writeStatus(status.getText());
    }

    @FXML
    public void about() {
        try {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Chess Bot v1.0.0");
            alert.setHeaderText("Developed By Husam Aljamal");
            alert.setContentText("m-husam@hotmail.com");
            UIUtils.alwaysInTop(alert);
            alert.showAndWait();
        } catch (Exception e) {

        }
    }
}
