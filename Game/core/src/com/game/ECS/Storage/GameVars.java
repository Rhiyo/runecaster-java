package com.game.ECS.Storage;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sean on 25/04/2015.
 *
 * The variables that are (semi)constant, needed through out the game
 *
 */
public class GameVars {
    public static final float PTM = 32f; //Pixels to metres
    public static final float DAMPING = 9f; //The damping for all Box2D objects

    //For a cohesive look - the games desired resolution is 240p. 1 world unit is 32p 240/32=7.5
    public static final float VIRTUAL_HEIGHT = 7.5f;
    public static final float VIRTUAL_WIDTH = VIRTUAL_HEIGHT * Gdx.graphics.getWidth() /
            Gdx.graphics.getHeight();
}
