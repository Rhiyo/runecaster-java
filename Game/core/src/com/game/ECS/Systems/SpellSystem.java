package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SpellComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Managers.WorldManager;
import com.game.ECS.Storage.GameVars;
import com.game.ECS.Storage.ProjectilePrefabs;

/**
 * Created by Sean on 8/05/2015.
 *
 * Grabs a spell component to cast it.
 *
 */
public class SpellSystem extends IteratingSystem{

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<SpellComponent> sm;

    WorldManager worldManager;
    Engine engine;

    public SpellSystem(WorldManager worldManager) {
        super(Family.all(PositionComponent.class, SpellComponent.class).get());
        pm = ComponentMapper.getFor(PositionComponent.class);
        sm = ComponentMapper.getFor(SpellComponent.class);

        this.worldManager = worldManager;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpellComponent spell = sm.get(entity);
        PositionComponent pos = pm.get(entity);
        if(spell.spellType == SpellComponent.Spell.FROST){
            engine.addEntity(ProjectilePrefabs.createIce(new Vector2(pos.x, pos.y),
                    entity, spell.spellDir, worldManager));
            entity.remove(SpellComponent.class);
        }
        if(spell.spellType == SpellComponent.Spell.GRAVITY_SHIFT){

            //Want caster to stay still
            worldManager.getWorld().setGravity(new Vector2((Gdx.input.getAccelerometerY()/ GameVars.PTM)*5, ((Gdx.input.getAccelerometerX()/GameVars.PTM)*5) *-1));
            VelocityComponent vc = entity.getComponent(VelocityComponent.class);
            if(vc != null){
                entity.remove(VelocityComponent.class);
            }
            BodyComponent bc = entity.getComponent(BodyComponent.class);
            if(bc != null){
                bc.body.setGravityScale(0);
            }
            spell.duration -= deltaTime;
            if(spell.duration <= 0){
                entity.add(new VelocityComponent(0, 0)).remove(SpellComponent.class);
                bc.body.setGravityScale(1);
                worldManager.getWorld().setGravity(new Vector2(0, 0));
            }
        }
        if(spell.spellType == SpellComponent.Spell.LUNGE){

        }
    }
}
