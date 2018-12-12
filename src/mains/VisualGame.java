package mains;

import gameplay.Display;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by liamkreiss on 12/11/18.
 */
public class VisualGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Display display = new Display(primaryStage);
    }
}
