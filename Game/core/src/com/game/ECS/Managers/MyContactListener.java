package com.game.ECS.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.ConsumableComponent;
import com.game.ECS.Components.DamageComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PlayerInputComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SoundEffectComponent;
import com.game.ECS.Storage.B2DVars;
import com.game.ECS.Storage.Particles;

import java.util.Random;


/**
 * Created by Sean on 1/05/2015.
 *
 * Handles the contact between Box2D objects
 * Pretty much handles attacks between different entities
 *
 */
public class MyContactListener implements ContactListener {

    private Engine engine;
    public MyContactListener(Engine engine){
        this.engine = engine;
    }


    //Getting random retreat time
    Random rnd = new Random();
    float min = 1;
    float max = 1.5f;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        //For projectiles
        if(fa.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE) {
            Entity projectile = (Entity) fa.getUserData();
            Entity entity = new Entity();

            PositionComponent pos = new PositionComponent(entity.getComponent(PositionComponent.class).x,
                    entity.getComponent(PositionComponent.class).y);
            ParticleEffectComponent effect = new ParticleEffectComponent(Particles.iceExplosion());

            entity.add(pos).add(effect).add(new DepthComponent(0));
            engine.addEntity(entity);
            engine.removeEntity(projectile);
            if(fb.getUserData() != null){
                if(fb.getUserData() instanceof Entity) {
                    Entity enemy = (Entity) fb.getUserData();
                    DamageComponent damage = enemy.getComponent(DamageComponent.class);
                    if (enemy.getComponent(DamageComponent.class) == null){
                        enemy.add(new DamageComponent(1));
                    }

                }
            }
        }
        if(fb.getFilterData().categoryBits == B2DVars.BIT_PROJECTILE) {
            Entity projectile = (Entity) fb.getUserData();
            Entity entity = new Entity();

            PositionComponent pos = new PositionComponent(projectile.getComponent(PositionComponent.class).x,
                    projectile.getComponent(PositionComponent.class).y);
            ParticleEffectComponent effect = new ParticleEffectComponent(Particles.iceExplosion());

            //TODO make an OnHit variable for Projectile, which stores an entity: Clear this entity after use
            entity.add(pos).add(effect).add(new DepthComponent(0));
            engine.addEntity(entity);
            engine.removeEntity(projectile);
            if(fa.getUserData() != null){
                if(fa.getUserData() instanceof Entity) {
                    Entity enemy = (Entity) fa.getUserData();
                    enemy.add(new DamageComponent(1));
                }
            }
        }

        //For consumables
        if(fa.getFilterData().categoryBits == B2DVars.BIT_CONSUMABLE) {
            Entity consumable = (Entity) fa.getUserData();
            Entity player = (Entity) fb.getUserData();
            ConsumableComponent consumeComp = consumable.getComponent(ConsumableComponent.class);
            //player.add(consumable.getComponent(ConsumableComponent.class));
            //consumable.remove(ConsumableComponent.class);
            SoundEffectComponent sound = consumable.getComponent(SoundEffectComponent.class);
            if(sound != null && sound.sound != null)
                sound.sound.play(0.3f);
            PlayerInputComponent input = player.getComponent(PlayerInputComponent.class);
            if(consumeComp != null) {
                if (consumeComp.type == ConsumableComponent.ConsumeType.Health)
                    input.healthPots++;
                if (consumeComp.type == ConsumableComponent.ConsumeType.Ink)
                    input.inkPots++;
            }else{
                player.add(consumable.getComponent(ConsumableComponent.class));
            }
            engine.removeEntity(consumable);
        }if(fb.getFilterData().categoryBits == B2DVars.BIT_CONSUMABLE) {
            Entity consumable = (Entity) fb.getUserData();
            Entity player = (Entity) fa.getUserData();
            ConsumableComponent consumeComp = consumable.getComponent(ConsumableComponent.class);
            //player.add(consumable.getComponent(ConsumableComponent.class));
            //consumable.remove(ConsumableComponent.class);
            SoundEffectComponent sound = consumable.getComponent(SoundEffectComponent.class);
            if(sound != null && sound.sound != null)
                sound.sound.play(0.3f);
            PlayerInputComponent input = player.getComponent(PlayerInputComponent.class);
            if(consumeComp != null) {
                if (consumeComp.type == ConsumableComponent.ConsumeType.Health)
                    input.healthPots++;
                if (consumeComp.type == ConsumableComponent.ConsumeType.Ink)
                    input.inkPots++;
            }else{
                player.add(consumable.getComponent(ConsumableComponent.class));
            }
            engine.removeEntity(consumable);
        }

        //Anything inbetween 2 husks
        if(fa.getUserData() instanceof Entity && fb.getUserData() instanceof Entity){
            Entity a = (Entity) fa.getUserData();
            Entity b = (Entity) fb.getUserData();
            AIComponent aic = a.getComponent(AIComponent.class);
            PlayerComponent pc = b.getComponent(PlayerComponent.class);
            if(aic != null && pc != null){
                if(aic.state == AIComponent.AIState.ATTACKING) {
                    b.add(new DamageComponent(1));
                    aic.state = AIComponent.AIState.RETREATING;
                    aic.retreatTime = rnd.nextFloat() * (max - min) + min;
                }
            }else {

                aic = b.getComponent(AIComponent.class);
                pc = a.getComponent(PlayerComponent.class);
                if (aic != null && pc != null) {
                    if(aic.state == AIComponent.AIState.ATTACKING) {
                        a.add(new DamageComponent(1));
                        aic.state = AIComponent.AIState.RETREATING;
                        aic.retreatTime = rnd.nextFloat() * (max - min) + min;

                    }
                }
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        //Stop Damaging

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
