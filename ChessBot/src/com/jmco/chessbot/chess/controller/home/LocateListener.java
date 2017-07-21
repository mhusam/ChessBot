package com.jmco.chessbot.chess.controller.home;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class LocateListener extends GlobalMouseAdapter {

    private ZonedDateTime pressedTime;
    private final ChronoUnit unit = ChronoUnit.SECONDS;

    private final TextField xAxis1;
    private final TextField yAxis1;
    private final TextField xAxis2;
    private final TextField yAxis2;

    private Boolean IS_GETING_FIRST_POINT = Boolean.TRUE;

    public LocateListener(TextField xAxis1, TextField yAxis1, TextField xAxis2, TextField yAxis2) {
        this.xAxis1 = xAxis1;
        this.yAxis1 = yAxis1;
        this.xAxis2 = xAxis2;
        this.yAxis2 = yAxis2;
        
        toggleAxies();
    }
    
    private void toggleAxies(){
        Platform.runLater(() -> {
            if (IS_GETING_FIRST_POINT) {
                xAxis1.setStyle("-fx-background-color: #a89093;");
                yAxis1.setStyle("-fx-background-color: #a89093;");
                xAxis2.setStyle("-fx-background-color: #FFFFFF;");
                yAxis2.setStyle("-fx-background-color: #FFFFFF;");
            } else {
                xAxis1.setStyle("-fx-background-color: #FFFFFF;");
                yAxis1.setStyle("-fx-background-color: #FFFFFF;");
                xAxis2.setStyle("-fx-background-color: #a89093;");
                yAxis2.setStyle("-fx-background-color: #a89093;");
            }
        });
    }

    @Override
    public void mousePressed(GlobalMouseEvent event) {
        if (isMouseRight(event)) {
            pressedTime = ZonedDateTime.now();
        }
    }

    @Override
    public void mouseReleased(GlobalMouseEvent event) {
        if (isMouseRight(event)) {
            long periodInSeconds = unit.between(pressedTime, ZonedDateTime.now());
            if (periodInSeconds > 1) {
                updateAxies(event);
            }
        }
    }

    private void updateAxies(GlobalMouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        if (IS_GETING_FIRST_POINT) {
            xAxis1.setText(String.valueOf(x));
            yAxis1.setText(String.valueOf(y));
        } else {
            xAxis2.setText(String.valueOf(x));
            yAxis2.setText(String.valueOf(y));
        }
        
        toggleAxies();
        IS_GETING_FIRST_POINT = !IS_GETING_FIRST_POINT;
    }

    private boolean isMouseRight(GlobalMouseEvent event) {
        return event.getButton() == GlobalMouseEvent.BUTTON_LEFT;
    }
}
