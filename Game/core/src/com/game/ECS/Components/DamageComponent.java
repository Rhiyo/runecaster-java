package com.game.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;


/**
 * Created by Sean on 1/05/2015.
 *
 * Damage on Self
 *
 */
public class DamageComponent extends Component {
    public float damage = 0;

    //Damage animation related
    public int flashes = 0; //How many flashes the animation has done
    public float animTimer = 0; //Time til next flash
    public Color originalColor; //Store original color of sprite
    //List of who is currently damaging the player, and how long until they receive next damage
    //Todo make contact be a condition of dealing damage, not the cause
    public HashMap<Entity, Float> damageAmount = new HashMap<Entity, Float>();
    public HashMap<Entity, Float> damageTime = new HashMap<Entity, Float>();
    public DamageComponent(){}
    public DamageComponent(float amt){ this.damage = amt; }
}
