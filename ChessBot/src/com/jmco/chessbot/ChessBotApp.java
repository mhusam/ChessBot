package com.jmco.chessbot;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 */
public class ChessBotApp extends Application {

    private final ChessBotAppSplash splash = new ChessBotAppSplash();

    @Override
    public void init() throws Exception {
        splash.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        splash.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
