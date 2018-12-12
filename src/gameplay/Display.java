package gameplay;

import board.Tile;
import gameplay.Game;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import players.ComputerPlayer;
import players.Player;

import java.util.HashMap;

/**
 * Created by liamkreiss on 12/11/18.
 */
public class Display {
    private final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;
    private final Color BACKGROUND_COLOR = Color.GRAY;
    private final Color BOARD_COLOR = Color.BLACK;
    private final Color TILE_COLOR = Color.LIGHTBLUE;

    private int BOARD_SIZE;
    private int SQUARE_SIZE;
    private int PIECE_RADIUS;
    private int OUTLINE_RADIUS;
    private int INLINE_RADIUS;

    private Rectangle[][] squares;
    private Rectangle[][] squareInlines;
    private Rectangle background;
    private Rectangle[] buttons;
    private Circle[][] gamePieces;
    private HashMap<String, Boolean> gameInfo= new HashMap<String, Boolean>();


//    private int BORDER_OUTLINE_RADIUS = OUTLINE_RADIUS + 1;
//    private int UPPER_LEFT_BOARD_X = (CANVAS_WIDTH / 2) - (2 * SQUARE_SIZE), UPPER_LEFT_BOARD_Y = (CANVAS_HEIGHT / 2) - (2 * SQUARE_SIZE);
//    private int CENTER_X_RED = UPPER_LEFT_BOARD_X + SQUARE_SIZE, CENTER_Y_RED = UPPER_LEFT_BOARD_Y - SQUARE_SIZE;
//    private int CENTER_X_BLUE = CENTER_X_RED + (2 * SQUARE_SIZE), CENTER_Y_BLUE = CENTER_Y_RED;
//    private int PLAY_AGAIN_BUTTON_WIDTH = 50, PLAY_AGAIN_BUTTON_HEIGHT = 30, BUTTON_DIST_BELOW_ORIG_TEXT_Y = 60, DIST_BETWEEN_PLAY_AGAIN_BUTTONS = 10;
//    private int ORIG_TEXT_X = (UPPER_LEFT_BOARD_X - (2 * PLAY_AGAIN_BUTTON_WIDTH) - DIST_BETWEEN_PLAY_AGAIN_BUTTONS) / 2, ORIG_TEXT_Y = UPPER_LEFT_BOARD_Y + 5, TEXT_STEP_SIZE = 15;
//    private int MENU_BUTTON_WIDTH = 100, MENU_BUTTON_HEIGHT = 100, UPPER_BUTTON_Y = 100, DIST_BETWEEN_DIFFICULTY_BUTTONS = 20;

    private Stage theStage;
    private Game game;
    private Player[] players;
    private boolean computerWaitsForBoardClickToMove = true;

    public Display(Stage primaryStage) throws Exception {
        this.theStage = primaryStage;
        theStage.setTitle("iReverse");
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //change when we improve the gui to ask user who goes first, who to play
        this.game = new Game(GameType.VISUAL_GAME, false);
        this.players = game.getPlayers();

        initializeSizes();

        createBoard();

        boardScreen(theStage, theScene, gc, game);
    }

    private void createBoard() {
        int x, y;
        Rectangle r;
        Circle c;
        squares = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        squareInlines = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        gamePieces = new Circle[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                x = SQUARE_SIZE * (i + 1);
                y = SQUARE_SIZE * (j + 1);
                r = new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                r.setFill(BOARD_COLOR);
                squares[j][i] = r;
                c = new Circle(x + (SQUARE_SIZE / 2), y + (SQUARE_SIZE / 2), PIECE_RADIUS);
                c.setFill(BOARD_COLOR);
                gamePieces[j][i] = c;
            }
        }

        background = new Rectangle(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        background.setFill(BACKGROUND_COLOR);
    }

    private void initializeSizes() {
        this.BOARD_SIZE = game.getBoardSize();
        this.SQUARE_SIZE = CANVAS_WIDTH / 10;
        this.PIECE_RADIUS = (SQUARE_SIZE - 4) / 2;
        this.OUTLINE_RADIUS = PIECE_RADIUS + 5;
        this.INLINE_RADIUS = PIECE_RADIUS - 10;
    }

    public void boardScreen(Stage theStage, Scene theScene, GraphicsContext gc, Game game) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        drawBoard(gc, game);
        theStage.show();

