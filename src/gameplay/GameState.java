package gameplay;

import players.Player;

/**
 * Created by liamkreiss on 12/11/18.
 */
public class GameState {
    private boolean gameOver;
    private Player winner;

    public GameState() {
        this.gameOver = false;
        this.winner = null;
    }

    public void gameOver(Player winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
