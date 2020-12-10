package com.game.ECS.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.game.ECS.Components.ConsumableComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Storage.B2DVars;

/**
 * Created by Sean on 28/04/2015.
 *
 * Check to see if a collision should happen
 *
 */
public class MyContactFilter implements ContactFilter {
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {

        boolean test = shouldBothCollide(fixtureA, fixtureB);
        if(!test){
            test = shouldBothCollide(fixtureB, fixtureA);
        }

        return test;
    }


    //Check both combinations of A and B
    private boolean shouldBothCollide(Fixture fixtureA, Fixture fixtureB){

        //Check if projectile is colliding with owner, if so, don't collide
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE &&
                fixtureB.getFilterData().categoryBits == fixtureA.getFilterData().maskBits){
            if(fixtureA.getUserData() instanceof Entity &&
                    fixtureB.getUserData() instanceof Entity){
                Entity entityA = (Entity) fixtureA.getUserData();
                Entity entityB = (Entity) fixtureB.getUserData();
                Entity owner = entityA.getComponent(ProjectileComponent.class).owner;
                if(entityB.equals(owner)){
                    return false;
                }

            }

            return true;

        }

        //Only collide with consumables if player
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_HUSK &&
                fixtureB.getFilterData().categoryBits == fixtureA.getFilterData().maskBits){
            if(fixtureA.getUserData() instanceof Entity &&
                    fixtureB.getUserData() instanceof Entity){
                Entity entityA = (Entity) fixtureA.getUserData();
                Entity entityB = (Entity) fixtureB.getUserData();
                Entity owner = entityA.getComponent(ProjectileComponent.class).owner;
                if(entityB.equals(owner)){
                    return false;
                }

            }

            return true;

        }

        //Player collide with consumable
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_HUSK &&
                fixtureB.getFilterData().categoryBits == B2DVars.BIT_CONSUMABLE) {
            Entity entityA = (Entity) fixtureA.getUserData();
            if(entityA.getComponent(PlayerComponent.class) != null){
                return true;
            }
        }

        //Collide if both are husks
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_HUSK &&
                fixtureB.getFilterData().categoryBits == B2DVars.BIT_HUSK){
            return true;
        }

        //Collide if hitting map boundaries
        if(fixtureA.getFilterData().categoryBits == B2DVars.BIT_HUSK &&
                fixtureB.getFilterData().categoryBits == B2DVars.BIT_COLLISION){
            return true;
        }

        return false;
    }

}
