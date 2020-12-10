package com.game.ECS.Storage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.game.ECS.Components.AIComponent;
import com.game.ECS.Components.AnimationSetComponent;
import com.game.ECS.Components.BodyComponent;
import com.game.ECS.Components.ConsumableComponent;
import com.game.ECS.Components.DepthComponent;
import com.game.ECS.Components.FacingComponent;
import com.game.ECS.Components.HealthComponent;
import com.game.ECS.Components.ParticleEffectComponent;
import com.game.ECS.Components.PositionComponent;
import com.game.ECS.Components.SoundEffectComponent;
import com.game.ECS.Components.SpriteAnimatingComponent;
import com.game.ECS.Components.SpriteComponent;
import com.game.ECS.Components.StateComponent;
import com.game.ECS.Components.VelocityComponent;
import com.game.ECS.Managers.ResourceManager;
import com.game.ECS.Managers.WorldManager;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Sean on 14/06/2015.
 */
public class ItemPrefabs {

        public static Entity createHealthPotion(WorldManager worldManager, Vector2 spawn) {
            Entity item = new Entity();

            SpriteComponent spriteComponent = new SpriteComponent();
            spriteComponent.sprite.setSize(1,1);
            spriteComponent.sprite.setTexture(ResourceManager.healthPot());

            PositionComponent position = new PositionComponent(
                    spawn.x, spawn.y);

            BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                    WorldManager.BodyType.CONSUMABLE, item
            ));

            bodyComponent.offset.y = -32f;
            bodyComponent.body.setTransform(new Vector2( (spawn.x / GameVars.PTM),
                    (spawn.y / GameVars.PTM)),
                    bodyComponent.body.getAngle());

            item.add(bodyComponent)
                    .add(spriteComponent)
                    .add(new DepthComponent(-0.5f))
                    .add(new ConsumableComponent(ConsumableComponent.ConsumeType.Health, 3))
                    .add(new SpriteAnimatingComponent(Assets.healthPotAnim()))
                    .add(new SoundEffectComponent(ResourceManager.soundPickUp()))
                    .add(position);

            return item;
        }

    public static Entity createInkwell(WorldManager worldManager, Vector2 spawn) {
        Entity item = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(1,1);
        spriteComponent.sprite.setTexture(ResourceManager.inkPot());

        PositionComponent position = new PositionComponent(
                spawn.x, spawn.y);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.CONSUMABLE, item
        ));

        bodyComponent.offset.y = -32f;
        bodyComponent.body.setTransform(new Vector2( spawn.x / GameVars.PTM, spawn.y / GameVars.PTM),
                bodyComponent.body.getAngle());

        item.add(bodyComponent)
                .add(spriteComponent)
                .add(new DepthComponent(-0.50f))
                .add(new ConsumableComponent(ConsumableComponent.ConsumeType.Ink, 10))
                .add(new SpriteAnimatingComponent(Assets.inkPotAnim()))
                .add(new SoundEffectComponent(ResourceManager.soundPickUp()))
                .add(position);

        return item;
    }

    public static Entity createLife(WorldManager worldManager, Vector2 spawn) {
        Entity item = new Entity();

        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.sprite.setSize(1,1);
        spriteComponent.sprite.setTexture(ResourceManager.inkPot());

        PositionComponent position = new PositionComponent(
                spawn.x, spawn.y);

        BodyComponent bodyComponent = new BodyComponent(worldManager.createBody(
                WorldManager.BodyType.CONSUMABLE, item
        ));

        bodyComponent.offset.y = -32f;
        bodyComponent.body.setTransform(new Vector2( spawn.x / GameVars.PTM, spawn.y / GameVars.PTM),
                bodyComponent.body.getAngle());

        item.add(bodyComponent)
                .add(spriteComponent)
                .add(new DepthComponent(-0.50f))
                .add(new ConsumableComponent(ConsumableComponent.ConsumeType.Life, 1))
                .add(new SpriteAnimatingComponent(Assets.lifePotAnim()))
                .add(position)
                .add(new SoundEffectComponent(ResourceManager.soundPickUp()))
                .add(new ParticleEffectComponent(Particles.lifePotion()));


        return item;
    }
}
