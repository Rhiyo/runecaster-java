package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 28/04/2015.
 */
public class ProjectileComponent extends Component {
    public Entity owner;
    public Vector2 dir;
    public float speed;
}
