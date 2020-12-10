package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Sean on 6/05/2015.
 *
 * The health a creature.
 *
 */
public class HealthComponent extends Component {
    public float maxHealth = 5f;
    public float currentHealth = 5f;

    public HealthComponent(){}
    public HealthComponent(float maxHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
}
