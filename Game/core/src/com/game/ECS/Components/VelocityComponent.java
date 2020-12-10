package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 25/04/2015.
 *
 * The velocity at which a move-able object is moving
 *
 */
public class VelocityComponent extends Component{
    public Vector2 velocity = new Vector2(0, 0);

    public VelocityComponent(float x, float y){
        this.velocity.x = x;
        this.velocity.y = y;
    }
}
