package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.CameraComponent;
import com.game.ECS.Components.PositionComponent;

/**
 * Created by Sean on 26/04/2015.
 *
 * Handles moving the gameworld camera.
 *
 */
public class CameraSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<CameraComponent> cm;

    public CameraSystem(int order) {
        super(Family.all(PositionComponent.class, CameraComponent.class).get(), order);

        pm = ComponentMapper.getFor(PositionComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = cm.get(entity);
        PositionComponent pos = pm.get(entity);

        cam.currentOffset.x = MathUtils.lerp(cam.currentOffset.x,cam.offset.x,0.16f);
        cam.currentOffset.y = MathUtils.lerp(cam.currentOffset.y,cam.offset.y,0.16f);



        //TODO Make this work with out breaking spell aiming
        if(Vector2.dst(cam.currentTarget.x,cam.currentTarget.y,pos.x,pos.y) > 2)
        {
            cam.currentTarget.x = MathUtils.lerp(cam.currentTarget.x, pos.x, 0.2f);
            cam.currentTarget.y = MathUtils.lerp(cam.currentTarget.y, pos.y, 0.2f);
        }
        else
        {
            cam.currentTarget.x = pos.x;
            cam.currentTarget.y = pos.y;
        }

        float x = cam.currentTarget.x;
        float y = cam.currentTarget.y;

        cam.camera.position.x = x + cam.currentOffset.x;
        cam.camera.position.y = y + cam.currentOffset.y;



        cam.camera.update(); //TODO maybe move to render
    }


}
