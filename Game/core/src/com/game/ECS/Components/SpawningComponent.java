package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 25/04/2015.
 *
 * If an entity has this, they need to spawn
 *
 */
public class SpawningComponent extends Component{
    public float delay = 0f;
    public SpawningComponent(float delay){
        this.delay = delay;
    }
}
