package com.credit.reports.controllers;

import com.jfoenix.controls.JFXSpinner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SplashController implements Initializable {
    @FXML
    private JFXSpinner spinner;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Runnable runnable = () -> {
        Runnable spinnerUpdater = () -> {
            int length = SplashController.this.byteArrayOutputStream.toString().split("\n").length;
            SplashController.this.spinner.setProgress((double)length / 70.0D);
        };
        Thread thread = new Thread(spinnerUpdater);
        thread.setName("Spinner Updater");

        do {
            try {
                Thread.sleep(500L);
                Platform.runLater(thread);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        } while(this.spinner.getProgress() < 1.0D);

    };

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.byteArrayOutputStream));
        Thread thread = new Thread(this.runnable);
        thread.setName("Init Spinner Updater");
        thread.start();
    }
}
