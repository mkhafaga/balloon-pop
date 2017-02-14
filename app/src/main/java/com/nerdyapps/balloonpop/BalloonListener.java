package com.nerdyapps.balloonpop;

import com.nerdyapps.balloonpop.sprites.Balloon;

/**
 * Created by mohamedkhafaga on 2/4/17.
 */

public interface BalloonListener {
    public void balloonPopped(Balloon b, boolean userTouch);
}
