package com.jmco.utils;

import com.jmco.chessbot.chess.position.CaptureProcessorListener;
import com.jmco.chessbot.chess.position.MovePosition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessProcessorListener implements CaptureProcessorListener {
    
    private RecorderBuilder recorderBuilder;
    private final Button recorderBtn;
    private final TextArea messages;
    
    private Boolean isStopped;
    
    public ChessProcessorListener(RecorderBuilder recorderBuilder, Button recorderBtn, TextArea messages, Boolean isStopped) {
        this.recorderBuilder = recorderBuilder;
        this.recorderBtn = recorderBtn;
        this.messages = messages;
        this.isStopped = isStopped;
    }

    public void setRecorderBuilder(RecorderBuilder recorderBuilder) {
        this.recorderBuilder = recorderBuilder;
    }
    
    @Override
    public void onMove(MovePosition message) {
        isStopped = Boolean.FALSE;
        Platform.runLater(() -> {
            if(messages != null){                
                messages.setText(new StringBuffer(messages.getText())
                        .append("\n")
                        .append(message).toString());
            }
        });
    }

    @Override
    public void doStop() {
        isStopped = Boolean.TRUE;
        Platform.runLater(() -> {
            recorderBuilder.stop();
            recorderBtn.setText("Start Game");
        });
    }
}
