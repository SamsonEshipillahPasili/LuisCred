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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@SpringBootApplication
public class CreditAnalysisToolApplication extends Application {
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        if(isServerRunning()){
            launchBrowser();
            Platform.exit();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(CreditAnalysisToolApplication.class.getResource("fxml/splash.fxml"));
            Parent rootNode = fxmlLoader.load();
            primaryStage.setScene(new Scene(rootNode));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

            new Thread(() -> {
                SpringApplication.run(CreditAnalysisToolApplication.class);
                Platform.runLater(primaryStage::hide);
                launchBrowser();
            }).start();
        }
    }

    private boolean isServerRunning(){
        try {
            URL url = new URL("http", "localhost", 8081,"log_in.html");
            URLConnection urlConnection = url.openConnection();
            int contentLength = urlConnection.getContentLength();
            return contentLength > -1;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Test URL is misconfigured!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Test URL is misconfigured!");
        }
    }


    private void launchBrowser(){
        try {
            new ProcessBuilder("x-www-browser", "http://localhost:8081/").start();
        } catch (IOException e) {
            e.printStackTrace();
            getHostServices().showDocument("http://localhost:8081");
        }
    }

}