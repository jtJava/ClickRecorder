package me.iowa.clickrecorder;

import com.jpro.webapi.JProApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClickRecorder extends JProApplication {

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));

        Scene scene = null;
        try
        {
            Parent root = loader.load();

            //create JavaFX scene
            scene = new Scene(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        stage.setTitle("Hello jpro!");
        stage.setScene(scene);

        //open JavaFX window
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
