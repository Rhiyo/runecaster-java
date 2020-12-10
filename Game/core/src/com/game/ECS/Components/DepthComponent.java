package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 25/04/2015.
 *
 * This component should only exist with a position component.
 *
 * This component is the offset from a PositionComponent and used to sort rendering.
 * So if Y is lower, make object appear in front, if higher, make it appear behind via sorting.
 *
 */
public class DepthComponent extends Component {
    public float y; //Offset from position component

    public DepthComponent(float y){
        this.y = y;
    }
}
