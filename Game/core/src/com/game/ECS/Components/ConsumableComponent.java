package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 14/06/2015.
 */
public class ConsumableComponent extends Component {
    public static enum ConsumeType{
        Health, Ink, Life
    }

    public ConsumeType type;
    public float amount = 0;

    public ConsumableComponent(ConsumeType type, float amount){
        this.type = type;
        this.amount = amount;
    }
}
