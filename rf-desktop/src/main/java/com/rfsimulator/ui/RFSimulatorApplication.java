package com.rfsimulator.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class RFSimulatorApplication extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("RF Simulator");
        StackPane root = new StackPane(title);
        Scene scene = new Scene(root, 200, 200);

        stage.setScene(scene);
        stage.setTitle("RF Simulator");
        stage.show();
    }
}
