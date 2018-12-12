package mains;

import gameplay.Game;
import gameplay.GameType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.HardComputer;
import players.Player;

import java.io.FileNotFoundException;
import java.util.Random;

public class TextGame extends Application {

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

        Game game = new Game(GameType.TEXT_GAME, false);

//        PositionEvaluator pe = new PositionEvaluator("all_black_board.txt");
//        PositionEvaluator pe2 = new PositionEvaluator("all_black_board.txt", true);
//        PositionEvaluator pe3 = new PositionEvaluator("all_black_board.txt", false);

//        double[][] startingWeights = new double[][]{
//                {+0.601, +0.245, -0.693, +0.623, -0.932, -0.330, -0.273, -0.164, -0.749, +0.281, -1.349,
//                        +0.386, -0.675, -0.485, +0.355, +1.237, -0.377, +0.489, -1.031, -0.126, +1.044,
//                        +1.114, -0.407, +0.250, +0.313, +0.143, +0.535, -0.235, -1.832, +0.539},
//                {+0.522, +0.199, -0.892, +0.517, -0.932, -0.397, -0.776, -0.164, -0.839, +0.493, -1.247,
//                        +0.106, -0.880, -0.485, +0.304, +1.237, -0.264, +0.489, -1.031, -0.282, +0.832,
//                        +1.124, -0.407, +0.248, +0.371, +0.143, +0.462, -0.139, -1.449, +0.539}};
//
//        GeneticLearning gs = new GeneticLearning(20, 50, startingWeights);
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
        Game game = new Game(GameType.TEXT_GAME, p1, p2);
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
