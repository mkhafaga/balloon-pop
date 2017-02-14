package com.nerdyapps.balloonpop.listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamedkhafaga on 2/11/17.
 */

public class GameStatePublisher {
    List<GameListener> gameListeners;

    public GameStatePublisher() {
        gameListeners = new ArrayList<>();
    }

    public void addGameListener(GameListener gameListener){
        gameListeners.add(gameListener);
    }
    public void fireGameOver() {
        for (int i = 0; i < gameListeners.size(); i++) {
            gameListeners.get(i).gameOver();
        }
    }

    public void fireScoreUpdated(int score) {
        for (int i = 0; i < gameListeners.size(); i++) {
            gameListeners.get(i).scoreUpdated(score);
        }
    }

    public void fireLevelUpdated(int level) {
        for (int i = 0; i < gameListeners.size(); i++) {
            gameListeners.get(i).levelUpdated(level);
        }
    }

    public void fireLevelFinished(int level) {
        for (int i = 0; i < gameListeners.size(); i++) {
            gameListeners.get(i).levelFinished(level);
        }
    }
}
