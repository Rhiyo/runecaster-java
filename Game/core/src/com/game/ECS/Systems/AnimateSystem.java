package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.game.ECS.Components.SpriteAnimatingComponent;
import com.game.ECS.Components.SpriteComponent;

/**
 * Created by Sean on 15/06/2015.
 *
 * Animates single sprites
 *
 */
public class AnimateSystem extends IteratingSystem {
    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<SpriteAnimatingComponent> sam;

    public AnimateSystem() {
        super(Family.all(SpriteComponent.class,
                SpriteAnimatingComponent.class).get());

        sm = ComponentMapper.getFor(SpriteComponent.class);
        sam = ComponentMapper.getFor(SpriteAnimatingComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent sprite = sm.get(entity);
        SpriteAnimatingComponent anim = sam.get(entity);


        if (anim.animation != null) {
            sprite.sprite.setRegion(anim.animation.getKeyFrame(anim.time, true));
            anim.time += deltaTime;
        }
    }
}

