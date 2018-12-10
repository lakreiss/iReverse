package mains;

import gameplay.Game;
import gameplay.PositionEvaluator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.HardComputer;
import players.Player;

import java.io.FileNotFoundException;
import java.util.Random;

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

//        Game game = new Game();
//        game.startGame();

        PositionEvaluator pe = new PositionEvaluator("all_black_board.txt");
        PositionEvaluator pe2 = new PositionEvaluator("all_black_board.txt", true);
        PositionEvaluator pe3 = new PositionEvaluator("all_black_board.txt", false);

//        GeneticLearning gs = new GeneticLearning(20, 100);
    }



    private static void oneVsOne(Random r) {
        double[] p1Weights = new double[30];
        double[] p2Weights = new double[30];
        for (int i = 0; i < 60; i++) {
            if (i < 30) {
                p1Weights[i] = (r.nextDouble() - 0.5) * 2;
            } else {
                p2Weights[i - 30] = (r.nextDouble() - 0.5) * 2;
            }
        }
        Player p1 = new HardComputer(true, p1Weights);
        Player p2 = new HardComputer(false, p2Weights);
        Game game = new Game(p1, p2);
        game.startGame();
    }

//    String letters = "ABCDEFGHIJ";
//    String s = "ABCDDCBABEFGGFEBCFHIIHFCDGIJJIGDDGIJJIGDCFHIIHFCBEFGGFEBABCDDCBA";
//        for (int i = 0; i < letters.length(); i++) {
//        System.out.print(letters.charAt(i) + ": ");
//        for (int j = 0; j < s.length(); j++) {
//            if (s.charAt(j) == letters.charAt(i)) {
//                System.out.print(j + ", ");
//            }
//        }
//        System.out.println();
//    }

    //code for gui
//    public static void main(String[] args) {
//        launch(args);
//    }
}
