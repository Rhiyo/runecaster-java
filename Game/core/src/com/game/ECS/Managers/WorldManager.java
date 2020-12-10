package com.game.ECS.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.ECS.Storage.B2DVars;
import com.game.ECS.Storage.GameVars;

/**
 * Created by Sean on 25/04/2015.
 *
 * WorldManager, a class for handling the Box2D world
 *
 */
public class WorldManager {

    public WorldManager (){
        world = new World(new Vector2(0,0), true);
    }

    public static enum BodyType{
        HUMANOID, PROJECTILE, CONSUMABLE
    }

    private World world;

    public Body createBody(BodyType type, Entity owner){
        switch(type){
            case HUMANOID:
                float posX = 0, posY = 0, offsetX = 1f, offsetY = 0f, circleRadius = 0.25f,
                        hitboxW = 0.25f, hitboxH = 0.50f, hitboxOSX = 0f, hitboxOSY = 0.5f;
                return createBody(posX+offsetX, posY+offsetY, circleRadius,
                        hitboxW, hitboxH, hitboxOSX, hitboxOSY, owner);
            case PROJECTILE:
                return createProjectileBody(owner);
            case CONSUMABLE:
                return createConsumableBody(owner);
        }
        return null;
    }

    private Body createBody(float x, float y, float collisionRadius, float hitboxW,
                            float hitboxH, float offsetX, float offsetY, Entity owner){
        Body body;
        BodyDef bodyDef;
        CircleShape circle;
        PolygonShape polygon;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x)/ GameVars.PTM, y/GameVars.PTM);
        bodyDef.fixedRotation = true;
        body = this.world.createBody(bodyDef);
        //Collision
        circle = new CircleShape();
        circle.setRadius(collisionRadius/GameVars.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HUSK;
        fixtureDef.filter.maskBits = B2DVars.BIT_COLLISION | B2DVars.BIT_HUSK;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(owner);
        circle.dispose();

        //Hitbox
        polygon = new PolygonShape();
        polygon.setAsBox(hitboxW/GameVars.PTM, hitboxH/GameVars.PTM,
                new Vector2(offsetX, offsetY/GameVars.PTM), 0);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_HITBOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_PROJECTILE;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(owner);
        polygon.dispose();
        body.setLinearDamping(GameVars.DAMPING);
        return body;
    }

    public Body createProjectileBody(Entity owner){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f/GameVars.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PROJECTILE;
        fixtureDef.filter.maskBits = B2DVars.BIT_HITBOX;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(owner);
        shape.dispose();

        return body;
    }

    public Body createConsumableBody(Entity owner){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.25f/GameVars.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_CONSUMABLE;
        fixtureDef.filter.maskBits = B2DVars.BIT_HUSK;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(owner);
        shape.dispose();
        body.setGravityScale(0);
        return body;
    }

    /**
     * Getters
     */

    public World getWorld(){
        return world;
    }

}
