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

        double[] weights = new double[]{1.141, -0.544, 1.932, 1.170, -0.620, 0.470, -1.112, 0.140,
                1.414, 0.509, -0.129, -0.098, 0.346, -0.587, -0.513, 0.582, -0.440, -0.393, 0.402, -0.093,
                0.637, 0.978, 0.541, 0.157, 1.508, -0.296, -0.083, 0.890, 0.477, -0.133};

        Board board = new Board(new Player[] {new HardComputer(true), new HardComputer(false)});
        createHeatChart(theStage, theScene, gc, weights, board);
    }

    private void createHeatChart(Stage theStage, Scene theScene, GraphicsContext gc, double[] weights, Board board) {

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        int stepSizeX = CANVAS_WIDTH / Board.BOARD_SIZE;
        int stepSizeY = CANVAS_HEIGHT / Board.BOARD_SIZE;
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                gc.setFill(Color.BLACK);
                gc.fillRect(i * stepSizeX, j * stepSizeY, stepSizeX, stepSizeY);
                gc.setFill(getColor(board, weights, i, j, 2));
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
