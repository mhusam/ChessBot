package com.jmco.utils;

import java.awt.Insets;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Husam
 * @since 1.0.0
 * @datetime Jun 30, 2017 4:21:33 PM
 */
public class UIUtils {

    public static void setAutoScroll(TextArea component) {
        //StringProperty textRecu = new SimpleStringProperty();
        //component.textProperty().bind(textRecu);

        component.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
//                component.selectPositionCaret(component.getLength());
//                component.deselect();
                component.setScrollLeft(Double.MIN_VALUE);
                component.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    public static void popupError(Exception e) {
//        try{
//            Notifications.create()
//                .text(String.valueOf(e) + " ")
//                .position(Pos.BOTTOM_CENTER)
//                .hideAfter(Duration.seconds(30))
//                .darkStyle()
//                .show();
//        } catch(Exception er){
//            er.printStackTrace();
//        }
    }

    public static void popupError(Exception e, String title) {
//        try{
//            Notifications.create()
//                .title(String.valueOf(title) + " ")
//                .text(String.valueOf(e) + " ")
//                .position(Pos.BOTTOM_CENTER)
//                .hideAfter(Duration.seconds(30))
//                .darkStyle()
//                .show();
//        } catch(Exception er){
//            er.printStackTrace();
//        }
    }

    public static void popup(String e, String title) {
//        try{
//            Notifications.create()
//                .title(String.valueOf(title) + " ")
//                .text(String.valueOf(e) + " ")
//                .position(Pos.BOTTOM_CENTER)
//                .hideAfter(Duration.seconds(30))
//                .darkStyle()
//                .show();
//        } catch(Exception er){
//            er.printStackTrace();
//        }
    }

    public static void alwaysInTop(Alert alert) {
        try{
            DialogPane root = alert.getDialogPane();

            Stage dialogStage = new Stage(StageStyle.UTILITY);

            for (ButtonType buttonType : root.getButtonTypes()) {
                ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
                button.setOnAction(evt -> {
                    root.setUserData(buttonType);
                    dialogStage.close();
                });
            }

            root.getScene().setRoot(new Group());

            Scene scene = new Scene(root);

            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setAlwaysOnTop(true);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
        }catch(Exception e){
            
        }
//        Optional<ButtonType> result = Optional.ofNullable((ButtonType) root.getUserData());
    }
}
