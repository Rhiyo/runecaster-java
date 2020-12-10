package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.VelocityComponent;

/**
 * Created by Sean on 27/04/2015.
 */
public class FacingSystem extends IteratingSystem{
    private ComponentMapper<FacingComponent> fm;

    public FacingSystem() {
        super(Family.all(FacingComponent.class).get());

        fm = ComponentMapper.getFor(FacingComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FacingComponent facing = fm.get(entity);
        if (facing.type == FacingComponent.FacingType.LEFTRIGHTUPDOWN) {
            if (facing.dir.y <= 0.60f && facing.dir.y > -0.60f && facing.dir.x > 0) {
                facing.facing = FacingComponent.Facing.RIGHT;
            } else if (facing.dir.y <= 0.60f && facing.dir.y > -0.60f && facing.dir.x < 0) {
                facing.facing = FacingComponent.Facing.LEFT;
            } else if (facing.dir.x <= 0.80f && facing.dir.x > -0.80f && facing.dir.y > 0) {
                facing.facing = FacingComponent.Facing.UP;
            } else if (facing.dir.x <= 0.80f && facing.dir.x > -0.80f && facing.dir.y < 0) {
                facing.facing = FacingComponent.Facing.DOWN;
            }
        } else if (facing.type == FacingComponent.FacingType.UPDOWN) {
            if(facing.dir.y < 0){
                facing.facing = FacingComponent.Facing.DOWN;
            }else if(facing.dir.y >= 0){
                facing.facing = FacingComponent.Facing.UP;
            }
        }
    }
}
