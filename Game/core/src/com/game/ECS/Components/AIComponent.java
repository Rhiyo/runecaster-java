package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 3/05/2015.
 *
 * When on an entity, it defines it as AI
 *
 */
public class AIComponent extends Component{
    public static enum AIState{
        FOLLOWING, ATTACKING, RETREATING, HURT
    }


    public float retreatTime = 0;//How long AI retreats for
    public AIState state = AIState.FOLLOWING;

}
