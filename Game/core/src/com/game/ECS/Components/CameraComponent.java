package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 26/04/2015.
 */
public class CameraComponent extends Component {
    public Vector2 currentOffset = new Vector2();
    public Vector2 offset = new Vector2(); //offet from world position
    public Vector2 currentTarget = new Vector2();
    public OrthographicCamera camera;
}
