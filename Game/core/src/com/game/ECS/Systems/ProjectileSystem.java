package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.ProjectileComponent;
import com.game.ECS.Storage.GameVars;

/**
 * Created by Sean on 28/04/2015.
 */
public class ProjectileSystem extends IteratingSystem{
    private ComponentMapper<ProjectileComponent> projm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<PositionComponent> pm;

    public ProjectileSystem() {
        super(Family.all(ProjectileComponent.class, BodyComponent.class,
                PositionComponent.class).get());

        projm = ComponentMapper.getFor(ProjectileComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ProjectileComponent projc = projm.get(entity);
        BodyComponent bc = bm.get(entity);
        PositionComponent pos = pm.get(entity);

        //Todo Delta fixed here

        bc.body.setLinearVelocity(projc.dir.x * projc.speed * deltaTime,
                projc.dir.y * projc.speed * deltaTime);

        pos.x = bc.body.getPosition().x * GameVars.PTM + bc.offset.x;
        pos.y = bc.body.getPosition().y * GameVars.PTM + bc.offset.y;
    }
}
