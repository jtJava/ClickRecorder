package me.iowa.clickrecorder.fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.iowa.clickrecorder.Data;
import me.iowa.clickrecorder.util.AlertBuilder;

import java.io.File;
import java.io.IOException;

public class Controller {

    private final Stage stage;

    private final Data data = new Data();

    @FXML
    public JFXButton clickRecorderButton, saveAsButton, copyButton, infoButton;

    @FXML
    public JFXSpinner spinner;

    public Controller() {
        this.stage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            this.stage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            this.stage.setTitle("Click Recorder");

        } catch (IOException e) {
            e.printStackTrace();
        }

        AlertBuilder builder = new AlertBuilder(infoButton);
        builder.body("Click in the \"Record Click\" box until the percentage below reaches 100%.\n" +
                "Press \"Save As\" to save the data set.\n" +
                "Press \"Copy\" to copy the data set to your clipboard.")
                .css("css/dialog-layout.css")
                .title("Information")
                .closeButtonText("Okay")
                .overlayClose(false)
                .build();
    }

    public void showStage() {
        this.stage.showAndWait();
    }


    // injection
    @FXML
    private void initialize() {
        clickRecorderButton.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mouseDown();
            }

        });
        clickRecorderButton.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mouseUp();
            }
        });

        saveAsButton.setOnMouseReleased(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("java.io.tmpdir")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.csv"));
            fileChooser.setInitialFileName("data.csv");
            File saveLocation = fileChooser.showSaveDialog(saveAsButton.getScene().getWindow());
            data.exportData(saveLocation);
        });
        saveAsButton.setDisable(true);

        copyButton.setOnMouseReleased(event -> {
            data.exportData(null);
        });
        copyButton.setDisable(true);

        spinner.getStylesheets().add("css/spinner.css");
    }

    public void mouseUp() {
        data.recordUpTime(System.currentTimeMillis());
    }

    public void mouseDown() {
        if (data.isTestInProgress()) {
            data.recordDownTime(System.currentTimeMillis());
        } else {
            data.recordDownTime(System.currentTimeMillis());
            data.setTestInProgress(true);
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new KeyValue(spinner.progressProperty(), 0)
                    ),
                    new KeyFrame(
                            Duration.seconds(10),
                            new KeyValue(spinner.progressProperty(), 1)
                    )
            );
            timeline.play();
            timeline.setOnFinished(event -> {
                data.setTestInProgress(false);
                this.clickRecorderButton.setDisable(true);
                this.clickRecorderButton.setText("Finished");
                this.saveAsButton.setDisable(false);
                this.copyButton.setDisable(false);
            });
        }
    }
}
