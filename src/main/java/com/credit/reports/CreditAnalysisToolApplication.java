package com.credit.reports;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@SpringBootApplication
public class CreditAnalysisToolApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void init() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreditAnalysisToolApplication.class.getResource("fxml/splash.fxml"));
        rootNode = fxmlLoader.load();
        new Thread(() -> {
            SpringApplication.run(CreditAnalysisToolApplication.class);
            Platform.runLater(() -> primaryStage.hide());
            try {
                new ProcessBuilder("x-www-browser", "http://localhost:8081/").start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        // save the primary stage
        this.primaryStage = primaryStage;
        // set the scene of the primary stage and display it.
        this.primaryStage.setScene(new Scene(rootNode));
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
        this.primaryStage.show();
    }

}