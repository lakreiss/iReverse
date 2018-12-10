package mains;

import board.Board;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import players.HardComputer;
import players.Player;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class HeatChart extends Application {
    private static final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) throws Exception {
        theStage.setTitle("Heat Chart");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( CANVAS_WIDTH, CANVAS_HEIGHT );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

//        double[] weights = new double[]{
//                +0.462, -0.808, +0.568, +0.063, -1.620, +0.691, -0.474, +0.459, +0.468, +0.460,
//                -0.773, -0.681, -0.601, -0.852, +0.156, +0.660, +0.450, -0.336, -0.594, +0.246, -0.772,
//                +1.758, +0.137, -0.200, +0.773, +0.122, -0.840, +0.871, -0.039, +0.750};
        double[] weights = new double[]{
                +0.601, +0.245, -0.693, +0.623, -0.932, -0.330, -0.273, -0.164, -0.749, +0.281, -1.349,
                +0.386, -0.675, -0.485, +0.355, +1.237, -0.377, +0.489, -1.031, -0.126, +1.044, +1.114,
                -0.407, +0.250, +0.313, +0.143, +0.535, -0.235, -1.832, +0.539};

        int whichWeight = 0;
        Board board = new Board(new Player[] {new HardComputer(true), new HardComputer(false)});
        createHeatChart(theStage, theScene, gc, weights, board, whichWeight);
    }

    private void createHeatChart(Stage theStage, Scene theScene, GraphicsContext gc, double[] weights, Board board, int whichWeight) {

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        int stepSizeX = CANVAS_WIDTH / Board.BOARD_SIZE;
        int stepSizeY = CANVAS_HEIGHT / Board.BOARD_SIZE;
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                gc.setFill(Color.BLACK);
                gc.fillRect(i * stepSizeX, j * stepSizeY, stepSizeX, stepSizeY);
                gc.setFill(getColor(board, weights, i, j, whichWeight));
                gc.fillRect(i * stepSizeX + 1, j * stepSizeY + 1, stepSizeX - 2, stepSizeY - 2);
                gc.setFill(Color.BLACK);
                gc.fillText(String.format("%.3f", weights[board.getGameboard()[i][j].getType().getNumVal()]), i * stepSizeX + 10, 17 + j * stepSizeY);
                gc.fillText(String.format("%.3f", weights[Board.BOARD_SIZE + board.getGameboard()[i][j].getType().getNumVal()]), i * stepSizeX + 10, 34 + j * stepSizeY);
                gc.fillText(String.format("%.3f", weights[Board.BOARD_SIZE * 2 + board.getGameboard()[i][j].getType().getNumVal()]), i * stepSizeX + 10, 51 + j * stepSizeY);
            }
        }
        theStage.show();
    }

    private Color getColor(Board board, double[] weights, int i, int j, int whichWeight) {
        double weight = weights[(Board.BOARD_SIZE * whichWeight) + board.getGameboard()[i][j].getType().getNumVal()];

        if (weight > 1) {
            weight = 0.99;
        }
        if (weight < -1) {
            weight = -0.99;
        }
        double power = (weight / 2) + 0.5;

        double H = power * 110; // Hue (note 100 = Green)
        double S = 1; // Saturation
        double B = 1; // Brightness
        return Color.hsb(H, S, B);
//        return new Color(1 - power, power, 0, 1);

    }


}
