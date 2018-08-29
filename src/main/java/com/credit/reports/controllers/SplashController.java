package com.credit.reports.controllers;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private JFXSpinner spinner;

    private ByteArrayOutputStream byteArrayOutputStream;



    private Runnable runnable = () -> {
        Runnable spinnerUpdater = new Runnable() {
            @Override
            public void run() {
                int length = byteArrayOutputStream.toString().split("\n").length;
                spinner.setProgress(length / 70d);
            }
        };
        Thread thread = new Thread(spinnerUpdater);
        thread.setName("Spinner Updater");
        while(true) {

            try {
                Thread.sleep(500);
                Platform.runLater(thread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(spinner.getProgress() >= 1){
                break;
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        byteArrayOutputStream  =  new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        Thread thread = new Thread(runnable);
        thread.setName("Init Spinner Updater");
        thread.start();

    }

}
