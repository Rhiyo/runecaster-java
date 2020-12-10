package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 27/04/2015.
 *
 * Animation State
 *
 */
public class StateComponent extends Component{

    public static enum State{
        STILL, WALK
    }

    public State state = State.STILL;
    public float time = 0.0f;
}
