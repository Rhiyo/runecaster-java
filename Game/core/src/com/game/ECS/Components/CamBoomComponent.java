package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 13/06/2015.
 *
 * Used to pan the camera for cinematic effect
 *
 */
public class CamBoomComponent extends Component {
    public float x = 0;
    public float y = 0;
    public CamBoomComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}
