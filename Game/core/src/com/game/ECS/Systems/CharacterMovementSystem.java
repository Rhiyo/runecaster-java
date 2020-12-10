package com.game.ECS.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SoundSetComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Storage.GameVars;


/**
 * Created by Sean on 25/04/2015.
 *
 * Moving all the things.
 *
 */

//TODO world needs to implement http://saltares.com/blog/games/fixing-your-timestep-in-libgdx-and-box2d/
public class CharacterMovementSystem extends IteratingSystem {

    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<FacingComponent> fm;

    private World world;

    public CharacterMovementSystem(int order, World world) {
        super(Family.all(BodyComponent.class, VelocityComponent.class,
                PositionComponent.class, StateComponent.class,
                FacingComponent.class).get(), order);

        bm = ComponentMapper.getFor(BodyComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        fm = ComponentMapper.getFor(FacingComponent.class);

        this.world = world;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(1f/60f, 6, 2);
    }
    public void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = bm.get(entity);
        Vector2 vel = vm.get(entity).velocity;
        StateComponent state = sm.get(entity);
        //Change state
        SoundSetComponent sounds = entity.getComponent(SoundSetComponent.class);


        Vector2 currentVelocity = body.body.getLinearVelocity();

        if(vel.x == 0 && vel.y == 0){
            if(sounds != null && sounds.step != null)
                sounds.step.stop();
            state.state = StateComponent.State.STILL;
        }else{
            if(state.state != StateComponent.State.WALK) {
                if(sounds != null){
                    if(sounds.step != null){
                        sounds.step.loop();
                        sounds.step.play();
                    }
                }
                state.state = StateComponent.State.WALK;
            }
        }
        float desiredXVel = 0;
        float desiredYVel = 0;

        if(vel.x != 0 && vel.y != 0){
            fm.get(entity).dir = new Vector2(vel).nor();
        }

        if(vel.x < 0){
            desiredXVel = vel.x;
        }
        if(vel.x > 0){
            desiredXVel = vel.x;
        }
        if(vel.y < 0){
            desiredYVel = vel.y;
        }
        if(vel.y > 0){
            desiredYVel = vel.y;
        }

        float velXChange = desiredXVel - currentVelocity.x;
        float velYChange = desiredYVel - currentVelocity.y;
        float impulseX = body.body.getMass() * velXChange; //disregard time factor
        float impulseY = body.body.getMass() * velYChange; //disregard time factor
        impulseX /= GameVars.PTM;
        impulseY /= GameVars.PTM;
        impulseX *= deltaTime;
        impulseY *= deltaTime;
        body.body.applyLinearImpulse(new Vector2((impulseX), impulseY), body.body.getWorldCenter(), true);

        pm.get(entity).x = body.body.getPosition().x * GameVars.PTM + body.offset.x;
        pm.get(entity).y = body.body.getPosition().y * GameVars.PTM + body.offset.y;
    }
}