package com.game.ECS.Tools;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sean on 26/04/2015.
 */
public class ResolutionHandler {
    public static final float getScale(){
        float scale = 1;

        if(Gdx.graphics.getHeight() == 1080){
            scale = 2;
        }
        if(Gdx.graphics.getHeight() == 1440){
            scale = 2.5f;
        }else if(Gdx.graphics.getHeight() != 480){
            scale = Gdx.graphics.getHeight()/480;
        }

        scale = Gdx.graphics.getHeight()/480;

        return scale;
    }
}
