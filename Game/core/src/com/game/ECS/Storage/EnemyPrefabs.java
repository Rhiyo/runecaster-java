package com.game.ECS.Storage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.EnemyTypeComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.HealthComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SoundSetComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Managers.WorldManager;

/**
 * Created by Sean on 3/05/2015.
 *
 * Prefabs of all the enemies spawnable
 *
 */
public class EnemyPrefabs {

    /**
     * Creates a Wolf enemy prefab at the specified spawn.
     * @param worldManager
     * @param spawn
     * @return created Wolf entity
     */
    public static Entity createWolf(WorldManager worldManager, Vector2 spawn){
        Entity enemy = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(2,2);
        TextureRegion texture = new TextureRegion(ResourceManager.sheetWolf(), 0,0,32,32);
        spriteComponent.sprite.setRegion(texture);
        PositionComponent position = new PositionComponent(
                spawn.x, spawn.y);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.HUMANOID, enemy
        ));
        bodyComponent.offset.y = 0.5f;
        bodyComponent.body.setTransform(new Vector2( spawn.x / GameVars.PTM, spawn.y / GameVars.PTM),
                bodyComponent.body.getAngle());

        SoundSetComponent sounds = new SoundSetComponent();

        sounds.damageTaken = new Array<Sound>();
        sounds.damageTaken.add(ResourceManager.soundWolfDmg1());

        sounds.lunge = new Array<Sound>();
        sounds.lunge.add(ResourceManager.soundWolfLunge1());
        sounds.lunge.add(ResourceManager.soundWolfLunge2());
        sounds.lunge.add(ResourceManager.soundWolfLunge3());

        sounds.death = new Array<Sound>();
        sounds.death.add(ResourceManager.soundWolfDeath1());
        sounds.death.add(ResourceManager.soundWolfDeath2());
        sounds.death.add(ResourceManager.soundWolfDeath3());

        enemy.add(new VelocityComponent(0, 0))
                .add(bodyComponent)
                .add(new FacingComponent())
                .add(spriteComponent)
                .add(new AnimationSetComponent(Assets.animWolf()))
                .add(new StateComponent())
                .add(new DepthComponent(-0.50f))
                .add(new AIComponent())
                .add(position)
                .add(sounds)
                .add(new HealthComponent(1))
                .add(new EnemyTypeComponent(EnemyTypeComponent.EnemyType.WOLF));

        return enemy;
    }

    /**
     * Creates a Fae enemy prefab at the specified spawn.
     * @param worldManager
     * @param spawn
     * @return created Fae entity
     */
    public static Entity createFae(WorldManager worldManager, Vector2 spawn){
        Entity enemy = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(2, 2);
        TextureRegion texture = new TextureRegion(ResourceManager.sheetFae(), 0,0,32,32);
        spriteComponent.sprite.setRegion(texture);
        PositionComponent position = new PositionComponent(
                spawn.x, spawn.y);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.HUMANOID, enemy
        ));
        bodyComponent.offset.y = 0.5f;
        bodyComponent.body.setTransform(new Vector2( spawn.x / GameVars.PTM, spawn.y / GameVars.PTM),
                bodyComponent.body.getAngle());

        //Set facing type to up down
        FacingComponent facing = new FacingComponent();
        facing.type = FacingComponent.FacingType.UPDOWN;

        SoundSetComponent sounds = new SoundSetComponent();

        sounds.damageTaken = new Array<Sound>();
        sounds.damageTaken.add(ResourceManager.soundWolfDmg1());

        sounds.lunge = new Array<Sound>();
        sounds.lunge.add(ResourceManager.soundWolfLunge1());
        sounds.lunge.add(ResourceManager.soundWolfLunge2());
        sounds.lunge.add(ResourceManager.soundWolfLunge3());

        sounds.death = new Array<Sound>();
        sounds.death.add(ResourceManager.soundWolfDeath1());
        sounds.death.add(ResourceManager.soundWolfDeath2());
        sounds.death.add(ResourceManager.soundWolfDeath3());

        enemy.add(new VelocityComponent(0, 0))
                .add(bodyComponent)
                .add(facing)
                .add(spriteComponent)
                .add(new AnimationSetComponent(Assets.animFae()))
                .add(new StateComponent())
                .add(new DepthComponent(-0.50f))
                .add(new AIComponent())
                .add(position)
                .add(sounds)
                .add(new HealthComponent(1))
                .add(new EnemyTypeComponent(EnemyTypeComponent.EnemyType.FAE));

        return enemy;
    }
}
