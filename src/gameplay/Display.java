package gameplay;

import board.Board;
import board.Tile;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import libraries.Alignment;
import libraries.StringAlignUtils;
import players.*;

import java.util.HashMap;

/**
 * Created by liamkreiss on 12/11/18.
 */
public class Display {
    private final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;
    private final int NUMBER_OF_PLAYER_TYPES = 4;
    private final Difficulty[] PLAYER_TYPES = new Difficulty[]{Difficulty.HUMAN, Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
    private final Color BACKGROUND_COLOR = Color.GRAY;
    private final Color BOARD_COLOR = Color.BLACK;
    private final Color TILE_COLOR = Color.LIGHTBLUE;
    private final Color BUTTON_COLOR = Color.LIGHTBLUE;
    private final Color HIGHLIGHTED_COLOR = Color.YELLOW;
    private final Color TEXT_COLOR = Color.BLACK;

    private int BOARD_SIZE;
    private int SQUARE_SIZE;
    private int PIECE_RADIUS;
    private int BUTTON_WIDTH;
    private int BUTTON_HEIGHT;

    private Rectangle[][] tiles;
    private Rectangle[] menuButtons;
    private Rectangle background;
    private Circle[][] gamePieces;
    private HashMap<String, Boolean> gameInfo = new HashMap<String, Boolean>();


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

        initializeSizes(Board.BOARD_SIZE);

        createMenu();

        mainMenuScreen(theStage, theScene, gc);

    }

    //button y starting point is (1/7 + 3/7y) of screen
    //button x starting point is (1/16 + 5/16x) of screen
    private void createMenu() {
        background = new Rectangle(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        background.setFill(BACKGROUND_COLOR);

        int x, y;
        Rectangle r;
        menuButtons = new Rectangle[NUMBER_OF_PLAYER_TYPES * 2];
        for (int i = 0; i < NUMBER_OF_PLAYER_TYPES * 2; i++) {
            y = (((i / NUMBER_OF_PLAYER_TYPES) * 3) + 1) * (CANVAS_HEIGHT / 7);
            x = (((i % NUMBER_OF_PLAYER_TYPES) * 5) + 1) * (CANVAS_WIDTH / (5 * NUMBER_OF_PLAYER_TYPES + 1));
            r = new Rectangle(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            r.setFill(BUTTON_COLOR);
            menuButtons[i] = r;
        }
    }

    private void createBoard() {
        int x, y;
        Rectangle r;
        Circle c;
        tiles = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        gamePieces = new Circle[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                x = SQUARE_SIZE * (i + 1);
                y = SQUARE_SIZE * (j + 1);
                r = new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE);
                r.setFill(BOARD_COLOR);
                tiles[j][i] = r;
                c = new Circle(x + (SQUARE_SIZE / 2), y + (SQUARE_SIZE / 2), PIECE_RADIUS);
                c.setFill(BOARD_COLOR);
                gamePieces[j][i] = c;
            }
        }
    }

    private void initializeSizes(int boardSize) {
        this.BOARD_SIZE = boardSize;
        this.SQUARE_SIZE = CANVAS_WIDTH / 10;
        this.PIECE_RADIUS = (SQUARE_SIZE - 4) / 2;
        this.BUTTON_WIDTH = CANVAS_WIDTH / (NUMBER_OF_PLAYER_TYPES + 1);
        this.BUTTON_HEIGHT = CANVAS_HEIGHT / 4;
    }

    private void mainMenuScreen(Stage theStage, Scene theScene, GraphicsContext gc) {
        Player[] possiblePlayers = new Player[2];
        possiblePlayers[0] = null;
        possiblePlayers[1] = null;
        drawMainMenu(gc, possiblePlayers);
        theStage.show();

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnButton", false);
        int[] buttonClickedOn = new int[1];

        theScene.setOnMouseClicked(
                e -> {
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        if (menuButtons[i].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnButton", true);
                            buttonClickedOn[0] = i;
                        }
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnButton")) {
                            Player newPlayer;
                            boolean playerGoesFirst = buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES == 0 ? true : false;
                            if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HUMAN.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new HumanPlayer(playerGoesFirst);
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.EASY.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new EasyComputer(playerGoesFirst);
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.MEDIUM.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new MediumComputer(playerGoesFirst);
                            } else if ((buttonClickedOn[0] % NUMBER_OF_PLAYER_TYPES) == Difficulty.HARD.getNumber()) {
                                possiblePlayers[buttonClickedOn[0] / NUMBER_OF_PLAYER_TYPES] = new HardComputer(playerGoesFirst);
                            } else {
                                throw new Error("illegal click");
                            }
                            if (possiblePlayers[0] != null && possiblePlayers[1] != null) {
                                this.game = new Game(GameType.VISUAL_GAME, possiblePlayers[0], possiblePlayers[1]);
                                this.players = game.getPlayers();
                                createBoard();
                                boardScreen(theStage, theScene, gc, game);
                            } else {
                                drawMainMenu(gc, possiblePlayers);
                            }
                        }
                    }
                }
        );


