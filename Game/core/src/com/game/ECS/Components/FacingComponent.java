package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 27/04/2015.
 *
 * Contains where the moving object is facing
 *
 */
public class FacingComponent extends Component {

    /**
     * UPDOWN: the entity can only face up and down
     * LEFTRIGHTUPDOWN: Can face all directions
     */
    public enum FacingType {
        UPDOWN, LEFTRIGHTUPDOWN
    }

    public enum Facing{
        LEFT, RIGHT, UP, DOWN
    }

    public Facing facing = Facing.DOWN;
    public FacingType type = FacingType.LEFTRIGHTUPDOWN;
    public Vector2 dir = new Vector2();
}
