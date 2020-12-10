package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 7/05/2015.
 *
 * A entity that uses ink.
 *
 */
public class InkComponent extends Component {
    public float currentInk = 10;
    public float maxInk = 10;

    public InkComponent(){}
    public InkComponent(float maxInk){
        currentInk = maxInk;
        this.maxInk = maxInk;
    }
}
