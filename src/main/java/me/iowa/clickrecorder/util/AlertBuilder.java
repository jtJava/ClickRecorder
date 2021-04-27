package me.iowa.clickrecorder.util;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;


public class AlertBuilder {

    private JFXButton originalButton;
    private JFXButton closeButton;
    private JFXDialogLayout layout;
    private JFXAlert alert;


    public AlertBuilder(JFXButton originalButton) {
        this.originalButton = originalButton;
        this.closeButton = new JFXButton();

        closeButton.setOnAction(event -> {
            alert.hideWithAnimation();
        });

        alert = new JFXAlert((Stage) originalButton.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        layout = new JFXDialogLayout();
    }

    public AlertBuilder closeButtonText(String text) {
        closeButton.setText(text);
        return this;
    }

    public AlertBuilder title(String title) {
        layout.setHeading(new Label(title));
        return this;
    }

    public AlertBuilder body(String body) {
        layout.setBody(new Label(body));
        return this;
    }

    public AlertBuilder overlayClose(boolean close) {
        alert.setOverlayClose(close);
        return this;
    }

    public AlertBuilder css(String url) {
        layout.getStylesheets().add(url);
        return this;
    }

    public void build() {
        this.originalButton.setOnAction(action -> {
            layout.setActions(closeButton);
            alert.setContent(layout);
            alert.show();
        });
    }
}
