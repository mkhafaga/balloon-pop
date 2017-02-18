package com.nerdyapps.balloonpop.utils;

/**
 * Created by mohamedkhafaga on 2/15/17.
 */

public class ScreenHelper {
    private static int screenWidth;
    private  static int screenHeight;

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        ScreenHelper.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        ScreenHelper.screenWidth = screenWidth;
    }

}
