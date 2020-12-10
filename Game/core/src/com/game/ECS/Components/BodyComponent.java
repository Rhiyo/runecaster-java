package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Sean on 25/04/2015.
 *
 * A Box2D body for collision and movement
 *
 */
public class BodyComponent extends Component {
    public Body body;
    public Vector2 offset = new Vector2(); //The offset from the PositionComponent

    public BodyComponent(){}
    public BodyComponent(Body body){
        this.body = body;
    }
}
