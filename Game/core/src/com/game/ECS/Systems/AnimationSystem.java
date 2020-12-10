package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;

/**
 * Created by Sean on 27/04/2015.
 */
//Todo Rename this animation state
public class AnimationSystem extends IteratingSystem {
    private ComponentMapper<SpriteComponent> sm;
    private ComponentMapper<AnimationSetComponent> am;
    private ComponentMapper<StateComponent> stm;
    private ComponentMapper<FacingComponent> fm;

    public AnimationSystem() {
        super(Family.all(SpriteComponent.class,
                AnimationSetComponent.class,
                StateComponent.class,
                FacingComponent.class).get());

        sm = ComponentMapper.getFor(SpriteComponent.class);
        am = ComponentMapper.getFor(AnimationSetComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);
        fm = ComponentMapper.getFor(FacingComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent sprite = sm.get(entity);
        AnimationSetComponent anim = am.get(entity);
        StateComponent state = stm.get(entity);
        Animation animation = null;
        switch(fm.get(entity).facing){
            case LEFT:  animation = anim.animations.left.get(state.state);
                break;
            case RIGHT:  animation = anim.animations.right.get(state.state);
                break;
            case UP:  animation = anim.animations.up.get(state.state);
                break;
            case DOWN:  animation = anim.animations.down.get(state.state);
                break;
        }

        if (animation != null) {
            sprite.sprite.setRegion(animation.getKeyFrame(state.time, true));
        }

        //TODO Delta fixed here
        state.time += deltaTime;
    }
}
