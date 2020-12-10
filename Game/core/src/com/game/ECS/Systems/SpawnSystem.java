package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.HealthComponent;
import com.game.ECS.Components.InkComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PlayerComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpawningComponent;
import com.game.ECS.Managers.MapManager;
import com.game.ECS.Storage.GameVars;
import com.game.ECS.Storage.Particles;

/**
 * Created by Sean on 25/04/2015.
 *
 * Spawns a player at a player spawn, with fresh health and ink.
 *
 */
public class SpawnSystem extends EntitySystem {

    private Engine engine;

    private ImmutableArray<Entity> playerEntities;

    private ComponentMapper<SpawningComponent> sm;
    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<BodyComponent> bm;
    private MapManager mapManager;

    public SpawnSystem(int order, MapManager mapManager) {
        super(order);
        sm = ComponentMapper.getFor(SpawningComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        this.mapManager = mapManager;
    }

    public void addedToEngine(Engine engine) {
        this.engine = engine;
        playerEntities = engine.getEntitiesFor(Family.all(SpawningComponent.class,
                PlayerComponent.class, BodyComponent.class).get());
    }


    public void update(float deltaTime) {
        for (int i = 0; i < playerEntities.size(); ++i) {
            HealthComponent health = playerEntities.get(i).getComponent(HealthComponent.class);
            //Reset Health
            if(health != null){
                health.currentHealth = health.maxHealth;
            }
            InkComponent ink = playerEntities.get(i).getComponent(InkComponent.class);
            //Reset Ink
            if(ink != null){
                ink.currentInk = ink.maxInk;
            }
            //Remove any left over velocity


            Vector2 spawn = mapManager.getRandomPlayerSpawn();
            Entity entity = playerEntities.get(i);
            PositionComponent position = new PositionComponent(
                    spawn.x, spawn.y);
            Body body = bm.get(playerEntities.get(i)).body;
            if(body != null) {
                body.setLinearVelocity(0, 0);
                body.setTransform(new Vector2(spawn.x / GameVars.PTM, spawn.y / GameVars.PTM), body.getAngle());
            }

            //Create spawn particle effect
            PositionComponent pos = new PositionComponent(spawn.x,
                    spawn.y);
            ParticleEffectComponent effect = new ParticleEffectComponent(Particles.leafSpawn());
            Entity spawnEffect = new Entity();
            spawnEffect.add(pos).add(effect).add(new DepthComponent(0));
            engine.addEntity(spawnEffect);

            entity.remove(SpawningComponent.class);
            entity.add(position);
        }
    }
}