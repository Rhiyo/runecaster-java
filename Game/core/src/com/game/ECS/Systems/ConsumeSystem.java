package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.game.ECS.Components.ConsumableComponent;
import com.game.ECS.Components.HealthComponent;
import com.game.ECS.Components.InkComponent;
import com.game.ECS.Components.PlayerInputComponent;

/**
 * Created by Sean on 14/06/2015.
 *
 * Adding to health or ink.
 *
 */
public class ConsumeSystem extends EntitySystem {

    private Engine engine;
    private ImmutableArray<Entity> entities;

    private ComponentMapper<ConsumableComponent> cm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<InkComponent> im;

    public ConsumeSystem(){
        cm = ComponentMapper.getFor(ConsumableComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        im = ComponentMapper.getFor(InkComponent.class);
    }

    public void addedToEngine(Engine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(HealthComponent.class,
                ConsumableComponent.class,
                InkComponent.class).get());
    }


    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            ConsumableComponent consumable = cm.get(entities.get(i));

            if(consumable.type == ConsumableComponent.ConsumeType.Health){
                HealthComponent health = hm.get(entities.get(i));
                if(health.currentHealth + consumable.amount > health.maxHealth){
                    health.currentHealth = health.maxHealth;
                }else{
                    health.currentHealth += consumable.amount;
                }
            }else if(consumable.type == ConsumableComponent.ConsumeType.Ink){
                InkComponent ink = im.get(entities.get(i));
                if(ink.currentInk + consumable.amount > ink.maxInk){
                    ink.currentInk = ink.maxInk;
                }else{
                    ink.currentInk += consumable.amount;
                }
            }else if(consumable.type == ConsumableComponent.ConsumeType.Life){
                PlayerInputComponent playerInput = entities.get(i).getComponent(PlayerInputComponent.class);
                if(playerInput != null)
                    playerInput.lives+=1;
            }
            entities.get(i).remove(ConsumableComponent.class);
        }

    }
}
