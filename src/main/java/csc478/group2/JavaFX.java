package csc478.group2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFX extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello JavaFX!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("CSC478 Scrabble App");
        stage.setScene(scene);
        stage.show();
    }
}
