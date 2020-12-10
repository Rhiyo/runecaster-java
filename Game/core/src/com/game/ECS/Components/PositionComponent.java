package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 25/04/2015.
 *
 * A position of the object in the game world.
 *
 * Sprites are centred on this
 *
 */
public class PositionComponent extends Component {
    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}
