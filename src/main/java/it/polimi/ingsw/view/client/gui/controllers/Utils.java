package it.polimi.ingsw.view.client.gui.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

//TODO: find a better name for this class

public class Utils {
    public static void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you really want to quit?");

        Optional<ButtonType> opt = alert.showAndWait();
        if (opt.isPresent() && opt.get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }
}