        if ((game.getP1Turn() && players[0] instanceof ComputerPlayer) ||  (!game.getP1Turn() && players[1] instanceof ComputerPlayer)) {
            if (computerWaitsForBoardClickToMove) {
                HashMap<String, Boolean> clickInfo = new HashMap<>();
                clickInfo.put("clicked", false);
                clickInfo.put("clickedOnBoard", false);

                theScene.setOnMouseClicked(
                        e -> {
                            //by checking the main menu button first, we can make a rectangle that covers the entire canvas and not worry about the click registering as both a screen click and a main menu click
                            if (background.contains(e.getX(), e.getY())) {
                                clickInfo.put("clicked", true);
                                clickInfo.put("clickedOnBoard", true);
                            }

                            if (clickInfo.get("clicked")) {
                                if (clickInfo.get("clickedOnBoard")) {
                                    int moveIndex = game.getP1Turn() ? ((ComputerPlayer) players[0]).getMove(game.getBoard()) : ((ComputerPlayer) players[1]).getMove(game.getBoard());
                                    GameState gs = game.makeMove(moveIndex);
                                    if (gs.isGameOver()) {
                                        gameOverScreen(theStage, theScene, gc, game, gs);
                                    } else {
                                        boardScreen(theStage, theScene, gc, game);
                                    }
                                }
                            }
                        }
                );
            } else {
                //disable clicking
                theScene.setOnMouseClicked(
                        e -> {
                        }
                );
                int moveIndex = gameInfo.get("p1Turn") ? ((ComputerPlayer) players[0]).getMove(game.getBoard()) : ((ComputerPlayer) players[1]).getMove(game.getBoard());
                GameState gs = game.makeMove(moveIndex);
                if (gs.isGameOver()) {
                    gameOverScreen(theStage, theScene, gc, game, gs);
                } else {
                    boardScreen(theStage, theScene, gc, game);
                }
            }
        } else {
            HashMap<String, Boolean> clickInfo = new HashMap<>();
            clickInfo.put("clicked", false);
            clickInfo.put("clickedOnSquare", false);
            clickInfo.put("clickedMainMenu", false);
            final int[] indexClickedOn = {-1};

            theScene.setOnMouseClicked(
                    e -> {
                        for (int i = 0; i < BOARD_SIZE; i++) {
                            for (int j = 0; j < BOARD_SIZE; j++) {
                                if (squares[i][j].contains(e.getX(), e.getY())) {
                                    clickInfo.put("clicked", true);
                                    clickInfo.put("clickedOnSquare", true);
                                    clickInfo.put("clickedMainMenu", false);
                                    indexClickedOn[0] = game.getBoard().getIndexFromRowCol(i, j);
                                }
                            }
                        }

                        if (clickInfo.get("clicked")) {
                            if (clickInfo.get("clickedMainMenu")) {
//                                pregameScreen(theStage, theScene, gc, new Player[3], output);
                            } else if (clickInfo.get("clickedOnSquare")) {
                                GameState gs = game.makeMove(indexClickedOn[0]);
                                if (gs != null) {
                                    if (gs.isGameOver()) {
                                        gameOverScreen(theStage, theScene, gc, game, gs);
                                    } else {
                                        boardScreen(theStage, theScene, gc, game);
                                    }
                                }
                            }
                        }
                    }
            );
        }

        theStage.show();
    }

    private void gameOverScreen(Stage theStage, Scene theScene, GraphicsContext gc, Game game, GameState gs) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill(background.getFill());
        gc.fillRect(background.getX(), background.getY(), background.getWidth(), background.getHeight());
    }

    private void drawBoard(GraphicsContext gc, Game game) {
        //makes background
        gc.setFill(background.getFill());
        gc.fillRect(background.getX(), background.getY(), background.getWidth(), background.getHeight());

        //makes squares
        for (Rectangle[] row : squares) {
            for (Rectangle square : row) {
                gc.setFill(square.getFill());
                gc.fillRect(square.getX(), square.getY(), square.getWidth(), square.getHeight());
                gc.setFill(TILE_COLOR);
                gc.fillRect(square.getX() + 1, square.getY() + 1, square.getWidth() - 2, square.getHeight() - 2);
            }
        }

        //makes pieces
        Tile t;
        Circle circle;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                t = game.getTiles()[i][j];
                if (t.getOccupied()) {
                    circle = gamePieces[i][j];
                    gc.setFill(t.getOwner().getColor());
                    gc.fillOval(circle.getCenterX() - circle.getRadius(), circle.getCenterY() - circle.getRadius(),
                            circle.getRadius() * 2, circle.getRadius() * 2);
                }
            }
        }
    }
}
