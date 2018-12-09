package sample;

import gameplay.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    //code for testing
    public static void main(String[] args) throws FileNotFoundException {
//        Game game = new Game("almost_full_board.txt");
        Game game = new Game();
        game.startGame();
    }

    //code for gui
//    public static void main(String[] args) {
//        launch(args);
//    }
}
