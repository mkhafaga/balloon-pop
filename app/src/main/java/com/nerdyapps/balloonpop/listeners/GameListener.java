package com.nerdyapps.balloonpop.listeners;

/**
 * Created by mohamedkhafaga on 2/11/17.
 */

public interface GameListener {
    public void gameOver();
    public void scoreUpdated(int score);
    public void levelUpdated(int level);
    public void levelFinished(int level);
}