//
    }

    public void boardScreen(Stage theStage, Scene theScene, GraphicsContext gc, Game game) {
        drawBoard(gc, game);
        drawScore(gc, game, "CURRENT");
        theStage.show();

        if ((game.getP1Turn() && players[0] instanceof ComputerPlayer) || (!game.getP1Turn() && players[1] instanceof ComputerPlayer)) {
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
                                if (tiles[i][j].contains(e.getX(), e.getY())) {
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
    }

    private void gameOverScreen(Stage theStage, Scene theScene, GraphicsContext gc, Game game, GameState gs) {
        drawBackground(gc);
        drawBoard(gc, game);
        drawScore(gc, game, "FINAL");
        drawWinner(gc, gs);

        //make clicking close the window
        theScene.setOnMouseClicked(
                e -> {
                    theStage.close();
                }
        );

    }

    private void drawMainMenu(GraphicsContext gc, Player[] players) {
        drawBackground(gc);
        gc.setFill(TEXT_COLOR);
        for (int i = 1; i <= 2; i++) {
            gc.setFill(TEXT_COLOR);
            gc.fillText(String.format("Who is player %d?", i), (CANVAS_WIDTH / 2) - 50, (CANVAS_HEIGHT / 14) * ((6 * i) - 5));
        }
        Rectangle button;
        StringAlignUtils util = new StringAlignUtils(20, Alignment.CENTER);
        for (int i = 0; i < NUMBER_OF_PLAYER_TYPES * 2; i++) {
            button = menuButtons[i];
            gc.setFill(getMenuButtonColor(i, button, players));
            gc.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            gc.setFill(TEXT_COLOR);
            gc.fillText(util.format(String.format("%s", PLAYER_TYPES[i % NUMBER_OF_PLAYER_TYPES].toString())), button.getX() + 10, button.getY() + 50);
        }
    }

    private Paint getMenuButtonColor(int i, Rectangle button, Player[] players) {
        Player player = players[i / NUMBER_OF_PLAYER_TYPES];
        if (player == null) {
            return button.getFill();
        } else {
            if (player instanceof HumanPlayer) {
                if (Difficulty.HUMAN.getNumber() == (i % NUMBER_OF_PLAYER_TYPES)) {
                    return HIGHLIGHTED_COLOR;
                }
            } else if (player instanceof EasyComputer) {
                if (Difficulty.EASY.getNumber() == i % NUMBER_OF_PLAYER_TYPES) {
                    return HIGHLIGHTED_COLOR;
                }
            } else if (player instanceof MediumComputer) {
                if (Difficulty.MEDIUM.getNumber() == i % NUMBER_OF_PLAYER_TYPES) {
                    return HIGHLIGHTED_COLOR;
                }
            } else if (player instanceof HardComputer) {
                if (Difficulty.HARD.getNumber() == i % NUMBER_OF_PLAYER_TYPES) {
                    return HIGHLIGHTED_COLOR;
                }
            }
        }
        return button.getFill();
    }

    private void drawWinner(GraphicsContext gc, GameState gs) {
        //write winner
        gc.setFill(TEXT_COLOR);
        String winner;
        if (gs.getWinner() == null) {
            winner = String.format("It's a Tie!");
        } else {
            winner = String.format("%s wins!", gs.getWinner().getName());
        }
        gc.fillText(winner, SQUARE_SIZE * 2.5, 3 * SQUARE_SIZE / 4);
    }

    private void drawBoard(GraphicsContext gc, Game game) {
        //makes background
        drawBackground(gc);

        //makes tiles
        Rectangle square;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                square = tiles[i][j];
                gc.setFill(square.getFill());
                gc.fillRect(square.getX(), square.getY(), square.getWidth(), square.getHeight());
                if (game.getActivePlayer() instanceof HumanPlayer && game.getBoard().getValidMoves(game.getActivePlayer()).contains(game.getBoard().getIndexFromRowCol(i, j))) {
                    gc.setFill(HIGHLIGHTED_COLOR);
                } else {
                    gc.setFill(TILE_COLOR);
                }
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

    private void drawScore(GraphicsContext gc, Game game, String scoreType) {
        //write score
        int[] pieceCounts = game.getBoard().getPieceCounts();
        gc.setFill(TEXT_COLOR);
        String score = String.format("%s SCORE: Black [%d] - [%d] White\n\n", scoreType.toUpperCase(), pieceCounts[0], pieceCounts[1]);
        gc.fillText(score, SQUARE_SIZE * 2.5, SQUARE_SIZE / 2);
    }

    private void drawBackground(GraphicsContext gc) {
        //clears background
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        //makes background
        gc.setFill(background.getFill());
        gc.fillRect(background.getX(), background.getY(), background.getWidth(), background.getHeight());
    }
}
