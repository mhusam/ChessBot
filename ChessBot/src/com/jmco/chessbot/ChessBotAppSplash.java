package com.jmco.chessbot;

import com.jmco.chessbot.pages.PagePath;
import com.jmco.chessbot.pages.PageResources;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author mhusam [m-husam@hotmail.com]
 * @since 1.0.0
 * @datetime Jun 30, 2017 2:05:27 PM
 */
public class ChessBotAppSplash {

    private static final String BASE_PACKAGE = "com/jmco/chessbot";
    public static final String APPLICATION_ICON = BASE_PACKAGE + "/robot-icon.png";
    public static final String SPLASH_IMAGE = BASE_PACKAGE + "/robot-icon.png";

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 512;
    private static final int SPLASH_HEIGHT = 512;

    public void init() {
        ImageView splash = new ImageView(new Image(
                SPLASH_IMAGE
        ));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        loadProgress.setStyle("-fx-padding: 10; ");
        progressText = new Label("Loading Chess Bot");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 10; "
                + "-fx-background-color: white; "
                + "-fx-border-width:3; "
                + "-fx-border-color: "
                + "linear-gradient("
                + "to bottom, "
                + "chocolate, "
                + "derive(chocolate, 50%)"
                + ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    public void start(final Stage initStage) {        
        initStage.getIcons().add(new Image(APPLICATION_ICON));
        initStage.setResizable(false);
        initStage.setAlwaysOnTop(true);
        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> titles = FXCollections.<String>observableArrayList();
                ObservableList<String> loadings = FXCollections.observableArrayList(
                        "Validate Version", "Running", "Initialize application", "Preparing ...", "Please Wait", "Done"
                );

                updateMessage("Checking Version");
                for (int i = 0; i < loadings.size(); i++) {
                    Thread.sleep(400);
                    updateProgress(i + 1, loadings.size());
                    String nextFriend = loadings.get(i);
                    titles.add(nextFriend);
                    updateMessage("Chess Bot 1.0.0 ...  " + nextFriend);
                }

                Thread.sleep(400);

                return titles;
            }
        };

        showSplash(initStage, friendTask, () -> showMainStage());
        new Thread(friendTask).start();
    }
    
    private void showMainStage() {        
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("Chess Bot");
        mainStage.setResizable(false);
        mainStage.setAlwaysOnTop(true);
        mainStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        mainStage.getIcons().add(new Image(APPLICATION_ICON));
        mainStage.setScene(new Scene(PageResources.getRoot(PagePath.PATH_SCREEN_APPLICATION), 208, 491));
        mainStage.show();
    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }

    public interface InitCompletionHandler {

        public void complete();
    }
}
