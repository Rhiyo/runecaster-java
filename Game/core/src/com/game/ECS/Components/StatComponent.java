package com.game.ECS.Components;

import java.awt.Component;

/**
 * Created by rhiyo on 12/10/2015.
 *
 * Stats of the entity
 *
 */
public class StatComponent extends Component {
    //Spell Damage and Max Health
    public int strength;

    //Health Regen and Projectile speed
    public int force;

    //How far away a player can see when aiming - how far until an enemy sees you size of AOE
    public int perception;

    //Speed of player, how long an effect lasts for
    public int agility;
}
